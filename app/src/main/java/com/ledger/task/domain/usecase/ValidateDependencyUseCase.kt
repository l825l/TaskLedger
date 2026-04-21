package com.ledger.task.domain.usecase

import com.ledger.task.domain.DependencyValidationResult
import com.ledger.task.domain.TaskDependencyValidator

/**
 * 验证依赖关系用例
 */
class ValidateDependencyUseCase(
    private val validator: TaskDependencyValidator
) {
    suspend operator fun invoke(taskId: Long, predecessorId: Long): DependencyValidationResult {
        return validator.validatePredecessorAddition(taskId, predecessorId)
    }
}
