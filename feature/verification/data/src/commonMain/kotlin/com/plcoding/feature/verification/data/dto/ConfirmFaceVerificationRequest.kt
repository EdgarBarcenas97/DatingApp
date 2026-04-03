package com.plcoding.feature.verification.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmFaceVerificationRequest(
    val imageUrl: String
)
