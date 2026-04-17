package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.ui.theme.BorderDim
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.StatusOverdue
import com.ledger.task.ui.theme.StatusProgress
import com.ledger.task.ui.theme.SurfaceBackground
import com.ledger.task.ui.theme.TextMuted
import com.ledger.task.ui.theme.TextPrimary
import com.ledger.task.viewmodel.StatsState

/**
 * 统计栏
 */
@Composable
fun StatsBar(
    stats: StatsState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderDim, RoundedCornerShape(6.dp))
            .background(SurfaceBackground, RoundedCornerShape(6.dp))
            .padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        StatItem(
            value = stats.total,
            label = "全部",
            valueColor = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        StatItem(
            value = stats.inProgress,
            label = "进行中",
            valueColor = StatusProgress,
            modifier = Modifier.weight(1f)
        )
        StatItem(
            value = stats.done,
            label = "已完成",
            valueColor = StatusDone,
            modifier = Modifier.weight(1f)
        )
        StatItem(
            value = stats.overdue,
            label = "已逾期",
            valueColor = StatusOverdue,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatItem(
    value: Int,
    label: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(SurfaceBackground, RoundedCornerShape(4.dp))
            .padding(vertical = 14.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = value.toString(),
            color = valueColor,
            fontFamily = FontFamily.Monospace,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = TextMuted,
            fontSize = 12.sp
        )
    }
}
