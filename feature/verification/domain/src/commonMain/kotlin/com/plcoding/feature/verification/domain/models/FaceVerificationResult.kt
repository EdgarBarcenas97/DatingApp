package com.plcoding.feature.verification.domain.models

data class FaceVerificationResult(
    val isValid: Boolean,
    val error: FaceVerificationError? = null,
    val confidence: Float? = null
)
