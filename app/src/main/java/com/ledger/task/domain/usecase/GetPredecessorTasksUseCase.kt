package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Task
import com.ledger.task.domain.repository.TaskRepository

/**
 * 获取前置依赖任务用例
 */
class GetPredecessorTasksUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long): List<Task> {
        return repository.getPredecessorTasks(taskId)
    }
}
