package com.plcoding.feature.verification.presentation.face_verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.core.domain.auth.SessionStorage
import com.plcoding.core.domain.util.onFailure
import com.plcoding.core.domain.util.onSuccess
import com.plcoding.feature.verification.domain.FaceVerificationRepository
import com.plcoding.feature.verification.domain.UpdateUserVerificationStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FaceVerificationViewModel(
    private val faceVerificationRepository: FaceVerificationRepository,
    private val updateUserVerificationStatusUseCase: UpdateUserVerificationStatusUseCase,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow(FaceVerificationState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = FaceVerificationState()
        )

    fun onAction(action: FaceVerificationAction) {
        when (action) {
            is FaceVerificationAction.OnUploadPictureClick -> {
                // This will be handled by the composable
            }
            is FaceVerificationAction.OnImageSelected -> selectImage(action.bytes, action.mimeType)
            is FaceVerificationAction.OnVerifyClick -> verifyFace()
            is FaceVerificationAction.OnRetryClick -> retryVerification()
            is FaceVerificationAction.OnSkipClick -> skipVerification()
            is FaceVerificationAction.OnShowGuidelinesClick -> showGuidelines()
            is FaceVerificationAction.OnGuidelinesDismiss -> dismissGuidelines()
            is FaceVerificationAction.OnDismiss -> dismissScreen()
        }
    }

    private fun selectImage(bytes: ByteArray, mimeType: String?) {
        _state.update { currentState ->
            currentState.copy(
                selectedImageBytes = bytes,
                selectedImageMimeType = mimeType,
                currentStep = VerificationStep.IMAGE_SELECTED,
                errorMessage = null
            )
        }
    }

    private fun verifyFace() {
        val currentState = _state.value
        val imageBytes = currentState.selectedImageBytes ?: return
        val mimeType = currentState.selectedImageMimeType ?: "image/jpeg"

        if (currentState.retryCount >= 3) {
            _state.update { it.copy(
                errorMessage = "Máximo de intentos alcanzado. Por favor, contacta a soporte."
            ) }
            return
        }

        _state.update { it.copy(
            isUploading = true,
            isVerifying = false,
            currentStep = VerificationStep.UPLOADING,
            errorMessage = null
        ) }

        viewModelScope.launch {
            faceVerificationRepository.verifyFace(imageBytes, mimeType)
                .onSuccess { result ->
                    _state.update { currentState ->
                        currentState.copy(
                            isUploading = false,
                            isVerifying = false,
                            verificationResult = result,
                            currentStep = if (result.isValid) {
                                VerificationStep.SUCCESS
                            } else {
                                VerificationStep.FAILED
                            },
                            errorMessage = result.error?.userMessage,
                            retryCount = if (result.isValid) currentState.retryCount else currentState.retryCount + 1
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { currentState ->
                        currentState.copy(
                            isUploading = false,
                            isVerifying = false,
                            currentStep = VerificationStep.FAILED,
                            errorMessage = "Error al procesar la imagen. Intenta nuevamente.",
                            retryCount = currentState.retryCount + 1
                        )
                    }
                }
        }
    }

    private fun retryVerification() {
        _state.update { it.copy(
            currentStep = VerificationStep.IMAGE_SELECTED,
            verificationResult = null,
            errorMessage = null
        ) }
    }

    private fun skipVerification() {
        dismissScreen()
    }

    private fun showGuidelines() {
        _state.update { it.copy(showGuidelinesDialog = true) }
    }

    private fun dismissGuidelines() {
        _state.update { it.copy(showGuidelinesDialog = false) }
    }

    private fun dismissScreen() {
        _state.update { FaceVerificationState() }
    }
}
