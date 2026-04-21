package com.ledger.task.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledger.task.backup.AutoBackupScheduler
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.model.TimeRange
import com.ledger.task.ui.component.DraggableFloatingActionButton
import com.ledger.task.ui.theme.getDeepBackground
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.viewmodel.ExportFileInfo
import com.ledger.task.viewmodel.LedgerCenterViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 台账中心屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerCenterScreen(
    viewModel: LedgerCenterViewModel,
    onNavigateToAdd: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToSettingsForBiometric: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // 显示导出消息
    LaunchedEffect(uiState.exportMessage) {
        uiState.exportMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearExportMessage()
        }
    }

    // 日期选择对话框状态
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }

    // 备份文件选择器
    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri: Uri? ->
        uri?.let { viewModel.createBackup(it) }
    }

    // 恢复文件选择器
    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { viewModel.restoreFromBackup(it) }
    }

    // 显示备份消息
    LaunchedEffect(uiState.backupMessage) {
        uiState.backupMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearBackupMessage()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(getDeepBackground())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "台账中心",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge
                )
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "设置",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // 时间范围选择
            TimeRangeSelectorSection(
                selectedRange = uiState.timeRange,
                customStartDate = uiState.customStartDate,
                customEndDate = uiState.customEndDate,
                onRangeChange = viewModel::onTimeRangeChange,
                onShowStartDatePicker = { showStartDatePicker = true },
                onShowEndDatePicker = { showEndDatePicker = true }
            )

            // 分类筛选
            CategoryFilterSection(
                categories = uiState.allCategories,
                selectedCategories = uiState.filterState.categories,
                onToggle = viewModel::onCategoryToggle,
                onSelectAll = viewModel::onSelectAllCategories,
                onDeselectAll = viewModel::onDeselectAllCategories
            )

            // 包含已归档开关
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.filterState.includeArchived,
                    onCheckedChange = viewModel::onIncludeArchivedChange
                )
                Text(
                    text = "包含已归档事项",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // 任务统计
            TaskStatisticsSection(
                tasks = uiState.tasks,
                modifier = Modifier.fillMaxWidth()
            )

            // 导出区域
            ExportSection(
                onExport = { showExportDialog = true },
                isExporting = uiState.isExporting,
                taskCount = uiState.tasks.size,
                modifier = Modifier.fillMaxWidth()
            )

            // 备份恢复区域
            BackupRestoreSection(
                isBackingUp = uiState.isBackingUp,
                isRestoring = uiState.isRestoring,
                onBackup = {
                    // 没有备份密码时显示确认对话框
                    if (!viewModel.hasBackupPassword()) {
                        viewModel.showNoPasswordConfirmDialog()
                    } else {
                        // 有密码，直接弹出保存对话框
                        val timestamp = java.time.LocalDateTime.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                        )
                        backupLauncher.launch("任务台账备份_$timestamp.zip")
                    }
                },
                onRestore = {
                    restoreLauncher.launch(arrayOf("application/zip", "*/*"))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 可拖动悬浮按钮
        DraggableFloatingActionButton(
            onClick = onNavigateToAdd
        )

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // 没有备份密码的确认对话框
    if (uiState.showNoPasswordConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissNoPasswordConfirmDialog() },
            title = { Text("需要设置备份密码") },
            text = { Text("创建备份需要先设置备份密码。是否前往设置页面开启生物识别？") },
            confirmButton = {
                Button(onClick = {
                    viewModel.dismissNoPasswordConfirmDialog()
                    onNavigateToSettingsForBiometric()
                }) {
                    Text("前往设置")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissNoPasswordConfirmDialog() }) {
                    Text("取消")
                }
            }
        )
    }

    // 开始日期选择器
    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.customStartDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.onCustomDateRangeChange(
                                date,
                                uiState.customEndDate
                            )
                        }
                        showStartDatePicker = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // 结束日期选择器
    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.customEndDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.onCustomDateRangeChange(
                                uiState.customStartDate,
                                date
                            )
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // 导出选项对话框
    if (showExportDialog) {
        ExportDialog(
            onDismiss = { showExportDialog = false },
            onExportCSV = { viewModel.exportCSV() },
            onExportExcel = { viewModel.exportExcel() },
            onShare = {
                viewModel.refreshExportHistory()
                showShareDialog = true
            },
            onOpenFileLocation = {
                viewModel.openExportFileLocation()
                showExportDialog = false
            },
            lastExportUri = uiState.lastExportUri,
            isExporting = uiState.isExporting
        )
    }

    // 分享选项对话框
    if (showShareDialog) {
        ShareDialog(
            onDismiss = { showShareDialog = false },
            onShareNewCSV = {
                showShareDialog = false
                viewModel.exportAndShareCSV()
            },
            onShareNewExcel = {
                showShareDialog = false
                viewModel.exportAndShareExcel()
            },
            onShareExisting = { uri ->
                showShareDialog = false
                viewModel.shareExistingFile(uri)
            },
            exportHistory = uiState.exportHistory
        )
    }

    // 恢复生物识别验证
    if (uiState.needsBiometricForRestore) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val activity = context as? androidx.fragment.app.FragmentActivity

        LaunchedEffect(uiState.biometricTriggerId) {
            if (activity != null) {
                val biometricManager = viewModel.getBiometricAuthManager()
                val cipher = viewModel.getPasswordStorage().getDecryptCipherForBiometric()

                if (cipher != null) {
                    biometricManager.authenticateWithCipher(
                        activity = activity,
                        cipher = cipher,
                        title = "验证身份",
                        subtitle = "请使用指纹或面容验证以恢复数据",
                        negativeButtonText = "使用密码",
                        onSuccess = { result ->
                            viewModel.performRestoreWithBiometric()
                        },
                        onError = { _, _ ->
                            viewModel.fallbackToPasswordForRestore()
                        },
                        onFailed = {
                            // 生物识别失败，检查是否需要回退到密码
                            viewModel.onBiometricFailedForRestore()
                        }
                    )
                } else {
                    viewModel.fallbackToPasswordForRestore()
                }
            } else {
                viewModel.fallbackToPasswordForRestore()
            }
        }
    }

    // 禁用生物识别验证
    if (uiState.needsBiometricForDisable) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val activity = context as? androidx.fragment.app.FragmentActivity

        LaunchedEffect(uiState.biometricTriggerId) {
            if (activity != null) {
                val biometricManager = viewModel.getBiometricAuthManager()
                val cipher = viewModel.getPasswordStorage().getDecryptCipherForBiometric()

                if (cipher != null) {
                    biometricManager.authenticateWithCipher(
                        activity = activity,
                        cipher = cipher,
                        title = "验证身份",
                        subtitle = "请使用指纹或面容验证以关闭生物识别",
                        negativeButtonText = "取消",
                        onSuccess = { result ->
                            viewModel.performDisableBiometricAccess()
                        },
                        onError = { _, _ ->
                            viewModel.cancelDisableBiometric()
                        },
                        onFailed = {
                            viewModel.onBiometricFailedForDisable()
                        }
                    )
                } else {
                    viewModel.cancelDisableBiometric()
                }
            } else {
                viewModel.cancelDisableBiometric()
            }
        }
    }

    // 关闭生物识别密码输入对话框
    if (uiState.showPasswordForDisable) {
        var password by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { viewModel.cancelDisableBiometric() },
            title = { Text("输入密码") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "生物识别验证失败，请输入备份密码以关闭生物识别：",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text("备份密码") },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        isError = errorMessage != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (viewModel.disableBiometricWithPassword(password)) {
                            // 成功
                        } else {
                            errorMessage = "密码错误"
                        }
                    },
                    enabled = password.length >= 6
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelDisableBiometric() }) {
                    Text("取消")
                }
            }
        )
    }

    // 恢复密码对话框
    if (uiState.showRestorePasswordDialog) {
        RestorePasswordDialog(
            onDismiss = viewModel::cancelRestorePasswordDialog,
            onConfirm = { password ->
                viewModel.confirmRestore(password)
            },
            title = "输入备份密码",
            passwordHint = uiState.passwordHint,
            error = uiState.backupPasswordError
        )
    }
}

