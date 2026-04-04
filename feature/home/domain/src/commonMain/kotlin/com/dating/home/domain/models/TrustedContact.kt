package com.dating.home.domain.models

data class TrustedContact(
    val id: String,
    val name: String,
    val phone: String,
    val notifyByCall: Boolean = false,
    val notifyBySms: Boolean = true
)
