package com.ledger.task.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Color(0xFFFFFFFF),
    secondary = AccentDim,
    onSecondary = TextPrimary,
    tertiary = PriorityMid,
    background = DeepBackground,
    onBackground = TextPrimary,
    surface = SurfaceBackground,
    onSurface = TextPrimary,
    surfaceVariant = ElevatedBackground,
    onSurfaceVariant = TextSecondary,
    outline = BorderDim,
    outlineVariant = BorderBright,
    error = PriorityHigh,
    onError = Color(0xFFFFFFFF)
)

@Composable
fun TaskLedgerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = TaskLedgerTypography,
        content = content
    )
}
