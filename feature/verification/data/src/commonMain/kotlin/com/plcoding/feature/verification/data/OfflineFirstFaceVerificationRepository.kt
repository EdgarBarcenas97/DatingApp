package com.plcoding.feature.verification.data

import com.plcoding.core.domain.auth.SessionStorage
import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.Result
import com.plcoding.core.domain.util.map
import com.plcoding.core.domain.util.onSuccess
import com.plcoding.feature.verification.domain.FaceVerificationRepository
import com.plcoding.feature.verification.domain.FaceVerificationService
import com.plcoding.feature.verification.domain.models.FaceVerificationResult
import kotlinx.coroutines.flow.first

class OfflineFirstFaceVerificationRepository(
    private val faceVerificationService: FaceVerificationService,
    private val sessionStorage: SessionStorage
) : FaceVerificationRepository {

    override suspend fun verifyFace(
        imageBytes: ByteArray,
        mimeType: String
    ): Result<FaceVerificationResult, DataError> {
        return faceVerificationService
            .getFaceVerificationUploadUrl(mimeType)
            .onSuccess { urls ->
                faceVerificationService.uploadFaceVerificationImage(
                    uploadUrl = urls.uploadUrl,
                    imageBytes = imageBytes,
                    headers = urls.headers
                ).onSuccess {
                    faceVerificationService.confirmFaceVerification(urls.publicUrl)
                        .onSuccess { result ->
                            if (result.isValid) {
                                updateUserVerificationStatus()
                            }
                        }
                }
            }
    }

    private suspend fun updateUserVerificationStatus() {
        val currentAuthInfo = sessionStorage.observeAuthInfo().first()
        if (currentAuthInfo != null) {
            val updatedUser = currentAuthInfo.user.copy(hasVerifiedFace = true)
            val updatedAuthInfo = currentAuthInfo.copy(user = updatedUser)
            sessionStorage.set(updatedAuthInfo)
        }
    }
}
