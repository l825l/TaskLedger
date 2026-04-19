package com.ledger.task.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ledger.task.data.model.DisplayStatus
import com.ledger.task.data.model.Priority
import com.ledger.task.data.model.Task
import com.ledger.task.data.model.TaskStatus
import com.ledger.task.ui.component.CategoryTag
import com.ledger.task.ui.component.PriorityBadge
import com.ledger.task.ui.component.StatusTag
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.TextMuted
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 可左滑/右滑的任务行
 * 左滑：显示完成/取消按钮（已完成任务显示撤销）
 * 右滑：显示升降级按钮
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableTaskRow(
    task: Task,
    taskIndex: Int,
    totalItems: Int,
    itemHeight: Float,
    isDragging: Boolean,
    dragOffsetY: Float,
    samePriorityStartIndex: Int = 0,
    samePriorityEndIndex: Int = totalItems - 1,
    onDragStart: (Long) -> Unit,
    onDragUpdate: (Float) -> Unit,
    onDragEnd: (Int, Int) -> Unit,
    onClick: () -> Unit,
    onCompleted: () -> Unit,
    onUndoComplete: () -> Unit,
    onPriorityUpgrade: () -> Unit,
    onPriorityDowngrade: () -> Unit,
    isCurrentlySwiped: Boolean = false,
    onSwipeStateChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    // 滑动偏移（负数为左滑，正数为右滑）
    var swipeOffset by remember { mutableFloatStateOf(0f) }
    var isSwiping by remember { mutableStateOf(false) }

    // 滑动按钮宽度
    val actionButtonWidth = with(density) { 120.dp.toPx() }

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val fullFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
    val isOverdue = task.displayStatus == DisplayStatus.OVERDUE
    val isCompleted = task.status == TaskStatus.DONE

    // 计算截止时间距离现在的时长
    val now = LocalDateTime.now()
    val durationToDeadline = Duration.between(now, task.deadline)
    val hoursToDeadline = durationToDeadline.toHours()
    // 是否需要显示完整日期（超过24小时）
    val showFullDate = hoursToDeadline > 24

    // 完成动画
    val completeScale = remember { Animatable(1f) }
    val completeAlpha = remember { Animatable(1f) }
    var showCompleteAnimation by remember { mutableStateOf(false) }

    // 是否可以拖拽（已完成的不能拖拽）
    val canDrag = totalItems > 1 && !isCompleted

    // 当其他卡片开始滑动时，重置当前卡片的滑动状态
    LaunchedEffect(isCurrentlySwiped) {
        if (!isCurrentlySwiped && swipeOffset != 0f) {
            swipeOffset = 0f
        }
    }

    LaunchedEffect(showCompleteAnimation) {
        if (showCompleteAnimation) {
            completeScale.animateTo(0.8f, spring(stiffness = Spring.StiffnessHigh))
            completeAlpha.animateTo(0f, spring(stiffness = Spring.StiffnessHigh))
            onCompleted()
            showCompleteAnimation = false
            completeScale.snapTo(1f)
            completeAlpha.snapTo(1f)
            swipeOffset = 0f
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(if (isDragging) 999f else 0f)
    ) {
        // 左滑背景按钮（在卡片右侧显示）- 已完成的任务显示"撤销完成"
        if (swipeOffset < -10f) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(120.dp)
                    .height(76.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                if (isCompleted) {
                    // 撤销完成按钮
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                // 撤销完成逻辑
                                onUndoComplete()
                                swipeOffset = 0f
                                onSwipeStateChanged(false)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "撤销",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "撤销",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                } else {
                    // 完成按钮
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(StatusDone)
                            .clickable { showCompleteAnimation = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "完成",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "完成",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                // 取消按钮
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF888888))
                        .clickable {
                            swipeOffset = 0f
                            onSwipeStateChanged(false)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "取消",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "取消",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // 右滑背景按钮（在卡片左侧显示）
        if (swipeOffset > 10f) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(120.dp)
                    .height(76.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                // 升级按钮（"低"可以升级到"中"，"中"可以升级到"高"，"高"可以升级到"紧急"）
                if (task.priority != Priority.HIGH) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable {
                                onPriorityUpgrade()
                                swipeOffset = 0f
                                onSwipeStateChanged(false)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "升级",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "升级",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                // 降级按钮（"低"优先级不能降级）
                if (task.priority != Priority.LOW) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable {
                                onPriorityDowngrade()
                                swipeOffset = 0f
                                onSwipeStateChanged(false)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "降级",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "降级",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }

        // 任务卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeOffset.roundToInt(), dragOffsetY.roundToInt()) }
                .graphicsLayer {
                    scaleX = completeScale.value
                    scaleY = completeScale.value
                    alpha = completeAlpha.value
                }
                .pointerInput(task.id, totalItems, taskIndex, canDrag, itemHeight) {
                    if (!canDrag) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            var totalDragX = 0f
                            var totalDragY = 0f
                            var isHorizontalSwipe = false
                            val startTime = System.currentTimeMillis()

                            drag(down.id) { change ->
                                val dragAmount = change.positionChange()
                                totalDragX += dragAmount.x
                                totalDragY += dragAmount.y

                                if (!isHorizontalSwipe && abs(totalDragX) > 20f && abs(totalDragX) > abs(totalDragY) * 1.5f) {
                                    isHorizontalSwipe = true
                                }

                                if (isHorizontalSwipe) {
                                    // 支持左滑和右滑
                                    val newOffset = swipeOffset + dragAmount.x
                                    swipeOffset = newOffset.coerceIn(-actionButtonWidth, actionButtonWidth)
                                    change.consume()
                                }
                            }

                            if (isHorizontalSwipe) {
                                when {
                                    swipeOffset < -actionButtonWidth / 2 -> {
                                        swipeOffset = -actionButtonWidth
                                        onSwipeStateChanged(true)
                                    }
                                    swipeOffset > actionButtonWidth / 2 -> {
                                        swipeOffset = actionButtonWidth
                                        onSwipeStateChanged(true)
                                    }
                                    else -> {
                                        swipeOffset = 0f
                                        onSwipeStateChanged(false)
                                    }
                                }
                            }

                            val duration = System.currentTimeMillis() - startTime
                            if (!isHorizontalSwipe && duration < 300 && abs(totalDragX) < 15f && abs(totalDragY) < 15f) {
                                onClick()
                            }
                        }
                        return@pointerInput
                    }

                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        var totalDragX = 0f
                        var totalDragY = 0f
                        var gestureCancelled = false
                        var currentDragOffsetY = 0f
                        var isDragTriggered = false
                        val startTime = System.currentTimeMillis()

                        val longPressJob = coroutineScope.launch {
                            delay(300)
                            if (!gestureCancelled && abs(totalDragX) < 15f && abs(totalDragY) < 15f) {
                                isDragTriggered = true
                                onDragStart(task.id)
                            }
                        }

                        drag(down.id) { change ->
                            val dragAmount = change.positionChange()
                            totalDragX += dragAmount.x
                            totalDragY += dragAmount.y

                            if (!isDragTriggered) {
                                if (abs(totalDragX) > 20f && abs(totalDragX) > abs(totalDragY) * 1.5f) {
                                    isSwiping = true
                                    longPressJob.cancel()
                                }
                            }

                            if (isSwiping) {
                                // 支持左滑和右滑
                                val newOffset = swipeOffset + dragAmount.x
                                swipeOffset = newOffset.coerceIn(-actionButtonWidth, actionButtonWidth)
                                change.consume()
                            } else if (isDragTriggered) {
                                // 限制在同优先级范围内拖拽
                                val canMoveUp = taskIndex > samePriorityStartIndex
                                val canMoveDown = taskIndex < samePriorityEndIndex
                                val minOffset = if (canMoveUp) -(taskIndex - samePriorityStartIndex) * itemHeight else 0f
                                val maxOffset = if (canMoveDown) (samePriorityEndIndex - taskIndex) * itemHeight else 0f

                                currentDragOffsetY = (currentDragOffsetY + dragAmount.y).coerceIn(minOffset, maxOffset)
                                onDragUpdate(currentDragOffsetY)
                                change.consume()
                            }
                        }

                        gestureCancelled = true
                        longPressJob.cancel()

                        if (isSwiping) {
                            when {
                                swipeOffset < -actionButtonWidth / 2 -> {
                                    swipeOffset = -actionButtonWidth
                                    onSwipeStateChanged(true)
                                }
                                swipeOffset > actionButtonWidth / 2 -> {
                                    swipeOffset = actionButtonWidth
                                    onSwipeStateChanged(true)
                                }
                                else -> {
                                    swipeOffset = 0f
                                    onSwipeStateChanged(false)
                                }
                            }
                            isSwiping = false
                        }

                        if (isDragTriggered) {
                            val movedPositions = (currentDragOffsetY / itemHeight).roundToInt()
                            val targetIndex = (taskIndex + movedPositions).coerceIn(0, totalItems - 1)
                            onDragEnd(taskIndex, targetIndex)
                        }

                        val duration = System.currentTimeMillis() - startTime
                        if (!isSwiping && !isDragTriggered && duration < 300 && abs(totalDragX) < 15f && abs(totalDragY) < 15f) {
                            onClick()
                        }
                    }
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = task.priority.bgColor
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DragIndicator,
                        contentDescription = "拖拽排序",
                        tint = TextMuted.copy(alpha = if (canDrag) 0.5f else 0.2f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = String.format("%02d", taskIndex + 1),
                        color = TextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.width(28.dp)
                    )
                    Text(
                        text = task.title,
                        color = if (isCompleted) TextMuted else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    PriorityBadge(priority = task.priority)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "截止: ${if (showFullDate) task.deadline.format(fullFormatter) else task.deadline.format(formatter)}",
                        color = if (isCompleted) TextMuted.copy(alpha = 0.6f)
                               else if (isOverdue) MaterialTheme.colorScheme.error
                               else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall.copy(
                            textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                        ),
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.weight(1f)
                    )
                    StatusTag(displayStatus = task.displayStatus)
                }

                // 时间信息 + 分类标签（与上方状态标签垂直对齐）
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 已完成卡片显示完成时间
                    if (isCompleted) {
                        val completedTime = task.completedAt?.let {
                            java.time.Instant.ofEpochMilli(it)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDateTime()
                        } ?: task.deadline // 默认使用截止时间
                        Text(
                            text = "完成: ${completedTime.format(fullFormatter)}",
                            color = TextMuted.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // 未完成卡片显示倒计时或逾期时间
                    else {
                        val now = LocalDateTime.now()
                        if (isOverdue) {
                            val duration = Duration.between(task.deadline, now)
                            val hours = duration.toHours()
                            val minutes = duration.toMinutes() % 60
                            val text = when {
                                hours >= 24 -> {
                                    val days = hours / 24
                                    val remainingHours = hours % 24
                                    "已逾期 ${days}天${remainingHours}小时"
                                }
                                hours > 0 -> "已逾期 ${hours}小时${minutes}分钟"
                                else -> "已逾期 ${minutes}分钟"
                            }
                            Text(
                                text = text,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            val duration = Duration.between(now, task.deadline)
                            val hours = duration.toHours()
                            val minutes = duration.toMinutes() % 60
                            val text = when {
                                hours >= 24 -> {
                                    val days = hours / 24
                                    val remainingHours = hours % 24
                                    "剩余 ${days}天${remainingHours}小时"
                                }
                                hours > 0 -> "剩余 ${hours}小时${minutes}分钟"
                                else -> "剩余 ${minutes}分钟"
                            }
                            Text(
                                text = text,
                                color = TextMuted,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    // 分类标签（右侧，与上方状态标签垂直对齐）
                    CategoryTag(category = task.category)
                }
            }
        }
    }
}
