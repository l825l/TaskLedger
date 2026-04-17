package com.ledger.task.backup;

import java.lang.System;

/**
 * 备份密码安全存储
 * 使用 EncryptedSharedPreferences 存储备份密码
 * 支持生物识别快捷访问
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u0000 %2\u00020\u0001:\u0001%B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001aJ\u0016\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010\u001e\u001a\u00020\u0006J\b\u0010\u001f\u001a\u0004\u0018\u00010\u001dJ\b\u0010 \u001a\u0004\u0018\u00010\u001aJ\u0010\u0010!\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010\"\u001a\u00020\u0017J\u0006\u0010#\u001a\u00020\u0017J\u000e\u0010$\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u001aR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u000e\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006&"}, d2 = {"Lcom/ledger/task/backup/BackupPasswordStorage;", "", "applicationContext", "Landroid/content/Context;", "(Landroid/content/Context;)V", "biometricAuthManager", "Lcom/ledger/task/backup/BiometricAuthManager;", "context", "kotlin.jvm.PlatformType", "encryptedPrefs", "Landroid/content/SharedPreferences;", "getEncryptedPrefs", "()Landroid/content/SharedPreferences;", "encryptedPrefs$delegate", "Lkotlin/Lazy;", "masterKey", "Landroidx/security/crypto/MasterKey;", "getMasterKey", "()Landroidx/security/crypto/MasterKey;", "masterKey$delegate", "clearAll", "", "disableBiometricAccess", "", "enableBiometricAccess", "password", "", "enableBiometricAccessWithCipher", "cipher", "Ljavax/crypto/Cipher;", "getBiometricAuthManager", "getDecryptCipherForBiometric", "getPassword", "getPasswordWithDecryptedCipher", "hasPassword", "isBiometricEnabled", "savePassword", "Companion", "app_debug"})
public final class BackupPasswordStorage {
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.backup.BackupPasswordStorage.Companion Companion = null;
    private static final java.lang.String TAG = "BackupPasswordStorage";
    private static final java.lang.String PREFS_FILE_NAME = "backup_password_prefs";
    private static final java.lang.String KEY_BACKUP_PASSWORD = "encrypted_backup_password";
    private static final java.lang.String KEY_BIOMETRIC_ENABLED = "biometric_enabled";
    private static final java.lang.String KEY_BIOMETRIC_CIPHER_IV = "biometric_cipher_iv";
    private static final java.lang.String KEY_BIOMETRIC_ENCRYPTED_PASSWORD = "biometric_encrypted_password";
    private final com.ledger.task.backup.BiometricAuthManager biometricAuthManager = null;
    private final kotlin.Lazy masterKey$delegate = null;
    private final kotlin.Lazy encryptedPrefs$delegate = null;
    
    public BackupPasswordStorage(@org.jetbrains.annotations.NotNull
    android.content.Context applicationContext) {
        super();
    }
    
    private final androidx.security.crypto.MasterKey getMasterKey() {
        return null;
    }
    
    private final android.content.SharedPreferences getEncryptedPrefs() {
        return null;
    }
    
    /**
     * 保存备份密码（基础存储）
     */
    public final boolean savePassword(@org.jetbrains.annotations.NotNull
    java.lang.String password) {
        return false;
    }
    
    /**
     * 获取备份密码
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getPassword() {
        return null;
    }
    
    /**
     * 检查是否已设置备份密码
     */
    public final boolean hasPassword() {
        return false;
    }
    
    /**
     * 启用生物识别快捷访问
     * 将密码用生物识别绑定的密钥加密存储
     */
    public final boolean enableBiometricAccess(@org.jetbrains.annotations.NotNull
    java.lang.String password) {
        return false;
    }
    
    /**
     * 使用已认证的 Cipher 启用生物识别快捷访问
     * 在用户通过生物识别认证后调用
     */
    public final boolean enableBiometricAccessWithCipher(@org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    javax.crypto.Cipher cipher) {
        return false;
    }
    
    /**
     * 禁用生物识别快捷访问
     */
    public final boolean disableBiometricAccess() {
        return false;
    }
    
    /**
     * 检查是否启用了生物识别快捷访问
     */
    public final boolean isBiometricEnabled() {
        return false;
    }
    
    /**
     * 获取用于生物识别解密的 Cipher
     */
    @org.jetbrains.annotations.Nullable
    public final javax.crypto.Cipher getDecryptCipherForBiometric() {
        return null;
    }
    
    /**
     * 使用生物识别解密后的 Cipher 获取密码
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getPasswordWithDecryptedCipher(@org.jetbrains.annotations.NotNull
    javax.crypto.Cipher cipher) {
        return null;
    }
    
    /**
     * 清除所有存储的密码
     */
    public final void clearAll() {
    }
    
    /**
     * 获取生物识别管理器（用于认证）
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.backup.BiometricAuthManager getBiometricAuthManager() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/ledger/task/backup/BackupPasswordStorage$Companion;", "", "()V", "KEY_BACKUP_PASSWORD", "", "KEY_BIOMETRIC_CIPHER_IV", "KEY_BIOMETRIC_ENABLED", "KEY_BIOMETRIC_ENCRYPTED_PASSWORD", "PREFS_FILE_NAME", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}