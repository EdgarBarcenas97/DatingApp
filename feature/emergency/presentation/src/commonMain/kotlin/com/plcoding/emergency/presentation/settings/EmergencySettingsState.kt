package com.plcoding.emergency.presentation.settings

import com.plcoding.emergency.presentation.model.EmergencyContactUi

data class EmergencySettingsState(
    val isEnabled: Boolean = false,
    val contacts: List<EmergencyContactUi> = emptyList(),
    val defaultMessage: String = "",
    val isEditingMessage: Boolean = false,
    val showDeleteConfirmation: String? = null,
    val hasCompletedOnboarding: Boolean = false
)
