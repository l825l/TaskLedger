package com.ledger.task.backup

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.crypto.Cipher

/**
 * 备份密码安全存储
 * 使用 EncryptedSharedPreferences 存储备份密码
 * 支持生物识别快捷访问
 */
class BackupPasswordStorage(applicationContext: Context) {

    private val context = applicationContext.applicationContext

    companion object {
        private const val TAG = "BackupPasswordStorage"
        private const val PREFS_FILE_NAME = "backup_password_prefs"
        private const val KEY_BACKUP_PASSWORD = "encrypted_backup_password"
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        private const val KEY_BIOMETRIC_CIPHER_IV = "biometric_cipher_iv"
        private const val KEY_BIOMETRIC_ENCRYPTED_PASSWORD = "biometric_encrypted_password"
        private const val KEY_PASSWORD_HINT = "password_hint"
    }

    private val biometricAuthManager = BiometricAuthManager(context)

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedPrefs by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * 保存备份密码（基础存储）
     */
    fun savePassword(password: String): Boolean {
        return try {
            val result = encryptedPrefs.edit()
                .putString(KEY_BACKUP_PASSWORD, password)
                .commit()
            if (!result) {
                Log.e(TAG, "savePassword commit returned false")
            }
            result
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save password: ${e.message}", e)
            false
        }
    }

    /**
     * 获取备份密码
     */
    fun getPassword(): String? {
        return try {
            encryptedPrefs.getString(KEY_BACKUP_PASSWORD, null)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get password: ${e.message}")
            null
        }
    }

    /**
     * 检查是否已设置备份密码
     */
    fun hasPassword(): Boolean {
        return try {
            encryptedPrefs.getString(KEY_BACKUP_PASSWORD, null) != null
        } catch (e: Exception) {
            Log.e(TAG, "hasPassword error: ${e.message}")
            false
        }
    }

    /**
     * 启用生物识别快捷访问
     * 将密码用生物识别绑定的密钥加密存储
     */
    fun enableBiometricAccess(password: String): Boolean {
        return try {
            // 创建生物识别密钥
            if (!biometricAuthManager.createBiometricKey()) {
                Log.e(TAG, "Failed to create biometric key")
                return false
            }

            // 获取加密 Cipher
            val cipher = biometricAuthManager.getEncryptCipher()
            if (cipher == null) {
                Log.e(TAG, "Failed to get encrypt cipher")
                return false
            }

            // 加密密码
            val passwordBytes = password.toByteArray(Charsets.UTF_8)
            val encryptedPassword = cipher.doFinal(passwordBytes)
            val iv = cipher.iv

            // 存储（使用 Base64 编码）
            val ivString = android.util.Base64.encodeToString(iv, android.util.Base64.NO_WRAP)
            val encryptedString = android.util.Base64.encodeToString(encryptedPassword, android.util.Base64.NO_WRAP)

            val result = encryptedPrefs.edit()
                .putString(KEY_BIOMETRIC_CIPHER_IV, ivString)
                .putString(KEY_BIOMETRIC_ENCRYPTED_PASSWORD, encryptedString)
                .putBoolean(KEY_BIOMETRIC_ENABLED, true)
                .commit()

            result
        } catch (e: Exception) {
            Log.e(TAG, "Failed to enable biometric access: ${e.message}", e)
            false
        }
    }

    /**
     * 使用已认证的 Cipher 启用生物识别快捷访问
     * 在用户通过生物识别认证后调用
     */
    fun enableBiometricAccessWithCipher(password: String, cipher: Cipher): Boolean {
        return try {
            // 使用已认证的 Cipher 加密密码
            val passwordBytes = password.toByteArray(Charsets.UTF_8)
            val encryptedPassword = cipher.doFinal(passwordBytes)
            val iv = cipher.iv

            // 存储（使用 Base64 编码）
            val ivString = android.util.Base64.encodeToString(iv, android.util.Base64.NO_WRAP)
            val encryptedString = android.util.Base64.encodeToString(encryptedPassword, android.util.Base64.NO_WRAP)

            val result = encryptedPrefs.edit()
                .putString(KEY_BIOMETRIC_CIPHER_IV, ivString)
                .putString(KEY_BIOMETRIC_ENCRYPTED_PASSWORD, encryptedString)
                .putBoolean(KEY_BIOMETRIC_ENABLED, true)
                .commit()

            result
        } catch (e: Exception) {
            Log.e(TAG, "Failed to enable biometric access with cipher: ${e.message}", e)
            false
        }
    }

    /**
     * 禁用生物识别快捷访问
     */
    fun disableBiometricAccess(): Boolean {
        return try {
            biometricAuthManager.deleteBiometricKey()
            encryptedPrefs.edit()
                .remove(KEY_BIOMETRIC_CIPHER_IV)
                .remove(KEY_BIOMETRIC_ENCRYPTED_PASSWORD)
                .putBoolean(KEY_BIOMETRIC_ENABLED, false)
                .commit()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to disable biometric access: ${e.message}")
            false
        }
    }

    /**
     * 检查是否启用了生物识别快捷访问
     */
    fun isBiometricEnabled(): Boolean {
        return try {
            encryptedPrefs.getBoolean(KEY_BIOMETRIC_ENABLED, false) &&
            biometricAuthManager.hasBiometricKey()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 保存密码提示
     */
    fun savePasswordHint(hint: String): Boolean {
        return try {
            encryptedPrefs.edit()
                .putString(KEY_PASSWORD_HINT, hint)
                .commit()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save password hint: ${e.message}")
            false
        }
    }

    /**
     * 获取密码提示
     */
    fun getPasswordHint(): String? {
        return try {
            encryptedPrefs.getString(KEY_PASSWORD_HINT, null)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取用于生物识别解密的 Cipher
     */
    fun getDecryptCipherForBiometric(): Cipher? {
        return try {
            val ivString = encryptedPrefs.getString(KEY_BIOMETRIC_CIPHER_IV, null) ?: return null
            val iv = android.util.Base64.decode(ivString, android.util.Base64.NO_WRAP)
            biometricAuthManager.getDecryptCipher(iv)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get decrypt cipher: ${e.message}")
            null
        }
    }

    /**
     * 使用生物识别解密后的 Cipher 获取密码
     */
    fun getPasswordWithDecryptedCipher(cipher: Cipher): String? {
        return try {
            val encryptedString = encryptedPrefs.getString(KEY_BIOMETRIC_ENCRYPTED_PASSWORD, null)
                ?: return null
            val encryptedBytes = android.util.Base64.decode(encryptedString, android.util.Base64.NO_WRAP)

            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decrypt password: ${e.message}")
            null
        }
    }

    /**
     * 清除所有存储的密码
     */
    fun clearAll() {
        try {
            biometricAuthManager.deleteBiometricKey()
            encryptedPrefs.edit().clear().commit()
            Log.i(TAG, "Cleared all stored passwords")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear passwords: ${e.message}")
        }
    }

    /**
     * 获取生物识别管理器（用于认证）
     */
    fun getBiometricAuthManager(): BiometricAuthManager = biometricAuthManager
}
