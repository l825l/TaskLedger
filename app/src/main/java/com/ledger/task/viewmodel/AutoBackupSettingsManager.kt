package com.ledger.task.viewmodel

import android.app.Application
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.ledger.task.backup.AutoBackupScheduler
import com.ledger.task.backup.BackupPasswordStorage

/**
 * 自动备份设置管理器
 * 负责自动备份设置的管理和测试
 */
class AutoBackupSettingsManager(
    private val application: Application,
    private val getState: () -> LedgerCenterUiState,
    private val updateState: (LedgerCenterUiState) -> Unit
) {
    companion object {
        private const val TAG = "AutoBackupSettingsManager"
    }

    private val passwordStorage = BackupPasswordStorage(application)

    /**
     * 加载自动备份设置
     */
    fun loadAutoBackupSettings() {
        val autoBackupSettings = AutoBackupScheduler.getSettings(application)
        val nextBackupTime = AutoBackupScheduler.getNextBackupDescription(application)

        updateState(getState().copy(
            autoBackupEnabled = autoBackupSettings.enabled,
            autoBackupFrequency = autoBackupSettings.frequency,
            autoBackupHour = autoBackupSettings.hour,
            autoBackupMinute = autoBackupSettings.minute,
            autoBackupNextTime = nextBackupTime
        ))
    }

    /**
     * 显示自动备份设置对话框
     */
    fun showAutoBackupSettingsDialog() {
        updateState(getState().copy(showAutoBackupSettingsDialog = true))
    }

    /**
     * 关闭自动备份设置对话框
     */
    fun dismissAutoBackupSettingsDialog() {
        updateState(getState().copy(showAutoBackupSettingsDialog = false))
    }

    /**
     * 保存自动备份设置
     */
    fun saveAutoBackupSettings(
        enabled: Boolean,
        frequency: AutoBackupScheduler.BackupFrequency,
        hour: Int,
        minute: Int
    ): Boolean {
        // 如果启用自动备份，检查是否设置了备份密码
        if (enabled && !passwordStorage.hasPassword()) {
            updateState(getState().copy(
                showAutoBackupSettingsDialog = false,
                backupMessage = "请先设置备份密码"
            ))
            return false
        }

        val settings = AutoBackupScheduler.AutoBackupSettings(
            enabled = enabled,
            frequency = frequency,
            hour = hour,
            minute = minute
        )
        AutoBackupScheduler.saveSettings(application, settings)

        val nextBackupTime = AutoBackupScheduler.getNextBackupDescription(application)
        updateState(getState().copy(
            autoBackupEnabled = enabled,
            autoBackupFrequency = frequency,
            autoBackupHour = hour,
            autoBackupMinute = minute,
            autoBackupNextTime = nextBackupTime,
            showAutoBackupSettingsDialog = false,
            backupMessage = if (enabled) "已启用自动备份" else "已关闭自动备份"
        ))
        return true
    }

    /**
     * 立即测试自动备份
     */
    fun testAutoBackup(): Boolean {
        if (!passwordStorage.hasPassword()) {
            updateState(getState().copy(backupMessage = "请先设置备份密码"))
            return false
        }
        AutoBackupScheduler.runBackupNow(application)
        updateState(getState().copy(backupMessage = "已触发备份，请查看通知"))
        return true
    }

    /**
     * 获取自动备份文件列表（从下载目录）
     */
    fun getAutoBackupFiles(): List<String> {
        val context = application.applicationContext
        val files = mutableListOf<String>()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 使用 MediaStore 查询
                val resolver = context.contentResolver
                val projection = arrayOf(
                    MediaStore.Downloads.DISPLAY_NAME,
                    MediaStore.Downloads.DATE_ADDED
                )
                val selection = "${MediaStore.Downloads.DISPLAY_NAME} LIKE ?"
                val selectionArgs = arrayOf("自动备份_%.zip")

                resolver.query(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    "${MediaStore.Downloads.DATE_ADDED} DESC"
                )?.use { cursor ->
                    val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
                    while (cursor.moveToNext()) {
                        files.add(cursor.getString(nameColumn))
                    }
                }
            } else {
                // Android 9 及以下
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
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
