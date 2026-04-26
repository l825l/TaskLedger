package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TaskRepository
import java.time.LocalDateTime

/**
 * 完成任务用例
 * 处理循环任务：完成后自动创建下一个实例
 */
class CompleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Long): Result<Unit> {
        val task = repository.getById(taskId)
            ?: return Result.failure(Exception("Task not found"))

        // 更新任务为已完成
        val completedTask = task.copy(
            status = TaskStatus.DONE,
            completedAt = System.currentTimeMillis()
        )
        repository.update(completedTask)

        // 如果是循环任务，创建下一个实例
        task.recurrence?.let { recurrence ->
            // 从当前时间计算下一个循环日期
            val now = LocalDateTime.now()
            val nextDeadline = recurrence.nextOccurrence(now)
            if (nextDeadline != null) {
                val nextTask = task.copy(
                    id = 0,  // 新任务
                    status = TaskStatus.PENDING,
                    deadline = nextDeadline,
                    displayStatus = DisplayStatus.PENDING,
                    completedAt = null,
                    isRecurringInstance = true,
                    parentRecurringId = task.id
                )
                repository.insert(nextTask)
            }
        }

        return Result.success(Unit)
    }
}
