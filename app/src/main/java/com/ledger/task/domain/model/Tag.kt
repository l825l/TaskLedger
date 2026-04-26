package com.ledger.task.domain.model

import androidx.compose.ui.graphics.Color

/**
 * 任务标签
 */
data class Tag(
    val id: Long = 0,
    val name: String,
    val color: Color,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        // 预设颜色
        val PRESET_COLORS = listOf(
            Color(0xFFEF5350), // 红色
            Color(0xFFFF7043), // 橙色
            Color(0xFFFFCA28), // 黄色
            Color(0xFF66BB6A), // 绿色
            Color(0xFF42A5F5), // 蓝色
            Color(0xFFAB47BC), // 紫色
            Color(0xFF78909C), // 灰色
            Color(0xFF26A69A)  // 青色
        )
    }
}
