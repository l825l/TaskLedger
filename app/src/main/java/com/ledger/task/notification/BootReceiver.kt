package com.ledger.task.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ledger.task.TaskLedgerApp
import com.ledger.task.backup.AutoBackupScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 开机自启动接收器
 * 设备启动后重新调度所有未完成任务的提醒和自动备份
 */
class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i(TAG, "Boot completed, rescheduling reminders and backups")

            // 使用 goAsync() 正确管理 BroadcastReceiver 中的异步操作
            val pendingResult = goAsync()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = (context.applicationContext as TaskLedgerApp).repository
                    val tasks = repository.getAllNow()

                    tasks.forEach { task ->
                        // 只为未完成的任务重新调度提醒
                        if (task.status != com.ledger.task.data.model.TaskStatus.DONE) {
                            val now = java.time.LocalDateTime.now()
                            // 只为截止时间在未来的任务调度
                            if (task.deadline.isAfter(now)) {
                                ReminderManager.scheduleReminder(
                                    context = context,
                                    taskId = task.id,
                                    title = task.title,
                                    deadline = task.deadline
                                )
                            }
                        }
                    }

                    Log.i(TAG, "Rescheduled ${tasks.size} task reminders")

                    // 重新调度自动备份
                    AutoBackupScheduler.rescheduleBackup(context)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to reschedule: ${e.message}")
                } finally {
                    // 确保在完成时调用 finish()
                    pendingResult.finish()
                }
            }
        }
    }
}
