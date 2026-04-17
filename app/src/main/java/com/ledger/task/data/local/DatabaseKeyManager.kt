package com.ledger.task.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

/**
 * 数据库加密密钥管理器
 * 使用 Android Keystore + EncryptedSharedPreferences 安全存储密钥
 */
object DatabaseKeyManager {

    private const val TAG = "DatabaseKeyManager"
    private const val PREFS_FILE_NAME = "secure_db_prefs"
    private const val KEY_NAME = "db_encryption_key"
    private const val KEY_SIZE = 32  // 256位密钥

    /**
     * 获取或创建数据库加密密钥
     * @return 加密密钥的字节数组，如果安全存储失败则返回 null
     */
    fun getOrCreateKey(context: Context): ByteArray {
        return try {
            val masterKey = getOrCreateMasterKey(context)
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                PREFS_FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            // 尝试读取已有密钥
            val existingKey = sharedPreferences.getString(KEY_NAME, null)
            if (existingKey != null) {
                Log.i(TAG, "Using existing database encryption key")
                hexStringToByteArray(existingKey)
            } else {
                // 生成新密钥
                val newKey = generateRandomKey()
                sharedPreferences.edit()
                    .putString(KEY_NAME, byteArrayToHexString(newKey))
                    .apply()
                Log.i(TAG, "Generated new database encryption key")
                newKey
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get/create encryption key: ${e.message}")
            // 安全失败：不使用不安全的回退方案，抛出异常让调用方处理
            throw SecurityException("无法安全初始化数据库加密密钥，请检查设备安全设置")
        }
    }

    /**
     * 获取或创建 Master Key
     */
    private fun getOrCreateMasterKey(context: Context): MasterKey {
        val spec = KeyGenParameterSpec.Builder(
            "_androidx_security_master_key_",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        return MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()
    }

    /**
     * 生成随机密钥
     */
    private fun generateRandomKey(): ByteArray {
        val secureRandom = SecureRandom()
        val key = ByteArray(KEY_SIZE)
        secureRandom.nextBytes(key)
        return key
    }

    /**
     * 字节数组转十六进制字符串
     */
    private fun byteArrayToHexString(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * 十六进制字符串转字节数组
     */
    private fun hexStringToByteArray(hex: String): ByteArray {
        return ByteArray(hex.length / 2) {
            hex.substring(it * 2, it * 2 + 2).toInt(16).toByte()
        }
    }

    /**
     * 检查密钥是否存在
     */
    fun hasKey(context: Context): Boolean {
        return try {
            val masterKey = getOrCreateMasterKey(context)
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                PREFS_FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            sharedPreferences.getString(KEY_NAME, null) != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 删除密钥（用于测试或重置）
     */
    fun deleteKey(context: Context) {
        try {
            val masterKey = getOrCreateMasterKey(context)
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                PREFS_FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            sharedPreferences.edit()
                .remove(KEY_NAME)
                .apply()
            Log.i(TAG, "Database encryption key deleted")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete encryption key: ${e.message}")
        }
    }

    /**
     * 导入密钥（用于恢复备份）
     * @param context 上下文
     * @param key 要导入的密钥
     * @return 是否成功
     */
    fun importKey(context: Context, key: ByteArray): Boolean {
        return try {
            val masterKey = getOrCreateMasterKey(context)
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                PREFS_FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            // 使用 commit() 同步写入，确保密钥立即生效
            val success = sharedPreferences.edit()
                .putString(KEY_NAME, byteArrayToHexString(key))
                .commit()
            if (success) {
                Log.i(TAG, "Database encryption key imported successfully")
            } else {
                Log.e(TAG, "Failed to commit imported key")
            }
            success
        } catch (e: Exception) {
            Log.e(TAG, "Failed to import encryption key: ${e.message}")
            false
        }
    }
}
