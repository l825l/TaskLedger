package com.ledger.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.ui.theme.TextMuted

/**
 * 空状态
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    height: Int = 0,
    text: String = "没有匹配的任务"
) {
    val heightDp = if (height > 0) height.dp else 200.dp
    Column(
        modifier = modifier.height(heightDp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📋",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
            color = TextMuted.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            color = TextMuted,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
