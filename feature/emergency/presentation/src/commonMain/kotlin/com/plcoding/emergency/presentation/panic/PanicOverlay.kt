package com.plcoding.emergency.presentation.panic

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle
import com.plcoding.emergency.domain.PanicState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PanicOverlay(
    viewModel: PanicOverlayViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (!state.isEmergencyEnabled || state.contactCount == 0) return

    Box(modifier = Modifier.fillMaxSize()) {
        when (state.panicState) {
            PanicState.IDLE -> {
                // Floating SOS button - bottom start
                FloatingActionButton(
                    onClick = { viewModel.onAction(PanicOverlayAction.OnPanicButtonClick) },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 24.dp)
                        .size(56.dp),
                    containerColor = Color(0xFFDC2626),
                    contentColor = Color.White,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Text(
                        text = "SOS",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }
            }

            PanicState.CONFIRMING -> {
                // Fullscreen confirmation overlay with countdown
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFDC2626)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Enviando alerta en",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDC2626)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${state.countdownSeconds}",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Se enviará tu ubicación a ${state.contactCount} contacto(s) de emergencia",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ChirpButton(
                                text = "Cancelar",
                                onClick = { viewModel.onAction(PanicOverlayAction.OnCancelPanic) },
                                style = ChirpButtonStyle.SECONDARY
                            )
                            ChirpButton(
                                text = "Enviar ahora",
                                onClick = { viewModel.onAction(PanicOverlayAction.OnConfirmPanic) },
                                style = ChirpButtonStyle.DESTRUCTIVE_PRIMARY
                            )
                        }
                    }
                }
            }

            PanicState.ACTIVE -> {
                // Active panic screen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFDC2626)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "ALERTA ACTIVA",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tu ubicación se está compartiendo con tus contactos de emergencia",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(48.dp))

                        ChirpButton(
                            text = "Llamar al 911",
                            onClick = { viewModel.onAction(PanicOverlayAction.OnCallEmergency) },
                            style = ChirpButtonStyle.SECONDARY,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            },
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ChirpButton(
                            text = "Desactivar alerta",
                            onClick = { viewModel.onAction(PanicOverlayAction.OnDeactivatePanic) },
                            style = ChirpButtonStyle.SECONDARY,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            },
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                    }
                }
            }

            PanicState.COOLDOWN -> {
                // Brief transition state, nothing to show
            }
        }
    }
}
