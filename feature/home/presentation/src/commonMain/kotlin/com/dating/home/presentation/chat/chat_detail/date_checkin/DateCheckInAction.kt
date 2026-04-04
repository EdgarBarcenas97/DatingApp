package com.dating.home.presentation.chat.chat_detail.date_checkin

sealed interface DateCheckInAction {
    data class OnOpenCheckInSetup(val proposalId: String) : DateCheckInAction
    data object OnDismissSetup : DateCheckInAction
    data class OnContactNameChanged(val name: String) : DateCheckInAction
    data class OnContactPhoneChanged(val phone: String) : DateCheckInAction
    data object OnAddContact : DateCheckInAction
    data class OnRemoveContact(val contactId: String) : DateCheckInAction
    data class OnTimerChanged(val minutes: Int) : DateCheckInAction
    data class OnLiveLocationToggled(val enabled: Boolean) : DateCheckInAction
    data object OnStartCheckIn : DateCheckInAction
    data object OnPerformCheckIn : DateCheckInAction
    data object OnCancelCheckIn : DateCheckInAction
    data class OnExtendTimer(val additionalMinutes: Int) : DateCheckInAction
    data object OnSendManualAlert : DateCheckInAction
}
