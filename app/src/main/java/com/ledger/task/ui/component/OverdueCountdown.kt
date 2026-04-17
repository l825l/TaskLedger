package com.ledger.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.ledger.task.ui.theme.PriorityHigh
import com.ledger.task.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

/**
 * 逾期倒计时组件
 */
@Composable
fun OverdueCountdown(
    deadline: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val deadlineDate = deadline.toLocalDate()
    val daysBetween = Period.between(today, deadlineDate).days
    val daysOverdue = -daysBetween

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        if (daysOverdue > 0) {
            Text(
                text = "已逾期 ${daysOverdue} 天",
                color = PriorityHigh,
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace
            )
        } else if (daysBetween == 0) {
            Text(
                text = "今天到期",
                color = com.ledger.task.ui.theme.PriorityMid,
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace
            )
        } else {
            Text(
                text = "还有 ${daysBetween} 天",
                color = TextSecondary,
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
