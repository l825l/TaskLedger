package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow

class GetAllTagsUseCase(private val repository: TagRepository) {
    operator fun invoke(): Flow<List<Tag>> = repository.getAllTags()
}
