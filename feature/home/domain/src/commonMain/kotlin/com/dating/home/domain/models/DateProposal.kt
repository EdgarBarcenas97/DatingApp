package com.dating.home.domain.models

import kotlin.time.Instant

data class DateProposal(
    val id: String,
    val chatId: String,
    val proposerId: String,
    val proposerUsername: String,
    val dateTime: Instant,
    val place: PlaceSuggestion,
    val status: DateProposalStatus,
    val note: String? = null,
    val createdAt: Instant,
    val respondedAt: Instant? = null,
    val modifiedFromId: String? = null
)
