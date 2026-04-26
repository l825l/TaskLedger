package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository

class SaveTagUseCase(private val repository: TagRepository) {
    suspend operator fun invoke(tag: Tag): Result<Long> {
        return runCatching {
            if (repository.isNameExists(tag.name, tag.id)) {
                throw IllegalArgumentException("标签名称已存在")
            }
            repository.saveTag(tag)
        }
    }
}
