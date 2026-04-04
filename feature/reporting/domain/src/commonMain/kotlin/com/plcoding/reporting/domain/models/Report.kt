package com.plcoding.reporting.domain.models

import kotlin.time.Instant

data class Report(
    val id: String,
    val reporterId: String,
    val reportedUserId: String,
    val messageId: String?,
    val chatId: String,
    val type: ReportType,
    val description: String,
    val createdAt: Instant,
    val status: ReportStatus = ReportStatus.PENDING,
    val severity: ReportSeverity = ReportSeverity.MEDIUM
)

enum class ReportStatus {
    PENDING,
    REVIEWING,
    RESOLVED,
    DISMISSED
}

enum class ReportSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}
