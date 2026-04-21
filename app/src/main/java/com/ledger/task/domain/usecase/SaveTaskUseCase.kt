package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Task
import com.ledger.task.domain.repository.TaskRepository

/**
 * 保存任务用例
 */
class SaveTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Long {
        return if (task.id > 0) {
            repository.update(task)
            task.id
        } else {
            repository.insert(task)
        }
    }
}
