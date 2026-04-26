package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.ui.theme.Accent
import com.ledger.task.ui.theme.getBorderDim
import com.ledger.task.ui.theme.getDeepBackground
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.PriorityHigh
import com.ledger.task.ui.theme.PriorityLow
import com.ledger.task.ui.theme.PriorityMid
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary
import java.time.format.DateTimeFormatter

/**
 * 关联任务小卡片
 */
@Composable
fun RelatedTaskChip(
    task: Task,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("MM.dd")
    val isOverdue = task.displayStatus == com.ledger.task.domain.model.DisplayStatus.OVERDUE

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(getSurfaceBackground())
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .clickable { onClose() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 优先级指示器
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        when (task.priority) {
                            Priority.HIGH -> PriorityHigh
                            Priority.MEDIUM -> PriorityMid
                            Priority.NORMAL -> PriorityMid
                            Priority.LOW -> PriorityLow
                        }
                    )
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    color = getTextPrimary(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "截止: ${task.deadline.format(formatter)}",
                        color = if (isOverdue) PriorityHigh else getTextMuted(),
                        style = MaterialTheme.typography.labelSmall
                    )
                    StatusTag(displayStatus = task.displayStatus)
                }
            }

            // 关闭按钮
            androidx.compose.material3.IconButton(
                onClick = onClose,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "移除",
                    tint = getTextMuted()
                )
            }
        }
    }
}

/**
 * 关联事项组件（前置依赖 + 相关任务合并）
 */
@Composable
fun RelatedTasksContainer(
    title: String,
    tasks: List<Task>,
    onRemove: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = getTextMuted(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(getDeepBackground())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "未选择关联任务",
                    color = getTextMuted(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(tasks, key = { it.id }) { task ->
                    RelatedTaskChip(
                        task = task,
                        onClose = { onRemove(task.id) }
                    )
                }
            }
        }
    }
}
