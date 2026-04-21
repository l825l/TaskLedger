package com.ledger.task.viewmodel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.ui.util.LedgerExporter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 导出文件信息
 */
data class ExportFileInfo(
    val uri: Uri,
    val fileName: String,
    val exportTime: Long
)

/**
 * 台账导出管理器
 * 负责导出、分享和导出历史管理
 */
class LedgerExportManager(
    private val application: Application,
    private val getState: () -> LedgerCenterUiState,
    private val updateState: (LedgerCenterUiState) -> Unit,
    private val getRelatedTaskSummaries: suspend (List<Task>) -> Map<Long, String>,
    private val repository: TaskRepository
) {
    companion object {
        private const val TAG = "LedgerExportManager"
        private const val PREFS_NAME = "export_history"
        private const val KEY_HISTORY = "history"
        private const val MAX_HISTORY = 50 // 每页5个，最多10页
    }

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * 加载导出历史
     */
    fun loadExportHistory() {
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
            updateState(getState().copy(exportHistory = mergedHistory))
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
            val context = application.applicationContext
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

    /**
     * 导出为 CSV（在 IO 线程执行）
     */
    fun exportCSV(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState(getState().copy(isExporting = true))

            try {
                val tasks = getState().tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = application.applicationContext

                val uri = LedgerExporter.exportToCSV(
                    context = context,
                    tasks = tasks,
                    relatedTaskSummaries = relatedSummaries
                )

                withContext(Dispatchers.Main) {
                    if (uri != null) {
                        val newHistory = addToExportHistory(uri, "csv")
                        updateState(getState().copy(
                            isExporting = false,
                            lastExportUri = uri,
                            exportMessage = "CSV 导出成功",
                            exportHistory = newHistory
                        ))
                        Log.d(TAG, "CSV export done, history size: ${newHistory.size}")
                    } else {
                        updateState(getState().copy(
                            isExporting = false,
                            exportMessage = "CSV 导出失败"
                        ))
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "CSV export failed", e)
                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    ))
                }
            }
        }
    }

    /**
     * 导出为 Excel（在 IO 线程执行）
     */
    fun exportExcel(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState(getState().copy(isExporting = true))

            try {
                val tasks = getState().tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = application.applicationContext

                val uri = LedgerExporter.exportToExcel(
                    context = context,
                    tasks = tasks,
                    relatedTaskSummaries = relatedSummaries
                )

                withContext(Dispatchers.Main) {
                    if (uri != null) {
                        val newHistory = addToExportHistory(uri, "xls")
                        updateState(getState().copy(
                            isExporting = false,
                            lastExportUri = uri,
                            exportMessage = "Excel 导出成功",
                            exportHistory = newHistory
                        ))
                        Log.d(TAG, "Excel export done, history size: ${newHistory.size}")
                    } else {
                        updateState(getState().copy(
                            isExporting = false,
                            exportMessage = "Excel 导出失败"
                        ))
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Excel export failed", e)
                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    ))
                }
            }
        }
    }

    /**
     * 添加到导出历史
     */
    private fun addToExportHistory(uri: Uri, extension: String): List<ExportFileInfo> {
        val currentHistory = getState().exportHistory.toMutableList()
        // 生成完整文件名
        val timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
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
        val context = application.applicationContext
        LedgerExporter.shareToWechat(context, uri)
    }

    /**
     * 分享到邮件
     */
    fun shareToEmail(uri: Uri) {
        val context = application.applicationContext
        LedgerExporter.shareToEmail(context, uri)
    }

    /**
     * 导出并分享到微信
     */
    fun exportAndShareWechat(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState(getState().copy(isExporting = true))
            try {
                val tasks = getState().tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = application.applicationContext

                val uri = LedgerExporter.exportToExcel(context, tasks, relatedSummaries)
                if (uri != null) {
                    withContext(Dispatchers.Main) {
                        updateState(getState().copy(
                            isExporting = false,
                            lastExportUri = uri
                        ))
                        LedgerExporter.shareToWechat(context, uri)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        updateState(getState().copy(
                            isExporting = false,
                            exportMessage = "导出失败"
                        ))
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Export and share failed", e)
                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    ))
                }
            }
        }
    }

    /**
     * 导出并分享到邮件
     */
    fun exportAndShareEmail(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState(getState().copy(isExporting = true))
            try {
                val tasks = getState().tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = application.applicationContext

                val uri = LedgerExporter.exportToExcel(context, tasks, relatedSummaries)
                if (uri != null) {
                    withContext(Dispatchers.Main) {
                        updateState(getState().copy(
                            isExporting = false,
                            lastExportUri = uri
                        ))
                        LedgerExporter.shareToEmail(context, uri)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        updateState(getState().copy(
                            isExporting = false,
                            exportMessage = "导出失败"
                        ))
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Export and share email failed", e)
                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    ))
                }
            }
        }
    }

    /**
     * 导出为 CSV 并分享
     */
    fun exportAndShareCSV(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState(getState().copy(isExporting = true))
            try {
                val tasks = getState().tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = application.applicationContext

                val uri = LedgerExporter.exportToCSV(context, tasks, relatedSummaries)
                if (uri != null) {
                    val newHistory = addToExportHistory(uri, "csv")
                    withContext(Dispatchers.Main) {
                        updateState(getState().copy(
                            isExporting = false,
                            lastExportUri = uri,
                            exportHistory = newHistory
                        ))
                        LedgerExporter.share(context, uri, "text/csv")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        updateState(getState().copy(
                            isExporting = false,
                            exportMessage = "导出失败"
                        ))
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Export and share CSV failed", e)
                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    ))
                }
            }
        }
    }

    /**
     * 导出为 Excel 并分享
     */
    fun exportAndShareExcel(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState(getState().copy(isExporting = true))
            try {
                val tasks = getState().tasks
                val relatedSummaries = getRelatedTaskSummaries(tasks)
                val context = application.applicationContext

                val uri = LedgerExporter.exportToExcel(context, tasks, relatedSummaries)
                if (uri != null) {
                    val newHistory = addToExportHistory(uri, "xls")
                    withContext(Dispatchers.Main) {
                        updateState(getState().copy(
                            isExporting = false,
                            lastExportUri = uri,
                            exportHistory = newHistory
                        ))
                        LedgerExporter.share(context, uri, "application/vnd.ms-excel")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        updateState(getState().copy(
                            isExporting = false,
                            exportMessage = "导出失败"
                        ))
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Export and share Excel failed", e)
                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isExporting = false,
                        exportMessage = "导出失败: ${e.message}"
                    ))
                }
            }
        }
    }

    /**
     * 导出并分享到其他应用（保留旧方法兼容）
     */
    fun exportAndShare(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        exportAndShareExcel(viewModelScope)
    }

    /**
     * 分享已导出的文件
     */
    fun shareExistingFile(uri: Uri) {
        val context = application.applicationContext
        LedgerExporter.share(context, uri, "*/*")
    }

    /**
     * 清除导出消息
     */
    fun clearExportMessage() {
        updateState(getState().copy(exportMessage = null))
    }

    /**
     * 打开导出文件所在位置
     */
    fun openExportFileLocation() {
        val context = application.applicationContext
        LedgerExporter.openFileLocation(context)
    }
}
