package com.ledger.task.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 开屏动画页面
 * 牛和马从两边跑到中间会合，一起说"牛马就该用牛马记账"
 */
@Composable
fun SplashScreen(
    onAnimationComplete: () -> Unit
) {
    // 动画状态
    val cowOffsetX = remember { Animatable(-300f) }
    val horseOffsetX = remember { Animatable(300f) }
    val cowScale = remember { Animatable(0.5f) }
    val horseScale = remember { Animatable(0.5f) }
    val speechAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }

    var showSpeech by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }

    // 背景色
    val backgroundColor = Color(0xFFF5F5DC) // 米白色

    LaunchedEffect(Unit) {
        // 阶段1: 牛马从两边跑入 (0-800ms)
        launch {
            cowOffsetX.animateTo(
                targetValue = -60f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        launch {
            horseOffsetX.animateTo(
                targetValue = 60f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        launch {
            cowScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        launch {
            horseScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }

        // 阶段2: 显示对话气泡 (800-1500ms)
        delay(800)
        showSpeech = true
        speechAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        )

        // 阶段3: 显示标题 (1500-2200ms)
        delay(1200)
        showTitle = true
        titleAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        )

        // 阶段4: 等待后结束 (2200-3200ms)
        delay(1000)
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部标题区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (showTitle) {
                    Text(
                        text = "牛马精神",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513),
                        modifier = Modifier.alpha(titleAlpha.value)
                    )
                }
            }

            // 中间牛马区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 牛
                    CowHead(
                        modifier = Modifier
                            .offset(x = cowOffsetX.value.dp)
                            .scale(cowScale.value)
                    )

                    // 马
                    HorseHead(
                        modifier = Modifier
                            .offset(x = horseOffsetX.value.dp)
                            .scale(horseScale.value)
                    )
                }

                // 对话气泡
                if (showSpeech) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 180.dp)
                            .alpha(speechAlpha.value),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        SpeechBubble(
                            text = "牛马就该用牛马记账！"
                        )
                    }
                }
            }

            // 底部留白
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

/**
 * 牛头组件
 */
@Composable
fun CowHead(modifier: Modifier = Modifier) {
    val cowColor = Color(0xFF8B4513) // 棕色
    val darkCowColor = Color(0xFF6B3410)

    Column(
        modifier = modifier.size(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 牛角
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 左角
            Box(
                modifier = Modifier
                    .size(12.dp, 20.dp)
                    .background(cowColor)
            )
            // 右角
            Box(
                modifier = Modifier
                    .size(12.dp, 20.dp)
                    .background(cowColor)
            )
        }

        // 牛头主体
        Box(
            modifier = Modifier
                .size(70.dp, 60.dp)
                .background(cowColor),
            contentAlignment = Alignment.Center
        ) {
            // 眼睛
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.White)
                )
            }
        }

        // 牛鼻子
        Box(
            modifier = Modifier
                .size(50.dp, 20.dp)
                .background(darkCowColor)
        )
    }
}

/**
 * 马头组件
 */
@Composable
fun HorseHead(modifier: Modifier = Modifier) {
    val horseColor = Color(0xFF1A1A1A) // 黑色
    val darkHorseColor = Color(0xFF2A2A2A)

    Column(
        modifier = modifier.size(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 马耳
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 左耳
            Box(
                modifier = Modifier
                    .size(10.dp, 18.dp)
                    .background(horseColor)
            )
            // 右耳
            Box(
                modifier = Modifier
                    .size(10.dp, 18.dp)
                    .background(horseColor)
            )
        }

        // 马头主体 - 修长
        Box(
            modifier = Modifier
                .size(65.dp, 70.dp)
                .background(horseColor),
            contentAlignment = Alignment.Center
        ) {
            // 眼睛
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.White)
                )
            }
        }

        // 马鼻子
        Box(
            modifier = Modifier
                .size(45.dp, 25.dp)
                .background(darkHorseColor)
        )
    }
}

/**
 * 对话气泡
 */
@Composable
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 气泡三角
        Box(
            modifier = Modifier
                .size(0.dp)
                .background(Color.Transparent)
        )

        // 气泡主体
        Box(
            modifier = Modifier
                .background(
                    Color.White.copy(alpha = 0.95f)
                )
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513),
                textAlign = TextAlign.Center
            )
        }
    }
}
