package com.ledger.task.ui.theme

import androidx.compose.ui.graphics.Color

// ==================== 亮色主题 ====================

// 亮色背景层级
val DeepBackgroundLight = Color(0xFFFBFDFF)
val SurfaceBackgroundLight = Color(0xFFFFFFFF)
val ElevatedBackgroundLight = Color(0xFFF3F7FA)
val RowHoverBackgroundLight = Color(0xFFE8F0F7)

// 亮色边框
val BorderDimLight = Color(0xFFD1E1EF)
val BorderBrightLight = Color(0xFFA3C4D9)

// 亮色文字
val TextPrimaryLight = Color(0xFF1A2A40)
val TextSecondaryLight = Color(0xFF5A6B85)
val TextMutedLight = Color(0xFF7C8DA3)

// ==================== 深色主题 ====================

// 深色背景层级
val DeepBackgroundDark = Color(0xFF0D1117)
val SurfaceBackgroundDark = Color(0xFF161B22)
val ElevatedBackgroundDark = Color(0xFF21262D)
val RowHoverBackgroundDark = Color(0xFF30363D)

// 深色边框
val BorderDimDark = Color(0xFF30363D)
val BorderBrightDark = Color(0xFF484F58)

// 深色文字
val TextPrimaryDark = Color(0xFFE6EDF3)
val TextSecondaryDark = Color(0xFFB1BAC4)
val TextMutedDark = Color(0xFF7D8590)

// ==================== 通用颜色（亮暗通用） ====================

// 强调色
val Accent = Color(0xFF4D9FFF)
val AccentDim = Color(0x1F4D9FFF)
val AccentGlow = Color(0x404D9FFF)

// 优先级颜色
val PriorityHigh = Color(0xFFFF4D6A)  // 红色 - 紧急
val PriorityHighBg = Color(0x1FFF4D6A)
val PriorityMid = Color(0xFFF0A030)   // 橙色 - 高
val PriorityMidBg = Color(0x1FF0A030)
val PriorityLow = Color(0xFF4D9FFF)   // 蓝色 - 中
val PriorityLowBg = Color(0x1F4D9FFF)

// 状态颜色
val StatusDone = Color(0xFF4DAB77)
val StatusDoneBg = Color(0x1F4DAB77)
val StatusProgress = Color(0xFF4D9FFF)
val StatusProgressBg = Color(0x1F4D9FFF)
val StatusPending = Color(0xFFA3C4D9)
val StatusPendingBg = Color(0x1FA3C4D9)
val StatusOverdue = Color(0xFFFF6B6B)
val StatusOverdueBg = Color(0x1FFF6B6B)

// ==================== 兼容旧代码（亮色默认值） ====================

val DeepBackground = DeepBackgroundLight
val SurfaceBackground = SurfaceBackgroundLight
val ElevatedBackground = ElevatedBackgroundLight
val RowHoverBackground = RowHoverBackgroundLight
val BorderDim = BorderDimLight
val BorderBright = BorderBrightLight
val TextPrimary = TextPrimaryLight
val TextSecondary = TextSecondaryLight
val TextMuted = TextMutedLight
