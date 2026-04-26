package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.domain.model.Priority
import com.ledger.task.ui.theme.Accent
import com.ledger.task.ui.theme.AccentDim
import com.ledger.task.ui.theme.getBorderDim
import com.ledger.task.ui.theme.PriorityHigh
import com.ledger.task.ui.theme.PriorityLow
import com.ledger.task.ui.theme.PriorityMid
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary
import com.ledger.task.ui.theme.getTextSecondary

/**
 * 搜索和筛选栏
 */
@Composable
fun SearchFilterBar(
    searchQuery: String,
    activeFilter: Priority?,
    onSearchChange: (String) -> Unit,
    onFilterChange: (Priority?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 搜索框
        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, getBorderDim(), RoundedCornerShape(6.dp))
                .background(getSurfaceBackground(), RoundedCornerShape(6.dp))
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text("搜索任务标题...", color = getTextMuted())
                },
                leadingIcon = {
                    Text(
                        text = "🔍",
                        modifier = Modifier.size(16.dp)
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = getTextPrimary(),
                    unfocusedTextColor = getTextPrimary(),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Accent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }

        // 优先级筛选按钮
        FilterChip(
            label = "紧急",
            color = PriorityHigh,
            isActive = activeFilter == Priority.HIGH,
            onClick = { onFilterChange(if (activeFilter == Priority.HIGH) null else Priority.HIGH) }
        )
        FilterChip(
            label = "一般",
            color = PriorityMid,
            isActive = activeFilter == Priority.MEDIUM,
            onClick = { onFilterChange(if (activeFilter == Priority.MEDIUM) null else Priority.MEDIUM) }
        )
        FilterChip(
            label = "低优",
            color = PriorityLow,
            isActive = activeFilter == Priority.LOW,
            onClick = { onFilterChange(if (activeFilter == Priority.LOW) null else Priority.LOW) }
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    color: Color,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isActive) AccentDim else getSurfaceBackground())
            .border(
                1.dp,
                if (isActive) Accent else getBorderDim(),
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            color = if (isActive) Accent else getTextSecondary(),
            fontSize = 13.sp
        )
    }
}
