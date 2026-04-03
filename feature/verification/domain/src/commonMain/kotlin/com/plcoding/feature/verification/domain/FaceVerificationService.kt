package com.plcoding.feature.verification.domain

import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.Result
import com.plcoding.feature.verification.domain.models.FaceVerificationResult
import com.plcoding.feature.verification.domain.models.FaceVerificationUrls

interface FaceVerificationService {
    suspend fun getFaceVerificationUploadUrl(
        mimeType: String
    ): Result<FaceVerificationUrls, DataError.Remote>

    suspend fun uploadFaceVerificationImage(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote>

    suspend fun confirmFaceVerification(
        imageUrl: String
    ): Result<FaceVerificationResult, DataError.Remote>
}
