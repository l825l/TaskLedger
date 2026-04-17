package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.ledger.task.data.model.DisplayStatus
import com.ledger.task.data.model.Task
import com.ledger.task.ui.theme.BorderDim
import com.ledger.task.ui.theme.PriorityMid
import com.ledger.task.ui.theme.TextMuted
import com.ledger.task.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 任务行
 */
@Composable
fun TaskRow(
    task: Task,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val today = LocalDate.now().toEpochDay()
    val isOverdue = task.displayStatus == DisplayStatus.OVERDUE
    val isSoon = task.displayStatus != DisplayStatus.DONE &&
            !isOverdue &&
            task.deadline.toLocalDate().isBefore(LocalDate.now().plusDays(3))

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 序号
            Text(
                text = String.format("%02d", index + 1),
                color = TextMuted,
                style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.width(40.dp)
            )

            // 标题
            Text(
                text = task.title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 优先级
            PriorityBadge(priority = task.priority)

            Spacer(modifier = Modifier.width(16.dp))

            // 时限
            Text(
                text = task.deadline.format(formatter),
                color = when {
                    isOverdue -> MaterialTheme.colorScheme.error
                    isSoon -> PriorityMid
                    else -> TextSecondary
                },
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.width(90.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 状态
            StatusTag(displayStatus = task.displayStatus)
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderDim, RoundedCornerShape(1.dp))
        )
    }
}
