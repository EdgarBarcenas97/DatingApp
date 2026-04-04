package com.dating.home.domain.models

import kotlin.time.Instant

data class SafetyReview(
    val id: String,
    val placeId: String,
    val reviewerUsername: String,
    val safetyRating: Int,
    val comment: String?,
    val tags: List<SafetyTag>,
    val createdAt: Instant
)
