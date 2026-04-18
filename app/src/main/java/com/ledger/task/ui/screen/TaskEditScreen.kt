package com.ledger.task.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledger.task.data.model.DefaultCategories
import com.ledger.task.data.model.Priority
import com.ledger.task.data.model.RichContent
import com.ledger.task.data.model.Task
import com.ledger.task.data.model.TaskStatus
import com.ledger.task.ui.theme.Accent
import com.ledger.task.ui.theme.AccentDim
import com.ledger.task.ui.theme.BorderDim
import com.ledger.task.ui.theme.DeepBackground
import com.ledger.task.ui.theme.ElevatedBackground
import com.ledger.task.ui.theme.PriorityHigh
import com.ledger.task.ui.theme.PriorityLow
import com.ledger.task.ui.theme.PriorityMid
import com.ledger.task.ui.theme.SurfaceBackground
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.StatusPending
import com.ledger.task.ui.theme.StatusProgress
import com.ledger.task.ui.theme.TextMuted
import com.ledger.task.ui.theme.TextPrimary
import com.ledger.task.ui.theme.TextSecondary
import com.ledger.task.ui.component.PriorityBadge
import com.ledger.task.ui.component.RelatedTasksContainer
import com.ledger.task.ui.component.RichTextEditor
import com.ledger.task.ui.component.StatusTag
import com.ledger.task.ui.component.TaskRelationDialog
import com.ledger.task.viewmodel.TaskEditViewModel
import com.ledger.task.R as AppR
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * 任务创建/编辑页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    viewModel: TaskEditViewModel,
    taskId: Long?,
    onNavigateBack: () -> Unit,
    onNavigateToTask: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 自动加载已有任务
    LaunchedEffect(taskId) {
        taskId?.let { viewModel.loadTask(it) }
    }

    // 保存成功后返回
    LaunchedEffect(uiState.saved) {
        if (uiState.saved) onNavigateBack()
    }

    // 删除成功后返回
    LaunchedEffect(uiState.deleted) {
        if (uiState.deleted) onNavigateBack()
    }

    Scaffold(
        containerColor = DeepBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEdit) context.getString(AppR.string.edit_task) else context.getString(AppR.string.new_task),
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = context.getString(AppR.string.back),
                            tint = TextSecondary
                        )
                    }
                },
                actions = {
                    // 删除按钮（仅编辑模式显示）
                    if (uiState.isEdit) {
                        IconButton(onClick = { viewModel.onShowDeleteDialog(true) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = context.getString(AppR.string.delete),
                                tint = com.ledger.task.ui.theme.StatusOverdue
                            )
                        }
                    }
                    // 保存按钮
                    IconButton(
                        onClick = viewModel::save,
                        enabled = !uiState.isSaving
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = context.getString(AppR.string.save_desc),
                            tint = Accent
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepBackground
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepBackground)
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // 顶部：状态滑块
            StatusSlider(
                currentStatus = uiState.status,
                onStatusChange = viewModel::onStatusChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )

            // 中部：属性网格
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 标题
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text(context.getString(AppR.string.title)) },
                    isError = uiState.titleError,
                    supportingText = if (uiState.titleError) {{ Text(context.getString(AppR.string.title_required)) }} else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = BorderDim,
                        focusedLabelColor = Accent,
                        unfocusedLabelColor = TextMuted,
                        cursorColor = Accent,
                        errorBorderColor = com.ledger.task.ui.theme.StatusOverdue,
                        errorTextColor = com.ledger.task.ui.theme.StatusOverdue
                    )
                )

                // 描述
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = { Text(context.getString(AppR.string.description)) },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = BorderDim,
                        focusedLabelColor = Accent,
                        unfocusedLabelColor = TextMuted,
                        cursorColor = Accent
                    )
                )

                // 属性网格：优先级 + 时限
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 优先级
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = context.getString(AppR.string.priority),
                                color = TextMuted,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                enumValues<Priority>().forEach { priority ->
                                    val color = priority.color
                                    val isActive = uiState.priority == priority
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isActive) AccentDim else ElevatedBackground)
                                            .border(
                                                1.dp,
                                                if (isActive) Accent else BorderDim,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable { viewModel.onPriorityChange(priority) }
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                        )
                                        Text(
                                            text = priority.label,
                                            color = if (isActive) Accent else TextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }

                        // 时限
                        val context2 = LocalContext.current
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = context.getString(AppR.string.deadline),
                                color = TextMuted,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // 日期
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ElevatedBackground)
                                        .border(1.dp, BorderDim, RoundedCornerShape(6.dp))
                                        .clickable {
                                            val date = uiState.deadline
                                            DatePickerDialog(
                                                context2,
                                                { _, year, month, day ->
                                                    viewModel.onDeadlineDateChange(LocalDate.of(year, month + 1, day))
                                                },
                                                date.year,
                                                date.monthValue - 1,
                                                date.dayOfMonth
                                            ).show()
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = uiState.deadline.format(java.time.format.DateTimeFormatter.ofPattern("MM.dd")),
                                        color = TextPrimary,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                // 时间
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ElevatedBackground)
                                        .border(1.dp, BorderDim, RoundedCornerShape(6.dp))
                                        .clickable {
                                            val time = uiState.deadline.toLocalTime()
                                            TimePickerDialog(
                                                context2,
                                                { _, hour, minute ->
                                                    viewModel.onDeadlineTimeChange(LocalTime.of(hour, minute))
                                                },
                                                time.hour,
                                                time.minute,
                                                false
                                            ).show()
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = uiState.deadline.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                                        color = TextPrimary,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }

                        // 完成时间（仅已完成任务显示）
                        if (uiState.status == TaskStatus.DONE) {
                            val completedDateTime = uiState.completedAt?.let {
                                Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            } ?: uiState.deadline  // 默认使用截止时间

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "完成",
                                    color = TextMuted,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 完成日期
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ElevatedBackground)
                                            .border(1.dp, BorderDim, RoundedCornerShape(6.dp))
                                            .clickable {
                                                DatePickerDialog(
                                                    context2,
                                                    { _, year, month, day ->
                                                        val newDate = LocalDate.of(year, month + 1, day)
                                                        val newDateTime = LocalDateTime.of(newDate, completedDateTime.toLocalTime())
                                                        viewModel.onCompletedAtChange(
                                                            newDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                                        )
                                                    },
                                                    completedDateTime.year,
                                                    completedDateTime.monthValue - 1,
                                                    completedDateTime.dayOfMonth
                                                ).show()
                                            }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = completedDateTime.format(java.time.format.DateTimeFormatter.ofPattern("MM.dd")),
                                            color = TextPrimary,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp
                                        )
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = null,
                                            tint = TextMuted,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }

                                    // 完成时间
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ElevatedBackground)
                                            .border(1.dp, BorderDim, RoundedCornerShape(6.dp))
                                            .clickable {
                                                TimePickerDialog(
                                                    context2,
                                                    { _, hour, minute ->
                                                        val newTime = LocalTime.of(hour, minute)
                                                        val newDateTime = LocalDateTime.of(completedDateTime.toLocalDate(), newTime)
                                                        viewModel.onCompletedAtChange(
                                                            newDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                                        )
                                                    },
                                                    completedDateTime.hour,
                                                    completedDateTime.minute,
                                                    false
                                                ).show()
                                            }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = completedDateTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                                            color = TextPrimary,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp
                                        )
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = TextMuted,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // 分类
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = context.getString(AppR.string.category),
                                color = TextMuted,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ElevatedBackground)
                                    .border(1.dp, BorderDim, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.onShowCategoryDialog(true) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // 颜色圆点
                                val categoryNode = uiState.categories.find { it.id == uiState.category || it.name == uiState.category }
                                val categoryColor = categoryNode?.color ?: DefaultCategories.ColorDefault
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(categoryColor)
                                )
                                Text(
                                    text = if (uiState.category.isEmpty()) "默认" else categoryNode?.name ?: uiState.category,
                                    color = TextPrimary,
                                    fontSize = 12.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                // 备注（富文本）
                RichTextEditor(
                    richContent = uiState.richContent,
                    onRichContentChange = { viewModel.onRichContentChange(it) },
                    label = "备注（文字+图片+附件）",
                    modifier = Modifier.fillMaxWidth()
                )

                // 关联事项区域
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // 标题行
                    Text(
                        text = "关联事项",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 添加按钮行
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 添加前置依赖按钮
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElevatedBackground)
                                .border(1.dp, BorderDim, RoundedCornerShape(8.dp))
                                .clickable { viewModel.onShowPredecessorDialog(true) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Accent,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "前置依赖",
                                color = TextPrimary,
                                fontSize = 13.sp
                            )
                            if (uiState.predecessorIds.isNotEmpty()) {
                                Text(
                                    text = "(${uiState.predecessorIds.size})",
                                    color = Accent,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // 添加相关任务按钮
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElevatedBackground)
                                .border(1.dp, BorderDim, RoundedCornerShape(8.dp))
                                .clickable { viewModel.onShowRelatedDialog(true) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Accent,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "相关任务",
                                color = TextPrimary,
                                fontSize = 13.sp
                            )
                            if (uiState.relatedIds.isNotEmpty()) {
                                Text(
                                    text = "(${uiState.relatedIds.size})",
                                    color = Accent,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // 底部：横向滚动显示关联事务
            if (uiState.predecessorTasks.isNotEmpty() || uiState.relatedTasks.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // 检查所有前置依赖是否已完成
                    val allPredecessorsCompleted = uiState.predecessorTasks.isNotEmpty() &&
                            uiState.predecessorTasks.all { it.status == TaskStatus.DONE }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = context.getString(AppR.string.related_tasks),
                            color = TextMuted,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        // 依赖项已解除提示
                        if (allPredecessorsCompleted) {
                            Row(
                                modifier = Modifier
                                    .padding(end = 24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(com.ledger.task.ui.theme.StatusDone.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = com.ledger.task.ui.theme.StatusDone,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "依赖项已解除",
                                    color = com.ledger.task.ui.theme.StatusDone,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 前置依赖任务
                        uiState.predecessorTasks.forEach { task ->
                            RelatedTaskCard(
                                task = task,
                                relationType = "前置",
                                isCompleted = task.status == TaskStatus.DONE,
                                onRemove = {
                                    val newIds = uiState.predecessorIds.filter { it != task.id }
                                    viewModel.onPredecessorIdsChange(newIds)
                                },
                                onClick = { onNavigateToTask(task.id) }
                            )
                        }

                        // 相关任务
                        uiState.relatedTasks.forEach { task ->
                            RelatedTaskCard(
                                task = task,
                                relationType = "相关",
                                isCompleted = task.status == TaskStatus.DONE,
                                onRemove = {
                                    val newIds = uiState.relatedIds.filter { it != task.id }
                                    viewModel.onRelatedIdsChange(newIds)
                                },
                                onClick = { onNavigateToTask(task.id) }
                            )
                        }
                    }
                }
            }

            // 底部占位
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 关联任务选择对话框
        if (uiState.showPredecessorDialog) {
            TaskRelationDialog(
                title = "选择前置依赖任务",
                availableTasks = uiState.availableTasks,
                selectedTaskIds = uiState.predecessorIds,
                onConfirm = { newIds ->
                    viewModel.onDialogConfirmPredecessors(newIds)
                },
                onDismiss = { viewModel.onShowPredecessorDialog(false) }
            )
        }

        if (uiState.showRelatedDialog) {
            TaskRelationDialog(
                title = "选择相关任务",
                availableTasks = uiState.availableTasks,
                selectedTaskIds = uiState.relatedIds,
                onConfirm = { newIds ->
                    viewModel.onDialogConfirmRelated(newIds)
                },
                onDismiss = { viewModel.onShowRelatedDialog(false) }
            )
        }

        // 分类选择对话框
        if (uiState.showCategoryDialog) {
            CategorySelectionDialog(
                categories = uiState.categories,
                selectedCategory = uiState.category,
                onSelect = { viewModel.onCategoryChange(it) },
                onDismiss = { viewModel.onShowCategoryDialog(false) },
                onAddCategory = { viewModel.onShowAddCategoryDialog(true) },
                onEditCategory = { viewModel.onEditCategory(it) },
                onDeleteCategory = { viewModel.onDeleteCategory(it) }
            )
        }

        // 添加/编辑分类对话框
        if (uiState.showAddCategoryDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onShowAddCategoryDialog(false) },
                title = { Text(if (uiState.editingCategoryId != null) "编辑分类" else "添加分类") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.newCategoryName,
                            onValueChange = { viewModel.onNewCategoryNameChange(it) },
                            label = { Text("分类名称") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 颜色选择
                        Text(
                            text = "选择颜色",
                            color = TextMuted,
                            style = MaterialTheme.typography.labelSmall
                        )

                        // 颜色选择网格
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DefaultCategories.availableColors.chunked(6).forEach { rowColors ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowColors.forEach { color ->
                                        val isSelected = uiState.newCategoryColor == color
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                                .then(
                                                    if (isSelected) {
                                                        Modifier.padding(4.dp)
                                                    } else {
                                                        Modifier
                                                    }
                                                )
                                                .clickable { viewModel.onNewCategoryColorChange(color) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                    // 填充空白以保持对齐
                                    repeat(6 - rowColors.size) {
                                        Spacer(modifier = Modifier.size(32.dp))
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (uiState.editingCategoryId != null) {
                                viewModel.onUpdateCategory()
                            } else {
                                viewModel.onAddCategory()
                            }
                        }
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onShowAddCategoryDialog(false) }) {
                        Text("取消")
                    }
                }
            )
        }

        // 删除确认对话框
        if (uiState.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onShowDeleteDialog(false) },
                title = { Text("删除任务") },
                text = { Text("确定要删除此任务吗？此操作无法撤销。") },
                confirmButton = {
                    TextButton(onClick = viewModel::delete) {
                        Text(
                            text = "删除",
                            color = com.ledger.task.ui.theme.StatusOverdue
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onShowDeleteDialog(false) }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        color = TextMuted,
        style = MaterialTheme.typography.labelSmall
    )
}

/**
 * 状态滑块组件
 */
@Composable
private fun StatusSlider(
    currentStatus: TaskStatus,
    onStatusChange: (TaskStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = listOf(TaskStatus.PENDING, TaskStatus.IN_PROGRESS, TaskStatus.DONE)
    val statusColors = listOf(StatusPending, StatusProgress, StatusDone)
    val currentIndex = statuses.indexOf(currentStatus)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceBackground),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            statuses.forEachIndexed { index, status ->
                val isActive = index == currentIndex
                val color = statusColors[index]

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(
                            topStart = if (index == 0) 12.dp else 0.dp,
                            topEnd = if (index == statuses.lastIndex) 12.dp else 0.dp,
                            bottomStart = if (index == 0) 12.dp else 0.dp,
                            bottomEnd = if (index == statuses.lastIndex) 12.dp else 0.dp
                        ))
                        .background(if (isActive) color.copy(alpha = 0.2f) else SurfaceBackground)
                        .clickable { onStatusChange(status) }
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isActive) color else color.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = status.label,
                        color = if (isActive) color else TextMuted,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        // 进度指示器
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .padding(top = 8.dp)
        ) {
            // 背景轨道
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(SurfaceBackground)
            )
            // 进度条
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (currentIndex + 1).toFloat() / statuses.size)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(statusColors[currentIndex])
            )
        }
    }
}

