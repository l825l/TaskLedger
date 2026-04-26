package com.ledger.task.domain.repository

import com.ledger.task.domain.model.Tag
import kotlinx.coroutines.flow.Flow

/**
 * 任务标签信息（用于任务卡片显示）
 */
data class TaskTagInfo(
    val taskId: Long,
    val tagName: String,
    val tagColorArgb: Int
)

interface TagRepository {
    fun getAllTags(): Flow<List<Tag>>
    suspend fun getTagById(id: Long): Tag?
    suspend fun saveTag(tag: Tag): Long
    suspend fun deleteTag(tag: Tag)
    fun getTagsForTask(taskId: Long): Flow<List<Tag>>
    fun getTaskCountForTag(tagId: Long): Flow<Int>
    suspend fun addTagToTask(taskId: Long, tagId: Long)
    suspend fun removeTagFromTask(taskId: Long, tagId: Long)
    suspend fun clearTaskTags(taskId: Long)
    fun getTaskIdsByTag(tagId: Long): Flow<List<Long>>
    suspend fun isNameExists(name: String, excludeId: Long = 0): Boolean
    // 批量获取任务的第一个标签（用于任务卡片显示）
    suspend fun getFirstTagForTasks(taskIds: List<Long>): List<TaskTagInfo>
}
