package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Task
import com.ledger.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * 获取优先任务用例
 */
class GetPriorityTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(nowMillis: Long): Flow<List<Task>> {
        return repository.getPriorityTasks(nowMillis)
    }
}