/**
 * 时间范围选择器区域
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeRangeSelectorSection(
    selectedRange: TimeRange,
    customStartDate: LocalDate,
    customEndDate: LocalDate,
    onRangeChange: (TimeRange) -> Unit,
    onShowStartDatePicker: () -> Unit,
    onShowEndDatePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                text = "时间范围",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    label = { Text("本周") },
                    selected = selectedRange == TimeRange.THIS_WEEK,
                    onClick = { onRangeChange(TimeRange.THIS_WEEK) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getElevatedBackground(),
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                FilterChip(
                    label = { Text("本月") },
                    selected = selectedRange == TimeRange.THIS_MONTH,
                    onClick = { onRangeChange(TimeRange.THIS_MONTH) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getElevatedBackground(),
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                FilterChip(
                    label = { Text("自定义") },
                    selected = selectedRange == TimeRange.CUSTOM,
                    onClick = { onRangeChange(TimeRange.CUSTOM) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getElevatedBackground(),
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            // 自定义日期范围显示
            if (selectedRange == TimeRange.CUSTOM) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        onValueChange = {},
                        label = { Text("开始日期") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = onShowStartDatePicker) {
                                Icon(Icons.Default.DateRange, "选择开始日期")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Text("至", color = getTextMuted())
                    OutlinedTextField(
                        value = customEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        onValueChange = {},
                        label = { Text("结束日期") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = onShowEndDatePicker) {
                                Icon(Icons.Default.DateRange, "选择结束日期")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * 分类筛选区域
 */
