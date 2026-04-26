package com.ledger.task.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.ui.theme.getTextMuted

/**
 * 空状态组件（带动画效果）
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    height: Int = 0,
    text: String = "没有匹配的任务"
) {
    val heightDp = if (height > 0) height.dp else 200.dp

    // 淡入淡出动画
    val infiniteTransition = rememberInfiniteTransition(label = "empty_state_animation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    // 轻微浮动动画
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Column(
        modifier = modifier.height(heightDp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📋",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
            color = getTextMuted().copy(alpha = alpha),
            modifier = Modifier.graphicsLayer {
                translationY = floatOffset
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            color = getTextMuted(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
