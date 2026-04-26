package com.ledger.task.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ledger.task.domain.model.SubTask

/**
 * 子任务实体
 */
@Entity(
    tableName = "sub_tasks",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["parentId"])]
)
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val parentId: Long,
    val title: String,
    val isCompleted: Boolean = false,
    val sortOrder: Int = 0
)

/**
 * 将 SubTaskEntity 转换为 SubTask
 */
fun SubTaskEntity.toDomain(): SubTask = SubTask(
    id = id,
    parentId = parentId,
    title = title,
    isCompleted = isCompleted,
    sortOrder = sortOrder
)

/**
 * 将 SubTask 转换为 SubTaskEntity
 */
fun SubTask.toEntity(): SubTaskEntity = SubTaskEntity(
    id = id,
    parentId = parentId,
    title = title,
    isCompleted = isCompleted,
    sortOrder = sortOrder
)
