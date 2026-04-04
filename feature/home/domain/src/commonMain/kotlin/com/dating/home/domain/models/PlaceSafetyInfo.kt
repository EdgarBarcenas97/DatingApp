package com.dating.home.domain.models

data class PlaceSafetyInfo(
    val placeId: String,
    val averageSafetyRating: Double,
    val totalReviews: Int,
    val topTags: List<SafetyTag>,
    val isVerifiedSafe: Boolean,
    val reviews: List<SafetyReview> = emptyList()
)
