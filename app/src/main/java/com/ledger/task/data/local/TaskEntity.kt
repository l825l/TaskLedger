package com.ledger.task.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.Recurrence
import com.ledger.task.domain.model.RecurrenceType
import com.ledger.task.domain.model.RichContent
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import java.time.DayOfWeek
import java.time.LocalDateTime

/**
 * 任务实体（Room Entity）
 * 仅用于数据库操作，不直接暴露给 UI 层
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val priority: Priority,
    val deadline: LocalDateTime,
    val status: TaskStatus,
    val description: String = "",
    val category: String = "",
    val hasImage: Boolean = false,
    val predecessorIds: String = "",  // 前置依赖任务ID，逗号分隔
    val relatedIds: String = "",      // 相关任务ID，逗号分隔
    val richContent: String = "",     // 富文本内容（序列化存储）
    val sortOrder: Int = 0,           // 排序顺序（今日待办专用）
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,    // 完成时间（毫秒时间戳）
    // 循环任务字段
    val recurrence: String? = null,   // 循环规则（JSON序列化）
    val isRecurringInstance: Boolean = false,
    val parentRecurringId: Long? = null
) {
    /**
     * 计算显示状态（运行时衍生值）
     */
    val displayStatus: DisplayStatus
        get() = calculateDisplayStatus(status, deadline)

    private fun calculateDisplayStatus(status: TaskStatus, deadline: LocalDateTime): DisplayStatus {
        val now = LocalDateTime.now()
        return when {
            status == TaskStatus.DONE -> DisplayStatus.DONE
            deadline.isBefore(now) -> DisplayStatus.OVERDUE
            else -> when (status) {
                TaskStatus.PENDING -> DisplayStatus.PENDING
                TaskStatus.IN_PROGRESS -> DisplayStatus.IN_PROGRESS
                TaskStatus.DONE -> DisplayStatus.DONE
            }
        }
    }
}

/**
 * 序列化 Recurrence 为字符串
 */
fun Recurrence?.serializeToString(): String? {
    if (this == null) return null
    val daysStr = daysOfWeek?.joinToString(",") { it.name } ?: ""
    val endStr = endDate?.toString() ?: ""
    return "${type.name}|$interval|$endStr|$daysStr|${count ?: ""}"
}

/**
 * 从字符串反序列化 Recurrence
 */
fun String?.deserializeToRecurrence(): Recurrence? {
    if (this.isNullOrBlank()) return null
    val parts = this.split("|")
    if (parts.size < 5) return null
    return try {
        Recurrence(
            type = RecurrenceType.valueOf(parts[0]),
            interval = parts[1].toIntOrNull() ?: 1,
            endDate = if (parts[2].isNotBlank()) LocalDateTime.parse(parts[2]) else null,
            daysOfWeek = if (parts[3].isNotBlank()) {
                parts[3].split(",").mapNotNull {
                    try { DayOfWeek.valueOf(it) } catch (e: Exception) { null }
                }.toSet().takeIf { it.isNotEmpty() }
            } else null,
            count = parts[4].toIntOrNull()
        )
    } catch (e: Exception) {
        null
    }
}

/**
 * 将 TaskEntity 转换为 Task（Domain Model）
 */
fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    priority = priority,
    deadline = deadline,
    status = status,
    displayStatus = displayStatus,
    description = description,
    category = category,
    hasImage = hasImage,
    richContent = RichContent.deserialize(richContent),
    predecessorIds = parseIdList(predecessorIds),
    relatedIds = parseIdList(relatedIds),
    sortOrder = sortOrder,
    createdAt = createdAt,
    completedAt = completedAt,
    recurrence = recurrence.deserializeToRecurrence(),
    isRecurringInstance = isRecurringInstance,
    parentRecurringId = parentRecurringId
)

/**
 * 将 Task（Domain Model）转换为 TaskEntity
 */
fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    priority = priority,
    deadline = deadline,
    status = status,
    description = description,
    category = category,
    hasImage = hasImage,
    richContent = richContent.serialize(),
    predecessorIds = formatIdList(predecessorIds),
    relatedIds = formatIdList(relatedIds),
    sortOrder = sortOrder,
    createdAt = createdAt,
    completedAt = completedAt,
    recurrence = recurrence.serializeToString(),
    isRecurringInstance = isRecurringInstance,
    parentRecurringId = parentRecurringId
)
