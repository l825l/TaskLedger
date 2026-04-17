package com.ledger.task.data.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 任务领域模型（Domain Model）
 * 用于在应用各层之间传递数据，不包含 Room 注解
 */

/**
 * 快捷标签枚举
 */
enum class QuickTag(val label: String, val color: Color) {
    WAITING_FEEDBACK("@等待反馈", Color(0xFFFFA726)),
    NEED_MEETING("@需开会确认", Color(0xFF42A5F5)),
    PENDING_REVIEW("@待审核", Color(0xFFAB47BC)),
    BLOCKED("@阻塞中", Color(0xFFEF5350));

    companion object {
        fun fromLabel(label: String): QuickTag? {
            return values().find { it.label == label }
        }
    }
}

/**
 * 富文本内容项
 */
sealed interface RichTextItem {
    data class Text(val content: String) : RichTextItem
    data class Image(val data: String) : RichTextItem  // Base64编码的图片
    data class Attachment(val name: String, val url: String) : RichTextItem
    data class QuickTagItem(val tag: QuickTag) : RichTextItem
}

/**
 * 富文本内容
 */
data class RichContent(
    val items: List<RichTextItem> = emptyList()
) {
    fun isEmpty(): Boolean = items.isEmpty()

    fun toPlainText(): String = items
        .filterIsInstance<RichTextItem.Text>()
        .joinToString(separator = "") { it.content }

    /**
     * 序列化为字符串（JSON格式）
     */
    fun serialize(): String = items.joinToString(separator = "|||") { item ->
        when (item) {
            is RichTextItem.Text -> "TEXT:${item.content}"
            is RichTextItem.Image -> "IMAGE:${item.data}"
            is RichTextItem.Attachment -> "ATTACHMENT:${item.name}:::${item.url}"
            is RichTextItem.QuickTagItem -> "QUICKTAG:${item.tag.name}"
        }
    }

    /**
     * 获取所有快捷标签
     */
    fun getQuickTags(): List<QuickTag> {
        return items.filterIsInstance<RichTextItem.QuickTagItem>().map { it.tag }
    }

    companion object {
        /**
         * 从字符串反序列化
         */
        fun deserialize(content: String): RichContent {
            if (content.isBlank()) return RichContent(emptyList())
            val items = content.split("|||").mapNotNull { part ->
                when {
                    part.startsWith("TEXT:") -> {
                        RichTextItem.Text(part.substring(5))
                    }
                    part.startsWith("IMAGE:") -> {
                        RichTextItem.Image(part.substring(6))
                    }
                    part.startsWith("ATTACHMENT:") -> {
                        val parts = part.substring(11).split(":::")
                        if (parts.size >= 2) {
                            RichTextItem.Attachment(parts[0], parts[1])
                        } else null
                    }
                    part.startsWith("QUICKTAG:") -> {
                        val tagName = part.substring(9)
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
}

data class Task(
    val id: Long = 0,
    val title: String,
    val priority: Priority,
    val deadline: LocalDateTime,
    val status: TaskStatus,
    val displayStatus: DisplayStatus,
    val description: String = "",  // 保留字符串字段用于简单场景
    val category: String = "",
    val hasImage: Boolean = false,
    val richContent: RichContent = RichContent(emptyList()),  // 富文本备注
    val predecessorIds: List<Long> = emptyList(),  // 前置依赖任务ID
    val relatedIds: List<Long> = emptyList(),      // 相关任务ID
    val sortOrder: Int = 0,                        // 排序顺序
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null                  // 完成时间（毫秒时间戳）
)
