package com.plcoding.emergency.data.platform

import android.content.Context
import android.telephony.SmsManager
import com.plcoding.emergency.domain.EmergencyContact
import com.plcoding.emergency.domain.SmsSender

actual class PlatformSmsSender(
    private val context: Context
) : SmsSender {

    override suspend fun sendSms(phoneNumber: String, message: String): Boolean {
        return try {
            val smsManager = context.getSystemService(SmsManager::class.java)
            val parts = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun sendToAllContacts(
        contacts: List<EmergencyContact>,
        message: String
    ): Map<String, Boolean> {
        return contacts.associate { contact ->
            contact.id to sendSms(contact.phoneNumber, message)
        }
    }
}
