package com.plcoding.feature.verification.presentation.face_verification

import com.plcoding.feature.verification.domain.models.FaceVerificationError
import com.plcoding.feature.verification.domain.models.FaceVerificationResult

data class FaceVerificationState(
    val currentStep: VerificationStep = VerificationStep.INITIAL,
    val selectedImageBytes: ByteArray? = null,
    val selectedImageMimeType: String? = null,
    val isUploading: Boolean = false,
    val isVerifying: Boolean = false,
    val verificationResult: FaceVerificationResult? = null,
    val errorMessage: String? = null,
    val showGuidelinesDialog: Boolean = false,
    val retryCount: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FaceVerificationState) return false

        if (currentStep != other.currentStep) return false
        if (selectedImageBytes != null) {
            if (other.selectedImageBytes == null) return false
            if (!selectedImageBytes.contentEquals(other.selectedImageBytes)) return false
        } else if (other.selectedImageBytes != null) return false
        if (selectedImageMimeType != other.selectedImageMimeType) return false
        if (isUploading != other.isUploading) return false
        if (isVerifying != other.isVerifying) return false
        if (verificationResult != other.verificationResult) return false
        if (errorMessage != other.errorMessage) return false
        if (showGuidelinesDialog != other.showGuidelinesDialog) return false
        if (retryCount != other.retryCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currentStep.hashCode()
        result = 31 * result + (selectedImageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (selectedImageMimeType?.hashCode() ?: 0)
        result = 31 * result + isUploading.hashCode()
        result = 31 * result + isVerifying.hashCode()
        result = 31 * result + (verificationResult?.hashCode() ?: 0)
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        result = 31 * result + showGuidelinesDialog.hashCode()
        result = 31 * result + retryCount
        return result
    }
}

enum class VerificationStep {
    INITIAL,
    IMAGE_SELECTED,
    UPLOADING,
    VERIFYING,
    SUCCESS,
    FAILED
}
