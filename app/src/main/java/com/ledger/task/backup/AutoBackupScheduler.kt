package com.ledger.task.backup

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * 自动备份调度器
 * 使用 WorkManager 实现定时备份
 */
object AutoBackupScheduler {

    private const val TAG = "AutoBackupScheduler"
    const val WORK_NAME_DAILY = "auto_backup_daily"
    const val WORK_NAME_WEEKLY = "auto_backup_weekly"

    // 存储设置的 SharedPreferences
    private const val PREFS_NAME = "auto_backup_settings"
    private const val KEY_ENABLED = "auto_backup_enabled"
    private const val KEY_FREQUENCY = "backup_frequency" // "daily" or "weekly"
    private const val KEY_HOUR = "backup_hour" // 0-23
    private const val KEY_MINUTE = "backup_minute" // 0-59

    /**
     * 备份频率
     */
    enum class BackupFrequency(val displayName: String) {
        DAILY("每天"),
        WEEKLY("每周")
    }

    /**
     * 自动备份设置
     */
    data class AutoBackupSettings(
        val enabled: Boolean = false,
        val frequency: BackupFrequency = BackupFrequency.DAILY,
        val hour: Int = 2,       // 默认凌晨 2 点
        val minute: Int = 0
    )

    /**
     * 获取自动备份设置
     */
    fun getSettings(context: Context): AutoBackupSettings {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val frequencyStr = prefs.getString(KEY_FREQUENCY, "daily") ?: "daily"
        return AutoBackupSettings(
            enabled = prefs.getBoolean(KEY_ENABLED, false),
            frequency = if (frequencyStr == "weekly") BackupFrequency.WEEKLY else BackupFrequency.DAILY,
            hour = prefs.getInt(KEY_HOUR, 2),
            minute = prefs.getInt(KEY_MINUTE, 0)
        )
    }

    /**
     * 保存自动备份设置
     */
    fun saveSettings(context: Context, settings: AutoBackupSettings) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ENABLED, settings.enabled)
            .putString(KEY_FREQUENCY,
                if (settings.frequency == BackupFrequency.WEEKLY) "weekly" else "daily")
            .putInt(KEY_HOUR, settings.hour)
            .putInt(KEY_MINUTE, settings.minute)
            .apply()

        // 更新调度
        if (settings.enabled) {
            scheduleBackup(context, settings)
        } else {
            cancelBackup(context)
        }
    }

    /**
     * 调度自动备份
     */
    fun scheduleBackup(context: Context, settings: AutoBackupSettings = getSettings(context)) {
        val workManager = WorkManager.getInstance(context)

        // 计算到下次备份时间的延迟
        val now = LocalDateTime.now()
        val backupTime = LocalTime.of(settings.hour, settings.minute)
        var nextBackup = now.with(backupTime)

        // 如果今天的备份时间已过，从明天开始
        if (nextBackup.isBefore(now) || nextBackup.isEqual(now)) {
            nextBackup = nextBackup.plusDays(1)
        }

        // 周备份：调整到下一个周日
        if (settings.frequency == BackupFrequency.WEEKLY) {
            while (nextBackup.dayOfWeek.value != 7) { // 7 = Sunday
                nextBackup = nextBackup.plusDays(1)
            }
        }

        val initialDelay = Duration.between(now, nextBackup).toMinutes()
        val repeatInterval = if (settings.frequency == BackupFrequency.WEEKLY) {
            7L * 24 * 60 // 7 天（分钟）
        } else {
            24L * 60 // 1 天（分钟）
        }

        Log.i(TAG, "Scheduling backup: initialDelay=${initialDelay}min, repeatInterval=${repeatInterval}min")

        // 取消现有的定时任务
        workManager.cancelUniqueWork(WORK_NAME_DAILY)
        workManager.cancelUniqueWork(WORK_NAME_WEEKLY)

        // 创建新的定时任务
        val workName = if (settings.frequency == BackupFrequency.WEEKLY) WORK_NAME_WEEKLY else WORK_NAME_DAILY

        val workRequest = PeriodicWorkRequestBuilder<AutoBackupWorker>(
            repeatInterval, TimeUnit.MINUTES
        )
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .addTag("auto_backup")
            .build()

        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )

        Log.i(TAG, "Auto backup scheduled: $workName at ${settings.hour}:${settings.minute}")
    }

    /**
     * 取消自动备份
     */
    fun cancelBackup(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(WORK_NAME_DAILY)
        workManager.cancelUniqueWork(WORK_NAME_WEEKLY)
        workManager.cancelAllWorkByTag("auto_backup")
        Log.i(TAG, "Auto backup cancelled")
    }

    /**
     * 重新调度备份（开机后调用）
     */
    fun rescheduleBackup(context: Context) {
        val settings = getSettings(context)
        if (settings.enabled) {
            scheduleBackup(context, settings)
            Log.i(TAG, "Auto backup rescheduled after boot")
        }
    }

    /**
     * 获取下次备份时间描述
     */
    fun getNextBackupDescription(context: Context): String {
        val settings = getSettings(context)
        if (!settings.enabled) return "未启用"

        val now = LocalDateTime.now()
        val backupTime = LocalTime.of(settings.hour, settings.minute)
        var nextBackup = now.with(backupTime)

        if (nextBackup.isBefore(now) || nextBackup.isEqual(now)) {
            nextBackup = nextBackup.plusDays(1)
        }

        if (settings.frequency == BackupFrequency.WEEKLY) {
            while (nextBackup.dayOfWeek.value != 7) {
                nextBackup = nextBackup.plusDays(1)
            }
        }

        val timeStr = String.format("%02d:%02d", settings.hour, settings.minute)
        return when {
            settings.frequency == BackupFrequency.WEEKLY -> {
                "每周日 $timeStr"
            }
            nextBackup.dayOfYear == now.dayOfYear -> {
                "今天 $timeStr"
            }
            else -> {
                "每天 $timeStr"
            }
        }
    }

    /**
     * 立即执行一次备份（用于测试）
     */
    fun runBackupNow(context: Context) {
        val workManager = WorkManager.getInstance(context)

        val workRequest = OneTimeWorkRequestBuilder<AutoBackupWorker>()
            .addTag("auto_backup_test")
            .build()

        workManager.enqueue(workRequest)
        Log.i(TAG, "Triggered immediate backup test")
    }
}
