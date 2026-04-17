package com.ledger.task.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ledger.task.data.model.DisplayStatus
import com.ledger.task.data.model.Priority
import com.ledger.task.data.model.RichContent
import com.ledger.task.data.model.TaskStatus
import java.time.LocalDate
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
    val completedAt: Long? = null     // 完成时间（毫秒时间戳）
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
 * 将 TaskEntity 转换为 Task（Domain Model）
 */
fun TaskEntity.toDomain(): com.ledger.task.data.model.Task = com.ledger.task.data.model.Task(
    id = id,
    title = title,
    priority = priority,
    deadline = deadline,
    status = status,
    displayStatus = displayStatus,
    description = description,
    category = category,
    hasImage = hasImage,
    richContent = com.ledger.task.data.model.RichContent.deserialize(richContent),
    predecessorIds = parseIdList(predecessorIds),
    relatedIds = parseIdList(relatedIds),
    sortOrder = sortOrder,
    createdAt = createdAt,
    completedAt = completedAt
)

/**
 * 将 Task（Domain Model）转换为 TaskEntity
 */
fun com.ledger.task.data.model.Task.toEntity(): TaskEntity = TaskEntity(
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
    completedAt = completedAt
)
