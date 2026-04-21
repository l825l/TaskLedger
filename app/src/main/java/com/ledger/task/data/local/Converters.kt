package com.ledger.task.data.local

import androidx.room.TypeConverter
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.QuickTag
import com.ledger.task.domain.model.RichContent
import com.ledger.task.domain.model.RichTextItem
import com.ledger.task.domain.model.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room 类型转换器
 */
class Converters {

    @TypeConverter
    fun fromPriority(value: Priority): String = value.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus = TaskStatus.valueOf(value)

    @TypeConverter
    fun fromLocalDate(value: LocalDate): Long = value.toEpochDay()

    @TypeConverter
    fun toLocalDate(value: Long): LocalDate = LocalDate.ofEpochDay(value)

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime): Long = value.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()

    @TypeConverter
    fun toLocalDateTime(value: Long): LocalDateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(value), java.time.ZoneId.systemDefault())

    // 富文本内容转换器
    @TypeConverter
    fun fromRichContent(value: RichContent): String = value.items.joinToString("|") { item ->
        when (item) {
            is RichTextItem.Text -> "T:${item.content}"
            is RichTextItem.Image -> "I:${item.data}"
            is RichTextItem.Attachment -> "A:${item.name}|${item.url}"
            is RichTextItem.QuickTagItem -> "Q:${item.tag.name}"
        }
    }

    @TypeConverter
    fun toRichContent(value: String): RichContent {
        val items = value.split("|").mapNotNull { part ->
            when {
                part.startsWith("T:") -> RichTextItem.Text(part.substring(2))
                part.startsWith("I:") -> RichTextItem.Image(part.substring(2))
                part.startsWith("A:") -> {
                    val rest = part.substring(2)
                    val pipeIdx = rest.indexOf('|')
                    if (pipeIdx > 0) {
                        RichTextItem.Attachment(rest.substring(0, pipeIdx), rest.substring(pipeIdx + 1))
                    } else null
                }
                part.startsWith("Q:") -> {
                    val tagName = part.substring(2)
                    QuickTag.values().find { it.name == tagName }?.let { tag ->
                        RichTextItem.QuickTagItem(tag)
                    }
                }
                else -> null
            }
        }
        return RichContent(items)
    }
}
