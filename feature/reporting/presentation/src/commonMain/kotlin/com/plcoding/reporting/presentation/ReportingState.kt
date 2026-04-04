package com.plcoding.reporting.presentation

data class ReportingState(
    val isLoading: Boolean = false,
    val showReportDialog: Boolean = false,
    val selectedMessageId: String? = null,
    val selectedUserId: String? = null,
    val reportDescription: String = "",
    val reportType: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val blockedUsers: List<String> = emptyList()
)