@Composable
private fun CategoryFilterSection(
    categories: List<String>,
    selectedCategories: Set<String>,
    onToggle: (String) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "分类筛选",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(onClick = onSelectAll) {
                        Text("全选", style = MaterialTheme.typography.labelSmall)
                    }
                    TextButton(onClick = onDeselectAll) {
                        Text("反选", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            if (categories.isEmpty()) {
                Text(
                    text = "暂无分类",
                    color = getTextMuted(),
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categories.forEach { category ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggle(category) }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedCategories.contains(category),
                                onCheckedChange = { onToggle(category) }
                            )
                            Text(
                                text = category,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 任务统计区域
 */
@Composable
private fun TaskStatisticsSection(
    tasks: List<Task>,
    modifier: Modifier = Modifier
) {
    val totalCount = tasks.size
    val doneCount = tasks.count { it.status == TaskStatus.DONE }
    val pendingCount = totalCount - doneCount

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = getSurfaceBackground())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "统计信息",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                StatItem(label = "总计", value = totalCount.toString())
                StatItem(label = "已完成", value = doneCount.toString())
                StatItem(label = "未完成", value = pendingCount.toString())
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = label,
            color = getTextMuted(),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

/**
 * 导出区域
 */
@Composable
private fun ExportSection(
    onExport: () -> Unit,
    isExporting: Boolean,
    taskCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
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
                text = "导出数据",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            if (taskCount == 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "当前筛选条件下没有数据可导出",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(
                onClick = onExport,
                enabled = !isExporting && taskCount > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isExporting) {
                    Text("导出中...")
                } else {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("导出/分享")
                }
            }
        }
    }
}

/**
 * 导出选项对话框
 */
@Composable
private fun ExportDialog(
    onDismiss: () -> Unit,
    onExportCSV: () -> Unit,
    onExportExcel: () -> Unit,
    onShare: () -> Unit,
    onOpenFileLocation: () -> Unit,
    lastExportUri: Uri?,
    isExporting: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("导出选项") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 导出格式
                Text(
                    text = "选择导出格式",
                    color = getTextMuted(),
                    style = MaterialTheme.typography.labelSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onExportCSV,
                        enabled = !isExporting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CSV")
                    }
                    Button(
                        onClick = onExportExcel,
                        enabled = !isExporting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Excel")
                    }
                }

                // 分享按钮
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onShare,
                    enabled = !isExporting,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("分享")
                }

                // 打开文件位置按钮（始终可点击）
                OutlinedButton(
                    onClick = onOpenFileLocation,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("打开文件位置")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}

/**
 * 分享选项对话框
 */
@Composable
private fun ShareDialog(
    onDismiss: () -> Unit,
    onShareNewCSV: () -> Unit,
    onShareNewExcel: () -> Unit,
    onShareExisting: (Uri) -> Unit,
    exportHistory: List<ExportFileInfo>
) {
    // 分页状态
    var currentPage by remember { mutableStateOf(0) }
    val pageSize = 5
    val totalPages = (exportHistory.size + pageSize - 1) / pageSize
    val startIndex = currentPage * pageSize

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("分享") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "导出新文件并分享",
                    color = getTextMuted(),
                    style = MaterialTheme.typography.labelSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onShareNewCSV,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CSV")
                    }
                    Button(
                        onClick = onShareNewExcel,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Excel")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "历史导出文件",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.labelSmall
                    )
                    if (exportHistory.isNotEmpty() && totalPages > 1) {
                        Text(
                            text = "${currentPage + 1}/$totalPages",
                            color = getTextMuted(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                if (exportHistory.isEmpty()) {
                    // 无历史文件时的提示
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "暂无历史导出文件",
                            color = getTextMuted(),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "导出新文件后将显示在这里",
                            color = getTextMuted(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                } else {
                    // 当前页的文件列表（固定显示5个位置）
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(pageSize) { index ->
                            val fileIndex = startIndex + index
                            if (fileIndex < exportHistory.size) {
                                val fileInfo = exportHistory[fileIndex]
                                OutlinedButton(
                                    onClick = { onShareExisting(fileInfo.uri) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = fileInfo.fileName,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 2,
                                        softWrap = true
                                    )
                                }
                            } else {
                                // 空占位，保持布局一致
                                Spacer(modifier = Modifier.height(36.dp))
                            }
                        }
                    }

                    // 分页控件
                    if (totalPages > 1) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { currentPage = maxOf(0, currentPage - 1) },
                                enabled = currentPage > 0
                            ) {
                                Text("上一页")
                            }
                            TextButton(
                                onClick = { currentPage = minOf(totalPages - 1, currentPage + 1) },
                                enabled = currentPage < totalPages - 1
                            ) {
                                Text("下一页")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 备份恢复区域
 */
@Composable
private fun BackupRestoreSection(
    isBackingUp: Boolean,
    isRestoring: Boolean,
    onBackup: () -> Unit,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
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
                text = "数据备份与恢复",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "备份将保存所有任务数据，可随时恢复",
                color = getTextMuted(),
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 备份按钮
                OutlinedButton(
                    onClick = onBackup,
                    enabled = !isBackingUp && !isRestoring,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isBackingUp) {
                        Text("备份中...")
                    } else {
                        Icon(Icons.Default.Backup, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("创建备份")
                    }
                }

                // 恢复按钮
                OutlinedButton(
                    onClick = onRestore,
                    enabled = !isBackingUp && !isRestoring,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isRestoring) {
                        Text("恢复中...")
                    } else {
                        Icon(Icons.Default.Restore, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("恢复数据")
                    }
                }
            }

            // 分隔线
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

/**
 * 恢复密码对话框
 */
@Composable
private fun RestorePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    title: String,
    passwordHint: String?,
    error: String? = null
) {
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "此备份文件已加密，请输入创建备份时设置的密码。",
                    color = getTextMuted(),
                    style = MaterialTheme.typography.bodySmall
                )

                // 显示密码提示
                if (!passwordHint.isNullOrBlank()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = getElevatedBackground())
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "提示：$passwordHint",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("备份密码") },
                    singleLine = true,
                    isError = error != null,
                    visualTransformation = if (showPassword)
                        androidx.compose.ui.text.input.VisualTransformation.None
                    else
                        androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(if (showPassword) "隐藏" else "显示")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // 显示错误信息
                if (!error.isNullOrBlank()) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                } else if (password.isNotEmpty() && password.length < 6) {
                    Text(
                        text = "密码长度至少6位",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (password.length >= 6) onConfirm(password) },
                enabled = password.length >= 6
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 生物识别设置对话框
 */
@Composable
private fun BiometricSetupDialog(
    isBiometricEnabled: Boolean,
    onEnable: (String) -> Unit,
    onDisable: () -> Unit,
    onDismiss: () -> Unit,
    hasBackupPassword: Boolean,
    needsSetupPassword: Boolean = false  // 从备份跳转过来，需要设置密码
) {
    var showPasswordInput by remember { mutableStateOf(needsSetupPassword && !hasBackupPassword) }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("生物识别设置") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isBiometricEnabled) {
                    // 已启用状态
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "生物识别快捷访问已启用",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "您可以使用指纹或面容快速授权备份操作，无需输入密码。",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.bodySmall
                    )
                } else if (!hasBackupPassword) {
                    // 未设置备份密码 - 需要设置密码
                    Text(
                        text = "请设置备份密码以启用生物识别快捷访问：",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text("备份密码（至少6位）") },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        isError = errorMessage != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Text(
                        text = "设置密码后，将进行生物识别认证，认证成功后自动启用生物识别快捷访问。",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.bodySmall
                    )
                } else if (showPasswordInput) {
                    // 输入密码以启用
                    Text(
                        text = "请输入备份密码以启用生物识别快捷访问：",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text("备份密码") },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        isError = errorMessage != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                } else {
                    // 未启用状态
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = getTextMuted(),
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "启用生物识别快捷访问",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "启用后，可以使用指纹或面容快速授权备份操作，无需每次输入密码。",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            if (isBiometricEnabled) {
                Button(onClick = onDisable) {
                    Text("禁用")
                }
            } else if (!hasBackupPassword) {
                // 需要设置密码
                Button(
                    onClick = {
                        android.util.Log.i("BiometricSetupDialog", "Set password and enable, password length: ${password.length}")
                        if (password.length >= 6) {
                            onEnable(password)
                        } else {
                            errorMessage = "密码长度至少6位"
                        }
                    },
                    enabled = password.length >= 6
                ) {
                    Text("设置并启用")
                }
            } else if (showPasswordInput) {
                Button(
                    onClick = {
                        android.util.Log.i("BiometricSetupDialog", "Enable button clicked, password length: ${password.length}")
                        if (password.length >= 6) {
                            onEnable(password)
                        } else {
                            errorMessage = "密码长度至少6位"
                        }
                    },
                    enabled = password.length >= 6
                ) {
                    Text("启用")
                }
            } else {
                Button(onClick = {
                    android.util.Log.i("BiometricSetupDialog", "First enable button clicked, showing password input")
                    showPasswordInput = true
                }) {
                    Text("启用")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 自动备份设置对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AutoBackupSettingsDialog(
    currentEnabled: Boolean,
    currentFrequency: AutoBackupScheduler.BackupFrequency,
    currentHour: Int,
    currentMinute: Int,
    hasBackupPassword: Boolean,
    backupFiles: List<String>,
    onSave: (Boolean, AutoBackupScheduler.BackupFrequency, Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var enabled by remember { mutableStateOf(currentEnabled) }
    var frequency by remember { mutableStateOf(currentFrequency) }
    var showTimePicker by remember { mutableStateOf(false) }
    var hour by remember { mutableStateOf(currentHour) }
    var minute by remember { mutableStateOf(currentMinute) }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("选择备份时间") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(onClick = {
                    hour = timePickerState.hour
                    minute = timePickerState.minute
                    showTimePicker = false
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("取消")
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("自动备份设置") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 密码提示
                if (!hasBackupPassword) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = getElevatedBackground())
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "请先创建备份并设置密码",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // 启用开关
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("启用自动备份")
                    Switch(
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                        enabled = hasBackupPassword
                    )
                }

                if (enabled) {
                    // 频率选择
                    Text("备份频率", style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            label = { Text("每天") },
                            selected = frequency == AutoBackupScheduler.BackupFrequency.DAILY,
                            onClick = { frequency = AutoBackupScheduler.BackupFrequency.DAILY }
                        )
                        FilterChip(
                            label = { Text("每周") },
                            selected = frequency == AutoBackupScheduler.BackupFrequency.WEEKLY,
                            onClick = { frequency = AutoBackupScheduler.BackupFrequency.WEEKLY }
                        )
                    }

                    // 时间选择
                    Text("备份时间", style = MaterialTheme.typography.labelMedium)
                    OutlinedButton(
                        onClick = { showTimePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = String.format("%02d:%02d", hour, minute),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "选择时间"
                        )
                    }

                    // 提示
                    Text(
                        text = "备份将在后台自动执行，备份文件保存在应用私有目录中。",
                        color = getTextMuted(),
                        style = MaterialTheme.typography.labelSmall
                    )

                    // 已有备份文件列表
                    if (backupFiles.isNotEmpty()) {
                        Text("已有备份文件", style = MaterialTheme.typography.labelMedium)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = getElevatedBackground()),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                backupFiles.take(5).forEach { fileName ->
                                    Text(
                                        text = fileName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = getTextMuted()
                                    )
                                }
                                if (backupFiles.size > 5) {
                                    Text(
                                        text = "还有 ${backupFiles.size - 5} 个文件...",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = getTextMuted()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(enabled, frequency, hour, minute) }) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
