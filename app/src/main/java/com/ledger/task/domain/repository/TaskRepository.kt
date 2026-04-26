package com.ledger.task.domain.repository

import com.ledger.task.domain.model.AllTasksFilterState
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.SubTask
import com.ledger.task.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * 任务仓库接口
 */
interface TaskRepository {
    fun getAll(): Flow<List<Task>>
    suspend fun getAllNow(): List<Task>
    suspend fun getById(id: Long): Task?
    suspend fun insert(task: Task): Long
    suspend fun update(task: Task): Result<Unit>
    suspend fun delete(task: Task): Result<Unit>
    fun countAll(): Flow<Int>
    fun countInProgress(): Flow<Int>
    fun countDone(): Flow<Int>
    fun countOverdue(todayEpochDay: Long): Flow<Int>
    fun searchAndFilter(query: String, priority: Priority?): Flow<List<Task>>

    // 新增查询方法
    fun getTodayTasks(todayStartMillis: Long, todayEndMillis: Long): Flow<List<Task>>
    fun getPriorityTasks(nowMillis: Long): Flow<List<Task>>
    fun filterAllTasks(filter: AllTasksFilterState): Flow<List<Task>>
    fun getLedgerTasks(
        startDateEpoch: Long,
        endDateEpoch: Long,
        includeArchived: Boolean,
        category: String?
    ): Flow<List<Task>>
    fun getAllCategories(): Flow<List<String>>

    // 关联任务查询
    suspend fun getPredecessorTasks(taskId: Long): List<Task>
    suspend fun getRelatedTasks(taskId: Long): List<Task>
    suspend fun addPredecessor(taskId: Long, predecessorId: Long)
    suspend fun removePredecessor(taskId: Long, predecessorId: Long)
    suspend fun addRelatedTask(taskId: Long, relatedId: Long)
    suspend fun removeRelatedTask(taskId: Long, relatedId: Long)
    suspend fun getTasksByIds(ids: List<Long>): List<Task>

    // 排序更新
    suspend fun updateSortOrder(id: Long, sortOrder: Int)

    // 子任务操作
    fun getSubTasks(parentId: Long): Flow<List<SubTask>>
    suspend fun getSubTasksNow(parentId: Long): List<SubTask>
    suspend fun insertSubTask(subTask: SubTask): Long
    suspend fun updateSubTask(subTask: SubTask)
    suspend fun deleteSubTask(id: Long)
    suspend fun deleteSubTasksByParentId(parentId: Long)
}
