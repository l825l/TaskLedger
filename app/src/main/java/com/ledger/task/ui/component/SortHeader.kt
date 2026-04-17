package com.ledger.task.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ledger.task.viewmodel.SortField
import com.ledger.task.viewmodel.SortState

/**
 * 排序表头
 */
@Composable
fun SortHeader(
    label: String,
    field: SortField,
    sortState: SortState,
    onSort: (SortField) -> Unit,
    modifier: Modifier = Modifier
) {
    val isActive = sortState.field == field
    val color = if (isActive) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier.clickable { onSort(field) },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelSmall
        )
        if (isActive) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (sortState.ascending) "▲" else "▼",
                color = color,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
