package com.plcoding.feature.verification.presentation.face_verification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle

@Composable
fun FaceVerificationGuidelineDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Directrices para Verificación",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                GuidelineItem(
                    title = "Iluminación",
                    description = "Asegúrate de tener buena iluminación frontal. Evita sombras en tu rostro."
                )
                Spacer(modifier = Modifier.height(12.dp))

                GuidelineItem(
                    title = "Framing",
                    description = "Centra tu rostro en la foto. Incluye desde la frente hasta la barbilla."
                )
                Spacer(modifier = Modifier.height(12.dp))

                GuidelineItem(
                    title = "Expresión",
                    description = "Mira directamente a la cámara con una expresión neutral o sonrisa natural."
                )
                Spacer(modifier = Modifier.height(12.dp))

                GuidelineItem(
                    title = "Accesorios",
                    description = "Evita gafas de sol o accesorios que cubran tu rostro."
                )
                Spacer(modifier = Modifier.height(12.dp))

                GuidelineItem(
                    title = "Fondo",
                    description = "Un fondo simple y claro funciona mejor que fondos complicados."
                )
                Spacer(modifier = Modifier.height(12.dp))

                GuidelineItem(
                    title = "Calidad",
                    description = "Usa una foto clara y enfocada. Evita imágenes pixeladas o borrosas."
                )
            }
        },
        confirmButton = {
            ChirpButton(
                text = "Entendido",
                onClick = onDismiss,
                style = ChirpButtonStyle.Primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
private fun GuidelineItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
