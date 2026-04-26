package com.ledger.task.ui.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledger.task.BuildConfig
import com.ledger.task.backup.AutoBackupScheduler
import com.ledger.task.ui.theme.ThemeMode
import com.ledger.task.ui.theme.getDeepBackground
import com.ledger.task.ui.theme.getElevatedBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary
import com.ledger.task.ui.theme.getTextSecondary
import com.ledger.task.viewmodel.SettingsViewModel
import com.ledger.task.viewmodel.UpdateState
import com.ledger.task.viewmodel.UpdateViewModel
import org.koin.androidx.compose.koinViewModel
import java.io.File

/**
 * 设置页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    updateViewModel: UpdateViewModel = koinViewModel(),
    enableBiometricOnStart: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val updateUiState by updateViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // 从台账中心跳转过来需要开启生物识别
    LaunchedEffect(enableBiometricOnStart) {
        if (enableBiometricOnStart) {
            val activity = context as? FragmentActivity
            viewModel.requestEnableBiometricFromLedger(activity)
        }
    }

    // 显示消息
    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        containerColor = getDeepBackground(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "设置",
                        color = getTextPrimary()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                            tint = getTextSecondary()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = getDeepBackground()
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 外观设置
            SettingsSection(title = "外观") {
                SettingsItem(
                    icon = Icons.Default.Brightness6,
                    title = "主题模式",
                    subtitle = uiState.themeMode.displayName,
                    onClick = { viewModel.onShowThemeDialog(true) }
                )
            }

            // 备份设置
            SettingsSection(title = "备份") {
                SettingsItem(
                    icon = Icons.Default.Schedule,
                    title = "自动备份",
                    subtitle = if (uiState.autoBackupEnabled) "已开启" else "已关闭",
                    trailing = {
                        Switch(
                            checked = uiState.autoBackupEnabled,
                            onCheckedChange = { viewModel.onAutoBackupEnabledChange(it) }
                        )
                    }
                )
                if (uiState.autoBackupEnabled) {
                    SettingsItem(
                        icon = Icons.Default.Storage,
                        title = "备份频率",
                        subtitle = "${uiState.autoBackupFrequency.displayName} ${String.format("%02d:%02d", uiState.autoBackupHour, uiState.autoBackupMinute)}",
                        onClick = { viewModel.showBackupFrequencyDialog() }
                    )
                    if (uiState.nextBackupTime.isNotEmpty()) {
                        SettingsItem(
                            icon = Icons.Default.Info,
                            title = "下次备份",
                            subtitle = uiState.nextBackupTime,
                            enabled = false
                        )
                    }
                }
            }

            // 安全设置
            SettingsSection(title = "安全") {
                if (uiState.isBiometricAvailable) {
                    SettingsItem(
                        icon = Icons.Default.Fingerprint,
                        title = "生物识别",
                        subtitle = if (uiState.biometricEnabled) "已启用" else "未启用",
                        trailing = {
                            Switch(
                                checked = uiState.biometricEnabled,
                                onCheckedChange = {
                                    val activity = context as? FragmentActivity
                                    viewModel.onBiometricToggle(activity, it)
                                }
                            )
                        }
                    )
                }
            }

            // 通知设置
            SettingsSection(title = "通知") {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "任务提醒",
                    subtitle = "截止日期前提醒",
                    trailing = {
                        Switch(
                            checked = uiState.reminderEnabled,
                            onCheckedChange = { viewModel.onReminderEnabledChange(it) }
                        )
                    }
                )
            }

            // 数据管理
            SettingsSection(title = "数据") {
                SettingsItem(
                    icon = Icons.Default.CleaningServices,
                    title = "清理已完成任务",
                    subtitle = "共 ${uiState.completedTasks} 个已完成任务",
                    onClick = { viewModel.onShowClearCompletedDialog(true) }
                )
                SettingsItem(
                    icon = Icons.Default.Delete,
                    title = "清除所有数据",
                    subtitle = "共 ${uiState.totalTasks} 个任务",
                    onClick = { viewModel.onShowClearAllDialog(true) }
                )
            }

            // 关于
            SettingsSection(title = "关于") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "版本",
                    subtitle = BuildConfig.VERSION_NAME,
                    onClick = { }
                )
                SettingsItem(
                    icon = Icons.Default.SystemUpdate,
                    title = "检查更新",
                    subtitle = updateUiState.lastCheckTime?.let { "上次检查: $it" } ?: "从未检查",
                    onClick = { updateViewModel.checkUpdate() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // 主题选择对话框
    if (uiState.showThemeDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowThemeDialog(false) },
            title = { Text("主题模式") },
            text = {
                Column {
                    ThemeOption(
                        text = ThemeMode.SYSTEM.displayName,
                        selected = uiState.themeMode == ThemeMode.SYSTEM,
                        onClick = { viewModel.onThemeModeChange(ThemeMode.SYSTEM) }
                    )
                    ThemeOption(
                        text = ThemeMode.LIGHT.displayName,
                        selected = uiState.themeMode == ThemeMode.LIGHT,
                        onClick = { viewModel.onThemeModeChange(ThemeMode.LIGHT) }
                    )
                    ThemeOption(
                        text = ThemeMode.DARK.displayName,
                        selected = uiState.themeMode == ThemeMode.DARK,
                        onClick = { viewModel.onThemeModeChange(ThemeMode.DARK) }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onShowThemeDialog(false) }) {
                    Text("取消")
                }
            }
        )
    }

    // 清理已完成任务确认对话框
    if (uiState.showClearCompletedDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowClearCompletedDialog(false) },
            title = { Text("确认清理") },
            text = { Text("确定要清理 ${uiState.completedTasks} 个已完成的任务吗？此操作不可撤销。") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearCompletedTasks() }
                ) {
                    Text("确定", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowClearCompletedDialog(false) }) {
                    Text("取消")
                }
            }
        )
    }

    // 清除所有数据确认对话框
    if (uiState.showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowClearAllDialog(false) },
            title = { Text("确认清除") },
            text = { Text("确定要清除所有 ${uiState.totalTasks} 个任务吗？此操作不可撤销。") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearAllData() }
                ) {
                    Text("确定", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowClearAllDialog(false) }) {
                    Text("取消")
                }
            }
        )
    }

    // 密码设置对话框（用于启用生物识别）
    if (uiState.showPasswordDialog) {
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var hint by remember { mutableStateOf(uiState.passwordHint) }
        val activity = context as? FragmentActivity

        AlertDialog(
            onDismissRequest = { viewModel.dismissPasswordDialog() },
            title = { Text("设置备份密码") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "请设置备份密码（至少8位）：",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("备份密码") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("确认密码") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = confirmPassword.isNotEmpty() && password != confirmPassword,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = hint,
                        onValueChange = { hint = it },
                        label = { Text("密码提示（可选）") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                val isValid = password.length >= 8 && password == confirmPassword
                TextButton(
                    onClick = { viewModel.setPasswordAndEnableBiometric(activity, password, hint) },
                    enabled = isValid
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissPasswordDialog() }) {
                    Text("取消")
                }
            }
        )
    }

    // 备份频率设置对话框
    if (uiState.showBackupFrequencyDialog) {
        var selectedFrequency by remember { mutableStateOf(uiState.autoBackupFrequency) }
        var selectedHour by remember { mutableStateOf(uiState.autoBackupHour) }
        var selectedMinute by remember { mutableStateOf(uiState.autoBackupMinute) }
        var showTimePicker by remember { mutableStateOf(false) }

        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = selectedHour,
                initialMinute = selectedMinute,
                is24Hour = true
            )
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                title = { Text("选择备份时间") },
                text = {
                    androidx.compose.material3.TimePicker(state = timePickerState)
                },
                confirmButton = {
                    TextButton(onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
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
        } else {
            AlertDialog(
                onDismissRequest = { viewModel.dismissBackupFrequencyDialog() },
                title = { Text("备份频率设置") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 频率选择
                        Text("备份频率", style = MaterialTheme.typography.labelMedium)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            androidx.compose.material3.FilterChip(
                                selected = selectedFrequency == AutoBackupScheduler.BackupFrequency.DAILY,
                                onClick = { selectedFrequency = AutoBackupScheduler.BackupFrequency.DAILY },
                                label = { Text("每天") }
                            )
                            androidx.compose.material3.FilterChip(
                                selected = selectedFrequency == AutoBackupScheduler.BackupFrequency.WEEKLY,
                                onClick = { selectedFrequency = AutoBackupScheduler.BackupFrequency.WEEKLY },
                                label = { Text("每周") }
                            )
                        }

                        // 时间选择
                        Text("备份时间", style = MaterialTheme.typography.labelMedium)
                        androidx.compose.material3.OutlinedButton(
                            onClick = { showTimePicker = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = String.format("%02d:%02d", selectedHour, selectedMinute),
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "选择时间"
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.saveBackupFrequency(selectedFrequency, selectedHour, selectedMinute)
                    }) {
                        Text("保存")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissBackupFrequencyDialog() }) {
                        Text("取消")
                    }
                }
            )
        }
    }

    // 更新对话框
    UpdateDialog(
        updateState = updateUiState.updateState,
        needInstallPermission = updateUiState.needInstallPermission,
        onDismiss = { updateViewModel.resetState() },
        onDownload = { info -> updateViewModel.downloadUpdate(info) },
        onInstall = { file -> updateViewModel.installUpdate(file) },
        onRequestInstallPermission = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
                context.startActivity(intent)
            }
        },
        formatFileSize = { bytes -> updateViewModel.formatFileSize(bytes) }
    )
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = getTextMuted(),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = getElevatedBackground())
        ) {
            Column(content = content)
        }
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    trailing: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) getTextSecondary() else getTextMuted(),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (enabled) getTextPrimary() else getTextMuted(),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = subtitle,
                color = getTextMuted(),
                style = MaterialTheme.typography.bodySmall
            )
        }
        trailing()
    }
}

@Composable
private fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * 更新对话框
 */
