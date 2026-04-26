package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Task
import com.ledger.task.domain.repository.TaskRepository

/**
 * 获取单个任务用例
 */
class GetTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long): Task? {
        return repository.getById(id)
    }
}
