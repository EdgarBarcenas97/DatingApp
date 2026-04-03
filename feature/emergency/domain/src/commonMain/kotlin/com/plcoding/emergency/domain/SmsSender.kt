package com.plcoding.emergency.domain

interface SmsSender {
    suspend fun sendSms(phoneNumber: String, message: String): Boolean
    suspend fun sendToAllContacts(contacts: List<EmergencyContact>, message: String): Map<String, Boolean>
}
