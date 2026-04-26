package com.ledger.task.domain.model

import androidx.compose.ui.graphics.Color

/**
 * 任务优先级
 */
enum class Priority(val label: String, val order: Int, val color: Color, val bgColor: Color) {
    HIGH("紧急", 0, Color(0xFFFF4D6A), Color(0x1FFF4D6A)),  // 红色 - 紧急
    MEDIUM("高", 1, Color(0xFFF0A030), Color(0x1FF0A030)),  // 橙色 - 高
    NORMAL("中", 2, Color(0xFF4D9FFF), Color(0x1F4D9FFF)),  // 蓝色 - 中
    LOW("低", 3, Color(0xFF8892A4), Color(0x1F8892A4))      // 灰色 - 低
}
