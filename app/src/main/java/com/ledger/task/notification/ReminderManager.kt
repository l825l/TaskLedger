package com.ledger.task.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 提醒管理器
 * 负责调度和取消任务提醒
 */
object ReminderManager {

    private const val TAG = "ReminderManager"

    // 设置相关常量
    private const val PREFS_NAME = "reminder_settings"
    private const val KEY_REMINDER_ENABLED = "reminder_enabled"

    // 提醒时间点（截止前）
    private const val REMINDER_15_MIN = 15 * 60 * 1000L      // 15分钟
    private const val REMINDER_1_HOUR = 60 * 60 * 1000L      // 1小时
    private const val REMINDER_1_DAY = 24 * 60 * 60 * 1000L  // 1天

    // ==================== 设置管理 ====================

    /**
     * 检查提醒是否启用
     */
    fun isReminderEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_REMINDER_ENABLED, true)  // 默认启用
    }

    /**
     * 设置提醒开关
     * @param context 上下文
     * @param enabled 是否启用
     * @param pendingTasks 待处理的任务列表（用于关闭时取消或开启时调度）
     */
    fun setReminderEnabled(
        context: Context,
        enabled: Boolean,
        pendingTasks: List<Task> = emptyList()
    ) {
        // 保存设置
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_REMINDER_ENABLED, enabled)
            .apply()

        if (!enabled) {
            // 关闭提醒时，取消所有现有提醒
            cancelAllReminders(context, pendingTasks)
        } else {
            // 开启提醒时，为所有未完成任务重新调度
            scheduleAllReminders(context, pendingTasks)
        }

        Log.i(TAG, "Reminder enabled set to: $enabled")
    }

    /**
     * 取消所有任务的提醒
     */
    private fun cancelAllReminders(
        context: Context,
        tasks: List<Task>
    ) {
        tasks.forEach { task ->
            cancelReminder(context, task.id)
        }
        Log.i(TAG, "Cancelled all ${tasks.size} task reminders")
    }

    /**
     * 为所有未完成任务调度提醒
     */
    private fun scheduleAllReminders(
        context: Context,
        tasks: List<Task>
    ) {
        val now = LocalDateTime.now()
        tasks.forEach { task ->
            // 只为截止时间在未来的任务调度
            if (task.deadline.isAfter(now)) {
                scheduleReminder(
                    context = context,
                    taskId = task.id,
                    title = task.title,
                    deadline = task.deadline
                )
            }
        }
        Log.i(TAG, "Scheduled reminders for ${tasks.size} tasks")
    }

    /**
     * 条件调度（检查开关状态）
     */
    fun scheduleReminderIfEnabled(
        context: Context,
        taskId: Long,
        title: String,
        deadline: LocalDateTime
    ) {
        if (!isReminderEnabled(context)) {
            Log.d(TAG, "Reminder disabled, skipping schedule for task $taskId")
            return
        }
        scheduleReminder(context, taskId, title, deadline)
    }

    // ==================== 提醒调度 ====================

    /**
     * 调度任务提醒
     * @param context 上下文
     * @param taskId 任务ID
     * @param title 任务标题
     * @param deadline 截止时间
     */
    fun scheduleReminder(
        context: Context,
        taskId: Long,
        title: String,
        deadline: LocalDateTime
    ) {
        if (taskId <= 0) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val deadlineMillis = deadline.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val now = System.currentTimeMillis()

        // 调度15分钟前提醒
        scheduleAlarm(
            context = context,
            alarmManager = alarmManager,
            taskId = taskId,
            title = title,
            triggerTime = deadlineMillis - REMINDER_15_MIN,
            reminderType = "15min",
            requestCode = taskId.toInt() * 10 + 1
        )

        // 调度1小时前提醒
        scheduleAlarm(
            context = context,
            alarmManager = alarmManager,
            taskId = taskId,
            title = title,
            triggerTime = deadlineMillis - REMINDER_1_HOUR,
            reminderType = "1hour",
            requestCode = taskId.toInt() * 10 + 2
        )

        // 调度1天前提醒
        scheduleAlarm(
            context = context,
            alarmManager = alarmManager,
            taskId = taskId,
            title = title,
            triggerTime = deadlineMillis - REMINDER_1_DAY,
            reminderType = "1day",
            requestCode = taskId.toInt() * 10 + 3
        )

        Log.i(TAG, "Scheduled reminders for task $taskId")
    }

    /**
     * 调度单个闹钟
     */
    private fun scheduleAlarm(
        context: Context,
        alarmManager: AlarmManager,
        taskId: Long,
        title: String,
        triggerTime: Long,
        reminderType: String,
        requestCode: Int
    ) {
        // 如果提醒时间已过，不调度
        if (triggerTime <= System.currentTimeMillis()) {
            Log.d(TAG, "Skipping past reminder: $reminderType for task $taskId")
            return
        }

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_TASK_ID, taskId)
            putExtra(ReminderReceiver.EXTRA_TASK_TITLE, title)
            putExtra(ReminderReceiver.EXTRA_REMINDER_TYPE, reminderType)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    // 没有精确闹钟权限，使用非精确闹钟
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            Log.d(TAG, "Scheduled $reminderType reminder at $triggerTime for task $taskId")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to schedule alarm: ${e.message}")
        }
    }

    /**
     * 取消任务提醒
     */
    fun cancelReminder(context: Context, taskId: Long) {
        if (taskId <= 0) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 取消所有提醒
        for (i in 1..3) {
            val requestCode = taskId.toInt() * 10 + i
            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }

        // 取消通知
        NotificationHelper.cancelNotification(context, taskId)

        Log.i(TAG, "Cancelled reminders for task $taskId")
    }
}
