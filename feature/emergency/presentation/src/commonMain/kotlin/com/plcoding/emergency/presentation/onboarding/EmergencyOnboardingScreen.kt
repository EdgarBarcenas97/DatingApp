package com.plcoding.emergency.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle
import com.plcoding.core.designsystem.theme.extended
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmergencyOnboardingRoot(
    onFinish: () -> Unit,
    onAddContactClick: () -> Unit,
    viewModel: EmergencyOnboardingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmergencyOnboardingScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmergencyOnboardingAction.OnSkip,
                EmergencyOnboardingAction.OnFinish -> {
                    viewModel.onAction(action)
                    onFinish()
                }
                EmergencyOnboardingAction.OnAddContactClick -> onAddContactClick()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyOnboardingScreen(
    state: EmergencyOnboardingState,
    onAction: (EmergencyOnboardingAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (state.currentStep > 0) {
                        IconButton(onClick = { onAction(EmergencyOnboardingAction.OnPreviousStep) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Atrás"
                            )
                        }
                    }
                },
                actions = {
                    ChirpButton(
                        text = "Omitir",
                        onClick = { onAction(EmergencyOnboardingAction.OnSkip) },
                        style = ChirpButtonStyle.TEXT
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.2f))

            AnimatedContent(
                targetState = state.currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                    } else {
                        slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                    }
                }
            ) { step ->
                when (step) {
                    0 -> OnboardingStep(
                        icon = Icons.Default.Shield,
                        title = "Tu seguridad es nuestra prioridad",
                        description = "Agrega contactos de emergencia que puedan ayudarte cuando tengas una cita. Ellos recibirán una alerta si algo no va bien."
                    )
                    1 -> OnboardingStep(
                        icon = Icons.Default.Favorite,
                        title = "Personas de confianza",
                        description = "Agrega a familiares o amigos cercanos como contactos de emergencia. Puedes agregar tantos como quieras."
                    )
                    2 -> OnboardingStep(
                        icon = Icons.Default.Notifications,
                        title = "Alerta instantánea",
                        description = "Con un solo toque, tus contactos recibirán un mensaje con tu ubicación. También puedes personalizar el mensaje de emergencia."
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.3f))

            // Step indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                repeat(state.totalSteps) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == state.currentStep) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == state.currentStep) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.extended.textSecondary.copy(alpha = 0.3f)
                                }
                            )
                    )
                }
            }

            if (state.currentStep == state.totalSteps - 1) {
                ChirpButton(
                    text = "Agregar mi primer contacto",
                    onClick = { onAction(EmergencyOnboardingAction.OnAddContactClick) },
                    style = ChirpButtonStyle.PRIMARY,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                ChirpButton(
                    text = "Activar y configurar después",
                    onClick = { onAction(EmergencyOnboardingAction.OnFinish) },
                    style = ChirpButtonStyle.SECONDARY,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                ChirpButton(
                    text = "Siguiente",
                    onClick = { onAction(EmergencyOnboardingAction.OnNextStep) },
                    style = ChirpButtonStyle.PRIMARY,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun OnboardingStep(
    icon: ImageVector,
    title: String,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.extended.textSecondary
        )
    }
}
