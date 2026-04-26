package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Task
import com.ledger.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * 获取今日任务用例
 */
class GetTodayTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(todayStartMillis: Long, todayEndMillis: Long): Flow<List<Task>> {
        return repository.getTodayTasks(todayStartMillis, todayEndMillis)
    }
}
