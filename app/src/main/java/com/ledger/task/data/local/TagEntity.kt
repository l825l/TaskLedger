package com.ledger.task.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ledger.task.domain.model.Tag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * 标签实体（Room Entity）
 */
@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val colorArgb: Int,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Tag {
        return Tag(
            id = id,
            name = name,
            color = Color(colorArgb),
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomain(tag: Tag): TagEntity {
            return TagEntity(
                id = tag.id,
                name = tag.name,
                colorArgb = tag.color.toArgb(),
                createdAt = tag.createdAt
            )
        }
    }
}