/**
 * 关联任务卡片（横向滚动用）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RelatedTaskCard(
    task: Task,
    relationType: String,
    isCompleted: Boolean,
    onRemove: () -> Unit,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = task.priority.bgColor),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = relationType,
                        color = TextMuted,
                        style = MaterialTheme.typography.labelSmall
                    )
                    // 已完成标记
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "已完成",
                            tint = com.ledger.task.ui.theme.StatusDone,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "移除",
                    tint = TextMuted,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onRemove() }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.title,
                color = if (isCompleted) TextMuted else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                textDecoration = if (isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
            )
        }
    }
}

/**
 * 分类选择对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelectionDialog(
    categories: List<com.ledger.task.data.model.CategoryNode>,
    selectedCategory: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
    onAddCategory: () -> Unit,
    onEditCategory: (String) -> Unit,
    onDeleteCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择分类") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // 默认选项
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedCategory.isEmpty()) AccentDim else ElevatedBackground)
                        .clickable { onSelect("") }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 默认颜色圆点
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(com.ledger.task.data.model.DefaultCategories.ColorDefault)
                        )
                        Text(
                            text = "默认",
                            color = if (selectedCategory.isEmpty()) Accent else TextPrimary
                        )
                    }
                    if (selectedCategory.isEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 分类列表
                categories.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedCategory == category.id || selectedCategory == category.name) AccentDim else ElevatedBackground)
                            .clickable { onSelect(category.name) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            // 颜色圆点
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(category.color)
                            )
                            Text(
                                text = category.name,
                                color = if (selectedCategory == category.id || selectedCategory == category.name) Accent else TextPrimary
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // 编辑按钮
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "编辑",
                                tint = TextMuted,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onEditCategory(category.id) }
                            )
                            // 删除按钮
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "删除",
                                tint = TextMuted,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onDeleteCategory(category.id) }
                            )
                            // 选中标记
                            if (selectedCategory == category.id || selectedCategory == category.name) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Accent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // 添加分类按钮
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceBackground)
                        .clickable { onAddCategory() }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "添加分类",
                        color = Accent
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}
