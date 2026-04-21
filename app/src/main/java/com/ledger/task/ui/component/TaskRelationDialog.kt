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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.ui.theme.Accent
import com.ledger.task.ui.theme.getDeepBackground
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.PriorityHigh
import com.ledger.task.ui.theme.PriorityMid
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.StatusOverdue
import com.ledger.task.ui.theme.StatusPending
import com.ledger.task.ui.theme.StatusProgress
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary
import com.ledger.task.R as AppR
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 关联任务选择对话框
 * @param maxDeadline 最大允许的截止时间（用于前置依赖选择，前置任务截止时间不能晚于此时间）
 * @param excludeOverdue 是否排除已逾期任务
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskRelationDialog(
    title: String,
    availableTasks: List<Task>,
    selectedTaskIds: List<Long>,
    onConfirm: (List<Long>) -> Unit,
    onDismiss: () -> Unit,
    maxDeadline: LocalDateTime? = null,
    excludeOverdue: Boolean = false
) {
    var selectedIds by remember { mutableStateOf(selectedTaskIds) }

    // 过滤任务
    val filteredTasks = remember(availableTasks, maxDeadline, excludeOverdue) {
        availableTasks.filter { task ->
            // 排除已逾期任务
            if (excludeOverdue && task.displayStatus == DisplayStatus.OVERDUE) {
                return@filter false
            }
            // 排除截止时间晚于当前任务的任务
            if (maxDeadline != null && task.deadline.isAfter(maxDeadline)) {
                return@filter false
            }
            true
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                // 任务列表
                if (filteredTasks.isEmpty()) {
                    Text(
                        text = if (availableTasks.isNotEmpty()) "没有符合条件的前置任务\n（已排除逾期任务或截止时间较晚的任务）" else "暂无可用任务",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(300.dp)
                    ) {
                        items(filteredTasks) { task ->
                            TaskRelationItem(
                                task = task,
                                isSelected = selectedIds.contains(task.id),
                                onClick = {
                                    selectedIds = if (selectedIds.contains(task.id)) {
                                        selectedIds.filter { it != task.id }
                                    } else {
                                        selectedIds + task.id
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedIds) }
            ) {
                Text(text = "确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "取消")
            }
        }
    )
}

/**
 * 关联任务项
 */
@Composable
private fun TaskRelationItem(
    task: Task,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val isOverdue = task.displayStatus == DisplayStatus.OVERDUE

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Accent.copy(alpha = 0.12f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = null,
            modifier = Modifier.padding(end = 12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    color = getTextPrimary(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                PriorityBadge(priority = task.priority)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "截止: ${task.deadline.format(formatter)}",
                    color = if (isOverdue) StatusOverdue else getTextMuted(),
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatusTag(displayStatus = task.displayStatus)
            }
        }
    }
}
