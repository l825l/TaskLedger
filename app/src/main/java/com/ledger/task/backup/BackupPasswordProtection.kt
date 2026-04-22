package com.ledger.task.backup

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * 备份密码保护工具
 * 使用 PBKDF2 + AES-GCM 加密保护备份中的敏感数据
 */
object BackupPasswordProtection {

    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val KEY_ALGORITHM = "AES"
    private const val KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val KEY_SIZE = 256
    private const val SALT_SIZE = 32
    private const val IV_SIZE = 12
    private const val ITERATION_COUNT = 100000

    // 恢复码相关常量
    private const val RECOVERY_CODE_COUNT = 8
    private const val RECOVERY_CODE_LENGTH = 8
    private const val RECOVERY_CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // 排除易混淆字符

    const val MIN_PASSWORD_LENGTH = 8

    /**
     * 使用密码加密数据
     * @param password 用户设置的备份密码
     * @param data 要加密的数据（如数据库密钥）
     * @return 加密后的数据（salt + iv + ciphertext）
     */
    fun encrypt(password: String, data: ByteArray): ByteArray {
        if (password.length < MIN_PASSWORD_LENGTH) {
            throw IllegalArgumentException("密码长度至少${MIN_PASSWORD_LENGTH}位")
        }

        // 生成随机 salt
        val salt = ByteArray(SALT_SIZE)
        SecureRandom().nextBytes(salt)

        // 从密码派生密钥
        val secretKey = deriveKey(password, salt)

        // 生成随机 IV
        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv)

        // 加密
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        val ciphertext = cipher.doFinal(data)

        // 返回 salt + iv + ciphertext
        return salt + iv + ciphertext
    }

    /**
     * 使用密码解密数据
     * @param password 用户输入的备份密码
     * @param encryptedData 加密的数据（salt + iv + ciphertext）
     * @return 解密后的原始数据
     */
    fun decrypt(password: String, encryptedData: ByteArray): ByteArray {
        if (encryptedData.size < SALT_SIZE + IV_SIZE) {
            throw IllegalArgumentException("加密数据格式无效")
        }

        // 提取 salt, iv, ciphertext
        val salt = encryptedData.copyOfRange(0, SALT_SIZE)
        val iv = encryptedData.copyOfRange(SALT_SIZE, SALT_SIZE + IV_SIZE)
        val ciphertext = encryptedData.copyOfRange(SALT_SIZE + IV_SIZE, encryptedData.size)

        // 从密码派生密钥
        val secretKey = deriveKey(password, salt)

        // 解密
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        return cipher.doFinal(ciphertext)
    }

    /**
     * 使用 PBKDF2 从密码派生密钥
     */
    private fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE)
        val factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
        val secretKey = factory.generateSecret(spec)
        return SecretKeySpec(secretKey.encoded, KEY_ALGORITHM)
    }

    /**
     * 生成恢复码列表
     * @return 8 组恢复码，格式如 "XXXX-XXXX"
     */
    fun generateRecoveryCodes(): List<String> {
        val random = SecureRandom()
        return (1..RECOVERY_CODE_COUNT).map {
            val code = StringBuilder()
            repeat(RECOVERY_CODE_LENGTH) { index ->
                if (index == 4) code.append('-')
                val randomIndex = random.nextInt(RECOVERY_CODE_CHARS.length)
                code.append(RECOVERY_CODE_CHARS[randomIndex])
            }
            code.toString()
        }
    }

    /**
     * 使用恢复码加密数据
     * @param recoveryCode 恢复码
     * @param data 要加密的数据
     * @return 加密后的数据（salt + iv + ciphertext）
     */
    fun encryptWithRecoveryCode(recoveryCode: String, data: ByteArray): ByteArray {
        // 移除格式字符，只保留字母数字
        val cleanCode = recoveryCode.replace("-", "").uppercase()
        if (cleanCode.length != RECOVERY_CODE_LENGTH) {
            throw IllegalArgumentException("恢复码格式无效")
        }
        return encrypt(cleanCode, data)
    }

    /**
     * 使用恢复码解密数据
     * @param recoveryCode 恢复码
     * @param encryptedData 加密的数据
     * @return 解密后的原始数据
     */
    fun decryptWithRecoveryCode(recoveryCode: String, encryptedData: ByteArray): ByteArray {
        val cleanCode = recoveryCode.replace("-", "").uppercase()
        if (cleanCode.length != RECOVERY_CODE_LENGTH) {
            throw IllegalArgumentException("恢复码格式无效")
        }
        return decrypt(cleanCode, encryptedData)
    }

    /**
     * 验证密码强度
     * @return 密码是否足够强
     */
    fun validatePasswordStrength(password: String): PasswordStrength {
        if (password.length < MIN_PASSWORD_LENGTH) {
            return PasswordStrength.TOO_SHORT
        }

        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }

        return when {
            password.length >= MIN_PASSWORD_LENGTH && hasLetter && hasDigit && hasSpecial -> PasswordStrength.STRONG
            password.length >= MIN_PASSWORD_LENGTH && (hasLetter || hasDigit) -> PasswordStrength.MEDIUM
            else -> PasswordStrength.WEAK
        }
    }

    /**
     * 验证恢复码格式
     */
    fun validateRecoveryCodeFormat(code: String): Boolean {
        val cleanCode = code.replace("-", "").uppercase()
        if (cleanCode.length != RECOVERY_CODE_LENGTH) return false
        return cleanCode.all { it in RECOVERY_CODE_CHARS }
    }
}

/**
 * 密码强度等级
 */
enum class PasswordStrength(val description: String) {
    TOO_SHORT("密码长度至少8位"),
    WEAK("密码强度：弱"),
    MEDIUM("密码强度：中"),
    STRONG("密码强度：强")
}
