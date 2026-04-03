package com.plcoding.emergency.data.mappers

import com.plcoding.emergency.database.entities.EmergencyContactEntity
import com.plcoding.emergency.database.entities.EmergencySettingsEntity
import com.plcoding.emergency.domain.EmergencyContact
import com.plcoding.emergency.domain.EmergencySettings

fun EmergencyContactEntity.toDomain(): EmergencyContact {
    return EmergencyContact(
        id = contactId,
        name = name,
        phoneNumber = phoneNumber,
        relationship = relationship,
        createdAt = createdAt
    )
}

fun EmergencyContact.toEntity(): EmergencyContactEntity {
    return EmergencyContactEntity(
        contactId = id,
        name = name,
        phoneNumber = phoneNumber,
        relationship = relationship,
        createdAt = createdAt
    )
}

fun EmergencySettingsEntity?.toDomain(): EmergencySettings {
    return if (this == null) {
        EmergencySettings()
    } else {
        EmergencySettings(
            isEnabled = isEnabled,
            defaultMessage = defaultMessage
        )
    }
}
