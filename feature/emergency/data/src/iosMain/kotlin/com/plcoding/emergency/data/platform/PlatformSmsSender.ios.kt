package com.plcoding.emergency.data.platform

import com.plcoding.emergency.domain.EmergencyContact
import com.plcoding.emergency.domain.SmsSender

actual class PlatformSmsSender : SmsSender {
    override suspend fun sendSms(phoneNumber: String, message: String): Boolean {
        // iOS requires MFMessageComposeViewController which needs UI context
        // This will be triggered via UIApplication
        return false
    }

    override suspend fun sendToAllContacts(
        contacts: List<EmergencyContact>,
        message: String
    ): Map<String, Boolean> {
        return contacts.associate { it.id to sendSms(it.phoneNumber, message) }
    }
}
