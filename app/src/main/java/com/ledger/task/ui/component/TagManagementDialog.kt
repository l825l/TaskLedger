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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.domain.model.Tag
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary

/**
 * 标签管理对话框
 */
@Composable
fun TagManagementDialog(
    tags: List<Tag>,
    tagTaskCounts: Map<Long, Int>,
    onDismiss: () -> Unit,
    onCreateTag: (String, Color) -> Unit,
    onUpdateTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var editingTag by remember { mutableStateOf<Tag?>(null) }
    var deletingTag by remember { mutableStateOf<Tag?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("管理标签")
                IconButton(onClick = { showCreateDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "创建标签",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        text = {
            if (tags.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Label,
                        contentDescription = null,
                        tint = getTextMuted(),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "暂无标签",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击右上角 + 创建标签",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tags, key = { it.id }) { tag ->
                        TagListItem(
                            tag = tag,
                            taskCount = tagTaskCounts[tag.id] ?: 0,
                            onEdit = { editingTag = tag },
                            onDelete = { deletingTag = tag }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )

    // 创建标签对话框
    if (showCreateDialog) {
        TagEditDialog(
            title = "创建标签",
            initialName = "",
            initialColor = Tag.PRESET_COLORS.first(),
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, color ->
                onCreateTag(name, color)
                showCreateDialog = false
            }
        )
    }

    // 编辑标签对话框
    if (editingTag != null) {
        TagEditDialog(
            title = "编辑标签",
            initialName = editingTag!!.name,
            initialColor = editingTag!!.color,
            onDismiss = { editingTag = null },
            onConfirm = { name, color ->
                onUpdateTag(editingTag!!.copy(name = name, color = color))
                editingTag = null
            }
        )
    }

    // 删除确认对话框
    if (deletingTag != null) {
        val taskCount = tagTaskCounts[deletingTag!!.id] ?: 0
        AlertDialog(
            onDismissRequest = { deletingTag = null },
            title = { Text("删除标签") },
            text = {
                Column {
                    Text(
                        text = "确定要删除标签「${deletingTag!!.name}」吗？",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (taskCount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "该标签关联了 $taskCount 个任务，删除后将解除关联。",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTag(deletingTag!!)
                        deletingTag = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingTag = null }) {
                    Text("取消")
                }
            }
        )
    }
}

/**
 * 标签列表项
 */
@Composable
private fun TagListItem(
    tag: Tag,
    taskCount: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = getElevatedBackground())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 颜色指示器
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(tag.color)
            )

            // 标签名称
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tag.name,
                    color = getTextPrimary(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$taskCount 个任务",
                    color = getTextMuted(),
                    fontSize = 12.sp
                )
            }

            // 操作按钮
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "编辑",
                    tint = getTextMuted(),
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * 标签编辑对话框
 */
@Composable
private fun TagEditDialog(
    title: String,
    initialName: String,
    initialColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (String, Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(initialName) }
    var selectedColor by remember { mutableStateOf(initialColor) }
    var nameError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = { Text(title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 名称输入
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null
                    },
                    label = { Text("标签名称") },
                    singleLine = true,
                    isError = nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                nameError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // 颜色选择
                Text(
                    text = "选择颜色",
                    color = getTextMuted(),
                    style = MaterialTheme.typography.labelMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Tag.PRESET_COLORS.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedColor == color) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "已选择",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val trimmedName = name.trim()
                    if (trimmedName.isEmpty()) {
                        nameError = "标签名称不能为空"
                        return@Button
                    }
                    if (trimmedName.length > 10) {
                        nameError = "标签名称不能超过10个字符"
                        return@Button
                    }
                    onConfirm(trimmedName, selectedColor)
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
