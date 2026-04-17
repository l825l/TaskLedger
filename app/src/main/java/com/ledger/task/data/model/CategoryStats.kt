package com.ledger.task.data.model

/**
 * 分类统计信息
 */
data class CategoryStats(
    val category: String,
    val count: Int,
    val doneCount: Int
)
