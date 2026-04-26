package com.ledger.task.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 开屏动画页面
 * 简约 Logo 动画风格
 */
@Composable
fun SplashScreen(
    onAnimationComplete: () -> Unit
) {
    // 动画状态
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(30f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }

    // 配色
    val backgroundColor = Color(0xFFF8F9FA)
    val primaryColor = Color(0xFF1E3A5F)
    val secondaryColor = Color(0xFF6B7280)

    LaunchedEffect(Unit) {
        // 阶段1: Logo 缩放淡入 (0-600ms)
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        }

        // 阶段2: 标题从下方滑入 (600-1200ms)
        delay(600)
        launch {
            titleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
            )
        }
        launch {
            titleOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            )
        }

        // 阶段3: 副标题淡入 (1200-1800ms)
        delay(600)
        subtitleAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400, easing = LinearEasing)
        )

        // 阶段4: 等待后结束 (1800-2500ms)
        delay(700)
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            LedgerLogo(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 标题
            Text(
                text = "工作台账",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .offset(y = titleOffsetY.value.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 副标题
            Text(
                text = "高效管理每一天",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = secondaryColor,
                modifier = Modifier.alpha(subtitleAlpha.value)
            )
        }
    }
}

/**
 * 账本 Logo 组件
 */
@Composable
fun LedgerLogo(modifier: Modifier = Modifier) {
    val primaryColor = Color(0xFF1E3A5F)
    val pageColor = Color(0xFF2A4A6F)
    val darkColor = Color(0xFF0F2A45)
    val lineColor = Color(0xFF4A6A8F)
    val checkColor = Color(0xFF4CAF50)
    val dotColor = Color(0xFF9CA3AF)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(primaryColor)
        ) {
            // 内页
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .padding(6.dp)
                    .background(pageColor)
            ) {
                // 装订孔
                Column(
                    modifier = Modifier
                        .width(6.dp)
                        .padding(start = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.size(4.dp).background(darkColor))
                    Box(modifier = Modifier.size(4.dp).background(darkColor))
                    Box(modifier = Modifier.size(4.dp).background(darkColor))
                }

                // 页面内容
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 12.dp)
                        .width(60.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 第一行 - 已完成
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(2.dp)
                                .background(lineColor)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        CheckMark(color = checkColor, size = 8.dp)
                    }

                    // 第二行 - 已完成
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(2.dp)
                                .background(lineColor)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        CheckMark(color = checkColor, size = 8.dp)
                    }

                    // 第三行 - 待办
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(2.dp)
                                .background(lineColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(dotColor)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 勾选标记
 */
@Composable
fun CheckMark(
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size)
    ) {
        // 简化的勾选标记
        Box(
            modifier = Modifier
                .size(size)
                .background(color)
        )
    }
}
