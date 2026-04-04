package com.plcoding.reporting.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportResponse(
    val id: String,
    val reporterId: String,
    val reportedUserId: String,
    val messageId: String?,
    val chatId: String,
    val type: String,
    val description: String,
    val createdAt: Long,
    val status: String,
    val severity: String
)
