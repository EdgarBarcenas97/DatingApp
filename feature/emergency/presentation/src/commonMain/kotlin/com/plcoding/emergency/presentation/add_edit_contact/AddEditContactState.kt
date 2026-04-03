package com.plcoding.emergency.presentation.add_edit_contact

data class AddEditContactState(
    val contactId: String? = null,
    val name: String = "",
    val phoneNumber: String = "",
    val relationship: String = "",
    val isEditing: Boolean = false,
    val nameError: String? = null,
    val phoneError: String? = null,
    val isSaving: Boolean = false
)
