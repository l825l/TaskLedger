package com.ledger.task.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ledger.task.domain.model.SubTask
import kotlinx.coroutines.flow.Flow

/**
 * 子任务 DAO
 */
@Dao
interface SubTaskDao {

    /**
     * 获取指定任务的所有子任务
     */
    @Query("SELECT * FROM sub_tasks WHERE parentId = :parentId ORDER BY sortOrder ASC")
    fun getByParentId(parentId: Long): Flow<List<SubTaskEntity>>

    /**
     * 获取指定任务的所有子任务（一次性）
     */
    @Query("SELECT * FROM sub_tasks WHERE parentId = :parentId ORDER BY sortOrder ASC")
    suspend fun getByParentIdNow(parentId: Long): List<SubTaskEntity>

    /**
     * 插入子任务
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subTask: SubTaskEntity): Long

    /**
     * 批量插入子任务
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subTasks: List<SubTaskEntity>)

    /**
     * 更新子任务
     */
    @Update
    suspend fun update(subTask: SubTaskEntity)

    /**
     * 删除子任务
     */
    @Query("DELETE FROM sub_tasks WHERE id = :id")
    suspend fun delete(id: Long)

    /**
     * 删除指定任务的所有子任务
     */
    @Query("DELETE FROM sub_tasks WHERE parentId = :parentId")
    suspend fun deleteByParentId(parentId: Long)

    /**
     * 更新子任务完成状态
     */
    @Query("UPDATE sub_tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun updateCompleted(id: Long, completed: Boolean)

    /**
     * 更新子任务排序
     */
    @Query("UPDATE sub_tasks SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int)

    /**
     * 获取子任务数量
     */
    @Query("SELECT COUNT(*) FROM sub_tasks WHERE parentId = :parentId")
    suspend fun getCountByParentId(parentId: Long): Int

    /**
     * 获取已完成的子任务数量
     */
    @Query("SELECT COUNT(*) FROM sub_tasks WHERE parentId = :parentId AND isCompleted = 1")
    suspend fun getCompletedCountByParentId(parentId: Long): Int
}
