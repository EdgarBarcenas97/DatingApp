package com.plcoding.core.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequest(
    val reason: String,
    val details: String? = null
)
