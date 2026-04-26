package com.ledger.task.domain.usecase

import com.ledger.task.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class TagStats(
    val tagId: Long,
    val tagName: String,
    val taskCount: Int
)

class GetTagStatsUseCase(private val repository: TagRepository) {
    operator fun invoke(): Flow<List<TagStats>> {
        return repository.getAllTags().map { tags ->
            tags.map { tag ->
                TagStats(
                    tagId = tag.id,
                    tagName = tag.name,
                    taskCount = 0
                )
            }
        }
    }
}
