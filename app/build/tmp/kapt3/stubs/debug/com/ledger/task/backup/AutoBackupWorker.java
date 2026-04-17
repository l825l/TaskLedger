package com.ledger.task.backup;

import java.lang.System;

/**
 * 自动备份 Worker
 * 在后台执行备份任务
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002J\u0011\u0010\t\u001a\u00020\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ)\u0010\f\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J \u0010\u0012\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J \u0010\u0015\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\rH\u0002\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0018"}, d2 = {"Lcom/ledger/task/backup/AutoBackupWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "workerParams", "Landroidx/work/WorkerParameters;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "cleanupOldBackups", "", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "performBackupToDownloads", "", "fileName", "", "password", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveToDownloadsDirectory", "sourceFile", "Ljava/io/File;", "sendBackupNotification", "success", "Companion", "app_debug"})
public final class AutoBackupWorker extends androidx.work.CoroutineWorker {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.backup.AutoBackupWorker.Companion Companion = null;
    private static final java.lang.String TAG = "AutoBackupWorker";
    private static final java.lang.String NOTIFICATION_CHANNEL_ID = "auto_backup_channel";
    private static final int NOTIFICATION_ID = 1001;
    
    public AutoBackupWorker(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    androidx.work.WorkerParameters workerParams) {
        super(null, null);
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object doWork(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> continuation) {
        return null;
    }
    
    /**
     * 执行备份到下载目录
     */
    private final java.lang.Object performBackupToDownloads(android.content.Context context, java.lang.String fileName, java.lang.String password, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return null;
    }
    
    /**
     * 保存文件到下载目录
     */
    private final boolean saveToDownloadsDirectory(android.content.Context context, java.io.File sourceFile, java.lang.String fileName) {
        return false;
    }
    
    /**
     * 清理旧备份
     */
    private final void cleanupOldBackups(android.content.Context context) {
    }
    
    /**
     * 发送备份通知
     */
    private final void sendBackupNotification(android.content.Context context, java.lang.String fileName, boolean success) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/ledger/task/backup/AutoBackupWorker$Companion;", "", "()V", "NOTIFICATION_CHANNEL_ID", "", "NOTIFICATION_ID", "", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}