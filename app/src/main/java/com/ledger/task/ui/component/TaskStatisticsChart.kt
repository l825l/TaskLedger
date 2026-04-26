package com.ledger.task.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.ui.theme.StatusDone
import com.ledger.task.ui.theme.StatusOverdue
import com.ledger.task.ui.theme.StatusPending
import com.ledger.task.ui.theme.StatusProgress
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.min

/**
 * 任务统计图表数据
 */
data class TaskChartData(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val inProgressTasks: Int = 0,
    val pendingTasks: Int = 0,
    val overdueTasks: Int = 0,
    val priorityDistribution: Map<Priority, Int> = emptyMap(),
    val completionTrend: List<DailyCompletion> = emptyList()
)

/**
 * 每日完成数据
 */
data class DailyCompletion(
    val date: LocalDate,
    val completed: Int,
    val total: Int
)

/**
 * 任务统计图表组件
 */
@Composable
fun TaskStatisticsChart(
    tasks: List<Task>,
    modifier: Modifier = Modifier
) {
    val chartData = rememberChartData(tasks)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 完成趋势图
        if (chartData.completionTrend.isNotEmpty()) {
            ChartCard(title = "完成趋势（最近7天）") {
                CompletionTrendChart(
                    data = chartData.completionTrend,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }

        // 状态分布
        ChartCard(title = "状态分布") {
            StatusDistributionChart(
                total = chartData.totalTasks,
                completed = chartData.completedTasks,
                inProgress = chartData.inProgressTasks,
                pending = chartData.pendingTasks,
                overdue = chartData.overdueTasks,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 优先级分布
        if (chartData.priorityDistribution.isNotEmpty()) {
            ChartCard(title = "优先级分布") {
                PriorityDistributionChart(
                    distribution = chartData.priorityDistribution,
                    total = chartData.totalTasks,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * 图表卡片容器
 */
@Composable
private fun ChartCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = getSurfaceBackground())
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                color = getTextPrimary(),
                style = MaterialTheme.typography.titleMedium
            )
            content()
        }
    }
}

/**
 * 完成趋势折线图
 */
@Composable
private fun CompletionTrendChart(
    data: List<DailyCompletion>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无数据",
                color = getTextMuted(),
                style = MaterialTheme.typography.bodySmall
            )
        }
        return
    }

    val maxValue = data.maxOfOrNull { it.completed } ?: 0
    val minValue = 0
    val valueRange = (maxValue - minValue).coerceAtLeast(1)

    Column(modifier = modifier) {
        // 图表区域
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val chartWidth = size.width
            val chartHeight = size.height
            val barWidth = chartWidth / data.size * 0.6f
            val gap = chartWidth / data.size * 0.4f

            data.forEachIndexed { index, item ->
                val barHeight = if (item.completed > 0) {
                    (item.completed.toFloat() / valueRange) * chartHeight * 0.8f
                } else {
                    2f
                }

                val x = index * (barWidth + gap) + gap / 2
                val y = chartHeight - barHeight

                // 绘制柱状图
                drawRoundRect(
                    color = StatusDone,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            }
        }

        // 日期标签
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { item ->
                Text(
                    text = item.date.format(DateTimeFormatter.ofPattern("MM/dd")),
                    color = getTextMuted(),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 状态分布图表
 */
@Composable
private fun StatusDistributionChart(
    total: Int,
    completed: Int,
    inProgress: Int,
    pending: Int,
    overdue: Int,
    modifier: Modifier = Modifier
) {
    if (total == 0) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无任务",
                color = getTextMuted(),
                style = MaterialTheme.typography.bodySmall
            )
        }
        return
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatusBarItem(
            label = "已完成",
            value = completed,
            total = total,
            color = StatusDone,
            modifier = Modifier.weight(1f)
        )
        StatusBarItem(
            label = "进行中",
            value = inProgress,
            total = total,
            color = StatusProgress,
            modifier = Modifier.weight(1f)
        )
        StatusBarItem(
            label = "待办",
            value = pending,
            total = total,
            color = StatusPending,
            modifier = Modifier.weight(1f)
        )
        StatusBarItem(
            label = "逾期",
            value = overdue,
            total = total,
            color = StatusOverdue,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 状态条形项
 */
@Composable
private fun StatusBarItem(
    label: String,
    value: Int,
    total: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val percentage = if (total > 0) value.toFloat() / total else 0f

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 进度条
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(getElevatedBackground(), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage)
                    .height(8.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }

        // 数值
        Text(
            text = value.toString(),
            color = color,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace
        )

        // 标签
        Text(
            text = label,
            color = getTextMuted(),
            fontSize = 11.sp
        )
    }
}

/**
 * 优先级分布图表
 */
@Composable
private fun PriorityDistributionChart(
    distribution: Map<Priority, Int>,
    total: Int,
    modifier: Modifier = Modifier
) {
    if (total == 0 || distribution.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无数据",
                color = getTextMuted(),
                style = MaterialTheme.typography.bodySmall
            )
        }
        return
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 饼图
        Canvas(
            modifier = Modifier.size(100.dp)
        ) {
            var startAngle = -90f
            val sortedPriorities = listOf(
                Priority.HIGH to (distribution[Priority.HIGH] ?: 0),
                Priority.MEDIUM to (distribution[Priority.MEDIUM] ?: 0),
                Priority.NORMAL to (distribution[Priority.NORMAL] ?: 0),
                Priority.LOW to (distribution[Priority.LOW] ?: 0)
            )

            sortedPriorities.forEach { (priority, count) ->
                if (count > 0) {
                    val sweepAngle = 360f * count / total
                    drawArc(
                        color = priority.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true
                    )
                    startAngle += sweepAngle
                }
            }
        }

        // 图例
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Priority.HIGH to "紧急",
                Priority.MEDIUM to "高",
                Priority.NORMAL to "中",
                Priority.LOW to "低"
            ).forEach { (priority, label) ->
                val count = distribution[priority] ?: 0
                if (count > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(priority.color, RoundedCornerShape(2.dp))
                        )
                        Text(
                            text = label,
                            color = getTextMuted(),
                            fontSize = 12.sp,
                            modifier = Modifier.width(32.dp)
                        )
                        Text(
                            text = count.toString(),
                            color = getTextPrimary(),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "(${(100f * count / total).toInt()}%)",
                            color = getTextMuted(),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * 计算图表数据（带缓存）
 */
@Composable
private fun rememberChartData(tasks: List<Task>): TaskChartData {
    return remember(tasks) {
        val today = LocalDate.now()
        val zoneId = ZoneId.systemDefault()

        // 基础统计
        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.status == TaskStatus.DONE }
        val inProgressTasks = tasks.count { it.status == TaskStatus.IN_PROGRESS }
        val pendingTasks = tasks.count { it.status == TaskStatus.PENDING && !isOverdue(it, today) }
        val overdueTasks = tasks.count { isOverdue(it, today) }

        // 优先级分布
        val priorityDistribution = tasks
            .filter { it.status != TaskStatus.DONE }
            .groupingBy { it.priority }
            .eachCount()

        // 预先按完成时间分组，提高效率
        val tasksByCompletedDate = tasks
            .filter { it.status == TaskStatus.DONE && it.completedAt != null }
            .groupBy {
                Instant.ofEpochMilli(it.completedAt!!)
                    .atZone(zoneId)
                    .toLocalDate()
            }

        // 完成趋势（最近7天）
        val completionTrend = (6 downTo 0).map { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            val completedOnDay = tasksByCompletedDate[date]?.size ?: 0
            val totalOnDay = tasks.count { task -> task.createdAt <= date.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() }

            DailyCompletion(date = date, completed = completedOnDay, total = totalOnDay)
        }

        TaskChartData(
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            inProgressTasks = inProgressTasks,
            pendingTasks = pendingTasks,
            overdueTasks = overdueTasks,
            priorityDistribution = priorityDistribution,
            completionTrend = completionTrend
        )
    }
}

/**
 * 判断任务是否逾期
 */
private fun isOverdue(task: Task, today: LocalDate): Boolean {
    if (task.status == TaskStatus.DONE) return false
    val deadline = task.deadline
    val deadlineDate = deadline.toLocalDate()
    return deadlineDate < today
}
