package com.ledger.task.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.SubTask
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.getTextMuted

/**
 * 子任务列表组件
 */
@Composable
fun SubTaskList(
    subTasks: List<SubTask>,
    onToggle: (SubTask) -> Unit,
    onDelete: (SubTask) -> Unit,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newSubTaskTitle by remember { mutableStateOf("") }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = getElevatedBackground())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "子任务",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (subTasks.isNotEmpty()) {
                    val completedCount = subTasks.count { it.isCompleted }
                    Text(
                        text = "($completedCount/${subTasks.size})",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (completedCount == subTasks.size) StatusDone else getTextMuted()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 子任务列表
            if (subTasks.isEmpty()) {
                Text(
                    text = "暂无子任务",
                    style = MaterialTheme.typography.bodyMedium,
                    color = getTextMuted(),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.height((subTasks.size * 48).dp.coerceAtMost(240.dp))
                ) {
                    subTasks.forEach { subTask ->
                        SubTaskItem(
                            subTask = subTask,
                            onToggle = { onToggle(subTask) },
                            onDelete = { onDelete(subTask) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 添加子任务输入框
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newSubTaskTitle,
                    onValueChange = { newSubTaskTitle = it },
                    placeholder = { Text("添加子任务", color = getTextMuted()) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (newSubTaskTitle.isNotBlank()) {
                            onAdd(newSubTaskTitle.trim())
                            newSubTaskTitle = ""
                        }
                    },
                    enabled = newSubTaskTitle.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "添加",
                        tint = if (newSubTaskTitle.isNotBlank())
                            MaterialTheme.colorScheme.primary
                        else
                            getTextMuted()
                    )
                }
            }
        }
    }
}

/**
 * 子任务项
 */
@Composable
private fun SubTaskItem(
    subTask: SubTask,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (subTask.isCompleted)
            StatusDone.copy(alpha = 0.1f)
        else
            Color.Transparent,
        label = "backgroundColor"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onToggle() }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 完成状态指示器
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(
                    if (subTask.isCompleted) StatusDone
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (subTask.isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "已完成",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 标题
        Text(
            text = subTask.title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (subTask.isCompleted) getTextMuted() else MaterialTheme.colorScheme.onSurface,
            textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else null,
            modifier = Modifier.weight(1f)
        )

        // 删除按钮
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "删除",
                tint = getTextMuted().copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
