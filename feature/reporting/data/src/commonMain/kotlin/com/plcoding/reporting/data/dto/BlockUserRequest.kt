package com.plcoding.reporting.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class BlockUserRequest(
    val blockedUserId: String,
    val reason: String
)
