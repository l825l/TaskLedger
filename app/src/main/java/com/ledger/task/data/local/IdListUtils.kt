package com.ledger.task.data.local

/**
 * ID 列表工具函数
 * 用于解析和格式化任务关联 ID 列表
 */

/**
 * 解析 ID 列表字符串
 * @param value 逗号分隔的 ID 字符串
 * @return ID 列表
 */
fun parseIdList(value: String): List<Long> {
    return value.trim().takeIf { it.isNotEmpty() }?.split(",")?.map { it.trim().toLong() } ?: emptyList()
}

/**
 * 格式化 ID 列表为字符串
 * @param ids ID 列表
 * @return 逗号分隔的字符串
 */
fun formatIdList(ids: List<Long>): String {
    return ids.joinToString(",")
}
