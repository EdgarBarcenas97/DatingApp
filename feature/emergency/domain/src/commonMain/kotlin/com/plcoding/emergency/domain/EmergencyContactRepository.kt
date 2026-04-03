package com.plcoding.emergency.domain

import kotlinx.coroutines.flow.Flow

interface EmergencyContactRepository {
    fun getContacts(): Flow<List<EmergencyContact>>
    suspend fun getContactById(id: String): EmergencyContact?
    suspend fun upsertContact(contact: EmergencyContact)
    suspend fun deleteContact(id: String)
    fun getEmergencySettings(): Flow<EmergencySettings>
    suspend fun updateEmergencyEnabled(enabled: Boolean)
    suspend fun updateDefaultMessage(message: String)
}
