package com.ledger.task.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 底部导航栏
 */
@Composable
fun BottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = com.ledger.task.ui.theme.ElevatedBackground,
        contentColor = com.ledger.task.ui.theme.TextMuted,
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "今日") },
            label = { Text("今日") },
            selected = currentRoute == "today_tasks",
            onClick = { onNavigate("today_tasks") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "重点") },
            label = { Text("重点") },
            selected = currentRoute == "priority_tasks",
            onClick = { onNavigate("priority_tasks") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "全部") },
            label = { Text("全部") },
            selected = currentRoute == "all_tasks",
            onClick = { onNavigate("all_tasks") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.PieChart, contentDescription = "台账") },
            label = { Text("台账") },
            selected = currentRoute == "ledger_center",
            onClick = { onNavigate("ledger_center") }
        )
    }
}
