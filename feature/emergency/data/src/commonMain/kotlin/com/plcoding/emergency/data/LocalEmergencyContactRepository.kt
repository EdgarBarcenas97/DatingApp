package com.plcoding.emergency.data

import com.plcoding.emergency.data.mappers.toDomain
import com.plcoding.emergency.data.mappers.toEntity
import com.plcoding.emergency.database.dao.EmergencyContactDao
import com.plcoding.emergency.database.dao.EmergencySettingsDao
import com.plcoding.emergency.database.entities.EmergencySettingsEntity
import com.plcoding.emergency.domain.EmergencyContact
import com.plcoding.emergency.domain.EmergencyContactRepository
import com.plcoding.emergency.domain.EmergencySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalEmergencyContactRepository(
    private val contactDao: EmergencyContactDao,
    private val settingsDao: EmergencySettingsDao
) : EmergencyContactRepository {

    override fun getContacts(): Flow<List<EmergencyContact>> {
        return contactDao.observeContacts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getContactById(id: String): EmergencyContact? {
        return contactDao.getContactById(id)?.toDomain()
    }

    override suspend fun upsertContact(contact: EmergencyContact) {
        contactDao.upsertContact(contact.toEntity())
    }

    override suspend fun deleteContact(id: String) {
        contactDao.deleteContact(id)
    }

    override fun getEmergencySettings(): Flow<EmergencySettings> {
        return settingsDao.observeSettings().map { it.toDomain() }
    }

    override suspend fun updateEmergencyEnabled(enabled: Boolean) {
        ensureSettingsExist()
        settingsDao.updateEnabled(enabled)
    }

    override suspend fun updateDefaultMessage(message: String) {
        ensureSettingsExist()
        settingsDao.updateDefaultMessage(message)
    }

    private suspend fun ensureSettingsExist() {
        settingsDao.upsertSettings(EmergencySettingsEntity())
    }
}
