package com.ledger.task.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.foundation.layout.size
import com.ledger.task.domain.model.DefaultCategories
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.QuickTag
import com.ledger.task.ui.theme.getBorderDim
import com.ledger.task.ui.theme.getDeepBackground
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted

/**
 * 多维筛选器组件
 * 支持分类、优先级、状态、是否含图片、快捷标签组合筛选
 */
@Composable
fun MultiFilterBar(
    category: String?,
    priority: Priority?,
    status: DisplayStatus?,
    hasImage: Boolean?,
    quickTag: QuickTag?,
    onCategoryChange: (String?) -> Unit,
    onPriorityChange: (Priority?) -> Unit,
    onStatusChange: (DisplayStatus?) -> Unit,
    onHasImageChange: (Boolean?) -> Unit,
    onQuickTagChange: (QuickTag?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 分类筛选器
        FilterDropdown(
            label = "分类",
            value = category,
            options = listOf(null to "全部") + DefaultCategories.defaultTree.getAllNodes().map { it.id to it.name },
            onValueChange = onCategoryChange
        )

        // 优先级筛选器
        FilterDropdown(
            label = "优先级",
            value = priority,
            options = listOf(
                null to "全部",
                Priority.HIGH to "紧急",
                Priority.MEDIUM to "高",
                Priority.NORMAL to "中",
                Priority.LOW to "低"
            ),
            onValueChange = onPriorityChange
        )

        // 状态筛选器
        FilterDropdown(
            label = "状态",
            value = status,
            options = listOf(
                null to "全部",
                DisplayStatus.OVERDUE to "已逾期",
                DisplayStatus.PENDING to "待处理",
                DisplayStatus.IN_PROGRESS to "进行中",
                DisplayStatus.DONE to "已完成"
            ),
            onValueChange = onStatusChange
        )

        // 图片筛选器
        FilterDropdown(
            label = "图片",
            value = hasImage,
            options = listOf(
                null to "全部",
                true to "有图",
                false to "无图"
            ),
            onValueChange = onHasImageChange
        )

        // 快捷标签筛选器
        FilterDropdown(
            label = "标签",
            value = quickTag,
            options = listOf(null to "全部") + QuickTag.values().map { it to it.label },
            onValueChange = onQuickTagChange
        )
    }
}

/**
 * 单个下拉筛选器
 */
@Composable
private fun <T> FilterDropdown(
    label: String,
    value: T?,
    options: List<Pair<T?, String>>,
    onValueChange: (T?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.find { it.first == value }?.second ?: label

    Box(modifier = modifier) {
        // 筛选器按钮
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true },
            color = if (value != null) getSurfaceBackground() else getDeepBackground(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (value != null) selectedLabel else label,
                    color = if (value != null) MaterialTheme.colorScheme.onSurface else getTextMuted(),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "展开",
                    tint = if (value != null) MaterialTheme.colorScheme.onSurface else getTextMuted(),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // 下拉菜单
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(getElevatedBackground())
                .widthIn(min = 120.dp, max = 200.dp),
            properties = PopupProperties(focusable = true)
        ) {
            options.forEach { (optionValue, optionLabel) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = optionLabel,
                            color = if (optionValue == value) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onValueChange(optionValue)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        if (optionValue == value) getSurfaceBackground() else getElevatedBackground()
                    )
                )
            }
        }
    }
}

/**
 * 当前筛选状态显示（带清除按钮）
 */
@Composable
fun ActiveFiltersChip(
    category: String?,
    priority: Priority?,
    status: DisplayStatus?,
    hasImage: Boolean?,
    quickTag: QuickTag?,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasActiveFilters = category != null || priority != null || status != null || hasImage != null || quickTag != null

    if (hasActiveFilters) {
        Surface(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClearAll() },
            color = getSurfaceBackground(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "清除筛选",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelSmall
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "清除",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
    }
}
