package com.dating.home.domain.dateproposal

import com.dating.home.domain.models.DateProposal
import com.dating.home.domain.models.PlaceSuggestion
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface DateProposalRepository {
    fun getProposalsForChat(chatId: String): Flow<List<DateProposal>>
    suspend fun sendProposal(
        chatId: String,
        dateTime: Instant,
        place: PlaceSuggestion,
        note: String? = null
    ): Result<DateProposal, DataError>
    suspend fun respondToProposal(
        proposalId: String,
        accepted: Boolean
    ): Result<DateProposal, DataError>
    suspend fun modifyProposal(
        originalProposalId: String,
        dateTime: Instant,
        place: PlaceSuggestion,
        note: String? = null
    ): Result<DateProposal, DataError>
    suspend fun searchPlaces(query: String): Result<List<PlaceSuggestion>, DataError>
}
