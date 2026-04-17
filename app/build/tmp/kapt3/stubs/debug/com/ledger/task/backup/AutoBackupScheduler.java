package com.ledger.task.backup;

import java.lang.System;

/**
 * 自动备份调度器
 * 使用 WorkManager 实现定时备份
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u00c7\u0002\u0018\u00002\u00020\u0001:\u0002\u0018\u0019B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0013\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0016\u0010\u0015\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u0012J\u0018\u0010\u0017\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0016\u001a\u00020\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/ledger/task/backup/AutoBackupScheduler;", "", "()V", "KEY_ENABLED", "", "KEY_FREQUENCY", "KEY_HOUR", "KEY_MINUTE", "PREFS_NAME", "TAG", "WORK_NAME_DAILY", "WORK_NAME_WEEKLY", "cancelBackup", "", "context", "Landroid/content/Context;", "getNextBackupDescription", "getSettings", "Lcom/ledger/task/backup/AutoBackupScheduler$AutoBackupSettings;", "rescheduleBackup", "runBackupNow", "saveSettings", "settings", "scheduleBackup", "AutoBackupSettings", "BackupFrequency", "app_debug"})
public final class AutoBackupScheduler {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.backup.AutoBackupScheduler INSTANCE = null;
    private static final java.lang.String TAG = "AutoBackupScheduler";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WORK_NAME_DAILY = "auto_backup_daily";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WORK_NAME_WEEKLY = "auto_backup_weekly";
    private static final java.lang.String PREFS_NAME = "auto_backup_settings";
    private static final java.lang.String KEY_ENABLED = "auto_backup_enabled";
    private static final java.lang.String KEY_FREQUENCY = "backup_frequency";
    private static final java.lang.String KEY_HOUR = "backup_hour";
    private static final java.lang.String KEY_MINUTE = "backup_minute";
    
    private AutoBackupScheduler() {
        super();
    }
    
    /**
     * 获取自动备份设置
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.backup.AutoBackupScheduler.AutoBackupSettings getSettings(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    /**
     * 保存自动备份设置
     */
    public final void saveSettings(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.ledger.task.backup.AutoBackupScheduler.AutoBackupSettings settings) {
    }
    
    /**
     * 调度自动备份
     */
    public final void scheduleBackup(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    com.ledger.task.backup.AutoBackupScheduler.AutoBackupSettings settings) {
    }
    
    /**
     * 取消自动备份
     */
    public final void cancelBackup(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    /**
     * 重新调度备份（开机后调用）
     */
    public final void rescheduleBackup(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    /**
     * 获取下次备份时间描述
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getNextBackupDescription(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    /**
     * 立即执行一次备份（用于测试）
     */
    public final void runBackupNow(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    /**
     * 备份频率
     */
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2 = {"Lcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;", "", "displayName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getDisplayName", "()Ljava/lang/String;", "DAILY", "WEEKLY", "app_debug"})
    public static enum BackupFrequency {
        /*public static final*/ DAILY /* = new DAILY(null) */,
        /*public static final*/ WEEKLY /* = new WEEKLY(null) */;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String displayName = null;
        
        BackupFrequency(java.lang.String displayName) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getDisplayName() {
            return null;
        }
    }
    
    /**
     * 自动备份设置
     */
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00032\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000f\u00a8\u0006\u001b"}, d2 = {"Lcom/ledger/task/backup/AutoBackupScheduler$AutoBackupSettings;", "", "enabled", "", "frequency", "Lcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;", "hour", "", "minute", "(ZLcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;II)V", "getEnabled", "()Z", "getFrequency", "()Lcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;", "getHour", "()I", "getMinute", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "toString", "", "app_debug"})
    public static final class AutoBackupSettings {
        private final boolean enabled = false;
        @org.jetbrains.annotations.NotNull
        private final com.ledger.task.backup.AutoBackupScheduler.BackupFrequency frequency = null;
        private final int hour = 0;
        private final int minute = 0;
        
        /**
         * 自动备份设置
         */
        @org.jetbrains.annotations.NotNull
        public final com.ledger.task.backup.AutoBackupScheduler.AutoBackupSettings copy(boolean enabled, @org.jetbrains.annotations.NotNull
        com.ledger.task.backup.AutoBackupScheduler.BackupFrequency frequency, int hour, int minute) {
            return null;
        }
        
        /**
         * 自动备份设置
         */
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        /**
         * 自动备份设置
         */
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        /**
         * 自动备份设置
         */
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public java.lang.String toString() {
            return null;
        }
        
        public AutoBackupSettings() {
            super();
        }
        
        public AutoBackupSettings(boolean enabled, @org.jetbrains.annotations.NotNull
        com.ledger.task.backup.AutoBackupScheduler.BackupFrequency frequency, int hour, int minute) {
            super();
        }
        
        public final boolean component1() {
            return false;
        }
        
        public final boolean getEnabled() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.ledger.task.backup.AutoBackupScheduler.BackupFrequency component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.ledger.task.backup.AutoBackupScheduler.BackupFrequency getFrequency() {
            return null;
        }
        
        public final int component3() {
            return 0;
        }
        
        public final int getHour() {
            return 0;
        }
        
        public final int component4() {
            return 0;
        }
        
        public final int getMinute() {
            return 0;
        }
    }
}