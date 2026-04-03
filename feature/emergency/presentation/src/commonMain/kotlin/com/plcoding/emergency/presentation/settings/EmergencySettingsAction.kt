package com.plcoding.emergency.presentation.settings

sealed interface EmergencySettingsAction {
    data class OnToggleEnabled(val enabled: Boolean) : EmergencySettingsAction
    data object OnAddContactClick : EmergencySettingsAction
    data class OnEditContactClick(val contactId: String) : EmergencySettingsAction
    data class OnDeleteContactClick(val contactId: String) : EmergencySettingsAction
    data object OnConfirmDelete : EmergencySettingsAction
    data object OnDismissDeleteDialog : EmergencySettingsAction
    data class OnDefaultMessageChanged(val message: String) : EmergencySettingsAction
    data object OnSaveMessage : EmergencySettingsAction
    data object OnEditMessageClick : EmergencySettingsAction
    data object OnCancelEditMessage : EmergencySettingsAction
    data object OnBackClick : EmergencySettingsAction
    data object OnOnboardingClick : EmergencySettingsAction
}
