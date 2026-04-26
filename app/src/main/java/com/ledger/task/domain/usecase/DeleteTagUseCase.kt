package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository

class DeleteTagUseCase(private val repository: TagRepository) {
    suspend operator fun invoke(tag: Tag): Result<Unit> {
        return runCatching {
            repository.deleteTag(tag)
        }
    }
}
