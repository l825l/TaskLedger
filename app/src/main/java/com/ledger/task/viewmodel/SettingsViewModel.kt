package com.ledger.task.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.backup.AutoBackupScheduler
import com.ledger.task.backup.BackupPasswordStorage
import com.ledger.task.backup.BiometricAuthManager
import com.ledger.task.backup.BiometricStatus
import com.ledger.task.data.local.TaskDao
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.notification.ReminderManager
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.ui.theme.ThemeManager
import com.ledger.task.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 设置页面 UI 状态
 */
data class SettingsUiState(
    // 主题
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    // 备份
    val autoBackupEnabled: Boolean = false,
    val autoBackupFrequency: AutoBackupScheduler.BackupFrequency = AutoBackupScheduler.BackupFrequency.DAILY,
    val autoBackupHour: Int = 2,
    val autoBackupMinute: Int = 0,
    val nextBackupTime: String = "",
    // 安全
    val hasBackupPassword: Boolean = false,
    val isBiometricAvailable: Boolean = false,
    val biometricEnabled: Boolean = false,
    val passwordHint: String = "",
    // 通知
    val reminderEnabled: Boolean = true,
    // 数据
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    // 对话框
    val showThemeDialog: Boolean = false,
    val showClearCompletedDialog: Boolean = false,
    val showClearAllDialog: Boolean = false,
    val showPasswordDialog: Boolean = false,
    val showBackupFrequencyDialog: Boolean = false,
    // 导航标志：从台账中心跳转过来需要开启生物识别
    val needEnableBiometric: Boolean = false,
    // 消息
    val message: String? = null
)

/**
 * 设置页面 ViewModel
 */
