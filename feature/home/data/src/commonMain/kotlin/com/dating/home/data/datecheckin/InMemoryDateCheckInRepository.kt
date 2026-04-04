package com.dating.home.data.datecheckin

import com.dating.home.domain.datecheckin.DateCheckInRepository
import com.dating.home.domain.models.CheckInStatus
import com.dating.home.domain.models.DateCheckIn
import com.dating.home.domain.models.DateProposal
import com.dating.home.domain.models.TrustedContact
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.EmptyResult
import com.dating.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class InMemoryDateCheckInRepository(
    private val getProposal: suspend (String) -> DateProposal?,
    private val localUserId: suspend () -> String
) : DateCheckInRepository {

    private val _activeCheckIn = MutableStateFlow<DateCheckIn?>(null)

    override fun getActiveCheckIn(): Flow<DateCheckIn?> = _activeCheckIn

    override suspend fun startCheckIn(dateProposalId: String, trustedContacts: List<TrustedContact>, checkInTimerMinutes: Int, shareLiveLocation: Boolean): Result<DateCheckIn, DataError> {
        val proposal = getProposal(dateProposalId) ?: return Result.Failure(DataError.Local.NOT_FOUND)
        val checkIn = DateCheckIn(id = Uuid.random().toString(), dateProposalId = dateProposalId, userId = localUserId(), matchUsername = proposal.proposerUsername, matchProfileUrl = null, place = proposal.place, dateTime = proposal.dateTime, trustedContacts = trustedContacts, checkInTimerMinutes = checkInTimerMinutes, status = CheckInStatus.ACTIVE, isLiveLocationSharing = shareLiveLocation, createdAt = Clock.System.now())
        _activeCheckIn.update { checkIn }
        return Result.Success(checkIn)
    }

    override suspend fun performCheckIn(checkInId: String): EmptyResult<DataError> {
        val current = _activeCheckIn.value ?: return Result.Failure(DataError.Local.NOT_FOUND)
        if (current.id != checkInId) return Result.Failure(DataError.Local.NOT_FOUND)
        _activeCheckIn.update { it?.copy(status = CheckInStatus.CHECKED_IN, checkedInAt = Clock.System.now()) }
        return Result.Success(Unit)
    }

    override suspend fun cancelCheckIn(checkInId: String): EmptyResult<DataError> {
        val current = _activeCheckIn.value ?: return Result.Failure(DataError.Local.NOT_FOUND)
        if (current.id != checkInId) return Result.Failure(DataError.Local.NOT_FOUND)
        _activeCheckIn.update { null }
        return Result.Success(Unit)
    }

    override suspend fun toggleLiveLocation(checkInId: String, enabled: Boolean): EmptyResult<DataError> {
        val current = _activeCheckIn.value ?: return Result.Failure(DataError.Local.NOT_FOUND)
        if (current.id != checkInId) return Result.Failure(DataError.Local.NOT_FOUND)
        _activeCheckIn.update { it?.copy(isLiveLocationSharing = enabled) }
        return Result.Success(Unit)
    }

    override suspend fun extendTimer(checkInId: String, additionalMinutes: Int): EmptyResult<DataError> {
        val current = _activeCheckIn.value ?: return Result.Failure(DataError.Local.NOT_FOUND)
        if (current.id != checkInId) return Result.Failure(DataError.Local.NOT_FOUND)
        _activeCheckIn.update { it?.copy(checkInTimerMinutes = it.checkInTimerMinutes + additionalMinutes) }
        return Result.Success(Unit)
    }

    override suspend fun sendManualAlert(checkInId: String): EmptyResult<DataError> {
        val current = _activeCheckIn.value ?: return Result.Failure(DataError.Local.NOT_FOUND)
        if (current.id != checkInId) return Result.Failure(DataError.Local.NOT_FOUND)
        _activeCheckIn.update { it?.copy(status = CheckInStatus.ALERT_SENT, alertSentAt = Clock.System.now()) }
        return Result.Success(Unit)
    }
}
