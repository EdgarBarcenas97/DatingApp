package com.dating.home.domain.safeplace

import com.dating.home.domain.models.PlaceSafetyInfo
import com.dating.home.domain.models.SafetyReview
import com.dating.home.domain.models.SafetyTag
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.Result

interface SafePlaceRepository {
    suspend fun getSafetyInfo(placeId: String): Result<PlaceSafetyInfo, DataError>
    suspend fun getReviews(placeId: String): Result<List<SafetyReview>, DataError>
    suspend fun submitReview(
        placeId: String,
        safetyRating: Int,
        comment: String?,
        tags: List<SafetyTag>
    ): Result<SafetyReview, DataError>
}
