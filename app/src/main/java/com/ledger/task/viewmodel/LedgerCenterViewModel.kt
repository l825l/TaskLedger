package com.ledger.task.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.TaskLedgerApp
import com.ledger.task.backup.AutoBackupScheduler
import com.ledger.task.data.model.LedgerFilterState
import com.ledger.task.data.model.Task
import com.ledger.task.data.model.TimeRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * 台账中心 UI 状态
 */
data class LedgerCenterUiState(
    val tasks: List<Task> = emptyList(),
    val timeRange: TimeRange = TimeRange.THIS_MONTH,
    val customStartDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val customEndDate: LocalDate = LocalDate.now(),
    val filterState: LedgerFilterState = LedgerFilterState(
        startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
        endDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
    ),
    val allCategories: List<String> = emptyList(),
    val isExporting: Boolean = false,
    val lastExportUri: Uri? = null,
    val exportMessage: String? = null,
    val exportHistory: List<ExportFileInfo> = emptyList(),
    val isBackingUp: Boolean = false,
    val isRestoring: Boolean = false,
    val backupMessage: String? = null,
    // 备份密码相关
    val showBackupPasswordDialog: Boolean = false,
    val showRestorePasswordDialog: Boolean = false,
    val pendingBackupUri: Uri? = null,
    val pendingRestoreUri: Uri? = null,
    val backupPasswordError: String? = null,
    val isBackupPasswordProtected: Boolean = false,
    val passwordHint: String? = null,
    // 生物识别相关
    val isBiometricAvailable: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val showBiometricSetupDialog: Boolean = false,
    val needsBiometricForBackup: Boolean = false,
    val needsBiometricForRestore: Boolean = false,
    val needsSetupPasswordForBackup: Boolean = false,
    val needsBiometricForDisable: Boolean = false,
    val biometricFailedCount: Int = 0,
    val showPasswordForDisable: Boolean = false,  // 关闭生物识别时显示密码输入
    val needsShowBackupLauncher: Boolean = false, // 需要显示备份保存对话框
    val biometricTriggerId: Int = 0,  // 用于触发生物识别对话框重新显示
    // 自动备份相关
    val showAutoBackupSettingsDialog: Boolean = false,
    val autoBackupEnabled: Boolean = false,
    val autoBackupFrequency: AutoBackupScheduler.BackupFrequency = AutoBackupScheduler.BackupFrequency.DAILY,
    val autoBackupHour: Int = 2,
    val autoBackupMinute: Int = 0,
    val autoBackupNextTime: String = "未启用",
    val autoBackupFiles: List<String> = emptyList()
)

/**
 * 台账中心 ViewModel
 * 使用组合模式协调各功能模块
 */
class LedgerCenterViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "LedgerCenterViewModel"
    }

    private val repository = (application as TaskLedgerApp).repository

    private val _uiState = MutableStateFlow(LedgerCenterUiState())
    val uiState: StateFlow<LedgerCenterUiState> = _uiState.asStateFlow()

    // 状态更新辅助函数
    private fun updateState(newState: LedgerCenterUiState) {
        _uiState.value = newState
    }

    // 组合各功能模块
    private val dataLoader = LedgerDataLoader(
        application = application,
        getState = { _uiState.value },
        updateState = ::updateState
    )

    private val exportManager = LedgerExportManager(
        application = application,
        getState = { _uiState.value },
        updateState = ::updateState,
        getRelatedTaskSummaries = ::getRelatedTaskSummaries
    )

    private val backupCoordinator = LedgerBackupCoordinator(
        application = application,
        getState = { _uiState.value },
        updateState = ::updateState
    )

    private val autoBackupSettingsManager = AutoBackupSettingsManager(
        application = application,
        getState = { _uiState.value },
        updateState = ::updateState
    )

    init {
        exportManager.loadExportHistory()
        loadLedgerData()
        loadBiometricAndAutoBackupSettings()
    }

    /**
     * 加载生物识别和自动备份设置
     */
    private fun loadBiometricAndAutoBackupSettings() {
        backupCoordinator.loadBiometricSettings()
        autoBackupSettingsManager.loadAutoBackupSettings()
    }

    // ==================== 数据加载相关 ====================

    fun loadLedgerData() {
        dataLoader.loadLedgerData(viewModelScope)
    }

    fun onTimeRangeChange(range: TimeRange) {
        dataLoader.onTimeRangeChange(range)
        loadLedgerData()
    }

    fun onCustomDateRangeChange(startDate: LocalDate, endDate: LocalDate) {
        if (dataLoader.onCustomDateRangeChange(startDate, endDate)) {
            onTimeRangeChange(TimeRange.CUSTOM)
        }
    }

    fun onCategoryToggle(category: String) {
        dataLoader.onCategoryToggle(category)
        loadLedgerData()
    }

    fun onSelectAllCategories() {
        dataLoader.onSelectAllCategories()
        loadLedgerData()
    }

    fun onDeselectAllCategories() {
        dataLoader.onDeselectAllCategories()
        loadLedgerData()
    }

    fun onIncludeArchivedChange(include: Boolean) {
        dataLoader.onIncludeArchivedChange(include)
        loadLedgerData()
    }

    // ==================== 导出相关 ====================

    fun refreshExportHistory() {
        exportManager.refreshExportHistory()
    }

    fun exportCSV() {
        exportManager.exportCSV(viewModelScope)
    }

    fun exportExcel() {
        exportManager.exportExcel(viewModelScope)
    }

    fun shareToWechat(uri: Uri) {
        exportManager.shareToWechat(uri)
    }

    fun shareToEmail(uri: Uri) {
        exportManager.shareToEmail(uri)
    }

    fun exportAndShareWechat() {
        exportManager.exportAndShareWechat(viewModelScope)
    }

    fun exportAndShareEmail() {
        exportManager.exportAndShareEmail(viewModelScope)
    }

    fun exportAndShareCSV() {
        exportManager.exportAndShareCSV(viewModelScope)
    }

    fun exportAndShareExcel() {
        exportManager.exportAndShareExcel(viewModelScope)
    }

    fun exportAndShare() {
        exportManager.exportAndShare(viewModelScope)
    }

    fun shareExistingFile(uri: Uri) {
        exportManager.shareExistingFile(uri)
    }

    fun clearExportMessage() {
        exportManager.clearExportMessage()
    }

    fun openExportFileLocation() {
        exportManager.openExportFileLocation()
    }

    /**
     * 获取关联事项摘要（批量查询优化）
     * 使用 suspend 函数确保协程正确执行
     */
    private suspend fun getRelatedTaskSummaries(tasks: List<Task>): Map<Long, String> {
        if (tasks.isEmpty()) return emptyMap()

        val summaries = mutableMapOf<Long, String>()

        // 收集所有需要查询的关联任务 ID
        val allRelatedIds = tasks.flatMap { task ->
            task.predecessorIds + task.relatedIds
        }.distinct()

        // 批量查询所有关联任务
        val relatedTasksMap = if (allRelatedIds.isNotEmpty()) {
            repository.getTasksByIds(allRelatedIds).associateBy { it.id }
        } else {
            emptyMap()
        }

        // 构建每个任务的关联摘要
        tasks.forEach { task ->
            val relatedTasks = (task.predecessorIds + task.relatedIds)
                .mapNotNull { relatedTasksMap[it] }

            if (relatedTasks.isNotEmpty()) {
                val summary = relatedTasks.take(3).joinToString("；") { it.title }
                val more = if (relatedTasks.size > 3) "等${relatedTasks.size}项" else ""
                summaries[task.id] = summary + more
            }
        }

        return summaries
    }

    // ==================== 备份/恢复相关 ====================

    fun requestCreateBackup() {
        backupCoordinator.requestCreateBackup()
    }

    fun onBackupLauncherShown() {
        backupCoordinator.onBackupLauncherShown()
    }

    fun createBackup(uri: Uri) {
        backupCoordinator.createBackup(uri, viewModelScope)
    }

    fun performBackupAfterBiometricSetup() {
        backupCoordinator.performBackupAfterBiometricSetup()
    }

    fun confirmBackup(password: String?, passwordHint: String? = null) {
        backupCoordinator.confirmBackup(password, passwordHint, viewModelScope)
    }

    fun cancelBackupPasswordDialog() {
        backupCoordinator.cancelBackupPasswordDialog()
    }

    fun restoreFromBackup(uri: Uri) {
        backupCoordinator.restoreFromBackup(uri, viewModelScope)
    }

    fun performRestoreWithBiometric() {
        backupCoordinator.performRestoreWithBiometric(viewModelScope)
    }

    fun onBiometricFailedForRestore() {
        backupCoordinator.onBiometricFailedForRestore()
    }

    fun fallbackToPasswordForRestore() {
        backupCoordinator.fallbackToPasswordForRestore()
    }

    fun confirmRestore(password: String?) {
        backupCoordinator.confirmRestore(password, viewModelScope)
    }

    fun cancelRestorePasswordDialog() {
        backupCoordinator.cancelRestorePasswordDialog()
    }

    fun clearBackupMessage() {
        backupCoordinator.clearBackupMessage()
    }

    // ==================== 生物识别相关 ====================

    fun getBiometricAuthManager() = backupCoordinator.getBiometricAuthManager()

    fun getPasswordStorage() = backupCoordinator.getPasswordStorage()

    fun showBiometricSetupDialog() {
        backupCoordinator.showBiometricSetupDialog()
    }

    fun dismissBiometricSetupDialog() {
        backupCoordinator.dismissBiometricSetupDialog()
    }

    fun enableBiometricAccess(activity: androidx.fragment.app.FragmentActivity, password: String) {
        backupCoordinator.enableBiometricAccess(activity, password)
    }

    fun requestDisableBiometricAccess() {
        backupCoordinator.requestDisableBiometricAccess()
    }

    fun performDisableBiometricAccess() {
        backupCoordinator.performDisableBiometricAccess()
    }

    fun onBiometricFailedForDisable() {
        backupCoordinator.onBiometricFailedForDisable()
    }

    fun disableBiometricWithPassword(password: String): Boolean {
        return backupCoordinator.disableBiometricWithPassword(password)
    }

    fun cancelDisableBiometric() {
        backupCoordinator.cancelDisableBiometric()
    }

    fun saveBackupPassword(password: String): Boolean {
        return backupCoordinator.saveBackupPassword(password)
    }

    fun hasBackupPassword(): Boolean {
        return backupCoordinator.hasBackupPassword()
    }

    // ==================== 自动备份相关 ====================

    fun showAutoBackupSettingsDialog() {
        autoBackupSettingsManager.showAutoBackupSettingsDialog()
    }

    fun dismissAutoBackupSettingsDialog() {
        autoBackupSettingsManager.dismissAutoBackupSettingsDialog()
    }

    fun saveAutoBackupSettings(
        enabled: Boolean,
        frequency: AutoBackupScheduler.BackupFrequency,
        hour: Int,
        minute: Int
    ) {
        autoBackupSettingsManager.saveAutoBackupSettings(enabled, frequency, hour, minute)
    }

    fun testAutoBackup() {
        autoBackupSettingsManager.testAutoBackup()
    }

    fun getAutoBackupFiles(): List<String> {
        return autoBackupSettingsManager.getAutoBackupFiles()
    }

    override fun onCleared() {
        super.onCleared()
        dataLoader.cancelJobs()
    }
}
