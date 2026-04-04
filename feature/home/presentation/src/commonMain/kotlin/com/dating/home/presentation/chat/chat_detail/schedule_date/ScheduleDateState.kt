package com.dating.home.presentation.chat.chat_detail.schedule_date

import com.dating.home.domain.models.PlaceSafetyInfo
import com.dating.home.domain.models.PlaceSuggestion
import com.dating.home.domain.models.SafetyTag

enum class ScheduleDateStep {
    DATE_TIME,
    PLACE,
    REVIEW
}

data class ScheduleDateState(
    val isVisible: Boolean = false,
    val currentStep: ScheduleDateStep = ScheduleDateStep.DATE_TIME,
    val selectedYear: Int = 0,
    val selectedMonth: Int = 0,
    val selectedDay: Int = 0,
    val selectedHour: Int = 19,
    val selectedMinute: Int = 0,
    val placeQuery: String = "",
    val placeSuggestions: List<PlaceSuggestion> = emptyList(),
    val selectedPlace: PlaceSuggestion? = null,
    val isSearchingPlaces: Boolean = false,
    val note: String = "",
    val isSending: Boolean = false,
    val modifyingProposalId: String? = null,
    val safetyDetailPlace: PlaceSuggestion? = null,
    val safetyDetailInfo: PlaceSafetyInfo? = null,
    val isLoadingSafetyInfo: Boolean = false,
    val isReviewSheetVisible: Boolean = false,
    val reviewPlaceId: String? = null,
    val reviewPlaceName: String = "",
    val reviewRating: Int = 0,
    val reviewComment: String = "",
    val reviewSelectedTags: List<SafetyTag> = emptyList(),
    val isSubmittingReview: Boolean = false
) {
    val canProceedToPlace: Boolean get() = selectedYear > 0 && selectedMonth > 0 && selectedDay > 0
    val canProceedToReview: Boolean get() = selectedPlace != null
    val canSend: Boolean get() = canProceedToPlace && canProceedToReview && !isSending
    val canSubmitReview: Boolean get() = reviewRating > 0 && !isSubmittingReview
}
