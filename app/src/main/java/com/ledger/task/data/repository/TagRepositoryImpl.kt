package com.ledger.task.data.repository

import com.ledger.task.data.local.TagDao
import com.ledger.task.data.local.TagEntity
import com.ledger.task.data.local.TaskTagCrossRef
import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository
import com.ledger.task.domain.repository.TaskTagInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagRepositoryImpl(
    private val tagDao: TagDao
) : TagRepository {

    override fun getAllTags(): Flow<List<Tag>> {
        return tagDao.getAllTags().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTagById(id: Long): Tag? {
        return tagDao.getTagById(id)?.toDomain()
    }

    override suspend fun saveTag(tag: Tag): Long {
        val entity = TagEntity.fromDomain(tag)
        return tagDao.insertTag(entity)
    }

    override suspend fun deleteTag(tag: Tag) {
        tagDao.deleteTag(TagEntity.fromDomain(tag))
    }

    override fun getTagsForTask(taskId: Long): Flow<List<Tag>> {
        return tagDao.getTagsForTask(taskId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTaskCountForTag(tagId: Long): Flow<Int> {
        return tagDao.getTaskCountForTag(tagId)
    }

    override suspend fun addTagToTask(taskId: Long, tagId: Long) {
        tagDao.addTaskTag(TaskTagCrossRef(taskId, tagId))
    }

    override suspend fun removeTagFromTask(taskId: Long, tagId: Long) {
        tagDao.removeTaskTag(taskId, tagId)
    }

    override suspend fun clearTaskTags(taskId: Long) {
        tagDao.clearTaskTags(taskId)
    }

    override fun getTaskIdsByTag(tagId: Long): Flow<List<Long>> {
        return tagDao.getTaskIdsByTag(tagId)
    }

    override suspend fun isNameExists(name: String, excludeId: Long): Boolean {
        return tagDao.isNameExists(name, excludeId)
    }

    override suspend fun getFirstTagForTasks(taskIds: List<Long>): List<TaskTagInfo> {
        if (taskIds.isEmpty()) return emptyList()
        return tagDao.getFirstTagForTasks(taskIds).map { result ->
            TaskTagInfo(
                taskId = result.taskId,
                tagName = result.name,
                tagColorArgb = result.colorArgb
            )
        }
    }
}
