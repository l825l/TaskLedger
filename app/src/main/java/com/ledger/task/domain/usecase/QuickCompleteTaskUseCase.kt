package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TaskRepository

/**
 * 快速完成任务用例
 */
class QuickCompleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long): Result<Unit> {
        val task = repository.getById(taskId) ?: return Result.failure(Exception("Task not found"))
        val updatedTask = task.copy(
            status = TaskStatus.DONE,
            completedAt = System.currentTimeMillis()
        )
        return repository.update(updatedTask)
    }
}
