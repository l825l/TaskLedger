package com.ledger.task.data.local

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query

/**
 * 标签数据访问对象
 */
@Dao
interface TagDao {

    // 获取所有标签
    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun getAllTags(): Flow<List<TagEntity>>

    // 根据 ID 获取标签
    @Query("SELECT * FROM tags WHERE id = :tagId")
    suspend fun getTagById(tagId: Long): TagEntity?

    // 插入标签
    @Insert
    suspend fun insertTag(tag: TagEntity): Long

    // 删除标签
    @Delete
    suspend fun deleteTag(tag: TagEntity)

    // 获取任务的所有标签
    @Query("""
        SELECT t.* FROM tags t
        INNER JOIN task_tags tt ON t.id = tt.tagId
        WHERE tt.taskId = :taskId
        ORDER BY t.name ASC
    """)
    fun getTagsForTask(taskId: Long): Flow<List<TagEntity>>

    // 获取标签关联的任务数量
    @Query("SELECT COUNT(*) FROM task_tags WHERE tagId = :tagId")
    fun getTaskCountForTag(tagId: Long): Flow<Int>

    // 添加任务-标签关联
    @Insert
    suspend fun addTaskTag(crossRef: TaskTagCrossRef)

    // 删除任务-标签关联
    @Query("DELETE FROM task_tags WHERE taskId = :taskId AND tagId = :tagId")
    suspend fun removeTaskTag(taskId: Long, tagId: Long)

    // 删除任务的所有标签关联
    @Query("DELETE FROM task_tags WHERE taskId = :taskId")
    suspend fun clearTaskTags(taskId: Long)

    // 获取包含指定标签的任务 ID 列表
    @Query("SELECT taskId FROM task_tags WHERE tagId = :tagId")
    fun getTaskIdsByTag(tagId: Long): Flow<List<Long>>

    // 检查标签名称是否存在
    @Query("SELECT EXISTS(SELECT 1 FROM tags WHERE name = :name AND id != :excludeId)")
    suspend fun isNameExists(name: String, excludeId: Long = 0): Boolean
}