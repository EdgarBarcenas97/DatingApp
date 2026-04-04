package com.plcoding.reporting.domain.service

import com.plcoding.reporting.domain.models.ContentViolation

interface ContentModerationService {
    suspend fun analyzeMessage(content: String): ContentViolation
    suspend fun detectOffensiveLanguage(content: String): Boolean
    suspend fun detectSexualContent(content: String): Boolean
    suspend fun detectThreats(content: String): Boolean
}
