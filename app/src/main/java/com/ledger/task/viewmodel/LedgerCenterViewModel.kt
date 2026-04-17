package com.ledger.task.viewmodel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.TaskLedgerApp
import com.ledger.task.backup.AutoBackupScheduler
import com.ledger.task.backup.BackupInfo
import com.ledger.task.backup.BackupManager
import com.ledger.task.backup.BackupPasswordProtection
import com.ledger.task.backup.BiometricAuthManager
import com.ledger.task.backup.BiometricStatus
import com.ledger.task.backup.BackupPasswordStorage
import com.ledger.task.backup.RestoreManager
import com.ledger.task.data.model.*
import com.ledger.task.ui.util.LedgerExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

/**
 * 导出文件信息
 */
data class ExportFileInfo(
    val uri: Uri,
    val fileName: String,
    val exportTime: Long
)

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
 */
class LedgerCenterViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "LedgerCenterViewModel"
        private const val PREFS_NAME = "export_history"
        private const val KEY_HISTORY = "history"
        private const val MAX_HISTORY = 50 // 每页5个，最多10页
    }

    private val repository = (application as TaskLedgerApp).repository
    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // 生物识别和密码存储
    private val passwordStorage = BackupPasswordStorage(application)
    private val biometricAuthManager = BiometricAuthManager(application)

    // Flow 收集任务
    private var loadLedgerJob: Job? = null
    private var loadCategoriesJob: Job? = null

    private val _uiState = MutableStateFlow(LedgerCenterUiState())
    val uiState: StateFlow<LedgerCenterUiState> = _uiState.asStateFlow()

    init {
        loadExportHistory()
        loadLedgerData()
        loadBiometricAndAutoBackupSettings()
    }

    /**
     * 加载生物识别和自动备份设置
     */
    private fun loadBiometricAndAutoBackupSettings() {
        val biometricStatus = biometricAuthManager.canUseBiometric()
        val isBiometricAvailable = biometricStatus == BiometricStatus.AVAILABLE
        val isBiometricEnabled = passwordStorage.isBiometricEnabled()

        Log.i(TAG, "loadBiometricAndAutoBackupSettings: biometricStatus=$biometricStatus, isBiometricAvailable=$isBiometricAvailable, isBiometricEnabled=$isBiometricEnabled")

        val autoBackupSettings = AutoBackupScheduler.getSettings(getApplication())
        val nextBackupTime = AutoBackupScheduler.getNextBackupDescription(getApplication())

        _uiState.value = _uiState.value.copy(
            isBiometricAvailable = isBiometricAvailable,
            isBiometricEnabled = isBiometricEnabled,
            autoBackupEnabled = autoBackupSettings.enabled,
            autoBackupFrequency = autoBackupSettings.frequency,
            autoBackupHour = autoBackupSettings.hour,
            autoBackupMinute = autoBackupSettings.minute,
            autoBackupNextTime = nextBackupTime
        )
    }

    /**
     * 从 SharedPreferences 加载导出历史
     */
    private fun loadExportHistory() {
        refreshExportHistory()
    }

    /**
     * 刷新导出历史（扫描下载目录，过滤已删除文件）
     */
    fun refreshExportHistory() {
        try {
            val historyJson = prefs.getString(KEY_HISTORY, "[]") ?: "[]"
            Log.d(TAG, "Loading export history: $historyJson")
            val jsonArray = JSONArray(historyJson)
            val history = mutableListOf<ExportFileInfo>()
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                history.add(
                    ExportFileInfo(
                        uri = Uri.parse(item.getString("uri")),
                        fileName = item.getString("fileName"),
                        exportTime = item.getLong("exportTime")
                    )
                )
            }
            Log.d(TAG, "Loaded ${history.size} export history items")

            // 扫描下载目录中的历史文件
            val scannedFiles = scanDownloadDirectoryForExportFiles()
            val scannedUris = scannedFiles.map { it.uri.toString() }.toSet()

            // 过滤掉已删除的文件（只保留扫描到的文件）
            val validHistory = history.filter { it.uri.toString() in scannedUris }

            // 合并扫描到的新文件
            val existingUris = validHistory.map { it.uri.toString() }.toSet()
            val newFiles = scannedFiles.filter { it.uri.toString() !in existingUris }

            // 合并并按时间排序
            val mergedHistory = (validHistory + newFiles)
                .sortedByDescending { it.exportTime }
                .take(MAX_HISTORY)

            // 保存合并后的历史
            saveExportHistory(mergedHistory)
            _uiState.value = _uiState.value.copy(exportHistory = mergedHistory)
            Log.d(TAG, "Valid history: ${validHistory.size}, new files: ${newFiles.size}, total: ${mergedHistory.size}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load export history", e)
        }
    }

    /**
     * 扫描下载目录中的导出文件
     */
    private fun scanDownloadDirectoryForExportFiles(): List<ExportFileInfo> {
        val files = mutableListOf<ExportFileInfo>()
        try {
            val context = getApplication<Application>().applicationContext
            val resolver = context.contentResolver

            // 查询下载目录中的文件
            val projection = arrayOf(
                MediaStore.Downloads._ID,
                MediaStore.Downloads.DISPLAY_NAME,
                MediaStore.Downloads.DATE_ADDED
            )

            val selection = "${MediaStore.Downloads.DISPLAY_NAME} LIKE ? OR ${MediaStore.Downloads.DISPLAY_NAME} LIKE ?"
            val selectionArgs = arrayOf("任务台账_%.csv", "任务台账_%.xls")

            val cursor: Cursor? = resolver.query(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                "${MediaStore.Downloads.DATE_ADDED} DESC"
            )

            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Downloads._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
                val dateColumn = it.getColumnIndexOrThrow(MediaStore.Downloads.DATE_ADDED)

                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val fileName = it.getString(nameColumn)
                    val dateAdded = it.getLong(dateColumn) * 1000 // 转换为毫秒

                    val uri = Uri.withAppendedPath(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id.toString())
                    files.add(ExportFileInfo(uri, fileName, dateAdded))
                }
            }

            Log.d(TAG, "Scanned ${files.size} export files from download directory")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to scan download directory", e)
        }
        return files.take(MAX_HISTORY)
    }

    /**
     * 保存导出历史到 SharedPreferences
     */
    private fun saveExportHistory(history: List<ExportFileInfo>) {
        try {
            val jsonArray = JSONArray()
            history.forEach { info ->
                val item = JSONObject().apply {
                    put("uri", info.uri.toString())
                    put("fileName", info.fileName)
                    put("exportTime", info.exportTime)
                }
                jsonArray.put(item)
            }
            val jsonStr = jsonArray.toString()
            Log.d(TAG, "Saving export history: $jsonStr")
            prefs.edit().putString(KEY_HISTORY, jsonStr).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save export history", e)
        }
    }

    fun loadLedgerData() {
        val state = _uiState.value.filterState

        // 取消之前的收集任务
        loadLedgerJob?.cancel()
        loadCategoriesJob?.cancel()

        loadLedgerJob = viewModelScope.launch {
            // 将日期转换为毫秒（与数据库中的 deadline 字段格式一致）
            val startMillis = state.startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endMillis = state.endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            repository.getLedgerTasks(
                startMillis,
                endMillis,
                state.includeArchived,
                if (state.categories.isEmpty()) null else state.categories.firstOrNull()
            ).collect { tasks ->
                // 如果选择了多个分类，在内存中过滤
                val filteredTasks = if (state.categories.isEmpty() || state.categories.size == 1) {
                    tasks
                } else {
                    tasks.filter { it.category in state.categories }
                }

                _uiState.value = _uiState.value.copy(
                    tasks = filteredTasks
                )
            }
        }

        // 加载所有分类
        loadCategoriesJob = viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                _uiState.value = _uiState.value.copy(allCategories = categories)
            }
        }
    }

    fun onTimeRangeChange(range: TimeRange) {
        val today = LocalDate.now()
        val (startDate, endDate) = when (range) {
            TimeRange.THIS_WEEK -> {
                val start = today.with(DayOfWeek.MONDAY)
                val end = today.with(DayOfWeek.SUNDAY)
                start to end
            }
            TimeRange.THIS_MONTH -> {
                val start = today.withDayOfMonth(1)
                val end = today.with(TemporalAdjusters.lastDayOfMonth())
                start to end
            }
            TimeRange.CUSTOM -> {
                _uiState.value.customStartDate to _uiState.value.customEndDate
            }
        }
        _uiState.value = _uiState.value.copy(
            timeRange = range,
            filterState = _uiState.value.filterState.copy(
                startDate = startDate,
                endDate = endDate
            )
        )
        loadLedgerData()
    }

    fun onCustomDateRangeChange(startDate: LocalDate, endDate: LocalDate) {
        // 验证日期范围
        if (startDate.isAfter(endDate)) {
            _uiState.value = _uiState.value.copy(
                exportMessage = "开始日期不能晚于结束日期"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            customStartDate = startDate,
            customEndDate = endDate
        )
        onTimeRangeChange(TimeRange.CUSTOM)
    }

    fun onCategoryToggle(category: String) {
        val currentCategories = _uiState.value.filterState.categories
        val newCategories = if (currentCategories.contains(category)) {
            currentCategories - category
        } else {
            currentCategories + category
        }
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(categories = newCategories)
        )
        loadLedgerData()
    }

    fun onSelectAllCategories() {
        val allCategories = _uiState.value.allCategories.toSet()
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(categories = allCategories)
        )
        loadLedgerData()
    }

    fun onDeselectAllCategories() {
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(categories = emptySet())
        )
        loadLedgerData()
    }

    fun onIncludeArchivedChange(include: Boolean) {
        _uiState.value = _uiState.value.copy(
            filterState = _uiState.value.filterState.copy(includeArchived = include)
        )
        loadLedgerData()
    }

    /**
     * 导出为 CSV（在 IO 线程执行）
     */
    fun exportCSV() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isExporting = true)

            try {
                val tasks = _uiState.value.tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = getApplication<Application>().applicationContext

                val uri = LedgerExporter.exportToCSV(
                    context = context,
                    tasks = tasks,
                    relatedTaskSummaries = relatedSummaries
                )

                withContext(Dispatchers.Main) {
                    if (uri != null) {
                        val newHistory = addToExportHistory(uri, "csv")
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            lastExportUri = uri,
                            exportMessage = "CSV 导出成功",
                            exportHistory = newHistory
                        )
                        Log.d(TAG, "CSV export done, history size: ${newHistory.size}")
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            exportMessage = "CSV 导出失败"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "CSV export failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 导出为 Excel（在 IO 线程执行）
     */
    fun exportExcel() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isExporting = true)

            try {
                val tasks = _uiState.value.tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = getApplication<Application>().applicationContext

                val uri = LedgerExporter.exportToExcel(
                    context = context,
                    tasks = tasks,
                    relatedTaskSummaries = relatedSummaries
                )

                withContext(Dispatchers.Main) {
                    if (uri != null) {
                        val newHistory = addToExportHistory(uri, "xls")
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            lastExportUri = uri,
                            exportMessage = "Excel 导出成功",
                            exportHistory = newHistory
                        )
                        Log.d(TAG, "Excel export done, history size: ${newHistory.size}")
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            exportMessage = "Excel 导出失败"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Excel export failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 添加到导出历史
     */
    private fun addToExportHistory(uri: Uri, extension: String): List<ExportFileInfo> {
        val currentHistory = _uiState.value.exportHistory.toMutableList()
        // 生成完整文件名
        val timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        )
        val fileName = "任务台账_${timestamp}.$extension"
        val exportTime = System.currentTimeMillis()
        val newFile = ExportFileInfo(uri, fileName, exportTime)
        // 添加到列表开头，保留最近10个文件
        currentHistory.add(0, newFile)
        val newHistory = currentHistory.take(MAX_HISTORY)
        // 保存到 SharedPreferences
        saveExportHistory(newHistory)
        Log.d(TAG, "Added to history: $fileName, total: ${newHistory.size}")
        return newHistory
    }

    /**
     * 分享到微信（如果没有导出文件则先导出）
     */
    fun shareToWechat(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        LedgerExporter.shareToWechat(context, uri)
    }

    /**
     * 分享到邮件
     */
    fun shareToEmail(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        LedgerExporter.shareToEmail(context, uri)
    }

    /**
     * 导出并分享到微信
     */
    fun exportAndShareWechat() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isExporting = true)
            try {
                val tasks = _uiState.value.tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = getApplication<Application>().applicationContext

                val uri = LedgerExporter.exportToExcel(context, tasks, relatedSummaries)
                if (uri != null) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            lastExportUri = uri
                        )
                        LedgerExporter.shareToWechat(context, uri)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            exportMessage = "导出失败"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Export and share failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 导出并分享到邮件
     */
    fun exportAndShareEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isExporting = true)
            try {
                val tasks = _uiState.value.tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = getApplication<Application>().applicationContext

                val uri = LedgerExporter.exportToExcel(context, tasks, relatedSummaries)
                if (uri != null) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            lastExportUri = uri
                        )
                        LedgerExporter.shareToEmail(context, uri)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            exportMessage = "导出失败"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Export and share email failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 导出为 CSV 并分享
     */
    fun exportAndShareCSV() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isExporting = true)
            try {
                val tasks = _uiState.value.tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = getApplication<Application>().applicationContext

                val uri = LedgerExporter.exportToCSV(context, tasks, relatedSummaries)
                if (uri != null) {
                    val newHistory = addToExportHistory(uri, "csv")
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            lastExportUri = uri,
                            exportHistory = newHistory
                        )
                        LedgerExporter.share(context, uri, "text/csv")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            exportMessage = "导出失败"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Export and share CSV failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 导出为 Excel 并分享
     */
    fun exportAndShareExcel() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isExporting = true)
            try {
                val tasks = _uiState.value.tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = getApplication<Application>().applicationContext

                val uri = LedgerExporter.exportToExcel(context, tasks, relatedSummaries)
                if (uri != null) {
                    val newHistory = addToExportHistory(uri, "xls")
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            lastExportUri = uri,
                            exportHistory = newHistory
                        )
                        LedgerExporter.share(context, uri, "application/vnd.ms-excel")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isExporting = false,
                            exportMessage = "导出失败"
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Export and share Excel failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 导出并分享到其他应用（保留旧方法兼容）
     */
    fun exportAndShare() {
        exportAndShareExcel()
    }

    /**
     * 分享已导出的文件
     */
    fun shareExistingFile(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        LedgerExporter.share(context, uri, "*/*")
    }

    /**
     * 清除导出消息
     */
    fun clearExportMessage() {
        _uiState.value = _uiState.value.copy(exportMessage = null)
    }

    /**
     * 打开导出文件所在位置
     */
    fun openExportFileLocation() {
        val context = getApplication<Application>().applicationContext
        LedgerExporter.openFileLocation(context)
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

    /**
     * 请求创建备份（先检测密码）
     */
    fun requestCreateBackup() {
        if (passwordStorage.isBiometricEnabled() && hasBackupPassword()) {
            // 生物识别已启用，直接弹出保存对话框
            _uiState.value = _uiState.value.copy(
                needsShowBackupLauncher = true
            )
        } else if (hasBackupPassword()) {
            // 有密码但未启用生物识别，弹出保存对话框
            _uiState.value = _uiState.value.copy(
                needsShowBackupLauncher = true
            )
        } else {
            // 没有设置密码，跳转到生物识别设置
            _uiState.value = _uiState.value.copy(
                showBiometricSetupDialog = true,
                needsSetupPasswordForBackup = true
            )
        }
    }

    /**
     * 备份文件保存对话框已显示
     */
    fun onBackupLauncherShown() {
        _uiState.value = _uiState.value.copy(
            needsShowBackupLauncher = false
        )
    }

    /**
     * 创建备份（文件已选择）
     */
    fun createBackup(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            pendingBackupUri = uri,
            backupPasswordError = null
        )

        // 如果生物识别已启用且有保存的密码，直接执行备份（无需验证）
        if (passwordStorage.isBiometricEnabled() && hasBackupPassword()) {
            performBackupDirectly()
        } else if (hasBackupPassword()) {
            // 有密码但未启用生物识别，显示密码对话框
            _uiState.value = _uiState.value.copy(
                showBackupPasswordDialog = true
            )
        } else {
            // 不应该到达这里，因为已经在 requestCreateBackup 中处理了
            Log.e(TAG, "createBackup called without password")
        }
    }

    /**
     * 直接执行备份（生物识别已启用时）
     */
    private fun performBackupDirectly() {
        val uri = _uiState.value.pendingBackupUri ?: return
        val password = passwordStorage.getPassword() ?: return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                isBackingUp = true
            )
            try {
                val context = getApplication<Application>().applicationContext
                val result = BackupManager.createBackup(context, uri, password, null)

                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isBackingUp = false,
                        pendingBackupUri = null,
                        backupMessage = if (result.success) "备份成功" else "备份失败"
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Backup failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isBackingUp = false,
                        pendingBackupUri = null,
                        backupMessage = "备份失败: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 生物识别设置完成后，显示备份保存对话框
     */
    fun performBackupAfterBiometricSetup() {
        _uiState.value = _uiState.value.copy(
            needsSetupPasswordForBackup = false,
            needsShowBackupLauncher = true
        )
    }

    /**
     * 确认创建备份（带密码和密码提示）
     */
    fun confirmBackup(password: String?, passwordHint: String? = null) {
        val uri = _uiState.value.pendingBackupUri ?: return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                isBackingUp = true,
                showBackupPasswordDialog = false
            )
            try {
                val context = getApplication<Application>().applicationContext
                val result = BackupManager.createBackup(context, uri, password, passwordHint)

                // 如果备份成功且设置了密码，保存密码用于自动备份和生物识别
                if (result.success && password != null && password.length >= 6) {
                    passwordStorage.savePassword(password)
                    Log.i(TAG, "Saved backup password for auto backup and biometric")
                }

                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isBackingUp = false,
                        pendingBackupUri = null,
                        backupMessage = if (result.success) "备份成功" else "备份失败"
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Backup failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isBackingUp = false,
                        pendingBackupUri = null,
                        backupMessage = "备份失败: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * 取消备份密码对话框
     */
    fun cancelBackupPasswordDialog() {
        _uiState.value = _uiState.value.copy(
            showBackupPasswordDialog = false,
            pendingBackupUri = null,
            backupPasswordError = null,
            needsBiometricForBackup = false
        )
    }

    /**
     * 从备份恢复（检查是否需要密码，优先使用生物识别）
     */
    fun restoreFromBackup(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val backupInfo = BackupManager.getBackupInfo(context, uri)

            withContext(Dispatchers.Main) {
                if (backupInfo?.isPasswordProtected == true) {
                    // 需要密码
                    _uiState.value = _uiState.value.copy(
                        pendingRestoreUri = uri,
                        backupPasswordError = null,
                        isBackupPasswordProtected = true,
                        passwordHint = backupInfo.passwordHint,
                        biometricFailedCount = 0
                    )

                    // 如果生物识别已启用且有保存的密码，使用生物识别
                    if (passwordStorage.isBiometricEnabled() && hasBackupPassword()) {
                        _uiState.value = _uiState.value.copy(
                            showRestorePasswordDialog = false,
                            needsBiometricForRestore = true
                        )
                    } else {
                        // 否则显示密码对话框
                        _uiState.value = _uiState.value.copy(
                            showRestorePasswordDialog = true
                        )
                    }
                } else {
                    // 不需要密码，直接恢复
                    performRestore(uri, null)
                }
            }
        }
    }

    /**
     * 生物识别验证成功后执行恢复
     */
    fun performRestoreWithBiometric() {
        val uri = _uiState.value.pendingRestoreUri ?: return
        val password = passwordStorage.getPassword() ?: return

        _uiState.value = _uiState.value.copy(
            needsBiometricForRestore = false,
            biometricFailedCount = 0
        )
        performRestore(uri, password)
    }

    /**
     * 生物识别验证失败，检查是否需要回退到密码输入
     */
    fun onBiometricFailedForRestore() {
        val failedCount = _uiState.value.biometricFailedCount + 1
        Log.i(TAG, "Biometric failed for restore, count: $failedCount")

        if (failedCount >= 3) {
            // 三次失败，回退到密码输入
            _uiState.value = _uiState.value.copy(
                needsBiometricForRestore = false,
                showRestorePasswordDialog = true,
                biometricFailedCount = 0
            )
        } else {
            // 更新失败计数，增加触发器ID以重新显示生物识别
            _uiState.value = _uiState.value.copy(
                biometricFailedCount = failedCount,
                biometricTriggerId = _uiState.value.biometricTriggerId + 1
            )
        }
    }

    /**
     * 生物识别错误，回退到密码输入
     */
    fun fallbackToPasswordForRestore() {
        _uiState.value = _uiState.value.copy(
            needsBiometricForRestore = false,
            showRestorePasswordDialog = true,
            biometricFailedCount = 0
        )
    }

    /**
     * 确认恢复（带密码）
     */
    fun confirmRestore(password: String?) {
        val uri = _uiState.value.pendingRestoreUri ?: return
        _uiState.value = _uiState.value.copy(showRestorePasswordDialog = false)
        performRestore(uri, password)
    }

    /**
     * 执行恢复
     */
    private fun performRestore(uri: Uri, password: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isRestoring = true)
            try {
                val context = getApplication<Application>().applicationContext
                val success = RestoreManager.restoreFromBackup(context, uri, password)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isRestoring = false,
                        pendingRestoreUri = null,
                        backupMessage = if (success) "恢复成功，应用将重启" else "恢复失败"
                    )
                    if (success) {
                        // 延迟重启应用
                        restartApp(context)
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Restore failed", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isRestoring = false,
                        pendingRestoreUri = null,
                        backupMessage = "恢复失败: 密码错误"
                    )
                }
            }
        }
    }

    /**
     * 取消恢复密码对话框
     */
    fun cancelRestorePasswordDialog() {
        _uiState.value = _uiState.value.copy(
            showRestorePasswordDialog = false,
            pendingRestoreUri = null,
            backupPasswordError = null,
            passwordHint = null,
            needsBiometricForRestore = false
        )
    }

    /**
     * 重启应用
     */
    private fun restartApp(context: Context) {
        viewModelScope.launch {
            delay(1500) // 延迟让用户看到提示
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = intent?.component
            val mainIntent = android.content.Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }

    /**
     * 清除备份消息
     */
    fun clearBackupMessage() {
        _uiState.value = _uiState.value.copy(backupMessage = null)
    }

    // ==================== 生物识别相关 ====================

    /**
     * 获取生物识别管理器（用于 UI 层调用认证）
     */
    fun getBiometricAuthManager(): BiometricAuthManager = biometricAuthManager

    /**
     * 获取密码存储（用于 UI 层访问）
     */
    fun getPasswordStorage(): BackupPasswordStorage = passwordStorage

    /**
     * 显示生物识别设置对话框
     */
    fun showBiometricSetupDialog() {
        Log.i(TAG, "showBiometricSetupDialog called, hasBackupPassword=${hasBackupPassword()}, isBiometricAvailable=${biometricAuthManager.canUseBiometric()}")
        _uiState.value = _uiState.value.copy(showBiometricSetupDialog = true)
    }

    /**
     * 关闭生物识别设置对话框
     */
    fun dismissBiometricSetupDialog() {
        _uiState.value = _uiState.value.copy(showBiometricSetupDialog = false)
    }

    /**
     * 启用生物识别快捷访问
     * 需要先通过生物识别认证，然后才能加密密码
     * @param activity FragmentActivity 用于显示生物识别对话框
     * @param password 备份密码
     */
    fun enableBiometricAccess(activity: androidx.fragment.app.FragmentActivity, password: String) {
        Log.i(TAG, "enableBiometricAccess: starting")

        // 先保存密码（用于自动备份和后续操作）
        passwordStorage.savePassword(password)
        Log.i(TAG, "Password saved")

        // 先创建生物识别密钥
        if (!passwordStorage.getBiometricAuthManager().createBiometricKey()) {
            Log.e(TAG, "Failed to create biometric key")
            _uiState.value = _uiState.value.copy(
                showBiometricSetupDialog = false,
                backupMessage = "创建生物识别密钥失败"
            )
            return
        }
        Log.i(TAG, "Biometric key created")

        // 获取加密 Cipher
        val cipher = passwordStorage.getBiometricAuthManager().getEncryptCipher()
        if (cipher == null) {
            Log.e(TAG, "Failed to get encrypt cipher")
            _uiState.value = _uiState.value.copy(
                showBiometricSetupDialog = false,
                backupMessage = "获取加密器失败"
            )
            return
        }
        Log.i(TAG, "Got encrypt cipher, showing biometric prompt")

        // 显示生物识别提示，认证成功后加密密码
        passwordStorage.getBiometricAuthManager().authenticateWithCipher(
            activity = activity,
            cipher = cipher,
            title = "验证身份",
            subtitle = "请使用指纹或面容验证以启用生物识别快捷访问",
            negativeButtonText = "取消",
            onSuccess = { result ->
                Log.i(TAG, "Biometric auth succeeded")
                // 认证成功，使用已认证的 Cipher 加密密码
                val authenticatedCipher = result.cryptoObject?.cipher
                if (authenticatedCipher != null) {
                    val success = passwordStorage.enableBiometricAccessWithCipher(password, authenticatedCipher)
                    Log.i(TAG, "enableBiometricAccessWithCipher result: $success")

                    // 检查是否有待处理的备份
                    val needsBackup = _uiState.value.needsSetupPasswordForBackup

                    _uiState.value = _uiState.value.copy(
                        isBiometricEnabled = success,
                        showBiometricSetupDialog = false,
                        backupMessage = if (success) "已启用生物识别" else "启用生物识别失败"
                    )

                    // 如果是从备份跳转过来的，启用成功后执行备份
                    if (success && needsBackup) {
                        performBackupAfterBiometricSetup()
                    }
                } else {
                    Log.e(TAG, "Authenticated cipher is null")
                    _uiState.value = _uiState.value.copy(
                        showBiometricSetupDialog = false,
                        backupMessage = "启用生物识别失败"
                    )
                }
            },
            onError = { errorCode, errString ->
                Log.e(TAG, "Biometric auth error: $errorCode - $errString")
                _uiState.value = _uiState.value.copy(
                    showBiometricSetupDialog = false,
                    backupMessage = "验证失败: $errString"
                )
            },
            onFailed = {
                Log.w(TAG, "Biometric auth failed, user can retry")
                // 用户可以重试，不关闭对话框
            }
        )
    }

    /**
     * 请求禁用生物识别快捷访问（需要生物识别验证）
     */
    fun requestDisableBiometricAccess() {
        _uiState.value = _uiState.value.copy(
            needsBiometricForDisable = true,
            biometricFailedCount = 0
        )
    }

    /**
     * 生物识别验证成功后禁用生物识别
     */
    fun performDisableBiometricAccess() {
        val success = passwordStorage.disableBiometricAccess()
        _uiState.value = _uiState.value.copy(
            isBiometricEnabled = !success,
            needsBiometricForDisable = false,
            showPasswordForDisable = false,
            backupMessage = if (success) "已禁用生物识别" else "禁用生物识别失败"
        )
    }

    /**
     * 生物识别验证失败，检查是否需要回退到密码输入
     */
    fun onBiometricFailedForDisable() {
        val failedCount = _uiState.value.biometricFailedCount + 1
        Log.i(TAG, "Biometric failed for disable, count: $failedCount")

        if (failedCount >= 3) {
            // 三次失败，回退到密码输入
            _uiState.value = _uiState.value.copy(
                needsBiometricForDisable = false,
                showPasswordForDisable = true,
                biometricFailedCount = 0
            )
        } else {
            // 更新失败计数，增加触发器ID以重新显示生物识别
            _uiState.value = _uiState.value.copy(
                biometricFailedCount = failedCount,
                biometricTriggerId = _uiState.value.biometricTriggerId + 1
            )
        }
    }

    /**
     * 密码验证关闭生物识别
     */
    fun disableBiometricWithPassword(password: String): Boolean {
        val savedPassword = passwordStorage.getPassword()
        if (savedPassword == password) {
            performDisableBiometricAccess()
            return true
        }
        return false
    }

    /**
     * 取消禁用生物识别
     */
    fun cancelDisableBiometric() {
        _uiState.value = _uiState.value.copy(
            needsBiometricForDisable = false,
            showPasswordForDisable = false,
            biometricFailedCount = 0
        )
    }

    /**
     * 保存备份密码（用于自动备份）
     */
    fun saveBackupPassword(password: String): Boolean {
        return passwordStorage.savePassword(password)
    }

    /**
     * 检查是否已设置备份密码
     */
    fun hasBackupPassword(): Boolean {
        return passwordStorage.hasPassword()
    }

    // ==================== 自动备份相关 ====================

    /**
     * 显示自动备份设置对话框
     */
    fun showAutoBackupSettingsDialog() {
        _uiState.value = _uiState.value.copy(showAutoBackupSettingsDialog = true)
    }

    /**
     * 关闭自动备份设置对话框
     */
    fun dismissAutoBackupSettingsDialog() {
        _uiState.value = _uiState.value.copy(showAutoBackupSettingsDialog = false)
    }

    /**
     * 保存自动备份设置
     */
    fun saveAutoBackupSettings(
        enabled: Boolean,
        frequency: AutoBackupScheduler.BackupFrequency,
        hour: Int,
        minute: Int
    ) {
        // 如果启用自动备份，检查是否设置了备份密码
        if (enabled && !passwordStorage.hasPassword()) {
            _uiState.value = _uiState.value.copy(
                showAutoBackupSettingsDialog = false,
                backupMessage = "请先设置备份密码"
            )
            return
        }

        val settings = AutoBackupScheduler.AutoBackupSettings(
            enabled = enabled,
            frequency = frequency,
            hour = hour,
            minute = minute
        )
        AutoBackupScheduler.saveSettings(getApplication(), settings)

        val nextBackupTime = AutoBackupScheduler.getNextBackupDescription(getApplication())
        _uiState.value = _uiState.value.copy(
            autoBackupEnabled = enabled,
            autoBackupFrequency = frequency,
            autoBackupHour = hour,
            autoBackupMinute = minute,
            autoBackupNextTime = nextBackupTime,
            showAutoBackupSettingsDialog = false,
            backupMessage = if (enabled) "已启用自动备份" else "已关闭自动备份"
        )
    }

    /**
     * 立即测试自动备份
     */
    fun testAutoBackup() {
        if (!passwordStorage.hasPassword()) {
            _uiState.value = _uiState.value.copy(
                backupMessage = "请先设置备份密码"
            )
            return
        }
        AutoBackupScheduler.runBackupNow(getApplication())
        _uiState.value = _uiState.value.copy(
            backupMessage = "已触发备份，请查看通知"
        )
    }

    /**
     * 获取自动备份文件列表（从下载目录）
     */
    fun getAutoBackupFiles(): List<String> {
        val context = getApplication<Application>().applicationContext
        val files = mutableListOf<String>()

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                // Android 10+ 使用 MediaStore 查询
                val resolver = context.contentResolver
                val projection = arrayOf(
                    android.provider.MediaStore.Downloads.DISPLAY_NAME,
                    android.provider.MediaStore.Downloads.DATE_ADDED
                )
                val selection = "${android.provider.MediaStore.Downloads.DISPLAY_NAME} LIKE ?"
                val selectionArgs = arrayOf("自动备份_%.zip")

                resolver.query(
                    android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    "${android.provider.MediaStore.Downloads.DATE_ADDED} DESC"
                )?.use { cursor ->
                    val nameColumn = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Downloads.DISPLAY_NAME)
                    while (cursor.moveToNext()) {
                        files.add(cursor.getString(nameColumn))
                    }
                }
            } else {
                // Android 9 及以下
                val downloadsDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS)
                downloadsDir.listFiles()?.forEach { file ->
                    if (file.isFile && file.name.startsWith("自动备份_") && file.name.endsWith(".zip")) {
                        files.add(file.name)
                    }
                }
                files.sortByDescending { name ->
                    val file = java.io.File(downloadsDir, name)
                    file.lastModified()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get backup files: ${e.message}")
        }

        return files
    }
}
