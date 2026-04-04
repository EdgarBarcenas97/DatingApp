package com.dating.auth.presentation.feature_tour

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import aura.feature.auth.presentation.generated.resources.Res
import aura.feature.auth.presentation.generated.resources.feature_tour_emergency_description
import aura.feature.auth.presentation.generated.resources.feature_tour_emergency_title
import aura.feature.auth.presentation.generated.resources.feature_tour_get_started
import aura.feature.auth.presentation.generated.resources.feature_tour_next
import aura.feature.auth.presentation.generated.resources.feature_tour_schedule_description
import aura.feature.auth.presentation.generated.resources.feature_tour_schedule_title
import aura.feature.auth.presentation.generated.resources.feature_tour_skip
import aura.feature.auth.presentation.generated.resources.feature_tour_welcome_description
import aura.feature.auth.presentation.generated.resources.feature_tour_welcome_title
import com.dating.core.designsystem.components.buttons.AppButtonStyle
import com.dating.core.designsystem.components.buttons.ChirpButton
import com.dating.core.designsystem.components.layouts.AuthSnackbarScaffold
import com.dating.core.designsystem.theme.AppTheme
import com.dating.core.designsystem.theme.extended
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FeatureTourRoot(
    onFinish: () -> Unit
) {
    FeatureTourScreen(onFinish = onFinish)
}

@Composable
fun FeatureTourScreen(
    onFinish: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val successColor = MaterialTheme.colorScheme.extended.success
    val blueColor = MaterialTheme.colorScheme.extended.accentBlue

    val pages = listOf(
        FeatureTourPage(
            icon = Icons.Default.Favorite,
            title = stringResource(Res.string.feature_tour_welcome_title),
            description = stringResource(Res.string.feature_tour_welcome_description),
            accentColor = primaryColor
        ),
        FeatureTourPage(
            icon = Icons.Default.Shield,
            title = stringResource(Res.string.feature_tour_emergency_title),
            description = stringResource(Res.string.feature_tour_emergency_description),
            accentColor = successColor
        ),
        FeatureTourPage(
            icon = Icons.Default.DateRange,
            title = stringResource(Res.string.feature_tour_schedule_title),
            description = stringResource(Res.string.feature_tour_schedule_description),
            accentColor = blueColor
        ),
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.size - 1

    AuthSnackbarScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                val page = pages[pageIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(page.accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = page.icon,
                            contentDescription = null,
                            tint = page.accentColor,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.extended.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Page indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                pages.forEachIndexed { index, _ ->
                    val isSelected = pagerState.currentPage == index
                    val color by animateColorAsState(
                        targetValue = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.extended.disabledFill
                        }
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ChirpButton(
                text = if (isLastPage) {
                    stringResource(Res.string.feature_tour_get_started)
                } else {
                    stringResource(Res.string.feature_tour_next)
                },
                onClick = {
                    if (isLastPage) {
                        onFinish()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (!isLastPage) {
                Spacer(modifier = Modifier.height(8.dp))

                ChirpButton(
                    text = stringResource(Res.string.feature_tour_skip),
                    onClick = onFinish,
                    style = AppButtonStyle.TEXT,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private data class FeatureTourPage(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val accentColor: Color
)

@Preview
@Composable
private fun FeatureTourScreenPreview() {
    AppTheme {
        FeatureTourScreen(onFinish = {})
    }
}
