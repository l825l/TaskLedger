package com.ledger.task.data.repository

import com.ledger.task.data.local.TaskDao
import com.ledger.task.data.local.TaskEntity
import com.ledger.task.data.local.parseIdList
import com.ledger.task.data.local.toDomain
import com.ledger.task.data.local.toEntity
import com.ledger.task.data.model.AllTasksFilterState
import com.ledger.task.data.model.Priority
import com.ledger.task.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * 任务仓库实现
 */
class TaskRepositoryImpl(private val dao: TaskDao) : TaskRepository {

    override fun getAll(): Flow<List<Task>> = dao.getAll().map { tasks ->
        tasks.map { it.toDomain() }
    }

    override suspend fun getAllNow(): List<Task> = dao.getAll().first().map { it.toDomain() }

    override suspend fun getById(id: Long): Task? = dao.getById(id)?.toDomain()

    override suspend fun insert(task: Task): Long = runCatching { dao.insert(task.toEntity()) }
        .onFailure { if (it is kotlin.coroutines.cancellation.CancellationException) throw it }
        .getOrNull() ?: -1L

    override suspend fun update(task: Task) {
        runCatching { dao.update(task.toEntity()) }
            .onFailure {
                if (it is kotlin.coroutines.cancellation.CancellationException) throw it
            }
    }

    override suspend fun delete(task: Task) {
        runCatching { dao.delete(task.toEntity()) }
            .onFailure {
                if (it is kotlin.coroutines.cancellation.CancellationException) throw it
            }
    }

    override fun countAll(): Flow<Int> = dao.countAll()

    override fun countInProgress(): Flow<Int> = dao.countInProgress()

    override fun countDone(): Flow<Int> = dao.countDone()

    override fun countOverdue(todayEpochDay: Long): Flow<Int> = dao.countOverdue(todayEpochDay)

    override fun searchAndFilter(query: String, priority: Priority?): Flow<List<Task>> =
        dao.searchAndFilter(query, priority?.name).map { tasks ->
            tasks.map { it.toDomain() }
        }

    // 新增查询方法实现
    override fun getTodayTasks(todayStartMillis: Long, todayEndMillis: Long): Flow<List<Task>> =
        dao.getTodayTasks(todayStartMillis, todayEndMillis).map { tasks ->
            tasks.map { it.toDomain() }
        }

    override fun getPriorityTasks(nowMillis: Long): Flow<List<Task>> =
        dao.getPriorityTasks(nowMillis).map { tasks ->
            tasks.map { it.toDomain() }
        }

    override fun filterAllTasks(filter: AllTasksFilterState): Flow<List<Task>> =
        dao.filterAllTasks(
            query = if (filter.searchQuery.isBlank()) null else filter.searchQuery,
            priorityName = filter.priority?.name,
            statusName = filter.status?.name,
            hasImage = filter.hasImage
        ).map { tasks ->
            tasks.map { it.toDomain() }
        }

    override fun getLedgerTasks(
        startDateEpoch: Long,
        endDateEpoch: Long,
        includeArchived: Boolean,
        category: String?
    ): Flow<List<Task>> =
        dao.getLedgerTasks(startDateEpoch, endDateEpoch, includeArchived, category).map { tasks ->
            tasks.map { it.toDomain() }
        }

    override fun getAllCategories(): Flow<List<String>> = dao.getAllCategories()

    // 关联任务查询实现
    override suspend fun getPredecessorTasks(taskId: Long): List<Task> {
        val task = dao.getById(taskId) ?: return emptyList()
        val ids = parseIdList(task.predecessorIds)
        return if (ids.isEmpty()) emptyList() else dao.getTasksByIds(ids).first().map { it.toDomain() }
    }

    override suspend fun getRelatedTasks(taskId: Long): List<Task> {
        val task = dao.getById(taskId) ?: return emptyList()
        val ids = parseIdList(task.relatedIds)
        return if (ids.isEmpty()) emptyList() else dao.getTasksByIds(ids).first().map { it.toDomain() }
    }

    override suspend fun addPredecessor(taskId: Long, predecessorId: Long) {
        val task = dao.getById(taskId) ?: return
        val ids = parseIdList(task.predecessorIds)
        if (!ids.contains(predecessorId)) {
            val newIds = (ids + predecessorId).joinToString(",")
            dao.update(task.copy(predecessorIds = newIds))
        }
    }

    override suspend fun removePredecessor(taskId: Long, predecessorId: Long) {
        val task = dao.getById(taskId) ?: return
        val ids = parseIdList(task.predecessorIds)
        if (ids.contains(predecessorId)) {
            val newIds = ids.filter { it != predecessorId }.joinToString(",")
            dao.update(task.copy(predecessorIds = newIds))
        }
    }

    override suspend fun addRelatedTask(taskId: Long, relatedId: Long) {
        val task = dao.getById(taskId) ?: return
        val ids = parseIdList(task.relatedIds)
        if (!ids.contains(relatedId)) {
            val newIds = (ids + relatedId).joinToString(",")
            dao.update(task.copy(relatedIds = newIds))
        }
    }

    override suspend fun removeRelatedTask(taskId: Long, relatedId: Long) {
        val task = dao.getById(taskId) ?: return
        val ids = parseIdList(task.relatedIds)
        if (ids.contains(relatedId)) {
            val newIds = ids.filter { it != relatedId }.joinToString(",")
            dao.update(task.copy(relatedIds = newIds))
        }
    }

    override suspend fun getTasksByIds(ids: List<Long>): List<Task> {
        if (ids.isEmpty()) return emptyList()
        return dao.getTasksByIds(ids).first().map { it.toDomain() }
    }

    override suspend fun updateSortOrder(id: Long, sortOrder: Int) {
        dao.updateSortOrder(id, sortOrder)
    }
}
