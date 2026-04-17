package com.ledger.task.ui.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 可拖动悬浮按钮
 * 点击后跟随手指移动，位置会持久化保存
 * 默认位置在右下角（仅首次生效），可移动到屏幕任意位置
 */
@Composable
fun DraggableFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("drag_button", Context.MODE_PRIVATE) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    // 按钮尺寸
    val buttonSize = 56.dp
    val margin = 16.dp

    // 获取屏幕尺寸（dp）
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp

    // 计算边界：左右对称，都留margin边距
    val minX = margin.value
    val maxX = screenWidthDp - buttonSize.value - margin.value
    val minY = margin.value
    val maxY = screenHeightDp - buttonSize.value - margin.value - 72f // 减去底部导航栏高度

    // 检查是否是首次使用（没有保存过位置）
    val hasPosition = sharedPreferences.contains("offsetX")

    // 默认位置：右下角
    val defaultOffsetX = maxX.coerceAtLeast(margin.value)
    val defaultOffsetY = maxY.coerceAtLeast(margin.value)

    // 从 SharedPreferences 加载位置
    val savedOffsetX = remember { sharedPreferences.getFloat("offsetX", defaultOffsetX) }
    val savedOffsetY = remember { sharedPreferences.getFloat("offsetY", defaultOffsetY) }

    // 当前位置（dp值）
    var offsetX by remember { mutableFloatStateOf(savedOffsetX.coerceIn(minX, maxX.coerceAtLeast(minX))) }
    var offsetY by remember { mutableFloatStateOf(savedOffsetY.coerceIn(minY, maxY.coerceAtLeast(minY))) }
    var isDragging by remember { mutableStateOf(false) }

    // 拖动起始位置和总移动距离
    var startOffsetX by remember { mutableFloatStateOf(0f) }
    var startOffsetY by remember { mutableFloatStateOf(0f) }
    var totalDragX by remember { mutableFloatStateOf(0f) }
    var totalDragY by remember { mutableFloatStateOf(0f) }

    // 是否已初始化位置
    var positionInitialized by remember { mutableStateOf(false) }

    // 保存位置到 SharedPreferences
    fun savePosition(x: Float, y: Float) {
        sharedPreferences.edit(commit = true) {
            putFloat("offsetX", x)
            putFloat("offsetY", y)
        }
    }

    // 点击阈值（dp）
    val clickThreshold = 10.dp.value

    Box(
        modifier = modifier
            .offset {
                // 首次使用时，将按钮定位到右下角
                if (!positionInitialized) {
                    positionInitialized = true
                    // 如果是首次使用，设置为右下角
                    if (!hasPosition) {
                        offsetX = maxX.coerceAtLeast(margin.value)
                        offsetY = maxY.coerceAtLeast(margin.value)
                        savePosition(offsetX, offsetY)
                    }
                }

                IntOffset(
                    x = offsetX.dp.roundToPx(),
                    y = offsetY.dp.roundToPx()
                )
            }
            .shadow(if (isDragging) 12.dp else 6.dp, CircleShape)
            .size(buttonSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .pointerInput(Unit) {
                // 使用 awaitPointerEventScope 处理手势
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            androidx.compose.ui.input.pointer.PointerEventType.Press -> {
                                // 记录起始位置
                                startOffsetX = offsetX
                                startOffsetY = offsetY
                                totalDragX = 0f
                                totalDragY = 0f
                                isDragging = false
                            }
                            androidx.compose.ui.input.pointer.PointerEventType.Release -> {
                                if (isDragging) {
                                    // 拖动结束，保存位置
                                    savePosition(offsetX, offsetY)
                                } else {
                                    // 没有拖动，触发点击
                                    onClick()
                                }
                                isDragging = false
                            }
                            androidx.compose.ui.input.pointer.PointerEventType.Move -> {
                                if (event.changes.isNotEmpty()) {
                                    val change = event.changes.first()

                                    // 累计移动距离
                                    totalDragX += abs(change.position.x - change.previousPosition.x)
                                    totalDragY += abs(change.position.y - change.previousPosition.y)

                                    // 判断是否开始拖动（移动距离超过阈值）
                                    if (!isDragging && (totalDragX > clickThreshold || totalDragY > clickThreshold)) {
                                        isDragging = true
                                    }

                                    if (isDragging) {
                                        change.consume()

                                        // 计算移动距离
                                        val dragAmountX = (change.position.x - change.previousPosition.x) / density.density
                                        val dragAmountY = (change.position.y - change.previousPosition.y) / density.density

                                        // 更新位置
                                        val newOffsetX = offsetX + dragAmountX
                                        val newOffsetY = offsetY + dragAmountY

                                        // 限制在屏幕范围内
                                        offsetX = newOffsetX.coerceIn(minX, maxX.coerceAtLeast(minX))
                                        offsetY = newOffsetY.coerceIn(minY, maxY.coerceAtLeast(minY))
                                    }
                                }
                            }
                        }
                    }
                }
            }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "添加任务",
            modifier = Modifier
                .align(Alignment.Center)
                .size(24.dp),
            tint = Color.White
        )
    }
}
