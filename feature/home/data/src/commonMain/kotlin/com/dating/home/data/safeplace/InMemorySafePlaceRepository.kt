package com.dating.home.data.safeplace

import com.dating.home.domain.models.DEFAULT_SAFETY_TAGS
import com.dating.home.domain.models.PlaceSafetyInfo
import com.dating.home.domain.models.SafetyReview
import com.dating.home.domain.models.SafetyTag
import com.dating.home.domain.safeplace.SafePlaceRepository
import com.dating.core.domain.util.DataError
import com.dating.core.domain.util.Result
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class InMemorySafePlaceRepository(
    private val localUsername: suspend () -> String
) : SafePlaceRepository {

    private val reviews = mutableListOf(
        SafetyReview(id = "review_1", placeId = "place_1", reviewerUsername = "Ana M.", safetyRating = 5, comment = "Excelente lugar para una primera cita. Bien iluminado y el staff es muy atento.", tags = listOf(DEFAULT_SAFETY_TAGS[0], DEFAULT_SAFETY_TAGS[1], DEFAULT_SAFETY_TAGS[2]), createdAt = Clock.System.now()),
        SafetyReview(id = "review_2", placeId = "place_1", reviewerUsername = "Laura G.", safetyRating = 4, comment = "Muy tranquilo, zona segura. Hay taxis siempre disponibles afuera.", tags = listOf(DEFAULT_SAFETY_TAGS[5], DEFAULT_SAFETY_TAGS[7]), createdAt = Clock.System.now()),
        SafetyReview(id = "review_3", placeId = "place_2", reviewerUsername = "Sofia R.", safetyRating = 5, comment = "Restaurante muy popular, siempre lleno. Te sientes segura. Recomendadísimo para citas.", tags = listOf(DEFAULT_SAFETY_TAGS[0], DEFAULT_SAFETY_TAGS[2], DEFAULT_SAFETY_TAGS[4]), createdAt = Clock.System.now()),
        SafetyReview(id = "review_4", placeId = "place_3", reviewerUsername = "Maria T.", safetyRating = 3, comment = "El rooftop está bien pero la zona de noche es un poco sola. Ir temprano.", tags = listOf(DEFAULT_SAFETY_TAGS[4], DEFAULT_SAFETY_TAGS[8]), createdAt = Clock.System.now()),
        SafetyReview(id = "review_5", placeId = "place_4", reviewerUsername = "Carolina P.", safetyRating = 4, comment = "De día es perfecto. Mucha gente, zona abierta. De noche mejor evitar.", tags = listOf(DEFAULT_SAFETY_TAGS[2], DEFAULT_SAFETY_TAGS[3], DEFAULT_SAFETY_TAGS[5]), createdAt = Clock.System.now()),
        SafetyReview(id = "review_6", placeId = "place_6", reviewerUsername = "Diana L.", safetyRating = 5, comment = "El mejor lugar para citas. Zona muy transitada, cámaras, y el staff te cuida.", tags = listOf(DEFAULT_SAFETY_TAGS[0], DEFAULT_SAFETY_TAGS[1], DEFAULT_SAFETY_TAGS[6]), createdAt = Clock.System.now()),
        SafetyReview(id = "review_7", placeId = "place_8", reviewerUsername = "Valentina K.", safetyRating = 2, comment = "La zona de Zona Rosa puede ser insegura de madrugada. Ir con precaución.", tags = listOf(DEFAULT_SAFETY_TAGS[10], DEFAULT_SAFETY_TAGS[11]), createdAt = Clock.System.now())
    )

    override suspend fun getSafetyInfo(placeId: String): Result<PlaceSafetyInfo, DataError> {
        val placeReviews = reviews.filter { it.placeId == placeId }
        if (placeReviews.isEmpty()) {
            return Result.Success(PlaceSafetyInfo(placeId = placeId, averageSafetyRating = 0.0, totalReviews = 0, topTags = emptyList(), isVerifiedSafe = false))
        }
        val avgRating = placeReviews.map { it.safetyRating }.average()
        val tagCounts = placeReviews.flatMap { it.tags }.groupBy { it.id }.mapValues { it.value.size }.entries.sortedByDescending { it.value }.take(3).map { entry -> placeReviews.flatMap { it.tags }.first { it.id == entry.key } }
        return Result.Success(PlaceSafetyInfo(placeId = placeId, averageSafetyRating = (avgRating * 10).toInt() / 10.0, totalReviews = placeReviews.size, topTags = tagCounts, isVerifiedSafe = avgRating >= 4.0 && placeReviews.size >= 2, reviews = placeReviews.sortedByDescending { it.createdAt }))
    }

    override suspend fun getReviews(placeId: String): Result<List<SafetyReview>, DataError> {
        return Result.Success(reviews.filter { it.placeId == placeId }.sortedByDescending { it.createdAt })
    }

    override suspend fun submitReview(placeId: String, safetyRating: Int, comment: String?, tags: List<SafetyTag>): Result<SafetyReview, DataError> {
        val review = SafetyReview(id = Uuid.random().toString(), placeId = placeId, reviewerUsername = localUsername(), safetyRating = safetyRating, comment = comment, tags = tags, createdAt = Clock.System.now())
        reviews.add(review)
        return Result.Success(review)
    }
}
