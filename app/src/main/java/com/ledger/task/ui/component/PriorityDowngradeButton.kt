package com.ledger.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ledger.task.data.model.Priority
import com.ledger.task.ui.theme.Accent
import com.ledger.task.ui.theme.DeepBackground

/**
 * 优先级降级按钮
 */
@Composable
fun PriorityDowngradeButton(
    currentPriority: Priority,
    onDowngrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canDowngrade = currentPriority != Priority.LOW
    val label = when (currentPriority) {
        Priority.HIGH -> "降为一般"
        Priority.MEDIUM -> "降为一般"
        Priority.NORMAL -> "降为一般"
        Priority.LOW -> "已是最低"
    }

    Button(
        onClick = onDowngrade,
        enabled = canDowngrade,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (canDowngrade) Accent else DeepBackground,
            disabledContainerColor = DeepBackground
        ),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            color = if (canDowngrade) DeepBackground else Accent
        )
    }
}
