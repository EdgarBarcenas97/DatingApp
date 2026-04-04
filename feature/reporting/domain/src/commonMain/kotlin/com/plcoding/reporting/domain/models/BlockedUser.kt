package com.plcoding.reporting.domain.models

import kotlin.time.Instant

data class BlockedUser(
    val userId: String,
    val blockedByUserId: String,
    val reason: String,
    val blockedAt: Instant,
    val isAutomatic: Boolean = false
)
