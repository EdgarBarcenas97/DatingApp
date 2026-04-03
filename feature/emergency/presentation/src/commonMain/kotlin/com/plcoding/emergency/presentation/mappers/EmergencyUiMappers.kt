package com.plcoding.emergency.presentation.mappers

import com.plcoding.emergency.domain.EmergencyContact
import com.plcoding.emergency.presentation.model.EmergencyContactUi

fun EmergencyContact.toUi(): EmergencyContactUi {
    val initials = name.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { name.take(1).uppercase() }

    return EmergencyContactUi(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        relationship = relationship,
        initials = initials
    )
}
