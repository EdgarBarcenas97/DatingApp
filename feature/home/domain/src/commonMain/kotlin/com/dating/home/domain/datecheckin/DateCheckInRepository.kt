package com.dating.home.domain.datecheckin

import com.dating.home.domain.models.DateCheckIn
import com.dating.home.domain.models.TrustedContact
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.EmptyResult
import com.dating.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface DateCheckInRepository {
    fun getActiveCheckIn(): Flow<DateCheckIn?>
    suspend fun startCheckIn(
        dateProposalId: String,
        trustedContacts: List<TrustedContact>,
        checkInTimerMinutes: Int,
        shareLiveLocation: Boolean
    ): Result<DateCheckIn, DataError>
    suspend fun performCheckIn(checkInId: String): EmptyResult<DataError>
    suspend fun cancelCheckIn(checkInId: String): EmptyResult<DataError>
    suspend fun toggleLiveLocation(checkInId: String, enabled: Boolean): EmptyResult<DataError>
    suspend fun extendTimer(checkInId: String, additionalMinutes: Int): EmptyResult<DataError>
    suspend fun sendManualAlert(checkInId: String): EmptyResult<DataError>
}
