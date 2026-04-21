package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.CategoryNode
import com.ledger.task.domain.model.CategoryTree
import com.ledger.task.domain.model.DefaultCategories
import com.ledger.task.ui.theme.Accent
import com.ledger.task.ui.theme.getDeepBackground
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary
import com.ledger.task.R as AppR

/**
 * 分类多选器（保留原有功能用于筛选）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryMultiSelect(
    categories: List<String>,
    selectedCategories: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (categories.isEmpty()) {
        Text(
            text = "暂无分类",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
        return
    }

    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            androidx.compose.material3.FilterChip(
                label = { Text("全部") },
                selected = selectedCategories.isEmpty(),
                onClick = { onToggle("") },
                colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                    selectedContainerColor = com.ledger.task.ui.theme.SurfaceBackground,
                    selectedLabelColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = getDeepBackground(),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        items(categories) { category ->
            androidx.compose.material3.FilterChip(
                label = { Text(category) },
                selected = selectedCategories.contains(category),
                onClick = { onToggle(category) },
                colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                    selectedContainerColor = com.ledger.task.ui.theme.SurfaceBackground,
                    selectedLabelColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = getDeepBackground(),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

/**
 * 多级分类选择器（用于任务编辑时选择分类）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPicker(
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            text = context.getString(AppR.string.category),
            color = getTextMuted(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            label = { Text(context.getString(AppR.string.select_category)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = "选择分类"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Accent,
                unfocusedBorderColor = com.ledger.task.ui.theme.BorderDim,
                focusedLabelColor = Accent
            )
        )
    }

    if (showDialog) {
        CategoryTreeDialog(
            tree = DefaultCategories.defaultTree,
            selectedCategory = selectedCategory,
            onCategorySelect = { category ->
                onCategoryChange(category)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

/**
 * 分类树选择对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTreeDialog(
    tree: CategoryTree,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var expandedNodes by remember { mutableStateOf<Set<String>>(setOf()) }
    var selectedNodeId by remember { mutableStateOf(selectedCategory) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "选择分类") },
        text = {
            CategoryTreeContent(
                tree = tree,
                expandedNodes = expandedNodes,
                onToggleExpand = { nodeId ->
                    expandedNodes = if (expandedNodes.contains(nodeId)) {
                        expandedNodes - nodeId
                    } else {
                        expandedNodes + nodeId
                    }
                },
                selectedNodeId = selectedNodeId,
                onNodeSelect = { selectedNodeId = it }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedNodeId.isNotEmpty()) {
                        onCategorySelect(selectedNodeId)
                    }
                }
            ) {
                Text(text = "确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "取消")
            }
        }
    )
}

/**
 * 分类树内容
 */
@Composable
fun CategoryTreeContent(
    tree: CategoryTree,
    expandedNodes: Set<String>,
    onToggleExpand: (String) -> Unit,
    selectedNodeId: String,
    onNodeSelect: (String) -> Unit,
    level: Int = 0
) {
    val indent = (level * 24).dp

    LazyColumn(
        modifier = Modifier.height(300.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(tree.roots) { node ->
            CategoryNodeItem(
                node = node,
                isExpanded = expandedNodes.contains(node.id),
                onToggleExpand = onToggleExpand,
                isSelected = selectedNodeId == node.id,
                onSelect = onNodeSelect,
                indent = indent,
                level = level,
                expandedNodes = expandedNodes,
                selectedNodeId = selectedNodeId,
                onNodeSelect = onNodeSelect
            )
        }
    }
}

/**
 * 分类节点项
 */
@Composable
fun CategoryNodeItem(
    node: CategoryNode,
    isExpanded: Boolean,
    onToggleExpand: (String) -> Unit,
    isSelected: Boolean,
    onSelect: (String) -> Unit,
    indent: Dp,
    level: Int,
    expandedNodes: Set<String>,
    selectedNodeId: String,
    onNodeSelect: (String) -> Unit
) {
    val hasChildren = node.children.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Accent.copy(alpha = 0.12f) else Color.Transparent)
            .clickable { onSelect(node.id) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.width(indent))

            if (hasChildren) {
                IconButton(onClick = { onToggleExpand(node.id) }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ArrowBack,
                        contentDescription = if (isExpanded) "收起" else "展开",
                        modifier = Modifier.rotate(if (isExpanded) 0f else -90f)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(40.dp))
            }

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelect(node.id) },
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                text = node.name,
                color = getTextPrimary(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (isExpanded) {
            CategoryTreeContent(
                tree = CategoryTree(node.children),
                expandedNodes = expandedNodes,
                onToggleExpand = onToggleExpand,
                selectedNodeId = selectedNodeId,
                onNodeSelect = onNodeSelect,
                level = level + 1
            )
        }
    }
}
