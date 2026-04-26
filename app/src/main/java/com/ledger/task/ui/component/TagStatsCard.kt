package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.domain.model.Tag
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary

data class TagStatItem(
    val tag: Tag,
    val taskCount: Int
)

@Composable
fun TagStatsCard(
    tagStats: List<TagStatItem>,
    modifier: Modifier = Modifier
) {
    if (tagStats.isEmpty()) {
        return
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = getSurfaceBackground())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "标签统计",
                color = getTextPrimary(),
                style = MaterialTheme.typography.titleMedium
            )

            tagStats.sortedByDescending { it.taskCount }.forEach { stat ->
                TagStatRow(
                    tag = stat.tag,
                    count = stat.taskCount
                )
            }
        }
    }
}

@Composable
private fun TagStatRow(
    tag: Tag,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(tag.color)
        )

        Text(
            text = tag.name,
            color = getTextMuted(),
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = count.toString(),
            color = tag.color,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace
        )

        Text(
            text = "个任务",
            color = getTextMuted(),
            fontSize = 12.sp
        )
    }
}
