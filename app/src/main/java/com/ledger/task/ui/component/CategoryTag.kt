package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.domain.model.CategoryNode
import com.ledger.task.domain.model.DefaultCategories
import com.ledger.task.ui.theme.getTextMuted

/**
 * 分类标签组件
 * @param category 分类名称或ID
 * @param categoryColor 可选的颜色，如果不提供则自动从DefaultCategories查找
 * @param categories 可选的分类列表，用于查找颜色
 */
@Composable
fun CategoryTag(
    category: String,
    modifier: Modifier = Modifier,
    categoryColor: Color? = null,
    categories: List<CategoryNode> = emptyList()
) {
    val displayText = if (category.isEmpty()) "默认" else category

    // 确定颜色：优先使用传入的颜色，否则从分类列表或默认分类中查找
    val dotColor = when {
        categoryColor != null -> categoryColor
        category.isEmpty() -> DefaultCategories.ColorDefault
        categories.isNotEmpty() -> {
            // 从传入的分类列表中查找
            val node = categories.find { it.name == category || it.id == category }
            node?.color ?: DefaultCategories.ColorDefault
        }
        else -> DefaultCategories.getColorByName(category)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 颜色圆点
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        // 文字
        Text(
            text = displayText,
            color = getTextMuted(),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}
