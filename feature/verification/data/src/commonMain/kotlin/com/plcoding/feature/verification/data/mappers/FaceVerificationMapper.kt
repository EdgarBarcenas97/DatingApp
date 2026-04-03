package com.plcoding.feature.verification.data.mappers

import com.plcoding.feature.verification.data.dto.FaceVerificationResultResponse
import com.plcoding.feature.verification.data.dto.FaceVerificationUrlsResponse
import com.plcoding.feature.verification.domain.models.FaceVerificationError
import com.plcoding.feature.verification.domain.models.FaceVerificationResult
import com.plcoding.feature.verification.domain.models.FaceVerificationUrls

fun FaceVerificationUrlsResponse.toDomain(): FaceVerificationUrls {
    return FaceVerificationUrls(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers
    )
}

fun FaceVerificationResultResponse.toDomain(): FaceVerificationResult {
    return FaceVerificationResult(
        isValid = isValid,
        error = error?.let { errorString ->
            FaceVerificationError.entries.find { it.name == errorString }
        },
        confidence = confidence
    )
}
