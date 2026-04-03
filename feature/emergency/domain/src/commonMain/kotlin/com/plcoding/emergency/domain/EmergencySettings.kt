package com.plcoding.emergency.domain

data class EmergencySettings(
    val isEnabled: Boolean = false,
    val defaultMessage: String = ""
)
