package com.ledger.task.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledger.task.data.model.Priority
import com.ledger.task.data.model.Task
import com.ledger.task.data.model.TaskStatus
import com.ledger.task.ui.component.DraggableFloatingActionButton
import com.ledger.task.ui.component.EmptyState
import com.ledger.task.ui.theme.DeepBackground
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.SurfaceBackground
import com.ledger.task.ui.theme.TextMuted
import com.ledger.task.viewmodel.TodayTasksViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

/**
 * 今日待办屏幕
 */
@Composable
fun TodayTasksScreen(
    viewModel: TodayTasksViewModel,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 拖拽状态
    var draggingTaskId by remember { mutableLongStateOf(-1L) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    // 记录排序前的任务ID列表，用于检测列表变化
    var previousTaskIds by remember { mutableStateOf(emptyList<Long>()) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 顶部日期和进度条
            val today = LocalDate.now()
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 EEEE")
            val progress = if (uiState.totalCount > 0) {
                uiState.completedCount.toFloat() / uiState.totalCount.toFloat()
            } else 0f

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = today.format(dateFormatter),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${uiState.completedCount}/${uiState.totalCount}",
                        color = if (uiState.completedCount == uiState.totalCount && uiState.totalCount > 0)
                            StatusDone else TextMuted,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Monospace
                    )
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = StatusDone,
                    trackColor = SurfaceBackground
                )
            }

            // 任务列表
            if (uiState.tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "总计: 0",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 16.dp)
                    )
                    EmptyState(
                        modifier = Modifier.align(Alignment.Center),
                        height = 0,
                        text = "没有匹配的任务"
                    )
                }
            } else {
                // 分离待办和已完成任务
                val pendingTasks = uiState.tasks.filter { it.status != TaskStatus.DONE }
                val completedTasks = uiState.tasks.filter { it.status == TaskStatus.DONE }

                // 当前左滑的任务ID
                var currentlySwipedTaskId by remember { mutableLongStateOf(-1L) }

                // 卡片高度 (卡片高度 + 间距)
                val itemHeight = with(LocalDensity.current) { 88.dp.toPx() }

                // 当前任务ID列表
                val currentTaskIds = uiState.tasks.map { it.id }

                // 检测列表变化后重置拖拽状态
                LaunchedEffect(currentTaskIds) {
                    if (draggingTaskId != -1L && currentTaskIds != previousTaskIds) {
                        // 列表已更新，重置拖拽状态
                        draggingTaskId = -1L
                        dragOffsetY = 0f
                    }
                    previousTaskIds = currentTaskIds
                }

                // 预先计算优先级范围，避免 O(n²) 复杂度
                val priorityRanges = remember(pendingTasks) {
                    val ranges = mutableMapOf<Priority, Pair<Int, Int>>()
                    var currentPriority: Priority? = null
                    var startIndex = 0

                    pendingTasks.forEachIndexed { index, task ->
                        if (task.priority != currentPriority) {
                            // 记录上一个优先级的范围
                            currentPriority?.let { priority ->
                                ranges[priority] = startIndex to (index - 1)
                            }
                            currentPriority = task.priority
                            startIndex = index
                        }
                    }
                    // 记录最后一个优先级的范围
                    currentPriority?.let { priority ->
                        ranges[priority] = startIndex to (pendingTasks.lastIndex)
                    }
                    ranges
                }

                // 点击空白区域重置左滑状态
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (currentlySwipedTaskId != -1L) {
                                currentlySwipedTaskId = -1L
                            }
                        }
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 待办区
                        if (pendingTasks.isNotEmpty()) {
                            item {
                                Text(
                                    text = "待办 (${pendingTasks.size})",
                                    color = TextMuted,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }

                            itemsIndexed(
                                items = pendingTasks,
                                key = { _, task -> task.id }
                            ) { taskIndex, task ->
                                val isDraggingThis = draggingTaskId == task.id

                                // 从预计算的 Map 获取优先级范围
                                val (samePriorityStartIndex, samePriorityEndIndex) = priorityRanges[task.priority] ?: (0 to 0)

                                // 计算其他卡片的让位偏移（只在同优先级内）
                                val otherOffsetY = if (draggingTaskId != -1L && !isDraggingThis && dragOffsetY != 0f) {
                                    val draggingIndex = pendingTasks.indexOfFirst { it.id == draggingTaskId }
                                    if (draggingIndex != -1) {
                                        // 检查是否同优先级
                                        val draggingTask = pendingTasks.find { it.id == draggingTaskId }
                                        if (draggingTask != null && draggingTask.priority == task.priority) {
                                            val movedPositions = (dragOffsetY / itemHeight).roundToInt()
                                            val targetIndex = (draggingIndex + movedPositions).coerceIn(samePriorityStartIndex, samePriorityEndIndex)

                                            when {
                                                draggingIndex < targetIndex -> {
                                                    if (taskIndex in (draggingIndex + 1)..targetIndex) -itemHeight else 0f
                                                }
                                                draggingIndex > targetIndex -> {
                                                    if (taskIndex in targetIndex until draggingIndex) itemHeight else 0f
                                                }
                                                else -> 0f
                                            }
                                        } else 0f
                                    } else 0f
                                } else 0f

                                SwipeableTaskRow(
                                    task = task,
                                    taskIndex = taskIndex,
                                    totalItems = pendingTasks.size,
                                    itemHeight = itemHeight,
                                    isDragging = isDraggingThis,
                                    dragOffsetY = if (isDraggingThis) dragOffsetY else otherOffsetY,
                                    samePriorityStartIndex = samePriorityStartIndex,
                                    samePriorityEndIndex = samePriorityEndIndex,
                                    onDragStart = { taskId ->
                                        draggingTaskId = taskId
                                        dragOffsetY = 0f
                                    },
                                    onDragUpdate = { offset ->
                                        dragOffsetY = offset
                                    },
                                    onDragEnd = { fromIndex, toIndex ->
                                        if (fromIndex != toIndex) {
                                            viewModel.reorderTasksInPriority(pendingTasks, fromIndex, toIndex)
                                        } else {
                                            // 没有移动，直接重置
                                            draggingTaskId = -1L
                                            dragOffsetY = 0f
                                        }
                                    },
                                    onClick = { onNavigateToEdit(task.id) },
                                    onCompleted = { viewModel.onTaskCompleted(task.id) },
                                    onUndoComplete = { viewModel.onTaskUndoComplete(task.id) },
                                    onPriorityUpgrade = { viewModel.onPriorityUpgrade(task.id) },
                                    onPriorityDowngrade = { viewModel.onPriorityDowngrade(task.id) },
                                    isCurrentlySwiped = currentlySwipedTaskId == task.id,
                                    onSwipeStateChanged = { isSwiped ->
                                        if (isSwiped) {
                                            currentlySwipedTaskId = task.id
                                        } else if (currentlySwipedTaskId == task.id) {
                                            currentlySwipedTaskId = -1L
                                        }
                                    },
                                    modifier = Modifier
                                )
                            }
                        }

                        // 完成区
                        if (completedTasks.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "已完成 (${completedTasks.size})",
                                    color = StatusDone,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }

                            itemsIndexed(
                                items = completedTasks,
                                key = { _, task -> task.id }
                            ) { _, task ->
                                SwipeableTaskRow(
                                    task = task,
                                    taskIndex = 0,
                                    totalItems = completedTasks.size,
                                    itemHeight = itemHeight,
                                    isDragging = false,
                                    dragOffsetY = 0f,
                                    onDragStart = { },
                                    onDragUpdate = { },
                                    onDragEnd = { _, _ -> },
                                    onClick = { onNavigateToEdit(task.id) },
                                    onCompleted = { },
                                    onUndoComplete = { viewModel.onTaskUndoComplete(task.id) },
                                    onPriorityUpgrade = { },
                                    onPriorityDowngrade = { },
                                    isCurrentlySwiped = currentlySwipedTaskId == task.id,
                                    onSwipeStateChanged = { isSwiped ->
                                        if (isSwiped) {
                                            currentlySwipedTaskId = task.id
                                        } else if (currentlySwipedTaskId == task.id) {
                                            currentlySwipedTaskId = -1L
                                        }
                                    },
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "总计: ${uiState.tasks.size}",
                    color = TextMuted,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 12.dp)
                )
            }
        }

        // 可拖动悬浮按钮
        DraggableFloatingActionButton(
            onClick = onNavigateToAdd
        )
    }
}

