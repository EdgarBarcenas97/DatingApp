package com.dating.home.domain.models

import kotlin.time.Instant

data class DateCheckIn(
    val id: String,
    val dateProposalId: String,
    val userId: String,
    val matchUsername: String,
    val matchProfileUrl: String?,
    val place: PlaceSuggestion,
    val dateTime: Instant,
    val trustedContacts: List<TrustedContact>,
    val checkInTimerMinutes: Int,
    val status: CheckInStatus,
    val isLiveLocationSharing: Boolean,
    val createdAt: Instant,
    val checkedInAt: Instant? = null,
    val alertSentAt: Instant? = null
)
