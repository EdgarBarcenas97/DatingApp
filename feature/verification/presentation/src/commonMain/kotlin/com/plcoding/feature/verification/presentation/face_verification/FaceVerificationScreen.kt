package com.plcoding.feature.verification.presentation.face_verification

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle
import com.plcoding.core.designsystem.components.layouts.ChirpSnackbarScaffold
import com.plcoding.feature.verification.presentation.face_verification.components.FaceVerificationFeedback
import com.plcoding.feature.verification.presentation.face_verification.components.FaceVerificationGuidelineDialog
import com.plcoding.feature.verification.presentation.face_verification.components.FaceVerificationImagePreview
import com.plcoding.feature.verification.presentation.face_verification.components.FeedbackType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FaceVerificationRoot(
    viewModel: FaceVerificationViewModel = koinViewModel(),
    onSuccess: () -> Unit,
    onDismiss: () -> Unit,
    onUploadPicture: (callback: (ByteArray, String?) -> Unit) -> Unit = { _ -> }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    FaceVerificationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is FaceVerificationAction.OnDismiss -> onDismiss()
                is FaceVerificationAction.OnUploadPictureClick -> {
                    onUploadPicture { bytes, mimeType ->
                        viewModel.onAction(FaceVerificationAction.OnImageSelected(bytes, mimeType))
                    }
                }
                else -> Unit
            }
            viewModel.onAction(action)
        },
        onSuccess = onSuccess
    )
}

@Composable
fun FaceVerificationScreen(
    state: FaceVerificationState,
    onAction: (FaceVerificationAction) -> Unit,
    onSuccess: () -> Unit = {},
) {
    ChirpSnackbarScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TopAppBar(
                title = { Text("Verificar Identidad") },
                navigationIcon = {
                    IconButton(onClick = { onAction(FaceVerificationAction.OnDismiss) }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(
                    targetState = state.currentStep,
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn()).togetherWith(
                            slideOutVertically { -it } + fadeOut()
                        ).using(SizeTransform(clip = false))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { step ->
                    when (step) {
                        VerificationStep.INITIAL -> {
                            InitialContent(onAction = onAction)
                        }
                        VerificationStep.IMAGE_SELECTED -> {
                            ImageSelectedContent(
                                imageBytes = state.selectedImageBytes,
                                onAction = onAction
                            )
                        }
                        VerificationStep.UPLOADING, VerificationStep.VERIFYING -> {
                            LoadingContent(
                                step = step,
                                isUploading = state.isUploading
                            )
                        }
                        VerificationStep.SUCCESS -> {
                            SuccessContent(
                                confidence = state.verificationResult?.confidence,
                                onSuccess = onSuccess
                            )
                        }
                        VerificationStep.FAILED -> {
                            FailedContent(
                                errorMessage = state.errorMessage,
                                retryCount = state.retryCount,
                                onAction = onAction
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showGuidelinesDialog) {
        FaceVerificationGuidelineDialog(
            onDismiss = { onAction(FaceVerificationAction.OnGuidelinesDismiss) }
        )
    }
}

@Composable
private fun InitialContent(
    onAction: (FaceVerificationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Toma una foto clara de tu rostro",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Necesitamos una foto para verificar tu identidad",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ChirpButton(
            text = "Cargar Foto",
            onClick = { onAction(FaceVerificationAction.OnUploadPictureClick) },
            style = ChirpButtonStyle.Primary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ChirpButton(
            text = "Ver Directrices",
            onClick = { onAction(FaceVerificationAction.OnShowGuidelinesClick) },
            style = ChirpButtonStyle.Secondary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ImageSelectedContent(
    imageBytes: ByteArray?,
    onAction: (FaceVerificationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageBytes != null) {
            FaceVerificationImagePreview(
                imageBytes = imageBytes,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        ChirpButton(
            text = "Verificar Rostro",
            onClick = { onAction(FaceVerificationAction.OnVerifyClick) },
            style = ChirpButtonStyle.Primary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ChirpButton(
            text = "Cambiar Foto",
            onClick = { onAction(FaceVerificationAction.OnUploadPictureClick) },
            style = ChirpButtonStyle.Secondary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LoadingContent(
    step: VerificationStep,
    isUploading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isUploading) "Cargando foto..." else "Validando rostro...",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun SuccessContent(
    confidence: Float?,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FaceVerificationFeedback(
            type = FeedbackType.SUCCESS,
            message = "Tu identidad ha sido verificada exitosamente",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (confidence != null) {
            Text(
                text = "Confianza: ${(confidence * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        ChirpButton(
            text = "Continuar",
            onClick = onSuccess,
            style = ChirpButtonStyle.Primary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FailedContent(
    errorMessage: String?,
    retryCount: Int,
    onAction: (FaceVerificationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FaceVerificationFeedback(
            type = FeedbackType.ERROR,
            message = errorMessage ?: "No se pudo validar tu rostro. Intenta nuevamente.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        if (retryCount < 3) {
            ChirpButton(
                text = "Reintentar",
                onClick = { onAction(FaceVerificationAction.OnRetryClick) },
                style = ChirpButtonStyle.Primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Intento ${retryCount} de 3",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = "Máximo de intentos alcanzado.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            ChirpButton(
                text = "Cerrar",
                onClick = { onAction(FaceVerificationAction.OnDismiss) },
                style = ChirpButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
