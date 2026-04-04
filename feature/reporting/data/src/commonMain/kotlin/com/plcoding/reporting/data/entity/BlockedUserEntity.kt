package com.plcoding.reporting.data.entity

data class BlockedUserEntity(
    val userId: String,
    val blockedByUserId: String,
    val reason: String,
    val blockedAt: Long,
    val isAutomatic: Boolean = false
)
