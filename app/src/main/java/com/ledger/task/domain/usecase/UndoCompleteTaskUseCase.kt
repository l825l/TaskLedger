package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TaskRepository

/**
 * 撤销完成任务用例
 */
class UndoCompleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long): Result<Unit> {
        val task = repository.getById(taskId) ?: return Result.failure(Exception("Task not found"))
        val updatedTask = task.copy(
            status = TaskStatus.PENDING,
            completedAt = null
        )
        return repository.update(updatedTask)
    }
}
