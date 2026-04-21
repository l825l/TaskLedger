package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.ledger.task.R as AppR
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.Task
import com.ledger.task.ui.theme.getBorderDim
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.PriorityMid
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextSecondary
import com.ledger.task.viewmodel.SortField
import com.ledger.task.viewmodel.SortState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskTable(
    tasks: List<Task>,
    sortState: SortState,
    onSort: (SortField) -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val timeStr = remember { LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) }
    val today = LocalDate.now().toEpochDay()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, getBorderDim(), RoundedCornerShape(6.dp))
            .background(getSurfaceBackground(), RoundedCornerShape(6.dp))
    ) {
        // 表头
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(getElevatedBackground())
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#",
                color = getTextMuted(),
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.width(40.dp)
            )
            SortHeader(context.getString(AppR.string.title), SortField.TITLE, sortState, onSort, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            SortHeader(context.getString(AppR.string.priority), SortField.PRIORITY, sortState, onSort)
            Spacer(modifier = Modifier.width(16.dp))
            SortHeader(context.getString(AppR.string.deadline), SortField.DEADLINE, sortState, onSort, modifier = Modifier.width(90.dp))
            Spacer(modifier = Modifier.width(16.dp))
            SortHeader(context.getString(AppR.string.status), SortField.STATUS, sortState, onSort)
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(getBorderDim(), RoundedCornerShape(1.dp))
        )

        // 内容
        if (tasks.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(tasks, key = { _, task -> task.id }) { index, task ->
                    TaskRow(
                        task = task,
                        index = index,
                        onClick = { onTaskClick(task.id) }
                    )
                }
            }
        }

        // 底部信息
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(getBorderDim(), RoundedCornerShape(1.dp))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(getElevatedBackground())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = context.getString(AppR.string.records_count, tasks.size),
                color = getTextMuted(),
                style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = context.getString(AppR.string.updated_at) + " $timeStr",
                color = getTextMuted(),
                style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
