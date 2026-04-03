package com.plcoding.emergency.domain

data class EmergencyContact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val relationship: String? = null,
    val createdAt: Long = 0L
)
