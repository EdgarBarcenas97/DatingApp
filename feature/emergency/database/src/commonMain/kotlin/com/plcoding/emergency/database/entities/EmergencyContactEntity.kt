package com.plcoding.emergency.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmergencyContactEntity(
    @PrimaryKey
    val contactId: String,
    val name: String,
    val phoneNumber: String,
    val relationship: String?,
    val createdAt: Long
)
