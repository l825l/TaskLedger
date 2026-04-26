package com.ledger.task.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.backup.AutoBackupScheduler
import com.ledger.task.domain.model.LedgerFilterState
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TimeRange
import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.domain.usecase.DeleteTagUseCase
import com.ledger.task.domain.usecase.GetAllTagsUseCase
import com.ledger.task.domain.usecase.SaveTagUseCase
import com.ledger.task.ui.component.TagStatItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
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
    val showRestorePasswordDialog: Boolean = false,
    val showNoPasswordConfirmDialog: Boolean = false,  // 没有备份密码时的确认对话框
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
    val needsBiometricForDisable: Boolean = false,
    val biometricFailedCount: Int = 0,
    val showPasswordForDisable: Boolean = false,  // 关闭生物识别时显示密码输入
    val biometricTriggerId: Int = 0,  // 用于触发生物识别对话框重新显示
    // 自动备份相关
    val showAutoBackupSettingsDialog: Boolean = false,
    val autoBackupEnabled: Boolean = false,
    val autoBackupFrequency: AutoBackupScheduler.BackupFrequency = AutoBackupScheduler.BackupFrequency.DAILY,
    val autoBackupHour: Int = 2,
    val autoBackupMinute: Int = 0,
    val autoBackupNextTime: String = "未启用",
    val autoBackupFiles: List<String> = emptyList(),
    // 标签统计
    val tagStats: List<TagStatItem> = emptyList(),
    // 标签管理
    val allTags: List<Tag> = emptyList(),
    val tagTaskCounts: Map<Long, Int> = emptyMap(),
    val showTagManagementDialog: Boolean = false,
    // 标签筛选
    val selectedTagId: Long? = null
)

/**
 * 台账中心 ViewModel
 * 使用组合模式协调各功能模块
 */
class LedgerCenterViewModel(
    private val application: Application,
    private val repository: TaskRepository,
    private val tagRepository: TagRepository,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val saveTagUseCase: SaveTagUseCase,
    private val deleteTagUseCase: DeleteTagUseCase
) : ViewModel(), KoinComponent {

    companion object {
        private const val TAG = "LedgerCenterViewModel"
    }

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
        updateState = ::updateState,
        repository = repository
    )

    private val exportManager = LedgerExportManager(
        application = application,
        getState = { _uiState.value },
        updateState = ::updateState,
        getRelatedTaskSummaries = ::getRelatedTaskSummaries,
        repository = repository
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
        loadAllTags()
    }

    /**
     * 加载所有标签
     */
    private fun loadAllTags() {
        viewModelScope.launch {
            getAllTagsUseCase().collect { tags ->
                _uiState.value = _uiState.value.copy(allTags = tags)
                // 加载每个标签的任务数量
                loadTagTaskCounts(tags)
            }
        }
    }

    /**
     * 加载标签任务数量
     */
    private suspend fun loadTagTaskCounts(tags: List<Tag>) {
        val counts = mutableMapOf<Long, Int>()
        tags.forEach { tag ->
            tagRepository.getTaskCountForTag(tag.id).collect { count ->
                counts[tag.id] = count
            }
        }
        _uiState.value = _uiState.value.copy(tagTaskCounts = counts)
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

    fun requestCreateBackup(): Boolean {
        return backupCoordinator.requestCreateBackup()
    }

    fun createBackup(uri: Uri) {
        backupCoordinator.createBackup(uri, viewModelScope)
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

    fun isBiometricEnabled(): Boolean {
        return _uiState.value.isBiometricEnabled
    }

    /**
     * 显示没有备份密码的确认对话框
     */
    fun showNoPasswordConfirmDialog() {
        _uiState.value = _uiState.value.copy(showNoPasswordConfirmDialog = true)
    }

    /**
     * 隐藏没有备份密码的确认对话框
     */
    fun dismissNoPasswordConfirmDialog() {
        _uiState.value = _uiState.value.copy(showNoPasswordConfirmDialog = false)
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

    // ==================== 标签管理相关 ====================

    /**
     * 显示标签管理对话框
     */
    fun showTagManagementDialog() {
        _uiState.value = _uiState.value.copy(showTagManagementDialog = true)
    }

    /**
     * 隐藏标签管理对话框
     */
    fun dismissTagManagementDialog() {
        _uiState.value = _uiState.value.copy(showTagManagementDialog = false)
    }

    /**
     * 创建标签
     */
    fun createTag(name: String, color: androidx.compose.ui.graphics.Color) {
        viewModelScope.launch {
            val tag = Tag(name = name, color = color)
            saveTagUseCase(tag)
                .onSuccess {
                    // 标签列表会自动更新通过 Flow
                }
                .onFailure { error ->
                    Log.e(TAG, "创建标签失败", error)
                }
        }
    }

    /**
     * 更新标签
     */
    fun updateTag(tag: Tag) {
        viewModelScope.launch {
            saveTagUseCase(tag)
                .onSuccess {
                    // 标签列表会自动更新通过 Flow
                }
                .onFailure { error ->
                    Log.e(TAG, "更新标签失败", error)
                }
        }
    }

    /**
     * 删除标签
     */
    fun deleteTag(tag: Tag) {
        viewModelScope.launch {
            deleteTagUseCase(tag)
                .onSuccess {
                    // 标签列表会自动更新通过 Flow
                }
                .onFailure { error ->
                    Log.e(TAG, "删除标签失败", error)
                }
        }
    }

    // ==================== 标签筛选相关 ====================

    /**
     * 选择标签筛选
     */
    fun onTagFilterChange(tagId: Long?) {
        _uiState.value = _uiState.value.copy(selectedTagId = tagId)
        loadLedgerData()
    }

    override fun onCleared() {
        super.onCleared()
        dataLoader.cancelJobs()
    }
}
