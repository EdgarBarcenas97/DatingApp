package com.dating.home.domain.models

data class PlaceSuggestion(
    val placeId: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double? = null,
    val photoUrl: String? = null,
    val category: String? = null,
    val safetyInfo: PlaceSafetyInfo? = null
)
