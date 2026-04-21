package com.ledger.task.ui.screen

import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledger.task.ui.component.SearchFilterBar
import com.ledger.task.ui.component.StatsBar
import com.ledger.task.ui.component.TaskTable
import com.ledger.task.ui.theme.Accent
import com.ledger.task.ui.theme.AccentDim
import com.ledger.task.ui.theme.getDeepBackground
import com.ledger.task.ui.util.CsvExporter
import com.ledger.task.viewmodel.TaskListViewModel
import com.ledger.task.R as AppR

/**
 * 任务列表主页
 */
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        containerColor = getDeepBackground(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = AccentDim,
                contentColor = Accent,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = context.getString(AppR.string.new_task),
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(getDeepBackground())
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 顶栏
            HeaderRow(
                context = context,
                onExport = {
                    val uri = CsvExporter.exportToDownloads(
                        context = context,
                        tasks = uiState.tasks
                    )
                    if (uri != null) {
                        Toast.makeText(context, context.getString(AppR.string.export_success), Toast.LENGTH_SHORT).show()
                        CsvExporter.share(context, uri)
                    } else {
                        Toast.makeText(context, context.getString(AppR.string.export_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            )

            // 统计栏
            StatsBar(stats = uiState.stats)

            // 搜索筛选栏
            SearchFilterBar(
                searchQuery = uiState.searchQuery,
                activeFilter = uiState.activePriorityFilter,
                onSearchChange = viewModel::onSearchQueryChange,
                onFilterChange = viewModel::onPriorityFilterChange
            )

            // 任务表格
            TaskTable(
                tasks = uiState.tasks,
                sortState = uiState.sortState,
                onSort = viewModel::onSort,
                onTaskClick = onNavigateToEdit,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HeaderRow(context: android.content.Context, onExport: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            // 标签
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 脉冲点
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 0.4f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2500),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dotAlpha"
                )
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(Accent)
                        .graphicsLayer { this.alpha = alpha }
                )
                Text(
                    text = "TASK LEDGER",
                    color = Accent,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = context.getString(AppR.string.task_ledger),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )
        }

        // 导出按钮
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AccentDim)
                .border(1.dp, Accent.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                .clickable { onExport() }
                .padding(horizontal = 22.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FileDownload,
                contentDescription = null,
                tint = Accent,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = context.getString(AppR.string.export),
                color = Accent,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}
