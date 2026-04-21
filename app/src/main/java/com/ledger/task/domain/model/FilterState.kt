package com.ledger.task.domain.model

import java.time.LocalDate

/**
 * 全部事务筛选状态
 */
data class AllTasksFilterState(
    val searchQuery: String = "",
    val category: String? = null,  // 分类筛选
    val priority: Priority? = null,
    val status: DisplayStatus? = null,  // 使用 DisplayStatus 支持已逾期筛选
    val hasImage: Boolean? = null,
    val quickTag: QuickTag? = null  // 快捷标签筛选
)

/**
 * 台账筛选状态
 */
data class LedgerFilterState(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val categories: Set<String> = emptySet(),
    val includeArchived: Boolean = false
)

/**
 * 时间范围
 */
enum class TimeRange {
    THIS_WEEK,
    THIS_MONTH,
    CUSTOM
}
