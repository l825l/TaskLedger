package com.ledger.task.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.QuickTag
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.ui.component.DraggableFloatingActionButton
import com.ledger.task.ui.component.EmptyState
import com.ledger.task.ui.component.MultiFilterBar
import com.ledger.task.ui.component.ActiveFiltersChip
import com.ledger.task.ui.component.PriorityBadge
import com.ledger.task.ui.component.StatusTag
import com.ledger.task.ui.component.CategoryTag
import com.ledger.task.ui.theme.getDeepBackground
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.viewmodel.AllTasksViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

/**
 * 全部事务屏幕
 */
@Composable
fun AllTasksScreen(
    viewModel: AllTasksViewModel,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(getDeepBackground())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 标题
            Text(
                text = "全部事务",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

        // 筛选器面板
        FilterPanel(
            searchQuery = uiState.searchQuery,
            category = uiState.filterState.category,
            priority = uiState.filterState.priority,
            status = uiState.filterState.status,
            hasImage = uiState.filterState.hasImage,
            quickTag = uiState.filterState.quickTag,
            onSearchChange = viewModel::onSearchChange,
            onCategoryChange = viewModel::onCategoryFilterChange,
            onPriorityChange = viewModel::onPriorityFilterChange,
            onStatusChange = viewModel::onStatusFilterChange,
            onHasImageChange = viewModel::onHasImageFilterChange,
            onQuickTagChange = viewModel::onQuickTagFilterChange,
            onClearAllFilters = {
                viewModel.onCategoryFilterChange(null)
                viewModel.onPriorityFilterChange(null)
                viewModel.onStatusFilterChange(null)
                viewModel.onHasImageFilterChange(null)
                viewModel.onQuickTagFilterChange(null)
            }
        )

        // 任务列表
        if (uiState.tasks.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                text = "暂无匹配的任务"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(uiState.tasks, key = { _, task -> task.id }) { index, task ->
                    TaskRow(
                        task = task,
                        index = index,
                        onClick = { onNavigateToEdit(task.id) }
                    )
                }
            }
        }

        // 统计
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "总计: ${uiState.tasks.size}",
                color = getTextMuted(),
                style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace
            )
        }
        }

        // 可拖动悬浮按钮
        DraggableFloatingActionButton(
            onClick = onNavigateToAdd
        )
    }
}

@Composable
private fun FilterPanel(
    searchQuery: String,
    category: String?,
    priority: Priority?,
    status: DisplayStatus?,
    hasImage: Boolean?,
    quickTag: QuickTag?,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String?) -> Unit,
    onPriorityChange: (Priority?) -> Unit,
    onStatusChange: (DisplayStatus?) -> Unit,
    onHasImageChange: (Boolean?) -> Unit,
    onQuickTagChange: (QuickTag?) -> Unit,
    onClearAllFilters: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 搜索框
        androidx.compose.material3.OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            label = { Text("搜索标题") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = com.ledger.task.ui.theme.Accent,
                unfocusedBorderColor = com.ledger.task.ui.theme.BorderDim
            )
        )

        // 多维筛选器
        MultiFilterBar(
            category = category,
            priority = priority,
            status = status,
            hasImage = hasImage,
            quickTag = quickTag,
            onCategoryChange = onCategoryChange,
            onPriorityChange = onPriorityChange,
            onStatusChange = onStatusChange,
            onHasImageChange = onHasImageChange,
            onQuickTagChange = onQuickTagChange
        )

        // 清除筛选按钮
        ActiveFiltersChip(
            category = category,
            priority = priority,
            status = status,
            hasImage = hasImage,
            quickTag = quickTag,
            onClearAll = onClearAllFilters
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskRow(
    task: Task,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fullFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
    val isOverdue = task.displayStatus == DisplayStatus.OVERDUE
    val isCompleted = task.status == TaskStatus.DONE

    // 计算截止时间距离现在的时长
    val now = LocalDateTime.now()
    val durationToDeadline = Duration.between(now, task.deadline)
    val hoursToDeadline = durationToDeadline.toHours()

    // 计算逾期时长
    val overdueDuration = if (isOverdue) Duration.between(task.deadline, now) else Duration.ZERO
    val overdueHours = overdueDuration.toHours()
    val overdueMinutes = overdueDuration.toMinutes() % 60

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = task.priority.bgColor
        ),
        onClick = onClick
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
                    color = getTextMuted(),
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
                // 截止时间：始终显示完整日期时间
                Text(
                    text = "截止: ${task.deadline.format(fullFormatter)}",
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
                // 已完成卡片显示完成时间
                if (isCompleted) {
                    val completedTime = task.completedAt?.let {
                        java.time.Instant.ofEpochMilli(it)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDateTime()
                    } ?: task.deadline  // 默认使用截止时间
                    Text(
                        text = "完成: ${completedTime.format(fullFormatter)}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.weight(1f)
                    )
                }
                // 已逾期卡片显示逾期时长
                else if (isOverdue) {
                    val overdueText = when {
                        overdueHours >= 24 -> {
                            val days = overdueHours / 24
                            val remainingHours = overdueHours % 24
                            "已逾期 ${days}天${remainingHours}小时"
                        }
                        overdueHours > 0 -> "已逾期 ${overdueHours}小时${overdueMinutes}分钟"
                        else -> "已逾期 ${overdueMinutes}分钟"
                    }
                    Text(
                        text = overdueText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.weight(1f)
                    )
                }
                // 未完成且未逾期：显示剩余时间
                else {
                    val remainingText = when {
                        hoursToDeadline >= 24 -> {
                            val days = hoursToDeadline / 24
                            val remainingHours = hoursToDeadline % 24
                            "剩余 ${days}天${remainingHours}小时"
                        }
                        hoursToDeadline > 0 -> "剩余 ${hoursToDeadline}小时${durationToDeadline.toMinutes() % 60}分钟"
                        else -> "剩余 ${durationToDeadline.toMinutes() % 60}分钟"
                    }
                    Text(
                        text = remainingText,
                        color = getTextMuted(),
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
