package com.ledger.task.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ledger.task.MainActivity
import com.ledger.task.R

/**
 * 通知助手
 * 负责创建通知渠道和发送任务提醒通知
 */
object NotificationHelper {

    private const val CHANNEL_ID = "task_reminder"
    private const val CHANNEL_NAME = "任务提醒"
    private const val CHANNEL_DESCRIPTION = "任务截止时间提醒通知"

    /**
     * 创建通知渠道（Android 8.0+）
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * 发送任务提醒通知
     */
    fun showTaskReminder(
        context: Context,
        taskId: Long,
        title: String,
        message: String
    ) {
        // 点击通知时打开应用
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("taskId", taskId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(taskId.toInt(), notification)
        } catch (e: SecurityException) {
            // Android 13+ 需要请求通知权限
            android.util.Log.e("NotificationHelper", "Failed to show notification: ${e.message}")
        }
    }

    /**
     * 取消任务提醒通知
     */
    fun cancelNotification(context: Context, taskId: Long) {
        NotificationManagerCompat.from(context).cancel(taskId.toInt())
    }
}
