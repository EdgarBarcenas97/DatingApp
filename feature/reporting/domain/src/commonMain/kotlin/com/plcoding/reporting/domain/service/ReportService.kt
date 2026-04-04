package com.plcoding.reporting.domain.service

import com.plcoding.reporting.domain.models.Report
import com.plcoding.reporting.domain.models.ReportType
import kotlin.time.Instant

interface ReportService {
    suspend fun createReport(
        reporterId: String,
        reportedUserId: String,
        messageId: String?,
        chatId: String,
        type: ReportType,
        description: String
    ): Result<Report>

    suspend fun getReportsByUser(userId: String): Result<List<Report>>
    suspend fun updateReportStatus(reportId: String, status: String): Result<Unit>
}
