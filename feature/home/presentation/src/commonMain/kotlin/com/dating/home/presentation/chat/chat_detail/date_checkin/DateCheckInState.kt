package com.dating.home.presentation.chat.chat_detail.date_checkin

import com.dating.home.domain.models.CheckInStatus
import com.dating.home.domain.models.TrustedContact

data class DateCheckInState(
    val isSetupVisible: Boolean = false,
    val proposalIdForCheckIn: String? = null,
    val matchUsername: String = "",
    val placeName: String = "",
    val placeAddress: String = "",
    val formattedDateTime: String = "",
    val trustedContacts: List<TrustedContact> = emptyList(),
    val newContactName: String = "",
    val newContactPhone: String = "",
    val checkInTimerMinutes: Int = 60,
    val shareLiveLocation: Boolean = true,
    val isSending: Boolean = false,
    val activeCheckInId: String? = null,
    val activeStatus: CheckInStatus = CheckInStatus.NOT_STARTED,
    val remainingMinutes: Int = 0,
    val isLiveLocationActive: Boolean = false
) {
    val hasActiveCheckIn: Boolean get() = activeCheckInId != null && activeStatus == CheckInStatus.ACTIVE
    val canStartCheckIn: Boolean get() = trustedContacts.isNotEmpty() && !isSending
    val canAddContact: Boolean get() = newContactName.isNotBlank() && newContactPhone.length >= 10
}
