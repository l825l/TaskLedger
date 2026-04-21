package com.ledger.task.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ledger.task.domain.model.Priority
import kotlinx.coroutines.flow.Flow

/**
 * 任务数据访问对象
 */
@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Long): TaskEntity?

    @Insert
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    // 统计查询
    @Query("SELECT COUNT(*) FROM tasks")
    fun countAll(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE status = 'IN_PROGRESS'")
    fun countInProgress(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE status = 'DONE'")
    fun countDone(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE status != 'DONE' AND deadline < :todayEpochDay")
    fun countOverdue(todayEpochDay: Long): Flow<Int>

    // 搜索 + 优先级筛选组合
    @Query("""
        SELECT * FROM tasks
        WHERE title LIKE '%' || :query || '%'
        AND (:priorityName IS NULL OR priority = :priorityName)
        ORDER BY id DESC
    """)
    fun searchAndFilter(query: String, priorityName: String?): Flow<List<TaskEntity>>

    // 今日待办：截止日期在今天内（从今天开始到今天结束）
    // deadline 存储为毫秒（本地时区），使用今天的开始和结束时间（包含整个今天）
    // 按优先级降序排序（HIGH > MEDIUM > NORMAL > LOW），同优先级内按sortOrder排序
    @Query("""
        SELECT * FROM tasks
        WHERE deadline >= :todayStartMillis
        AND deadline < :todayEndMillis
        ORDER BY
            CASE WHEN status = 'DONE' THEN 1 ELSE 0 END,
            CASE priority
                WHEN 'HIGH' THEN 0
                WHEN 'MEDIUM' THEN 1
                WHEN 'NORMAL' THEN 2
                WHEN 'LOW' THEN 3
                ELSE 4
            END,
            sortOrder ASC
    """)
    fun getTodayTasks(todayStartMillis: Long, todayEndMillis: Long): Flow<List<TaskEntity>>

    // 批量更新排序顺序
    @Query("UPDATE tasks SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int)

    // 重点待办：紧急(HIGH)和高(MEDIUM)优先级、未完成（包含逾期）
    // 按优先级降序排序，同优先级内按sortOrder排序，逾期任务优先显示
    @Query("""
        SELECT * FROM tasks
        WHERE priority IN ('HIGH', 'MEDIUM')
        AND status != 'DONE'
        ORDER BY
            CASE WHEN deadline < :nowMillis THEN 0 ELSE 1 END,
            CASE priority
                WHEN 'HIGH' THEN 0
                WHEN 'MEDIUM' THEN 1
                ELSE 2
            END,
            sortOrder ASC
    """)
    fun getPriorityTasks(nowMillis: Long): Flow<List<TaskEntity>>

    // 带多维筛选的任务查询
    @Query("""
        SELECT * FROM tasks
        WHERE (:query IS NULL OR title LIKE '%' || :query || '%')
        AND (:priorityName IS NULL OR priority = :priorityName)
        AND (:statusName IS NULL OR status = :statusName)
        AND (:hasImage IS NULL OR hasImage = :hasImage)
        ORDER BY id DESC
    """)
    fun filterAllTasks(
        query: String?,
        priorityName: String?,
        statusName: String?,
        hasImage: Boolean?
    ): Flow<List<TaskEntity>>

    // 台账查询：按时间范围
    @Query("""
        SELECT * FROM tasks
        WHERE (:includeArchived = 1 OR status != 'DONE')
        AND (:category IS NULL OR category = :category)
        AND deadline >= :startDateEpoch
        AND deadline <= :endDateEpoch
        ORDER BY deadline DESC
    """)
    fun getLedgerTasks(
        startDateEpoch: Long,
        endDateEpoch: Long,
        includeArchived: Boolean,
        category: String?
    ): Flow<List<TaskEntity>>

    // 获取所有分类（去重）
    @Query("SELECT DISTINCT category FROM tasks WHERE category != ''")
    fun getAllCategories(): Flow<List<String>>

    // 根据 ID 列表获取任务（用于关联任务查询）
    @Query("SELECT * FROM tasks WHERE id IN (:ids)")
    fun getTasksByIds(ids: List<Long>): Flow<List<TaskEntity>>

    // 清空所有任务
    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
}
