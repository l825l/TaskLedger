package com.ledger.task.backup

import android.content.Context
import android.net.Uri
import android.util.Log
import com.ledger.task.TaskLedgerApp
import com.ledger.task.data.local.DatabaseKeyManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipInputStream

/**
 * 恢复管理器
 * 负责从 zip 文件恢复数据库和图片附件
 */
object RestoreManager {

    private const val TAG = "RestoreManager"
    private const val DATABASE_NAME = "task_ledger"

    /**
     * 从备份恢复
     * @param context 上下文
     * @param backupUri 备份文件 URI
     * @param password 备份密码（如果备份有密码保护）
     * @return 是否成功
     */
    suspend fun restoreFromBackup(
        context: Context,
        backupUri: Uri,
        password: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val app = context.applicationContext as TaskLedgerApp
            val resolver = context.contentResolver
            val inputStream = resolver.openInputStream(backupUri) ?: return@withContext false

            // 关闭数据库连接
            app.database.close()

            var encryptionKey: ByteArray? = null
            var encryptedKeyData: ByteArray? = null
            val dbFile = context.getDatabasePath(DATABASE_NAME)

            // 第一遍：读取所有内容到内存
            ZipInputStream(inputStream).use { zipIn ->
                var entry = zipIn.nextEntry
                while (entry != null) {
                    when {
                        // 读取加密的密钥
                        entry.name == "keys/db_key_encrypted.bin" -> {
                            encryptedKeyData = zipIn.readBytes()
                            Log.i(TAG, "Read encrypted key from backup")
                        }

                        // 读取未加密的密钥（向后兼容）
                        entry.name == "keys/db_key.bin" -> {
                            encryptionKey = zipIn.readBytes()
                            Log.i(TAG, "Read encryption key from backup (plain)")
                        }

                        // 读取数据库文件到临时位置
                        entry.name.startsWith("database/") -> {
                            val tempFile = File(dbFile.parentFile, "${DATABASE_NAME}.tmp")
                            tempFile.parentFile?.mkdirs()
                            tempFile.outputStream().use { output ->
                                zipIn.copyTo(output)
                            }
                            Log.i(TAG, "Read database file to temp")
                        }

                        // 恢复图片文件
                        entry.name.startsWith("images/") -> {
                            val imageName = entry.name.substringAfter("images/")
                            val imageDir = File(context.cacheDir, "export_images")
                            imageDir.mkdirs()
                            val imageFile = File(imageDir, imageName)
                            imageFile.outputStream().use { output ->
                                zipIn.copyTo(output)
                            }
                        }

                        // 跳过元数据和旧版恢复码密钥
                        entry.name == "metadata.txt" -> { }
                        entry.name == "keys/recovery_key.bin" -> { }
                    }
                    entry = zipIn.nextEntry
                }
            }

            // 尝试解密密钥
            if (encryptionKey == null && encryptedKeyData != null) {
                // 需要密码解密
                if (password != null) {
                    try {
                        encryptionKey = BackupPasswordProtection.decrypt(password, encryptedKeyData!!)
                        Log.i(TAG, "Decrypted encryption key with password")
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to decrypt key: ${e.message}")
                        return@withContext false
                    }
                } else {
                    Log.e(TAG, "Backup is password-protected but no password provided")
                    return@withContext false
                }
            }

            // 先导入密钥
            if (encryptionKey != null) {
                val keyImported = DatabaseKeyManager.importKey(context, encryptionKey!!)
                if (!keyImported) {
                    Log.e(TAG, "Failed to import encryption key")
                    return@withContext false
                }
                Log.i(TAG, "Imported encryption key from backup")
            }

            // 然后移动临时数据库文件到正确位置
            val tempFile = File(dbFile.parentFile, "${DATABASE_NAME}.tmp")
            if (tempFile.exists()) {
                if (dbFile.exists()) {
                    dbFile.delete()
                }
                tempFile.renameTo(dbFile)
                Log.i(TAG, "Restored database file")
            }

            // 重置数据库实例，下次访问时会使用新密钥
            app.resetDatabase()

            Log.i(TAG, "Restore completed successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to restore from backup: ${e.message}", e)
            false
        }
    }

    /**
     * 验证备份文件
     */
    suspend fun validateBackup(context: Context, uri: Uri): Boolean = withContext(Dispatchers.IO) {
        val info = BackupManager.getBackupInfo(context, uri)
        info?.isValid == true
    }
}
