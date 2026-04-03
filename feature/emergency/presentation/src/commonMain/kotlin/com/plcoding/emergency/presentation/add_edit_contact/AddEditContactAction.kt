package com.plcoding.emergency.presentation.add_edit_contact

sealed interface AddEditContactAction {
    data class OnNameChanged(val name: String) : AddEditContactAction
    data class OnPhoneNumberChanged(val phoneNumber: String) : AddEditContactAction
    data class OnRelationshipChanged(val relationship: String) : AddEditContactAction
    data object OnSaveClick : AddEditContactAction
    data object OnBackClick : AddEditContactAction
}
