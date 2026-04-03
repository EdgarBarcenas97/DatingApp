package com.plcoding.feature.verification.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FaceVerificationResultResponse(
    val isValid: Boolean,
    val error: String? = null,
    val confidence: Float? = null
)
