package com.plcoding.feature.verification.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FaceVerificationUrlsResponse(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)
