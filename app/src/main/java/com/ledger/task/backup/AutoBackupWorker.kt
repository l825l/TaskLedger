package com.ledger.task.backup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ledger.task.R
import com.ledger.task.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 自动备份 Worker
 * 在后台执行备份任务
 */
class AutoBackupWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    companion object {
        private const val TAG = "AutoBackupWorker"
        private const val NOTIFICATION_CHANNEL_ID = "auto_backup_channel"
        private const val NOTIFICATION_ID = 1001
    }

    private val database: AppDatabase by inject()

    override suspend fun doWork(): Result {
        Log.i(TAG, "Starting auto backup doWork")

        return try {
            val context = applicationContext

            // 检查是否已设置备份密码
            val passwordStorage = BackupPasswordStorage(context)
            val password = passwordStorage.getPassword()

            if (password == null) {
                Log.w(TAG, "No backup password set, skipping auto backup")
                return Result.failure()
            }

            // 生成备份文件名
            val timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            )
            val fileName = "自动备份_$timestamp.zip"

            // 关闭数据库连接
            database.close()

            // 执行备份（保存到下载目录）
            val result = performBackupToDownloads(context, fileName, password)

            // 数据库会在下次访问时自动重新初始化

            if (result) {
                Log.i(TAG, "Auto backup completed: $fileName")

                // 发送通知
                sendBackupNotification(context, fileName, true)

                Result.success()
            } else {
                Log.e(TAG, "Auto backup failed")
                sendBackupNotification(context, fileName, false)
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Auto backup error: ${e.message}", e)
            Result.failure()
        }
    }

    /**
     * 执行备份到下载目录
     */
    private suspend fun performBackupToDownloads(
        context: Context,
        fileName: String,
        password: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val dbFile = context.getDatabasePath("task_ledger")

            if (!dbFile.exists()) {
                Log.e(TAG, "Database file not found")
                return@withContext false
            }

            // 先写入临时文件
            val tempFile = File(context.cacheDir, fileName)

            java.util.zip.ZipOutputStream(tempFile.outputStream()).use { zipOut ->
                // 1. 备份数据库文件
                java.io.FileInputStream(dbFile).use { input ->
                    zipOut.putNextEntry(java.util.zip.ZipEntry("database/task_ledger"))
                    input.copyTo(zipOut)
                    zipOut.closeEntry()
                }
                Log.i(TAG, "Added database to backup")

                // 2. 备份加密密钥（使用密码加密）
                val encryptionKey = com.ledger.task.data.local.DatabaseKeyManager.getOrCreateKey(context)
                val encryptedKey = BackupPasswordProtection.encrypt(password, encryptionKey)
                zipOut.putNextEntry(java.util.zip.ZipEntry("keys/db_key_encrypted.bin"))
                zipOut.write(encryptedKey)
                zipOut.closeEntry()
                Log.i(TAG, "Added encrypted key to backup")

                // 3. 备份图片缓存目录
                val imageCacheDir = File(context.cacheDir, "export_images")
                if (imageCacheDir.exists()) {
                    imageCacheDir.listFiles()?.forEach { file ->
                        if (file.isFile) {
                            java.io.FileInputStream(file).use { input ->
                                zipOut.putNextEntry(java.util.zip.ZipEntry("images/${file.name}"))
                                input.copyTo(zipOut)
                                zipOut.closeEntry()
                            }
                        }
                    }
                    Log.i(TAG, "Added images to backup")
                }

                // 4. 备份元数据
                val metadata = buildString {
                    appendLine("version=4")
                    appendLine("timestamp=${System.currentTimeMillis()}")
                    appendLine("database=task_ledger")
                    appendLine("encrypted=true")
                    appendLine("keyProtection=encrypted")
                    appendLine("passwordHint=自动备份")
                    appendLine("hasRecoveryCode=false")
                }
                zipOut.putNextEntry(java.util.zip.ZipEntry("metadata.txt"))
                zipOut.write(metadata.toByteArray(Charsets.UTF_8))
                zipOut.closeEntry()
            }

            // 保存到下载目录
            val savedToDownloads = saveToDownloadsDirectory(context, tempFile, fileName)

            // 清理临时文件
            tempFile.delete()

            // 清理旧备份（保留最近 7 个）
            if (savedToDownloads) {
                cleanupOldBackups(context)
            }

            savedToDownloads
        } catch (e: Exception) {
            Log.e(TAG, "Failed to perform backup: ${e.message}", e)
            false
        }
    }

    /**
     * 保存文件到下载目录
     */
    private fun saveToDownloadsDirectory(context: Context, sourceFile: File, fileName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 使用 MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/zip")
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        sourceFile.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    // 标记文件不再处于待处理状态
                    contentValues.clear()
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)

                    Log.i(TAG, "Saved backup to Downloads: $fileName")
                    true
                } else {
                    Log.e(TAG, "Failed to create entry in MediaStore")
                    false
                }
            } else {
                // Android 9 及以下使用传统方式
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val destFile = File(downloadsDir, fileName)
                sourceFile.copyTo(destFile, overwrite = true)
                Log.i(TAG, "Saved backup to Downloads: ${destFile.absolutePath}")
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save to Downloads: ${e.message}", e)
            false
        }
    }

    /**
     * 清理旧备份
     */
    private fun cleanupOldBackups(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 使用 MediaStore 查询
                val resolver = context.contentResolver
                val projection = arrayOf(
                    MediaStore.Downloads._ID,
                    MediaStore.Downloads.DISPLAY_NAME,
                    MediaStore.Downloads.DATE_ADDED
                )
                val selection = "${MediaStore.Downloads.DISPLAY_NAME} LIKE ?"
                val selectionArgs = arrayOf("自动备份_%.zip")

                val files = mutableListOf<Pair<Long, String>>()

                resolver.query(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    "${MediaStore.Downloads.DATE_ADDED} DESC"
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID)
                    val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        files.add(id to name)
                    }
                }

                // 删除超过 7 个的旧备份
                if (files.size > 7) {
                    files.drop(7).forEach { (id, name) ->
                        val uri = android.net.Uri.withAppendedPath(
                            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                            id.toString()
                        )
                        resolver.delete(uri, null, null)
                        Log.d(TAG, "Deleted old backup: $name")
                    }
                }
            } else {
                // Android 9 及以下
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val files = downloadsDir.listFiles()
                    ?.filter { it.name.startsWith("自动备份_") && it.name.endsWith(".zip") }
                    ?.sortedByDescending { it.lastModified() }
                    ?: return

                if (files.size > 7) {
                    files.drop(7).forEach { file ->
                        file.delete()
                        Log.d(TAG, "Deleted old backup: ${file.name}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cleanup old backups: ${e.message}")
        }
    }

    /**
     * 发送备份通知
     */
    private fun sendBackupNotification(context: Context, fileName: String, success: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 创建通知渠道
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "自动备份",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "自动备份完成通知"
        }
        notificationManager.createNotificationChannel(channel)

        val title = if (success) "自动备份完成" else "自动备份失败"
        val message = if (success) "备份已保存到下载目录：$fileName" else "请检查备份设置"

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
