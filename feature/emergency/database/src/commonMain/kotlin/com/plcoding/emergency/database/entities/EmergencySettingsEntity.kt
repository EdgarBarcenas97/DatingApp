package com.plcoding.emergency.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmergencySettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val isEnabled: Boolean = false,
    val defaultMessage: String = "Estoy en una situación de emergencia. Por favor contáctame o llama a emergencias."
)
