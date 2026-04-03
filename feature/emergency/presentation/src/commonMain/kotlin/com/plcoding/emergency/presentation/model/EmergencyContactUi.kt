package com.plcoding.emergency.presentation.model

data class EmergencyContactUi(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val relationship: String?,
    val initials: String
)
