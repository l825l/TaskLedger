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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledger.task.data.model.DisplayStatus
import com.ledger.task.data.model.Priority
import com.ledger.task.data.model.Task
import com.ledger.task.ui.component.CategoryTag
import com.ledger.task.ui.component.DraggableFloatingActionButton
import com.ledger.task.ui.component.EmptyState
import com.ledger.task.ui.component.PriorityBadge
import com.ledger.task.ui.component.StatusTag
import com.ledger.task.ui.theme.DeepBackground
import com.ledger.task.ui.theme.PriorityHighBg
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.SurfaceBackground
import com.ledger.task.ui.theme.TextMuted
import com.ledger.task.viewmodel.PriorityTasksUiState
import com.ledger.task.viewmodel.PriorityTasksViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 重点待办屏幕
 */
@Composable
fun PriorityTasksScreen(
    viewModel: PriorityTasksViewModel,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            // 标题
            Text(
                text = "重点待办",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

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
                // 当前左滑的任务ID（用于单次左滑限制）
                var currentlySwipedTaskId by remember { mutableLongStateOf(-1L) }

                // 点击空白区域重置左滑状态
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                        ) {
                            // 点击空白区域时重置左滑状态
                            if (currentlySwipedTaskId != -1L) {
                                currentlySwipedTaskId = -1L
                            }
                        }
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(uiState.tasks, key = { _, task -> task.id }) { index, task ->
                            SwipeablePriorityTaskRow(
                                task = task,
                                index = index,
                                onClick = { onNavigateToEdit(task.id) },
                                onCompleted = { viewModel.onTaskCompleted(task.id) },
                                onPriorityUpgrade = { viewModel.onPriorityUpgrade(task.id) },
                                onPriorityDowngrade = { viewModel.onPriorityDowngrade(task.id) },
                                isCurrentlySwiped = currentlySwipedTaskId == task.id,
                                onSwipeStateChanged = { isSwiped ->
                                    if (isSwiped) {
                                        currentlySwipedTaskId = task.id
                                    } else if (currentlySwipedTaskId == task.id) {
                                        currentlySwipedTaskId = -1L
                                    }
                                }
                            )
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

/**
 * 可左滑/右滑的重点任务行
 * 左滑：显示完成/取消按钮
 * 右滑：显示升降级按钮
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeablePriorityTaskRow(
    task: Task,
    index: Int,
    onClick: () -> Unit,
    onCompleted: () -> Unit,
    onPriorityUpgrade: () -> Unit,
    onPriorityDowngrade: () -> Unit,
    isCurrentlySwiped: Boolean = false,
    onSwipeStateChanged: (Boolean) -> Unit = {}
) {
    val density = LocalDensity.current

    // 滑动偏移（负数为左滑，正数为右滑）
    var swipeOffset by remember { mutableFloatStateOf(0f) }
    var isSwiping by remember { mutableStateOf(false) }

    // 降级确认对话框
    var showDowngradeDialog by remember { mutableStateOf(false) }

    // 滑动按钮宽度
    val actionButtonWidth = with(density) { 120.dp.toPx() }

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val fullFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
    val isOverdue = task.displayStatus == DisplayStatus.OVERDUE

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

    // 降级确认对话框
    if (showDowngradeDialog) {
        AlertDialog(
            onDismissRequest = { showDowngradeDialog = false },
            title = { Text("确认降级") },
            text = { Text("从「高」降级到「中」后，该任务将从重点待办列表中移除。确定要降级吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onPriorityDowngrade()
                        showDowngradeDialog = false
                        swipeOffset = 0f
                        onSwipeStateChanged(false)
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDowngradeDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // 左滑背景按钮（在卡片右侧显示）
        if (swipeOffset < -10f) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(120.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                // 完成按钮
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                        .background(StatusDone)
                        .clickable {
                            showCompleteAnimation = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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

                // 取消按钮
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                        .background(Color(0xFF888888))
                        .clickable {
                            swipeOffset = 0f
                            onSwipeStateChanged(false)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                    .height(68.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                // 升级按钮（只有"中"优先级可以升级到"高"）
                if (task.priority == Priority.MEDIUM) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable {
                                onPriorityUpgrade()
                                swipeOffset = 0f
                                onSwipeStateChanged(false)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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

                // 降级按钮
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            // 如果是"高"(MEDIUM)优先级降级，需要确认
                            if (task.priority == Priority.MEDIUM) {
                                showDowngradeDialog = true
                            } else {
                                onPriorityDowngrade()
                                swipeOffset = 0f
                                onSwipeStateChanged(false)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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

        // 任务卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeOffset.roundToInt(), 0) }
                .graphicsLayer {
                    scaleX = completeScale.value
                    scaleY = completeScale.value
                    alpha = completeAlpha.value
                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        var totalDragX = 0f
                        var totalDragY = 0f

                        val startTime = System.currentTimeMillis()

                        drag(down.id) { change ->
                            val dragAmount = change.positionChange()
                            totalDragX += dragAmount.x
                            totalDragY += dragAmount.y

                            // 判断手势类型
                            if (!isSwiping) {
                                if (abs(totalDragX) > 20f && abs(totalDragX) > abs(totalDragY) * 1.5f) {
                                    isSwiping = true
                                }
                            }

                            if (isSwiping) {
                                // 支持左滑和右滑
                                val newOffset = swipeOffset + dragAmount.x
                                swipeOffset = newOffset.coerceIn(-actionButtonWidth, actionButtonWidth)
                                change.consume()
                            }
                        }

                        if (isSwiping) {
                            // 根据滑动方向决定展开或收回
                            when {
                                swipeOffset < -actionButtonWidth / 2 -> {
                                    // 左滑展开
                                    swipeOffset = -actionButtonWidth
                                    onSwipeStateChanged(true)
                                }
                                swipeOffset > actionButtonWidth / 2 -> {
                                    // 右滑展开
                                    swipeOffset = actionButtonWidth
                                    onSwipeStateChanged(true)
                                }
                                else -> {
                                    // 收回
                                    swipeOffset = 0f
                                    onSwipeStateChanged(false)
                                }
                            }
                            isSwiping = false
                        }

                        // 点击处理
                        val duration = System.currentTimeMillis() - startTime
                        if (!isSwiping && duration < 300 && abs(totalDragX) < 15f && abs(totalDragY) < 15f) {
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
                    Text(
                        text = String.format("%02d", index + 1),
                        color = TextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.width(32.dp)
                    )
                    Text(
                        text = task.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
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
                        text = "截止: ${if (isOverdue || showFullDate) task.deadline.format(fullFormatter) else task.deadline.format(formatter)}",
                        color = if (isOverdue) MaterialTheme.colorScheme.error
                               else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
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
                    if (isOverdue) {
                        val now = LocalDateTime.now()
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
                        val now = LocalDateTime.now()
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
                    // 分类标签（右侧，与上方状态标签垂直对齐）
                    CategoryTag(category = task.category)
                }
            }
        }
    }
}
