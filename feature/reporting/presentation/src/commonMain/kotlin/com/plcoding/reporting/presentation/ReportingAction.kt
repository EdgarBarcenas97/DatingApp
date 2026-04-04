package com.plcoding.reporting.presentation

sealed class ReportingAction {
    data class OpenReportDialog(val messageId: String, val userId: String) : ReportingAction()
    object CloseReportDialog : ReportingAction()
    data class SetReportDescription(val description: String) : ReportingAction()
    data class SetReportType(val type: String) : ReportingAction()
    object SubmitReport : ReportingAction()
    data class BlockUser(val userId: String, val reason: String) : ReportingAction()
    data class UnblockUser(val userId: String) : ReportingAction()
    object LoadBlockedUsers : ReportingAction()
}
