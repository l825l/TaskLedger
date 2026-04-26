package com.ledger.task.backup

import android.content.Context
import android.net.Uri
import android.util.Log
import com.ledger.task.data.local.AppDatabase
import com.ledger.task.data.local.DatabaseKeyManager
import com.ledger.task.backup.BackupPasswordProtection.MIN_PASSWORD_LENGTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * 备份创建结果
 */
data class BackupResult(
    val success: Boolean
)

/**
 * 备份管理器
 * 负责打包数据库和图片附件为 zip 文件
 */
object BackupManager : KoinComponent {

    private const val TAG = "BackupManager"
    private const val DATABASE_NAME = "task_ledger"

    private val database: AppDatabase by inject()

    /**
     * 创建备份
     * @param context 上下文
     * @param outputUri 输出文件 URI
     * @param password 备份密码（可选，用于加密数据库密钥）
     * @param passwordHint 密码提示（可选）
     * @return 备份结果
     */
    suspend fun createBackup(
        context: Context,
        outputUri: Uri,
        password: String? = null,
        passwordHint: String? = null
    ): BackupResult = withContext(Dispatchers.IO) {
        try {
            val resolver = context.contentResolver
            val outputStream = resolver.openOutputStream(outputUri) ?: return@withContext BackupResult(false)

            // 关闭数据库连接，确保所有数据写入磁盘
            database.close()
            Log.i(TAG, "Database closed for backup")

            ZipOutputStream(outputStream).use { zipOut ->
                // 1. 备份数据库文件
                val dbFile = context.getDatabasePath(DATABASE_NAME)
                if (dbFile.exists()) {
                    addToZip(zipOut, "database/$DATABASE_NAME", dbFile)
                    Log.i(TAG, "Added database to backup")
                }

                // 2. 备份加密密钥（使用密码保护）
                val encryptionKey = DatabaseKeyManager.getOrCreateKey(context)
                val keyData = if (password != null && password.length >= MIN_PASSWORD_LENGTH) {
                    // 使用密码加密密钥
                    val encryptedKey = BackupPasswordProtection.encrypt(password, encryptionKey)
                    zipOut.putNextEntry(ZipEntry("keys/db_key_encrypted.bin"))
                    zipOut.write(encryptedKey)
                    zipOut.closeEntry()
                    Log.i(TAG, "Added password-protected encryption key to backup")
                    "encrypted"
                } else {
                    // 无密码保护（向后兼容）
                    zipOut.putNextEntry(ZipEntry("keys/db_key.bin"))
                    zipOut.write(encryptionKey)
                    zipOut.closeEntry()
                    Log.w(TAG, "Added encryption key to backup without password protection")
                    "plain"
                }

                // 3. 备份图片缓存目录
                val imageCacheDir = File(context.cacheDir, "export_images")
                if (imageCacheDir.exists()) {
                    imageCacheDir.listFiles()?.forEach { file ->
                        if (file.isFile) {
                            addToZip(zipOut, "images/${file.name}", file)
                        }
                    }
                    Log.i(TAG, "Added ${imageCacheDir.listFiles()?.size ?: 0} images to backup")
                }

                // 4. 备份元数据
                val metadata = buildString {
                    appendLine("version=4")
                    appendLine("timestamp=${System.currentTimeMillis()}")
                    appendLine("database=$DATABASE_NAME")
                    appendLine("encrypted=true")
                    appendLine("keyProtection=$keyData")
                    if (passwordHint != null && passwordHint.isNotBlank()) {
                        appendLine("passwordHint=$passwordHint")
                    }
                }
                val metadataBytes = metadata.toByteArray(Charsets.UTF_8)
                zipOut.putNextEntry(ZipEntry("metadata.txt"))
                zipOut.write(metadataBytes)
                zipOut.closeEntry()
            }

            // 数据库会在下次访问时自动重新初始化
            Log.i(TAG, "Backup created successfully")
            BackupResult(true)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create backup: ${e.message}", e)
            BackupResult(false)
        }
    }

    /**
     * 添加文件到 zip
     */
    private fun addToZip(zipOut: ZipOutputStream, entryName: String, file: File) {
        FileInputStream(file).use { input ->
            zipOut.putNextEntry(ZipEntry(entryName))
            input.copyTo(zipOut)
            zipOut.closeEntry()
        }
    }

    /**
     * 获取备份信息
     */
    suspend fun getBackupInfo(context: Context, uri: Uri): BackupInfo? = withContext(Dispatchers.IO) {
        try {
            val resolver = context.contentResolver
            val inputStream = resolver.openInputStream(uri) ?: return@withContext null

            java.util.zip.ZipInputStream(inputStream).use { zipIn ->
                var entry = zipIn.nextEntry
                while (entry != null) {
                    if (entry.name == "metadata.txt") {
                        val metadata = String(zipIn.readBytes(), Charsets.UTF_8)
                        val lines = metadata.lines()
                        val version = lines.find { it.startsWith("version=") }?.substringAfter("=") ?: "1"
                        val timestamp = lines.find { it.startsWith("timestamp=") }?.substringAfter("=")?.toLongOrNull() ?: 0L
                        val keyProtection = lines.find { it.startsWith("keyProtection=") }?.substringAfter("=") ?: "plain"
                        val passwordHint = lines.find { it.startsWith("passwordHint=") }?.substringAfter("=")
                        return@withContext BackupInfo(
                            version = version,
                            timestamp = timestamp,
                            isValid = true,
                            isPasswordProtected = keyProtection == "encrypted",
                            passwordHint = passwordHint
                        )
                    }
                    entry = zipIn.nextEntry
                }
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read backup info: ${e.message}")
            null
        }
    }
}

/**
 * 备份信息
 */
data class BackupInfo(
    val version: String,
    val timestamp: Long,
    val isValid: Boolean,
    val isPasswordProtected: Boolean = false,
    val passwordHint: String? = null
)
