package com.plcoding.feature.verification.domain.models

data class FaceVerificationUrls(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)