@Composable
private fun UpdateDialog(
    updateState: UpdateState,
    needInstallPermission: Boolean = false,
    onDismiss: () -> Unit,
    onDownload: (com.ledger.task.update.ReleaseInfo) -> Unit,
    onInstall: (java.io.File) -> Boolean,
    onRequestInstallPermission: () -> Unit = {},
    formatFileSize: (Long) -> String
) {
    when (updateState) {
        is UpdateState.Idle -> { /* 不显示对话框 */ }
        is UpdateState.Checking -> {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("检查更新") },
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("正在检查...", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                },
                confirmButton = {}
            )
        }
        is UpdateState.UpdateAvailable -> {
            val info = updateState.info
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("发现新版本") },
                text = {
                    Column {
                        Text("版本 ${info.version}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "更新内容",
                            style = MaterialTheme.typography.labelMedium,
                            color = getTextMuted()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = info.changelog.ifEmpty { "暂无更新说明" },
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (info.fileSize > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "大小: ${formatFileSize(info.fileSize)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = getTextMuted()
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onDownload(info) }) {
                        Text("立即更新")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text("稍后")
                    }
                }
            )
        }
        is UpdateState.NoUpdate -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("检查更新") },
                text = { Text("当前已是最新版本") },
                confirmButton = {
                    TextButton(onClick = onDismiss) {
                        Text("确定")
                    }
                }
            )
        }
        is UpdateState.Downloading -> {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("正在下载") },
                text = {
                    Column {
                        LinearProgressIndicator(
                            progress = { updateState.progress / 100f },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (updateState.progress >= 0) {
                                "${updateState.progress}%"
                            } else {
                                "${formatFileSize(updateState.downloaded)}"
                            }
                        )
                    }
                },
                confirmButton = {}
            )
        }
        is UpdateState.DownloadComplete -> {
            if (needInstallPermission) {
                // 需要安装权限
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text("需要权限") },
                    text = { Text("安装应用需要授予安装权限，是否前往设置开启？") },
                    confirmButton = {
                        TextButton(onClick = {
                            onRequestInstallPermission()
                            onDismiss()
                        }) {
                            Text("去设置")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onDismiss) {
                            Text("取消")
                        }
                    }
                )
            } else {
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text("下载完成") },
                    text = { Text("是否立即安装更新？") },
                    confirmButton = {
                        TextButton(onClick = {
                            val success = onInstall(updateState.file)
                            if (success) {
                                onDismiss()
                            }
                            // 如果安装失败（需要权限），不关闭对话框，让用户看到权限提示
                        }) {
                            Text("安装")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onDismiss) {
                            Text("稍后")
                        }
                    }
                )
            }
        }
        is UpdateState.Error -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text("更新失败") },
                text = { Text(updateState.message) },
                confirmButton = {
                    TextButton(onClick = onDismiss) {
                        Text("确定")
                    }
                }
            )
        }
    }
}
