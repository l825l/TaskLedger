package com.ledger.task.backup;

import java.lang.System;

/**
 * 生物识别授权管理器
 * 使用 Android Keystore 创建生物识别绑定的密钥
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0004\b\u0007\u0018\u0000 *2\u00020\u0001:\u0001*B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004Jh\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u00122\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u000e0\u00162\u0018\u0010\u0018\u001a\u0014\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000e0\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001cJp\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u00122\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u000e0\u00162\u0018\u0010\u0018\u001a\u0014\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000e0\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001cJ\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020#J\u0006\u0010$\u001a\u00020\u000eJ\u0010\u0010%\u001a\u0004\u0018\u00010\u001f2\u0006\u0010&\u001a\u00020\'J\b\u0010(\u001a\u0004\u0018\u00010\u001fJ\u0006\u0010)\u001a\u00020#R\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n\u00a8\u0006+"}, d2 = {"Lcom/ledger/task/backup/BiometricAuthManager;", "", "applicationContext", "Landroid/content/Context;", "(Landroid/content/Context;)V", "context", "kotlin.jvm.PlatformType", "keyStore", "Ljava/security/KeyStore;", "getKeyStore", "()Ljava/security/KeyStore;", "keyStore$delegate", "Lkotlin/Lazy;", "authenticate", "", "activity", "Landroidx/fragment/app/FragmentActivity;", "title", "", "subtitle", "negativeButtonText", "onSuccess", "Lkotlin/Function1;", "Landroidx/biometric/BiometricPrompt$AuthenticationResult;", "onError", "Lkotlin/Function2;", "", "onFailed", "Lkotlin/Function0;", "authenticateWithCipher", "cipher", "Ljavax/crypto/Cipher;", "canUseBiometric", "Lcom/ledger/task/backup/BiometricStatus;", "createBiometricKey", "", "deleteBiometricKey", "getDecryptCipher", "iv", "", "getEncryptCipher", "hasBiometricKey", "Companion", "app_debug"})
public final class BiometricAuthManager {
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.backup.BiometricAuthManager.Companion Companion = null;
    private static final java.lang.String TAG = "BiometricAuthManager";
    private static final java.lang.String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final java.lang.String BACKUP_KEY_ALIAS = "backup_biometric_key";
    private static final java.lang.String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int TAG_SIZE = 128;
    private static final int AUTH_VALIDITY_SECONDS = -1;
    private final kotlin.Lazy keyStore$delegate = null;
    
    public BiometricAuthManager(@org.jetbrains.annotations.NotNull
    android.content.Context applicationContext) {
        super();
    }
    
    private final java.security.KeyStore getKeyStore() {
        return null;
    }
    
    /**
     * 检查设备是否支持生物识别
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.backup.BiometricStatus canUseBiometric() {
        return null;
    }
    
    /**
     * 创建或获取生物识别绑定的密钥
     */
    public final boolean createBiometricKey() {
        return false;
    }
    
    /**
     * 检查生物识别密钥是否存在
     */
    public final boolean hasBiometricKey() {
        return false;
    }
    
    /**
     * 删除生物识别密钥
     */
    public final void deleteBiometricKey() {
    }
    
    /**
     * 获取用于加密的 Cipher（需要用户认证）
     */
    @org.jetbrains.annotations.Nullable
    public final javax.crypto.Cipher getEncryptCipher() {
        return null;
    }
    
    /**
     * 获取用于解密的 Cipher（需要用户认证）
     */
    @org.jetbrains.annotations.Nullable
    public final javax.crypto.Cipher getDecryptCipher(@org.jetbrains.annotations.NotNull
    byte[] iv) {
        return null;
    }
    
    /**
     * 显示生物识别提示并执行操作
     */
    public final void authenticate(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.Nullable
    java.lang.String subtitle, @org.jetbrains.annotations.NotNull
    java.lang.String negativeButtonText, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super androidx.biometric.BiometricPrompt.AuthenticationResult, kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onError, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onFailed) {
    }
    
    /**
     * 使用生物识别加密的 Cipher 进行认证
     */
    public final void authenticateWithCipher(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull
    javax.crypto.Cipher cipher, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.Nullable
    java.lang.String subtitle, @org.jetbrains.annotations.NotNull
    java.lang.String negativeButtonText, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super androidx.biometric.BiometricPrompt.AuthenticationResult, kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onError, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onFailed) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/ledger/task/backup/BiometricAuthManager$Companion;", "", "()V", "AUTH_VALIDITY_SECONDS", "", "BACKUP_KEY_ALIAS", "", "KEYSTORE_PROVIDER", "TAG", "TAG_SIZE", "TRANSFORMATION", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}