package com.plcoding.chat.presentation.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import chirp.core.designsystem.generated.resources.Res as DesignRes
import chirp.core.designsystem.generated.resources.dismiss_dialog
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.cancel
import chirp.feature.chat.presentation.generated.resources.delete_account_confirm
import chirp.feature.chat.presentation.generated.resources.delete_account_description
import chirp.feature.chat.presentation.generated.resources.delete_account_title
import chirp.feature.chat.presentation.generated.resources.reason_bad_experience
import chirp.feature.chat.presentation.generated.resources.reason_found_someone
import chirp.feature.chat.presentation.generated.resources.reason_not_finding_matches
import chirp.feature.chat.presentation.generated.resources.reason_other
import chirp.feature.chat.presentation.generated.resources.reason_other_placeholder
import chirp.feature.chat.presentation.generated.resources.reason_privacy_concerns
import chirp.feature.chat.presentation.generated.resources.reason_taking_a_break
import chirp.feature.chat.presentation.generated.resources.reason_too_many_notifications
import chirp.feature.chat.presentation.generated.resources.why_are_you_leaving
import com.plcoding.chat.presentation.profile.DeleteAccountReason
import com.plcoding.chat.presentation.profile.ProfileAction
import com.plcoding.chat.presentation.profile.ProfileState
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle
import com.plcoding.core.designsystem.components.textfields.ChirpTextField
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteAccountSurveyDialog(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
                .widthIn(max = 480.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.delete_account_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.extended.textPrimary
                )
                Text(
                    text = stringResource(Res.string.delete_account_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary
                )
                Text(
                    text = stringResource(Res.string.why_are_you_leaving),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.extended.textPrimary
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DeleteAccountReason.entries.forEach { reason ->
                        ReasonOption(
                            text = stringResource(reason.toStringResource()),
                            selected = state.selectedDeleteReason == reason,
                            onClick = {
                                onAction(ProfileAction.OnSelectDeleteReason(reason))
                            }
                        )
                    }
                }

                AnimatedVisibility(
                    visible = state.selectedDeleteReason == DeleteAccountReason.OTHER
                ) {
                    ChirpTextField(
                        state = state.otherReasonText,
                        placeholder = stringResource(Res.string.reason_other_placeholder),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChirpButton(
                        text = stringResource(Res.string.cancel),
                        onClick = onDismiss,
                        style = ChirpButtonStyle.SECONDARY
                    )
                    ChirpButton(
                        text = stringResource(Res.string.delete_account_confirm),
                        onClick = {
                            onAction(ProfileAction.OnConfirmDeleteAccount)
                        },
                        style = ChirpButtonStyle.DESTRUCTIVE_PRIMARY,
                        enabled = state.selectedDeleteReason != null && !state.isDeletingAccount,
                        isLoading = state.isDeletingAccount
                    )
                }
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(DesignRes.string.dismiss_dialog),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }
    }
}

@Composable
private fun ReasonOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.extended.textPrimary
        )
    }
}

private fun DeleteAccountReason.toStringResource(): StringResource {
    return when (this) {
        DeleteAccountReason.FOUND_SOMEONE -> Res.string.reason_found_someone
        DeleteAccountReason.NOT_FINDING_MATCHES -> Res.string.reason_not_finding_matches
        DeleteAccountReason.PRIVACY_CONCERNS -> Res.string.reason_privacy_concerns
        DeleteAccountReason.TOO_MANY_NOTIFICATIONS -> Res.string.reason_too_many_notifications
        DeleteAccountReason.BAD_EXPERIENCE -> Res.string.reason_bad_experience
        DeleteAccountReason.TAKING_A_BREAK -> Res.string.reason_taking_a_break
        DeleteAccountReason.OTHER -> Res.string.reason_other
    }
}
