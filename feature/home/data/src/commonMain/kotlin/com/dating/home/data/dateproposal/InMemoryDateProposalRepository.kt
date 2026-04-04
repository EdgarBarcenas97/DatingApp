package com.dating.home.data.dateproposal

import com.dating.home.domain.dateproposal.DateProposalRepository
import com.dating.home.domain.models.DateProposal
import com.dating.home.domain.models.DateProposalStatus
import com.dating.home.domain.models.PlaceSuggestion
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class InMemoryDateProposalRepository(
    private val localUserId: suspend () -> String,
    private val localUsername: suspend () -> String
) : DateProposalRepository {

    private val proposals = MutableStateFlow<List<DateProposal>>(emptyList())

    private val mockPlaces = listOf(
        PlaceSuggestion(placeId = "place_1", name = "The Coffee Garden", address = "123 Main St, Downtown", latitude = 19.4326, longitude = -99.1332, rating = 4.5, category = "Cafe"),
        PlaceSuggestion(placeId = "place_2", name = "Bella Italia Restaurant", address = "456 Oak Ave, Midtown", latitude = 19.4350, longitude = -99.1400, rating = 4.7, category = "Restaurant"),
        PlaceSuggestion(placeId = "place_3", name = "Sunset Rooftop Bar", address = "789 Sky Blvd, Uptown", latitude = 19.4280, longitude = -99.1500, rating = 4.3, category = "Bar"),
        PlaceSuggestion(placeId = "place_4", name = "Central Park", address = "101 Green Way, City Center", latitude = 19.4200, longitude = -99.1350, rating = 4.8, category = "Park"),
        PlaceSuggestion(placeId = "place_5", name = "Cinema Palace", address = "222 Entertainment Dr", latitude = 19.4400, longitude = -99.1250, rating = 4.2, category = "Entertainment"),
        PlaceSuggestion(placeId = "place_6", name = "Sushi Master", address = "333 Food Court Ln", latitude = 19.4150, longitude = -99.1450, rating = 4.6, category = "Restaurant"),
        PlaceSuggestion(placeId = "place_7", name = "Art Gallery Moderna", address = "444 Culture St, Arts District", latitude = 19.4380, longitude = -99.1280, rating = 4.4, category = "Museum"),
        PlaceSuggestion(placeId = "place_8", name = "Moonlight Lounge", address = "555 Night Ave, Zona Rosa", latitude = 19.4260, longitude = -99.1550, rating = 4.1, category = "Bar")
    )

    override fun getProposalsForChat(chatId: String): Flow<List<DateProposal>> {
        return proposals.map { list -> list.filter { it.chatId == chatId }.sortedByDescending { it.createdAt } }
    }

    override suspend fun sendProposal(chatId: String, dateTime: Instant, place: PlaceSuggestion, note: String?): Result<DateProposal, DataError> {
        val proposal = DateProposal(id = Uuid.random().toString(), chatId = chatId, proposerId = localUserId(), proposerUsername = localUsername(), dateTime = dateTime, place = place, status = DateProposalStatus.PENDING, note = note, createdAt = Clock.System.now())
        proposals.update { it + proposal }
        return Result.Success(proposal)
    }

    override suspend fun respondToProposal(proposalId: String, accepted: Boolean): Result<DateProposal, DataError> {
        val proposal = proposals.value.find { it.id == proposalId } ?: return Result.Failure(DataError.Local.NOT_FOUND)
        val updated = proposal.copy(status = if (accepted) DateProposalStatus.ACCEPTED else DateProposalStatus.DECLINED, respondedAt = Clock.System.now())
        proposals.update { list -> list.map { if (it.id == proposalId) updated else it } }
        return Result.Success(updated)
    }

    override suspend fun modifyProposal(originalProposalId: String, dateTime: Instant, place: PlaceSuggestion, note: String?): Result<DateProposal, DataError> {
        val original = proposals.value.find { it.id == originalProposalId } ?: return Result.Failure(DataError.Local.NOT_FOUND)
        proposals.update { list -> list.map { if (it.id == originalProposalId) it.copy(status = DateProposalStatus.MODIFIED) else it } }
        val modified = DateProposal(id = Uuid.random().toString(), chatId = original.chatId, proposerId = localUserId(), proposerUsername = localUsername(), dateTime = dateTime, place = place, status = DateProposalStatus.PENDING, note = note, createdAt = Clock.System.now(), modifiedFromId = originalProposalId)
        proposals.update { it + modified }
        return Result.Success(modified)
    }

    override suspend fun searchPlaces(query: String): Result<List<PlaceSuggestion>, DataError> {
        if (query.isBlank()) return Result.Success(mockPlaces)
        val filtered = mockPlaces.filter { place -> place.name.contains(query, ignoreCase = true) || place.address.contains(query, ignoreCase = true) || place.category?.contains(query, ignoreCase = true) == true }
        return Result.Success(filtered)
    }
}
