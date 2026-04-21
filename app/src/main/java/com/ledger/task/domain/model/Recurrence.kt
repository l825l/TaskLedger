package com.ledger.task.domain.model

import java.time.DayOfWeek
import java.time.LocalDateTime

/**
 * 循环类型
 */
enum class RecurrenceType {
    DAILY,      // 每天
    WEEKLY,     // 每周
    MONTHLY,    // 每月
    YEARLY      // 每年
}

/**
 * 循环规则
 */
data class Recurrence(
    val type: RecurrenceType,
    val interval: Int = 1,                          // 间隔（每2周）
    val endDate: LocalDateTime? = null,             // 结束日期
    val daysOfWeek: Set<DayOfWeek>? = null,         // 周几（仅周循环）
    val count: Int? = null                          // 重复次数
) {
    /**
     * 计算下一个循环日期
     */
    fun nextOccurrence(from: LocalDateTime): LocalDateTime? {
        // 如果有结束日期且已过期
        if (endDate != null && from >= endDate) return null

        return when (type) {
            RecurrenceType.DAILY -> from.plusDays(interval.toLong())
            RecurrenceType.WEEKLY -> {
                if (daysOfWeek != null && daysOfWeek.isNotEmpty()) {
                    // 找到下一个指定的周几
                    var next = from.plusDays(1)
                    while (next.dayOfWeek !in daysOfWeek) {
                        next = next.plusDays(1)
                    }
                    next
                } else {
                    from.plusWeeks(interval.toLong())
                }
            }
            RecurrenceType.MONTHLY -> from.plusMonths(interval.toLong())
            RecurrenceType.YEARLY -> from.plusYears(interval.toLong())
        }
    }

    /**
     * 格式化显示文本
     */
    fun displayText(): String {
        val intervalText = if (interval > 1) "每${interval}" else "每"
        return when (type) {
            RecurrenceType.DAILY -> "${intervalText}天"
            RecurrenceType.WEEKLY -> {
                if (daysOfWeek != null && daysOfWeek.isNotEmpty()) {
                    val days = daysOfWeek.map {
                        when (it) {
                            DayOfWeek.MONDAY -> "一"
                            DayOfWeek.TUESDAY -> "二"
                            DayOfWeek.WEDNESDAY -> "三"
                            DayOfWeek.THURSDAY -> "四"
                            DayOfWeek.FRIDAY -> "五"
                            DayOfWeek.SATURDAY -> "六"
                            DayOfWeek.SUNDAY -> "日"
                        }
                    }.joinToString("、")
                    "每周$days"
                } else {
                    "${intervalText}周"
                }
            }
            RecurrenceType.MONTHLY -> "${intervalText}月"
            RecurrenceType.YEARLY -> "${intervalText}年"
        }
    }
}
