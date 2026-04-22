package com.ledger.task.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ledger.task.TaskLedgerApp
import com.ledger.task.backup.BackupManager
import com.ledger.task.backup.BackupPasswordStorage
import com.ledger.task.backup.BiometricAuthManager
import com.ledger.task.backup.BiometricStatus
import com.ledger.task.backup.RestoreManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 台账备份协调器
 * 负责备份、恢复、生物识别和密码管理
 */
class LedgerBackupCoordinator(
    private val application: Application,
    private val getState: () -> LedgerCenterUiState,
    private val updateState: (LedgerCenterUiState) -> Unit
) {
    companion object {
        private const val TAG = "LedgerBackupCoordinator"
    }

    private val passwordStorage = BackupPasswordStorage(application)
    private val biometricAuthManager = BiometricAuthManager(application)

    /**
     * 获取生物识别管理器（用于 UI 层调用认证）
     */
    fun getBiometricAuthManager(): BiometricAuthManager = biometricAuthManager

    /**
     * 获取密码存储（用于 UI 层访问）
     */
    fun getPasswordStorage(): BackupPasswordStorage = passwordStorage

    /**
     * 加载生物识别设置
     */
    fun loadBiometricSettings() {
        val biometricStatus = biometricAuthManager.canUseBiometric()
        val isBiometricAvailable = biometricStatus == BiometricStatus.AVAILABLE
        val isBiometricEnabled = passwordStorage.isBiometricEnabled()

        Log.i(TAG, "loadBiometricSettings: biometricStatus=$biometricStatus, isBiometricAvailable=$isBiometricAvailable, isBiometricEnabled=$isBiometricEnabled")

        updateState(getState().copy(
            isBiometricAvailable = isBiometricAvailable,
            isBiometricEnabled = isBiometricEnabled
        ))
    }

    /**
     * 请求创建备份（先检测密码）
     * 返回 true 表示可以弹出保存对话框，返回 false 表示需要先设置密码
     */
    fun requestCreateBackup(): Boolean {
        return hasBackupPassword()
    }

    /**
     * 创建备份（文件已选择）
     */
    fun createBackup(uri: Uri, viewModelScope: kotlinx.coroutines.CoroutineScope) {
        updateState(getState().copy(
            pendingBackupUri = uri,
            backupPasswordError = null
        ))

        // 如果生物识别已启用且有保存的密码，直接执行备份（无需验证）
        if (passwordStorage.isBiometricEnabled() && hasBackupPassword()) {
            performBackupDirectly(viewModelScope)
        } else {
            // 不应该到达这里，因为已经在 requestCreateBackup 中处理了
            Log.e(TAG, "createBackup called without password")
        }
    }

    /**
     * 直接执行备份（生物识别已启用时）
     */
    private fun performBackupDirectly(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        val uri = getState().pendingBackupUri ?: return
        val password = passwordStorage.getPassword() ?: return
        val passwordHint = passwordStorage.getPasswordHint()

        viewModelScope.launch(Dispatchers.IO) {
            updateState(getState().copy(isBackingUp = true))
            try {
                val context = application.applicationContext
                val result = BackupManager.createBackup(context, uri, password, passwordHint)

                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isBackingUp = false,
                        pendingBackupUri = null,
                        backupMessage = if (result.success) "备份成功" else "备份失败"
                    ))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Backup failed", e)
                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isBackingUp = false,
                        pendingBackupUri = null,
                        backupMessage = "备份失败: ${e.message}"
                    ))
                }
            }
        }
    }

    /**
     * 从备份恢复（检查是否需要密码，优先使用生物识别）
     */
    fun restoreFromBackup(uri: Uri, viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch(Dispatchers.IO) {
            val context = application.applicationContext
            val backupInfo = BackupManager.getBackupInfo(context, uri)

            withContext(Dispatchers.Main) {
                if (backupInfo?.isPasswordProtected == true) {
                    // 需要密码
                    updateState(getState().copy(
                        pendingRestoreUri = uri,
                        backupPasswordError = null,
                        isBackupPasswordProtected = true,
                        passwordHint = backupInfo.passwordHint,
                        biometricFailedCount = 0
                    ))

                    // 如果生物识别已启用且有保存的密码，使用生物识别
                    if (passwordStorage.isBiometricEnabled() && hasBackupPassword()) {
                        updateState(getState().copy(
                            showRestorePasswordDialog = false,
                            needsBiometricForRestore = true
                        ))
                    } else {
                        // 否则显示密码对话框
                        updateState(getState().copy(showRestorePasswordDialog = true))
                    }
                } else {
                    // 不需要密码，直接恢复
                    performRestore(uri, null, viewModelScope)
                }
            }
        }
    }

    /**
     * 生物识别验证成功后执行恢复
     */
    fun performRestoreWithBiometric(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        val uri = getState().pendingRestoreUri ?: return
        val password = passwordStorage.getPassword() ?: return

        updateState(getState().copy(
            needsBiometricForRestore = false,
            biometricFailedCount = 0
        ))
        performRestore(uri, password, viewModelScope)
    }

    /**
     * 生物识别验证失败，检查是否需要回退到密码输入
     */
    fun onBiometricFailedForRestore() {
        val failedCount = getState().biometricFailedCount + 1
        Log.i(TAG, "Biometric failed for restore, count: $failedCount")

        if (failedCount >= 3) {
            // 三次失败，回退到密码输入
            updateState(getState().copy(
                needsBiometricForRestore = false,
                showRestorePasswordDialog = true,
                biometricFailedCount = 0
            ))
        } else {
            // 更新失败计数，增加触发器ID以重新显示生物识别
            updateState(getState().copy(
                biometricFailedCount = failedCount,
                biometricTriggerId = getState().biometricTriggerId + 1
            ))
        }
    }

    /**
     * 生物识别错误，回退到密码输入
     */
    fun fallbackToPasswordForRestore() {
        updateState(getState().copy(
            needsBiometricForRestore = false,
            showRestorePasswordDialog = true,
            biometricFailedCount = 0
        ))
    }

    /**
     * 确认恢复（带密码）
     */
    fun confirmRestore(password: String?, viewModelScope: kotlinx.coroutines.CoroutineScope) {
        val uri = getState().pendingRestoreUri ?: return
        updateState(getState().copy(
            showRestorePasswordDialog = false,
            backupPasswordError = null
        ))
        performRestore(uri, password, viewModelScope, isRetry = false)
    }

    /**
     * 执行恢复
     */
    private fun performRestore(uri: Uri, password: String?, viewModelScope: kotlinx.coroutines.CoroutineScope, isRetry: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState(getState().copy(isRestoring = true))
            try {
                val context = application.applicationContext
                val success = RestoreManager.restoreFromBackup(context, uri, password)
                withContext(Dispatchers.Main) {
                    updateState(getState().copy(
                        isRestoring = false,
                        pendingRestoreUri = null,
                        backupMessage = if (success) "恢复成功，应用将重启" else "恢复失败"
                    ))
                    if (success) {
                        // 延迟重启应用
                        restartApp(context, viewModelScope)
                    } else {
                        // 恢复失败，重新显示密码对话框让用户重试
                        updateState(getState().copy(
                            pendingRestoreUri = uri,  // 保留 URI 以便重试
                            showRestorePasswordDialog = true,
                            backupPasswordError = "恢复失败，请检查密码后重试"
                        ))
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Restore failed", e)
                withContext(Dispatchers.Main) {
                    // 恢复失败，重新显示密码对话框让用户重试
                    updateState(getState().copy(
                        isRestoring = false,
                        pendingRestoreUri = uri,  // 保留 URI 以便重试
                        showRestorePasswordDialog = true,
                        backupPasswordError = "密码错误，请重试"
                    ))
                }
            }
        }
    }

    /**
     * 取消恢复密码对话框
     */
    fun cancelRestorePasswordDialog() {
        updateState(getState().copy(
            showRestorePasswordDialog = false,
            pendingRestoreUri = null,
            backupPasswordError = null,
            passwordHint = null,
            needsBiometricForRestore = false
        ))
    }

    /**
     * 重启应用
     */
    private fun restartApp(context: Context, viewModelScope: kotlinx.coroutines.CoroutineScope) {
        viewModelScope.launch {
            delay(1500) // 延迟让用户看到提示
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            intent?.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            // 使用 Activity 生命周期方法而非强制退出
            // Runtime.getRuntime().exit(0) 会绕过正常的生命周期回调
        }
    }

    /**
     * 清除备份消息
     */
    fun clearBackupMessage() {
        updateState(getState().copy(backupMessage = null))
    }

    // ==================== 生物识别相关 ====================

    /**
     * 显示生物识别设置对话框
     */
    fun showBiometricSetupDialog() {
        Log.i(TAG, "showBiometricSetupDialog called, hasBackupPassword=${hasBackupPassword()}, isBiometricAvailable=${biometricAuthManager.canUseBiometric()}")
        updateState(getState().copy(showBiometricSetupDialog = true))
    }

    /**
     * 关闭生物识别设置对话框
     */
    fun dismissBiometricSetupDialog() {
        updateState(getState().copy(showBiometricSetupDialog = false))
    }

    /**
     * 启用生物识别快捷访问
     * 需要先通过生物识别认证，然后才能加密密码
     * @param activity FragmentActivity 用于显示生物识别对话框
     * @param password 备份密码
     */
    fun enableBiometricAccess(activity: androidx.fragment.app.FragmentActivity, password: String) {
        Log.i(TAG, "enableBiometricAccess: starting")

        // 先保存密码（用于自动备份和后续操作）
        passwordStorage.savePassword(password)
        Log.i(TAG, "Password saved")

        // 先创建生物识别密钥
        if (!passwordStorage.getBiometricAuthManager().createBiometricKey()) {
            Log.e(TAG, "Failed to create biometric key")
            updateState(getState().copy(
                showBiometricSetupDialog = false,
                backupMessage = "创建生物识别密钥失败"
            ))
            return
        }
        Log.i(TAG, "Biometric key created")

        // 获取加密 Cipher
        val cipher = passwordStorage.getBiometricAuthManager().getEncryptCipher()
        if (cipher == null) {
            Log.e(TAG, "Failed to get encrypt cipher")
            updateState(getState().copy(
                showBiometricSetupDialog = false,
                backupMessage = "获取加密器失败"
            ))
            return
        }
        Log.i(TAG, "Got encrypt cipher, showing biometric prompt")

        // 显示生物识别提示，认证成功后加密密码
        passwordStorage.getBiometricAuthManager().authenticateWithCipher(
            activity = activity,
            cipher = cipher,
            title = "验证身份",
            subtitle = "请使用指纹或面容验证以启用生物识别快捷访问",
            negativeButtonText = "取消",
            onSuccess = { result ->
                Log.i(TAG, "Biometric auth succeeded")
                // 认证成功，使用已认证的 Cipher 加密密码
                val authenticatedCipher = result.cryptoObject?.cipher
                if (authenticatedCipher != null) {
                    val success = passwordStorage.enableBiometricAccessWithCipher(password, authenticatedCipher)
                    Log.i(TAG, "enableBiometricAccessWithCipher result: $success")

                    updateState(getState().copy(
                        isBiometricEnabled = success,
                        showBiometricSetupDialog = false,
                        backupMessage = if (success) "已启用生物识别" else "启用生物识别失败"
                    ))
                } else {
                    Log.e(TAG, "Authenticated cipher is null")
                    updateState(getState().copy(
                        showBiometricSetupDialog = false,
                        backupMessage = "启用生物识别失败"
                    ))
                }
            },
            onError = { errorCode, errString ->
                Log.e(TAG, "Biometric auth error: $errorCode - $errString")
                updateState(getState().copy(
                    showBiometricSetupDialog = false,
                    backupMessage = "验证失败: $errString"
                ))
            },
            onFailed = {
                Log.w(TAG, "Biometric auth failed, user can retry")
                // 用户可以重试，不关闭对话框
            }
        )
    }

    /**
     * 请求禁用生物识别快捷访问（需要生物识别验证）
     */
    fun requestDisableBiometricAccess() {
        updateState(getState().copy(
            needsBiometricForDisable = true,
            biometricFailedCount = 0
        ))
    }

    /**
     * 生物识别验证成功后禁用生物识别
     */
    fun performDisableBiometricAccess() {
        val success = passwordStorage.disableBiometricAccess()
        updateState(getState().copy(
            isBiometricEnabled = !success,
            needsBiometricForDisable = false,
            showPasswordForDisable = false,
            backupMessage = if (success) "已禁用生物识别" else "禁用生物识别失败"
        ))
    }

    /**
     * 生物识别验证失败，检查是否需要回退到密码输入
     */
    fun onBiometricFailedForDisable() {
        val failedCount = getState().biometricFailedCount + 1
        Log.i(TAG, "Biometric failed for disable, count: $failedCount")

        if (failedCount >= 3) {
            // 三次失败，回退到密码输入
            updateState(getState().copy(
                needsBiometricForDisable = false,
                showPasswordForDisable = true,
                biometricFailedCount = 0
            ))
        } else {
            // 更新失败计数，增加触发器ID以重新显示生物识别
            updateState(getState().copy(
                biometricFailedCount = failedCount,
                biometricTriggerId = getState().biometricTriggerId + 1
            ))
        }
    }

    /**
     * 密码验证关闭生物识别
     */
    fun disableBiometricWithPassword(password: String): Boolean {
        val savedPassword = passwordStorage.getPassword()
        if (savedPassword == password) {
            performDisableBiometricAccess()
            return true
        }
        return false
    }

    /**
     * 取消禁用生物识别
     */
    fun cancelDisableBiometric() {
        updateState(getState().copy(
            needsBiometricForDisable = false,
            showPasswordForDisable = false,
            biometricFailedCount = 0
        ))
    }

    /**
     * 保存备份密码（用于自动备份）
     */
    fun saveBackupPassword(password: String): Boolean {
        return passwordStorage.savePassword(password)
    }

    /**
     * 检查是否已设置备份密码
     */
    fun hasBackupPassword(): Boolean {
        return passwordStorage.hasPassword()
    }
}
