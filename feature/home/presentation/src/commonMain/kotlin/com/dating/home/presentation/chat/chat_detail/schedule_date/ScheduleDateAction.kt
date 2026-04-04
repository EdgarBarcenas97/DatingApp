package com.dating.home.presentation.chat.chat_detail.schedule_date

import com.dating.home.domain.models.PlaceSuggestion
import com.dating.home.domain.models.SafetyTag

sealed interface ScheduleDateAction {
    data object OnOpenScheduleDate : ScheduleDateAction
    data object OnDismiss : ScheduleDateAction
    data object OnNextStep : ScheduleDateAction
    data object OnPreviousStep : ScheduleDateAction
    data class OnDateSelected(val year: Int, val month: Int, val day: Int) : ScheduleDateAction
    data class OnTimeSelected(val hour: Int, val minute: Int) : ScheduleDateAction
    data class OnPlaceQueryChanged(val query: String) : ScheduleDateAction
    data class OnPlaceSelected(val place: PlaceSuggestion) : ScheduleDateAction
    data class OnNoteChanged(val note: String) : ScheduleDateAction
    data object OnSendProposal : ScheduleDateAction
    data class OnAcceptProposal(val proposalId: String) : ScheduleDateAction
    data class OnDeclineProposal(val proposalId: String) : ScheduleDateAction
    data class OnModifyProposal(val proposalId: String) : ScheduleDateAction
    data class OnViewSafetyDetail(val place: PlaceSuggestion) : ScheduleDateAction
    data object OnDismissSafetyDetail : ScheduleDateAction
    data class OnOpenReviewSheet(val placeId: String, val placeName: String) : ScheduleDateAction
    data object OnDismissReviewSheet : ScheduleDateAction
    data class OnReviewRatingChanged(val rating: Int) : ScheduleDateAction
    data class OnReviewCommentChanged(val comment: String) : ScheduleDateAction
    data class OnReviewTagToggled(val tag: SafetyTag) : ScheduleDateAction
    data object OnSubmitReview : ScheduleDateAction
}
