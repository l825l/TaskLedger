package com.ledger.task.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * 提醒广播接收器
 * 接收 AlarmManager 发送的提醒广播并显示通知
 */
class ReminderReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ReminderReceiver"

        const val EXTRA_TASK_ID = "task_id"
        const val EXTRA_TASK_TITLE = "task_title"
        const val EXTRA_REMINDER_TYPE = "reminder_type"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, 0)
        val taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "任务"
        val reminderType = intent.getStringExtra(EXTRA_REMINDER_TYPE) ?: ""

        Log.i(TAG, "Received reminder for task $taskId: $reminderType")

        if (taskId > 0) {
            val message = when (reminderType) {
                "15min" -> "任务将在15分钟后截止"
                "1hour" -> "任务将在1小时后截止"
                "1day" -> "任务将在1天后截止"
                else -> "任务即将截止"
            }

            NotificationHelper.showTaskReminder(
                context = context,
                taskId = taskId,
                title = taskTitle,
                message = message
            )
        }
    }
}
