package com.plcoding.reporting.data.entity

data class UserReputationEntity(
    val userId: String,
    val reputationScore: Int = 100,
    val reportCount: Int = 0,
    val isBlocked: Boolean = false,
    val isBanned: Boolean = false
)
