package com.plcoding.feature.verification.presentation.face_verification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FaceVerificationFeedback(
    type: FeedbackType,
    message: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (type) {
        FeedbackType.SUCCESS -> MaterialTheme.colorScheme.primaryContainer
        FeedbackType.ERROR -> MaterialTheme.colorScheme.errorContainer
        FeedbackType.WARNING -> MaterialTheme.colorScheme.tertiaryContainer
    }

    val textColor = when (type) {
        FeedbackType.SUCCESS -> MaterialTheme.colorScheme.onPrimaryContainer
        FeedbackType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        FeedbackType.WARNING -> MaterialTheme.colorScheme.onTertiaryContainer
    }

    val icon = when (type) {
        FeedbackType.SUCCESS -> Icons.Default.CheckCircle
        FeedbackType.ERROR -> Icons.Default.Error
        FeedbackType.WARNING -> Icons.Default.Warning
    }

    val iconTint = when (type) {
        FeedbackType.SUCCESS -> MaterialTheme.colorScheme.primary
        FeedbackType.ERROR -> MaterialTheme.colorScheme.error
        FeedbackType.WARNING -> MaterialTheme.colorScheme.tertiary
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

enum class FeedbackType {
    SUCCESS,
    ERROR,
    WARNING
}