class SettingsViewModel(
    application: Application,
    private val repository: TaskRepository,
    private val taskDao: TaskDao
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val passwordStorage = BackupPasswordStorage(application)
    private val biometricAuthManager = BiometricAuthManager(application)

    // 保存已认证的 Cipher，用于后续加密密码
    private var authenticatedCipher: javax.crypto.Cipher? = null

    init {
        loadSettings()
    }

    private fun loadSettings() {
        val context = getApplication<Application>()

        // 加载主题设置
        val themeMode = ThemeManager.getThemeMode(context)

        // 加载自动备份设置
        val backupSettings = AutoBackupScheduler.getSettings(context)
        val nextBackup = AutoBackupScheduler.getNextBackupDescription(context)

        // 检查是否有备份密码
        val hasPassword = passwordStorage.hasPassword()

        // 加载生物识别设置
        val biometricStatus = biometricAuthManager.canUseBiometric()
        val isBiometricAvailable = biometricStatus == BiometricStatus.AVAILABLE
        val isBiometricEnabled = passwordStorage.isBiometricEnabled()

        // 加载密码提示
        val passwordHint = passwordStorage.getPasswordHint() ?: ""

        // 加载提醒设置
        val reminderEnabled = ReminderManager.isReminderEnabled(context)

        // 先同步更新生物识别可用性状态
        _uiState.value = _uiState.value.copy(
            themeMode = themeMode,
            autoBackupEnabled = backupSettings.enabled,
            autoBackupFrequency = backupSettings.frequency,
            autoBackupHour = backupSettings.hour,
            autoBackupMinute = backupSettings.minute,
            nextBackupTime = nextBackup,
            hasBackupPassword = hasPassword,
            isBiometricAvailable = isBiometricAvailable,
            biometricEnabled = isBiometricEnabled,
            passwordHint = passwordHint,
            reminderEnabled = reminderEnabled
        )

        // 加载任务统计
        viewModelScope.launch {
            val allTasks = repository.getAllNow()
            val completed = allTasks.count { it.status == TaskStatus.DONE }
            _uiState.value = _uiState.value.copy(
                totalTasks = allTasks.size,
                completedTasks = completed
            )
        }
    }

    // ==================== 主题设置 ====================

    fun onThemeModeChange(mode: ThemeMode) {
        val context = getApplication<Application>()
        ThemeManager.saveThemeMode(context, mode)
        _uiState.value = _uiState.value.copy(
            themeMode = mode,
            showThemeDialog = false
        )
    }

    fun onShowThemeDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showThemeDialog = show)
    }

    // ==================== 备份设置 ====================

    fun onAutoBackupEnabledChange(enabled: Boolean) {
        if (enabled && !_uiState.value.hasBackupPassword) {
            _uiState.value = _uiState.value.copy(message = "请先设置备份密码")
            return
        }
        saveAutoBackupSettings(enabled, _uiState.value.autoBackupFrequency, _uiState.value.autoBackupHour, _uiState.value.autoBackupMinute)
    }

    fun showBackupFrequencyDialog() {
        _uiState.value = _uiState.value.copy(showBackupFrequencyDialog = true)
    }

    fun dismissBackupFrequencyDialog() {
        _uiState.value = _uiState.value.copy(showBackupFrequencyDialog = false)
    }

    fun saveBackupFrequency(frequency: AutoBackupScheduler.BackupFrequency, hour: Int, minute: Int) {
        saveAutoBackupSettings(_uiState.value.autoBackupEnabled, frequency, hour, minute)
        _uiState.value = _uiState.value.copy(showBackupFrequencyDialog = false)
    }

    private fun saveAutoBackupSettings(enabled: Boolean, frequency: AutoBackupScheduler.BackupFrequency, hour: Int, minute: Int) {
        val context = getApplication<Application>()
        val settings = AutoBackupScheduler.AutoBackupSettings(
            enabled = enabled,
            frequency = frequency,
            hour = hour,
            minute = minute
        )
        AutoBackupScheduler.saveSettings(context, settings)

        // 如果启用，调度备份
        if (enabled) {
            AutoBackupScheduler.scheduleBackup(context, settings)
        } else {
            AutoBackupScheduler.cancelBackup(context)
        }

        val nextBackup = AutoBackupScheduler.getNextBackupDescription(context)

        _uiState.value = _uiState.value.copy(
            autoBackupEnabled = enabled,
            autoBackupFrequency = frequency,
            autoBackupHour = hour,
            autoBackupMinute = minute,
            nextBackupTime = nextBackup,
            message = if (enabled) "已启用自动备份" else "已关闭自动备份"
        )
    }

    // ==================== 提醒设置 ====================

    fun onReminderEnabledChange(enabled: Boolean) {
        val context = getApplication<Application>()

        viewModelScope.launch {
            // 获取所有未完成任务
            val allTasks = repository.getAllNow()
            val pendingTasks = allTasks.filter { it.status != TaskStatus.DONE }

            // 更新设置并处理提醒
            ReminderManager.setReminderEnabled(context, enabled, pendingTasks)

            _uiState.value = _uiState.value.copy(
                reminderEnabled = enabled,
                message = if (enabled) "已启用任务提醒" else "已关闭任务提醒"
            )
        }
    }

    // ==================== 生物识别设置 ====================

    /**
     * 获取生物识别管理器（用于 UI 层调用认证）
     */
    fun getBiometricAuthManager(): BiometricAuthManager = biometricAuthManager

    /**
     * 禁用生物识别
     */
    fun disableBiometric() {
        passwordStorage.disableBiometricAccess()
        biometricAuthManager.deleteBiometricKey()
        // 清除备份密码和密码提示
        passwordStorage.clearAll()
        _uiState.value = _uiState.value.copy(
            biometricEnabled = false,
            hasBackupPassword = false,
            passwordHint = "",
            message = "生物识别已禁用"
        )
    }

    /**
     * 切换生物识别状态
     */
    fun onBiometricToggle(activity: FragmentActivity?, enabled: Boolean) {
        if (activity == null) {
            _uiState.value = _uiState.value.copy(message = "无法获取 Activity")
            return
        }

        if (enabled) {
            // 开启生物识别：先创建密钥，然后进行生物认证获取 Cipher
            if (!biometricAuthManager.createBiometricKey()) {
                _uiState.value = _uiState.value.copy(message = "创建生物识别密钥失败")
                return
            }

            val cipher = biometricAuthManager.getEncryptCipher()
            if (cipher == null) {
                _uiState.value = _uiState.value.copy(message = "获取加密密钥失败")
                return
            }

            // 进行生物认证，认证成功后保存 Cipher 并显示密码设置对话框
            biometricAuthManager.authenticateWithCipher(
                activity = activity,
                cipher = cipher,
                title = "验证身份",
                subtitle = "请使用指纹或面容验证以启用生物识别",
                negativeButtonText = "取消",
                onSuccess = { result ->
                    // 保存已认证的 Cipher
                    authenticatedCipher = result.cryptoObject?.cipher
                    if (authenticatedCipher != null) {
                        val savedHint = passwordStorage.getPasswordHint() ?: ""
                        _uiState.value = _uiState.value.copy(
                            showPasswordDialog = true,
                            passwordHint = savedHint
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(message = "认证失败")
                    }
                },
                onError = { _, errString ->
                    _uiState.value = _uiState.value.copy(message = errString)
                },
                onFailed = {
                    _uiState.value = _uiState.value.copy(message = "认证失败，请重试")
                }
            )
        } else {
            // 关闭生物识别：直接禁用，不需要验证
            disableBiometric()
        }
    }

    /**
     * 设置密码并启用生物识别
     */
    fun setPasswordAndEnableBiometric(activity: FragmentActivity?, password: String, hint: String = "") {
        if (password.length < 8) {
            _uiState.value = _uiState.value.copy(message = "密码长度至少8位")
            return
        }

        // 保存密码提示
        if (hint.isNotBlank()) {
            passwordStorage.savePasswordHint(hint)
        }

        // 保存密码
        passwordStorage.savePassword(password)

        // 使用已认证的 Cipher 启用生物识别
        val cipher = authenticatedCipher
        if (cipher != null) {
            val success = passwordStorage.enableBiometricAccessWithCipher(password, cipher)
            authenticatedCipher = null  // 清除已使用的 Cipher
            _uiState.value = _uiState.value.copy(
                showPasswordDialog = false,
                biometricEnabled = success,
                hasBackupPassword = success,
                message = if (success) "生物识别已启用" else "启用生物识别失败"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                showPasswordDialog = false,
                message = "认证已过期，请重试"
            )
        }
    }

    /**
     * 关闭密码对话框
     */
    fun dismissPasswordDialog() {
        _uiState.value = _uiState.value.copy(showPasswordDialog = false)
    }

    // ==================== 数据管理 ====================

    fun onShowClearCompletedDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showClearCompletedDialog = show)
    }

    fun onShowClearAllDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showClearAllDialog = show)
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            val allTasks = repository.getAllNow()
            val completedTasks = allTasks.filter { it.status == TaskStatus.DONE }
            completedTasks.forEach { task ->
                repository.delete(task)
            }
            _uiState.value = _uiState.value.copy(
                showClearCompletedDialog = false,
                completedTasks = 0,
                totalTasks = _uiState.value.totalTasks - completedTasks.size,
                message = "已清理 ${completedTasks.size} 个已完成任务"
            )
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            taskDao.deleteAll()

            _uiState.value = _uiState.value.copy(
                showClearAllDialog = false,
                totalTasks = 0,
                completedTasks = 0,
                message = "已清除所有数据"
            )
        }
    }

    // ==================== 从台账中心跳转 ====================

    /**
     * 从台账中心跳转过来需要开启生物识别
     */
    fun requestEnableBiometricFromLedger(activity: FragmentActivity?) {
        if (activity == null) {
            _uiState.value = _uiState.value.copy(message = "无法获取 Activity")
            return
        }

        // 直接检查生物识别可用性
        val biometricStatus = biometricAuthManager.canUseBiometric()
        if (biometricStatus != BiometricStatus.AVAILABLE) {
            _uiState.value = _uiState.value.copy(message = "设备不支持生物识别")
            return
        }

        if (_uiState.value.biometricEnabled) {
            _uiState.value = _uiState.value.copy(message = "生物识别已启用")
            return
        }

        // 先创建密钥
        if (!biometricAuthManager.createBiometricKey()) {
            _uiState.value = _uiState.value.copy(message = "创建生物识别密钥失败")
            return
        }

        val cipher = biometricAuthManager.getEncryptCipher()
        if (cipher == null) {
            _uiState.value = _uiState.value.copy(message = "获取加密密钥失败")
            return
        }

        // 进行生物认证，认证成功后保存 Cipher 并显示密码设置对话框
        biometricAuthManager.authenticateWithCipher(
            activity = activity,
            cipher = cipher,
            title = "验证身份",
            subtitle = "请使用指纹或面容验证以启用生物识别",
            negativeButtonText = "取消",
            onSuccess = { result ->
                // 保存已认证的 Cipher
                authenticatedCipher = result.cryptoObject?.cipher
                if (authenticatedCipher != null) {
                    val savedHint = passwordStorage.getPasswordHint() ?: ""
                    _uiState.value = _uiState.value.copy(
                        showPasswordDialog = true,
                        passwordHint = savedHint,
                        needEnableBiometric = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(message = "认证失败")
                }
            },
            onError = { _, errString ->
                _uiState.value = _uiState.value.copy(message = errString)
            },
            onFailed = {
                _uiState.value = _uiState.value.copy(message = "认证失败，请重试")
            }
        )
    }

    /**
     * 清除需要开启生物识别的标志
     */
    fun clearNeedEnableBiometric() {
        _uiState.value = _uiState.value.copy(needEnableBiometric = false)
    }

    // ==================== 消息 ====================

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}
