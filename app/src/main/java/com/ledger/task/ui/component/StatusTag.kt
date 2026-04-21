package com.ledger.task.ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.StatusDoneBg
import com.ledger.task.ui.theme.StatusOverdue
import com.ledger.task.ui.theme.StatusOverdueBg
import com.ledger.task.ui.theme.StatusPending
import com.ledger.task.ui.theme.StatusPendingBg
import com.ledger.task.ui.theme.StatusProgress
import com.ledger.task.ui.theme.StatusProgressBg

/**
 * 状态标签
 */
@Composable
fun StatusTag(displayStatus: DisplayStatus, modifier: Modifier = Modifier) {
    val (color, bgColor) = when (displayStatus) {
        DisplayStatus.DONE -> StatusDone to StatusDoneBg
        DisplayStatus.IN_PROGRESS -> StatusProgress to StatusProgressBg
        DisplayStatus.PENDING -> StatusPending to StatusPendingBg
        DisplayStatus.OVERDUE -> StatusOverdue to StatusOverdueBg
    }

    // 进行中状态的脉冲动画
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (displayStatus == DisplayStatus.IN_PROGRESS) {
                        Modifier.graphicsLayer { this.alpha = alpha }
                    } else {
                        Modifier
                    }
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = displayStatus.label,
            color = color,
            style = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
                fontFamily = androidx.compose.ui.text.font.FontFamily.Default
            )
        )
    }
}
