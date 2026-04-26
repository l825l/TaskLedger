package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.AllTasksFilterState
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * 获取所有任务用例
 */
class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(filter: AllTasksFilterState): Flow<List<Task>> {
        return repository.filterAllTasks(filter)
    }
}
