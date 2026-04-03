package com.plcoding.feature.verification.presentation.face_verification

sealed interface FaceVerificationAction {
    data class OnImageSelected(val bytes: ByteArray, val mimeType: String?) : FaceVerificationAction {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is OnImageSelected) return false

            if (!bytes.contentEquals(other.bytes)) return false
            if (mimeType != other.mimeType) return false

            return true
        }

        override fun hashCode(): Int {
            var result = bytes.contentHashCode()
            result = 31 * result + (mimeType?.hashCode() ?: 0)
            return result
        }
    }

    data object OnVerifyClick : FaceVerificationAction
    data object OnRetryClick : FaceVerificationAction
    data object OnSkipClick : FaceVerificationAction
    data object OnShowGuidelinesClick : FaceVerificationAction
    data object OnGuidelinesDismiss : FaceVerificationAction
    data object OnDismiss : FaceVerificationAction
}
