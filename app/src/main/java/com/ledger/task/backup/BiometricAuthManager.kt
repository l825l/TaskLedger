package com.ledger.task.backup

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * 生物识别授权管理器
 * 使用 Android Keystore 创建生物识别绑定的密钥
 */
class BiometricAuthManager(applicationContext: Context) {

    private val context = applicationContext.applicationContext

    companion object {
        private const val TAG = "BiometricAuthManager"
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val BACKUP_KEY_ALIAS = "backup_biometric_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val TAG_SIZE = 128

        // 生物识别有效期（秒）- 每次都需要验证
        private const val AUTH_VALIDITY_SECONDS = -1
    }

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
    }

    /**
     * 检查设备是否支持生物识别
     */
    fun canUseBiometric(): BiometricStatus {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricStatus.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.HARDWARE_UNAVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.NONE_ENROLLED
            else -> BiometricStatus.UNKNOWN
        }
    }

    /**
     * 创建或获取生物识别绑定的密钥
     */
    fun createBiometricKey(): Boolean {
        return try {
            // 如果密钥已存在，先删除
            if (keyStore.containsAlias(BACKUP_KEY_ALIAS)) {
                keyStore.deleteEntry(BACKUP_KEY_ALIAS)
            }

            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE_PROVIDER
            )

            val spec = KeyGenParameterSpec.Builder(
                BACKUP_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationValidityDurationSeconds(AUTH_VALIDITY_SECONDS)
                .build()

            keyGenerator.init(spec)
            keyGenerator.generateKey()

            Log.i(TAG, "Created biometric-bound key")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create biometric key: ${e.message}")
            false
        }
    }

    /**
     * 检查生物识别密钥是否存在
     */
    fun hasBiometricKey(): Boolean {
        return try {
            keyStore.containsAlias(BACKUP_KEY_ALIAS)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 删除生物识别密钥
     */
    fun deleteBiometricKey() {
        try {
            if (keyStore.containsAlias(BACKUP_KEY_ALIAS)) {
                keyStore.deleteEntry(BACKUP_KEY_ALIAS)
            }
            Log.i(TAG, "Deleted biometric key")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete biometric key: ${e.message}")
        }
    }

    /**
     * 获取用于加密的 Cipher（需要用户认证）
     */
    fun getEncryptCipher(): Cipher? {
        return try {
            val secretKey = keyStore.getKey(BACKUP_KEY_ALIAS, null) as SecretKey
            Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.ENCRYPT_MODE, secretKey)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get encrypt cipher: ${e.message}")
            null
        }
    }

    /**
     * 获取用于解密的 Cipher（需要用户认证）
     */
    fun getDecryptCipher(iv: ByteArray): Cipher? {
        return try {
            val secretKey = keyStore.getKey(BACKUP_KEY_ALIAS, null) as SecretKey
            Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(TAG_SIZE, iv))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get decrypt cipher: ${e.message}")
            null
        }
    }

    /**
     * 显示生物识别提示并执行操作
     */
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String? = null,
        negativeButtonText: String = "使用密码",
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Log.i(TAG, "Biometric authentication succeeded")
                    onSuccess(result)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Log.e(TAG, "Biometric error: $errorCode - $errString")
                    onError(errorCode, errString.toString())
                }

                override fun onAuthenticationFailed() {
                    Log.w(TAG, "Biometric authentication failed")
                    onFailed()
                }
            }
        )

        val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )

        // 当允许 DEVICE_CREDENTIAL 时，不能设置 NegativeButtonText
        // 系统会自动显示"使用设备凭据"选项

        val promptInfo = promptInfoBuilder.build()

        biometricPrompt.authenticate(promptInfo)
    }

    /**
     * 使用生物识别加密的 Cipher 进行认证
     */
    fun authenticateWithCipher(
        activity: FragmentActivity,
        cipher: Cipher,
        title: String,
        subtitle: String? = null,
        negativeButtonText: String = "使用密码",
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Log.i(TAG, "Biometric authentication with cipher succeeded")
                    onSuccess(result)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Log.e(TAG, "Biometric error: $errorCode - $errString")
                    onError(errorCode, errString.toString())
                }

                override fun onAuthenticationFailed() {
                    Log.w(TAG, "Biometric authentication failed")
                    onFailed()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }
}

/**
 * 生物识别状态
 */
enum class BiometricStatus {
    AVAILABLE,           // 可用
    NO_HARDWARE,         // 无硬件支持
    HARDWARE_UNAVAILABLE, // 硬件不可用
    NONE_ENROLLED,       // 未录入生物识别
    UNKNOWN              // 未知错误
}
