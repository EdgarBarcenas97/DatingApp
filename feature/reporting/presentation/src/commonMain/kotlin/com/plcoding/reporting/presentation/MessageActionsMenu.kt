package com.plcoding.reporting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageActionsMenu(
    messageId: String,
    senderId: String,
    onReport: (String, String) -> Unit,
    onBlock: (String) -> Unit,
    isBlocked: Boolean = false,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded.value = true },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More options",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        DropdownMenuItem(
            text = { Text("Report Message") },
            onClick = {
                onReport(messageId, senderId)
                expanded.value = false
            }
        )

        DropdownMenuItem(
            text = { Text(if (isBlocked) "Unblock User" else "Block User") },
            onClick = {
                onBlock(senderId)
                expanded.value = false
            }
        )
    }
}
