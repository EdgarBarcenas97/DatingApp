package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.EmergencyContact
import com.plcoding.emergency.domain.SmsSender

actual class PlatformSmsSender : SmsSender {
    override suspend fun sendSms(phoneNumber: String, message: String): Boolean = false
    override suspend fun sendToAllContacts(
        contacts: List<EmergencyContact>,
        message: String
    ): Map<String, Boolean> = emptyMap()
}
