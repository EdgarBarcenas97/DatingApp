package com.plcoding.reporting.data.service

import com.plcoding.reporting.domain.models.Report
import com.plcoding.reporting.domain.models.ReportSeverity
import com.plcoding.reporting.domain.models.ReportStatus
import com.plcoding.reporting.domain.models.ReportType
import com.plcoding.reporting.domain.service.ContentModerationService
import com.plcoding.reporting.domain.service.ReportService
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

class ReportServiceImpl(
    private val contentModerationService: ContentModerationService
) : ReportService {

    private val reports = mutableMapOf<String, Report>()

    override suspend fun createReport(
        reporterId: String,
        reportedUserId: String,
        messageId: String?,
        chatId: String,
        type: ReportType,
        description: String
    ): Result<Report> {
        return try {
            val violation = if (messageId != null) {
                contentModerationService.analyzeMessage(description)
            } else {
                null
            }

            val severity = when {
                violation?.violationType?.name?.contains("THREATS") == true -> ReportSeverity.CRITICAL
                violation?.violationType?.name?.contains("SEXUAL") == true -> ReportSeverity.HIGH
                violation != null -> ReportSeverity.MEDIUM
                else -> ReportSeverity.LOW
            }

            val report = Report(
                id = Uuid.random().toString(),
                reporterId = reporterId,
                reportedUserId = reportedUserId,
                messageId = messageId,
                chatId = chatId,
                type = type,
                description = description,
                createdAt = Clock.System.now(),
                status = ReportStatus.PENDING,
                severity = severity
            )

            reports[report.id] = report
            Result.success(report)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReportsByUser(userId: String): Result<List<Report>> {
        return try {
            val userReports = reports.values.filter { it.reporterId == userId }
            Result.success(userReports)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateReportStatus(reportId: String, status: String): Result<Unit> {
        return try {
            val report = reports[reportId]
            if (report != null) {
                val newStatus = ReportStatus.valueOf(status.uppercase())
                reports[reportId] = report.copy(status = newStatus)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Report not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
