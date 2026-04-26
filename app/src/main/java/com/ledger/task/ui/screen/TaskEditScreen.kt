package com.ledger.task.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Replay
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledger.task.domain.model.DefaultCategories
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.RichContent
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
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
import com.ledger.task.ui.component.SubTaskList
import com.ledger.task.ui.component.TagSelector
import com.ledger.task.ui.component.TaskRelationDialog
import com.ledger.task.domain.model.Recurrence
import com.ledger.task.domain.model.RecurrenceType
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
            // 顶部：状态滑块（带阻塞提示）
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // 阻塞提示
                if (uiState.dependencyState.isBlocked) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(com.ledger.task.ui.theme.StatusOverdue.copy(alpha = 0.15f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = context.getString(AppR.string.blocked),
                            tint = com.ledger.task.ui.theme.StatusOverdue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = context.getString(AppR.string.blocked_hint),
                            color = com.ledger.task.ui.theme.StatusOverdue,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                StatusSlider(
                    currentStatus = uiState.status,
                    onStatusChange = viewModel::onStatusChangeWithValidation,
                    modifier = Modifier.fillMaxWidth()
                )
            }

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

                        // 标签选择
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "标签",
                                color = TextMuted,
                                style = MaterialTheme.typography.labelMedium
                            )
                            // 显示已选标签数量
                            if (uiState.selectedTagIds.isNotEmpty()) {
                                Text(
                                    text = "已选 ${uiState.selectedTagIds.size} 个",
                                    color = Accent,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                        TagSelector(
                            allTags = uiState.allTags,
                            selectedTagIds = uiState.selectedTagIds,
                            onTagToggle = { viewModel.toggleTag(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // 备注（富文本）
                RichTextEditor(
                    richContent = uiState.richContent,
                    onRichContentChange = { viewModel.onRichContentChange(it) },
                    label = "备注（文字+图片+附件）",
                    modifier = Modifier.fillMaxWidth()
                )

                // 子任务列表
                SubTaskList(
                    subTasks = uiState.subTasks,
                    onToggle = { viewModel.onToggleSubTask(it) },
                    onDelete = { viewModel.onDeleteSubTask(it) },
                    onAdd = { viewModel.onAddSubTask(it) },
                    modifier = Modifier.fillMaxWidth()
                )

                // 循环任务设置
                RecurrenceSection(
                    recurrence = uiState.recurrence,
                    onShowDialog = { viewModel.onShowRecurrenceDialog(true) },
                    onClear = { viewModel.onClearRecurrence() },
                    modifier = Modifier.fillMaxWidth()
                )

                // 关联事项区域
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 前置依赖区域
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 标题行 + 添加按钮
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "前置依赖",
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                if (uiState.predecessorTasks.isNotEmpty()) {
                                    val completed = uiState.predecessorTasks.count { it.status == TaskStatus.DONE }
                                    Text(
                                        text = "($completed/${uiState.predecessorTasks.size})",
                                        color = if (completed == uiState.predecessorTasks.size) StatusDone else TextMuted,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }

                            // 添加按钮
                            Surface(
                                onClick = { viewModel.onShowPredecessorDialog(true) },
                                shape = RoundedCornerShape(6.dp),
                                color = AccentDim,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Accent.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = Accent,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "添加",
                                        color = Accent,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        // 前置依赖卡片列表
                        if (uiState.predecessorTasks.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                uiState.predecessorTasks.forEach { task ->
                                    RelationTaskItem(
                                        task = task,
                                        isCompleted = task.status == TaskStatus.DONE,
                                        onRemove = {
                                            viewModel.onShowRemoveConfirmDialog(task.id, isPredecessor = true)
                                        },
                                        onQuickComplete = { viewModel.onQuickCompleteTask(task.id) },
                                        onQuickUndoComplete = { viewModel.onQuickUndoCompleteTask(task.id) },
                                        onClick = { onNavigateToTask(task.id) }
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceBackground)
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "无前置依赖",
                                    color = TextMuted,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    // 相关任务区域
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 标题行 + 添加按钮
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "相关任务",
                                    color = TextPrimary,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                if (uiState.relatedTasks.isNotEmpty()) {
                                    Text(
                                        text = "(${uiState.relatedTasks.size})",
                                        color = TextMuted,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }

                            // 添加按钮
                            Surface(
                                onClick = { viewModel.onShowRelatedDialog(true) },
                                shape = RoundedCornerShape(6.dp),
                                color = ElevatedBackground,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDim)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = Accent,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "添加",
                                        color = Accent,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        // 相关任务卡片列表（使用过滤后的列表）
                        if (uiState.filteredRelatedTasks.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                uiState.filteredRelatedTasks.forEach { task ->
                                    RelationTaskItem(
                                        task = task,
                                        isCompleted = task.status == TaskStatus.DONE,
                                        onRemove = {
                                            viewModel.onShowRemoveConfirmDialog(task.id, isPredecessor = false)
                                        },
                                        onQuickComplete = { viewModel.onQuickCompleteTask(task.id) },
                                        onQuickUndoComplete = { viewModel.onQuickUndoCompleteTask(task.id) },
                                        onClick = { onNavigateToTask(task.id) }
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceBackground)
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "无相关任务",
                                    color = TextMuted,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
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

        // 依赖验证错误对话框
        if (uiState.dependencyValidationError != null) {
            AlertDialog(
                onDismissRequest = { viewModel.onDismissDependencyError() },
                title = { Text(context.getString(AppR.string.cannot_add_dependency)) },
                text = {
                    Text(when (val error = uiState.dependencyValidationError) {
                        is com.ledger.task.domain.DependencyValidationResult.SelfReference -> context.getString(AppR.string.self_reference_error)
                        is com.ledger.task.domain.DependencyValidationResult.DirectCycle -> context.getString(AppR.string.cycle_dependency_error)
                        is com.ledger.task.domain.DependencyValidationResult.PredecessorNotFound -> context.getString(AppR.string.predecessor_not_found)
                        else -> context.getString(AppR.string.unknown_error)
                    })
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.onDismissDependencyError() }) {
                        Text(context.getString(AppR.string.got_it))
                    }
                }
            )
        }

        // 循环任务设置对话框
        if (uiState.showRecurrenceDialog) {
            RecurrenceDialog(
                recurrence = uiState.recurrence,
                onTypeChange = { viewModel.onRecurrenceTypeChange(it) },
                onIntervalChange = { viewModel.onRecurrenceIntervalChange(it) },
                onDayOfWeekToggle = { viewModel.onDayOfWeekToggle(it) },
                onConfirm = { viewModel.onConfirmRecurrence() },
                onDismiss = { viewModel.onShowRecurrenceDialog(false) }
            )
        }

        // 阻塞提示对话框
        if (uiState.showDependencyBlockedDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onShowDependencyBlockedDialog(false) },
                title = { Text(context.getString(AppR.string.cannot_start_task)) },
                text = {
                    Column {
                        Text(context.getString(AppR.string.blocking_tasks_hint))
                        Spacer(modifier = Modifier.height(8.dp))
                        (uiState.dependencyState as? com.ledger.task.domain.DependencyState.Blocked)
                            ?.blockingTasks?.forEach { task ->
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(com.ledger.task.ui.theme.StatusOverdue, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(task.title)
                                }
                            }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.onShowDependencyBlockedDialog(false) }) {
                        Text(context.getString(AppR.string.got_it))
                    }
                }
            )
        }
    }
}

/**
 * 关联任务项（支持滑动完成/撤销）
 */
@Composable
private fun RelationTaskItem(
    task: Task,
    isCompleted: Boolean,
    onRemove: () -> Unit,
    onQuickComplete: () -> Unit = {},
    onQuickUndoComplete: () -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("MM月dd日 HH:mm")
    val priorityBgColor = task.priority.bgColor

    // 滑动状态
    var offsetX by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val swipeThreshold = with(density) { 100.dp.toPx() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    offsetX < -swipeThreshold -> if (isCompleted) StatusPending.copy(alpha = 0.3f) else StatusDone.copy(alpha = 0.3f)
                    else -> Color.Transparent
                }
            )
    ) {
        // 背景操作提示
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = if (offsetX < 0) Arrangement.Start else Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (offsetX < 0) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.Close else Icons.Default.Check,
                    contentDescription = if (isCompleted) "撤销完成" else "完成",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isCompleted) "撤销" else "完成",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        // 前景卡片
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(priorityBgColor)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX < -swipeThreshold) {
                                // 执行完成/撤销
                                if (isCompleted) onQuickUndoComplete() else onQuickComplete()
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            // 只允许左滑
                            val newOffset = offsetX + dragAmount
                            offsetX = newOffset.coerceIn(-swipeThreshold * 1.5f, 0f)
                        }
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 任务信息
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = task.title,
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 截止日期
                        Text(
                            text = "截止: ${task.deadline.format(formatter)}",
                            color = TextMuted,
                            style = MaterialTheme.typography.labelSmall
                        )
                        // 状态标签
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (task.status) {
                                TaskStatus.DONE -> StatusDone.copy(alpha = 0.3f)
                                TaskStatus.IN_PROGRESS -> StatusProgress.copy(alpha = 0.3f)
                                TaskStatus.PENDING -> StatusPending.copy(alpha = 0.3f)
                            }
                        ) {
                            Text(
                                text = task.status.label,
                                color = when (task.status) {
                                    TaskStatus.DONE -> StatusDone
                                    TaskStatus.IN_PROGRESS -> StatusProgress
                                    TaskStatus.PENDING -> StatusPending
                                },
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // 移除按钮
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "移除",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
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
    categories: List<com.ledger.task.domain.model.CategoryNode>,
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
                                .background(com.ledger.task.domain.model.DefaultCategories.ColorDefault)
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

/**
 * 循环任务设置区域
 */
@Composable
private fun RecurrenceSection(
    recurrence: Recurrence?,
    onShowDialog: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ElevatedBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "循环任务",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (recurrence != null) {
                    // 清除按钮
                    TextButton(onClick = onClear) {
                        Text("清除", color = TextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (recurrence != null) {
                // 显示当前循环设置
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceBackground)
                        .clickable { onShowDialog() }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = recurrence.displayText(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "编辑",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                // 添加循环按钮
                Surface(
                    onClick = onShowDialog,
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceBackground
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "设置循环",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

/**
 * 循环任务设置对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecurrenceDialog(
    recurrence: Recurrence?,
    onTypeChange: (RecurrenceType) -> Unit,
    onIntervalChange: (Int) -> Unit,
    onDayOfWeekToggle: (java.time.DayOfWeek) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentType = recurrence?.type ?: RecurrenceType.DAILY
    val currentInterval = recurrence?.interval ?: 1
    val currentDaysOfWeek = recurrence?.daysOfWeek ?: emptySet()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("设置循环") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 循环类型选择
                Text(
                    text = "重复方式",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextMuted
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RecurrenceType.entries.forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (currentType == type) AccentDim
                                    else SurfaceBackground
                                )
                                .clickable { onTypeChange(type) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when (type) {
                                    RecurrenceType.DAILY -> "每天"
                                    RecurrenceType.WEEKLY -> "每周"
                                    RecurrenceType.MONTHLY -> "每月"
                                    RecurrenceType.YEARLY -> "每年"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (currentType == type) Accent else MaterialTheme.colorScheme.onSurface
                            )
                            if (currentType == type) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Accent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // 间隔设置
                if (currentType != RecurrenceType.YEARLY) {
                    Column {
                        Text(
                            text = "间隔",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // 减少按钮
                            IconButton(
                                onClick = { onIntervalChange(currentInterval - 1) },
                                enabled = currentInterval > 1
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "减少",
                                    tint = if (currentInterval > 1) Accent else TextMuted
                                )
                            }

                            Text(
                                text = "$currentInterval ${when (currentType) {
                                    RecurrenceType.DAILY -> "天"
                                    RecurrenceType.WEEKLY -> "周"
                                    RecurrenceType.MONTHLY -> "月"
                                    RecurrenceType.YEARLY -> "年"
                                }}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            // 增加按钮
                            IconButton(
                                onClick = { onIntervalChange(currentInterval + 1) },
                                enabled = currentInterval < 99
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "增加",
                                    tint = if (currentInterval < 99) Accent else TextMuted
                                )
                            }
                        }
                    }
                }

                // 周几选择（仅周循环）
                if (currentType == RecurrenceType.WEEKLY) {
                    Column {
                        Text(
                            text = "重复日期",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val dayLabels = listOf("一", "二", "三", "四", "五", "六", "日")
                            val days = listOf(
                                java.time.DayOfWeek.MONDAY,
                                java.time.DayOfWeek.TUESDAY,
                                java.time.DayOfWeek.WEDNESDAY,
                                java.time.DayOfWeek.THURSDAY,
                                java.time.DayOfWeek.FRIDAY,
                                java.time.DayOfWeek.SATURDAY,
                                java.time.DayOfWeek.SUNDAY
                            )
                            days.forEachIndexed { index, day ->
                                val isSelected = day in currentDaysOfWeek
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) Accent
                                            else SurfaceBackground
                                        )
                                        .clickable { onDayOfWeekToggle(day) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dayLabels[index],
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) Color.White else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
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
