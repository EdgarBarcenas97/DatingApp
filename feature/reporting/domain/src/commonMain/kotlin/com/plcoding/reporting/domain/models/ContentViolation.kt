package com.plcoding.reporting.domain.models

data class ContentViolation(
    val isViolation: Boolean,
    val violationType: ViolationType?,
    val confidenceScore: Float = 0f,
    val message: String = ""
)

enum class ViolationType {
    OFFENSIVE_LANGUAGE,
    SEXUAL_CONTENT,
    THREATS,
    SPAM
}
