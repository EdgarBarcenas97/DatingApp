package com.plcoding.feature.verification.domain

import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.Result
import com.plcoding.feature.verification.domain.models.FaceVerificationResult

interface FaceVerificationRepository {
    suspend fun verifyFace(
        imageBytes: ByteArray,
        mimeType: String
    ): Result<FaceVerificationResult, DataError>
}
