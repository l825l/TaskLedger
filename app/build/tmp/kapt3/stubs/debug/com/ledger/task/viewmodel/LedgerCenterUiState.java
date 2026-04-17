package com.ledger.task.viewmodel;

import java.lang.System;

/**
 * 台账中心 UI 状态
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\bZ\b\u0087\b\u0018\u00002\u00020\u0001B\u00a7\u0003\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\r\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\r\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0011\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0011\u0012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\r\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\r\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u000f\u0012\b\b\u0002\u0010 \u001a\u00020\u000f\u0012\b\b\u0002\u0010!\u001a\u00020\u000f\u0012\b\b\u0002\u0010\"\u001a\u00020\u000f\u0012\b\b\u0002\u0010#\u001a\u00020\u000f\u0012\b\b\u0002\u0010$\u001a\u00020\u000f\u0012\b\b\u0002\u0010%\u001a\u00020\u000f\u0012\b\b\u0002\u0010&\u001a\u00020\'\u0012\b\b\u0002\u0010(\u001a\u00020\u000f\u0012\b\b\u0002\u0010)\u001a\u00020\u000f\u0012\b\b\u0002\u0010*\u001a\u00020\'\u0012\b\b\u0002\u0010+\u001a\u00020\u000f\u0012\b\b\u0002\u0010,\u001a\u00020\u000f\u0012\b\b\u0002\u0010-\u001a\u00020.\u0012\b\b\u0002\u0010/\u001a\u00020\'\u0012\b\b\u0002\u00100\u001a\u00020\'\u0012\b\b\u0002\u00101\u001a\u00020\r\u0012\u000e\b\u0002\u00102\u001a\b\u0012\u0004\u0012\u00020\r0\u0003\u00a2\u0006\u0002\u00103J\u000f\u0010]\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010^\u001a\b\u0012\u0004\u0012\u00020\u00140\u0003H\u00c6\u0003J\t\u0010_\u001a\u00020\u000fH\u00c6\u0003J\t\u0010`\u001a\u00020\u000fH\u00c6\u0003J\u000b\u0010a\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\t\u0010b\u001a\u00020\u000fH\u00c6\u0003J\t\u0010c\u001a\u00020\u000fH\u00c6\u0003J\u000b\u0010d\u001a\u0004\u0018\u00010\u0011H\u00c6\u0003J\u000b\u0010e\u001a\u0004\u0018\u00010\u0011H\u00c6\u0003J\u000b\u0010f\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\t\u0010g\u001a\u00020\u000fH\u00c6\u0003J\t\u0010h\u001a\u00020\u0006H\u00c6\u0003J\u000b\u0010i\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\t\u0010j\u001a\u00020\u000fH\u00c6\u0003J\t\u0010k\u001a\u00020\u000fH\u00c6\u0003J\t\u0010l\u001a\u00020\u000fH\u00c6\u0003J\t\u0010m\u001a\u00020\u000fH\u00c6\u0003J\t\u0010n\u001a\u00020\u000fH\u00c6\u0003J\t\u0010o\u001a\u00020\u000fH\u00c6\u0003J\t\u0010p\u001a\u00020\u000fH\u00c6\u0003J\t\u0010q\u001a\u00020\'H\u00c6\u0003J\t\u0010r\u001a\u00020\u000fH\u00c6\u0003J\t\u0010s\u001a\u00020\bH\u00c6\u0003J\t\u0010t\u001a\u00020\u000fH\u00c6\u0003J\t\u0010u\u001a\u00020\'H\u00c6\u0003J\t\u0010v\u001a\u00020\u000fH\u00c6\u0003J\t\u0010w\u001a\u00020\u000fH\u00c6\u0003J\t\u0010x\u001a\u00020.H\u00c6\u0003J\t\u0010y\u001a\u00020\'H\u00c6\u0003J\t\u0010z\u001a\u00020\'H\u00c6\u0003J\t\u0010{\u001a\u00020\rH\u00c6\u0003J\u000f\u0010|\u001a\b\u0012\u0004\u0012\u00020\r0\u0003H\u00c6\u0003J\t\u0010}\u001a\u00020\bH\u00c6\u0003J\t\u0010~\u001a\u00020\u000bH\u00c6\u0003J\u000f\u0010\u007f\u001a\b\u0012\u0004\u0012\u00020\r0\u0003H\u00c6\u0003J\n\u0010\u0080\u0001\u001a\u00020\u000fH\u00c6\u0003J\f\u0010\u0081\u0001\u001a\u0004\u0018\u00010\u0011H\u00c6\u0003J\f\u0010\u0082\u0001\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\u00ac\u0003\u0010\u0083\u0001\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\r2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u000f2\b\b\u0002\u0010\u0016\u001a\u00020\u000f2\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\r2\b\b\u0002\u0010\u0018\u001a\u00020\u000f2\b\b\u0002\u0010\u0019\u001a\u00020\u000f2\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\r2\b\b\u0002\u0010\u001d\u001a\u00020\u000f2\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\r2\b\b\u0002\u0010\u001f\u001a\u00020\u000f2\b\b\u0002\u0010 \u001a\u00020\u000f2\b\b\u0002\u0010!\u001a\u00020\u000f2\b\b\u0002\u0010\"\u001a\u00020\u000f2\b\b\u0002\u0010#\u001a\u00020\u000f2\b\b\u0002\u0010$\u001a\u00020\u000f2\b\b\u0002\u0010%\u001a\u00020\u000f2\b\b\u0002\u0010&\u001a\u00020\'2\b\b\u0002\u0010(\u001a\u00020\u000f2\b\b\u0002\u0010)\u001a\u00020\u000f2\b\b\u0002\u0010*\u001a\u00020\'2\b\b\u0002\u0010+\u001a\u00020\u000f2\b\b\u0002\u0010,\u001a\u00020\u000f2\b\b\u0002\u0010-\u001a\u00020.2\b\b\u0002\u0010/\u001a\u00020\'2\b\b\u0002\u00100\u001a\u00020\'2\b\b\u0002\u00101\u001a\u00020\r2\u000e\b\u0002\u00102\u001a\b\u0012\u0004\u0012\u00020\r0\u0003H\u00c6\u0001J\u0015\u0010\u0084\u0001\u001a\u00020\u000f2\t\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\n\u0010\u0086\u0001\u001a\u00020\'H\u00d6\u0001J\n\u0010\u0087\u0001\u001a\u00020\rH\u00d6\u0001R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u00105R\u0011\u0010,\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u00107R\u0017\u00102\u001a\b\u0012\u0004\u0012\u00020\r0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u00105R\u0011\u0010-\u001a\u00020.\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010:R\u0011\u0010/\u001a\u00020\'\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010<R\u0011\u00100\u001a\u00020\'\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010<R\u0011\u00101\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010?R\u0013\u0010\u0017\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010?R\u0013\u0010\u001c\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010?R\u0011\u0010&\u001a\u00020\'\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010<R\u0011\u0010*\u001a\u00020\'\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010<R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010ER\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u0010ER\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u00105R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010?R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010JR\u0011\u0010\u0015\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u00107R\u0011\u0010\u001d\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u00107R\u0011\u0010\u001f\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u00107R\u0011\u0010 \u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u00107R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u00107R\u0011\u0010\u0016\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u00107R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010LR\u0011\u0010\"\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u00107R\u0011\u0010%\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u00107R\u0011\u0010#\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u00107R\u0011\u0010$\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bP\u00107R\u0011\u0010)\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bQ\u00107R\u0013\u0010\u001e\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\bR\u0010?R\u0013\u0010\u001a\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bS\u0010LR\u0013\u0010\u001b\u001a\u0004\u0018\u00010\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\bT\u0010LR\u0011\u0010+\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bU\u00107R\u0011\u0010\u0018\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bV\u00107R\u0011\u0010!\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bW\u00107R\u0011\u0010(\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bX\u00107R\u0011\u0010\u0019\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\bY\u00107R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bZ\u00105R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b[\u0010\\\u00a8\u0006\u0088\u0001"}, d2 = {"Lcom/ledger/task/viewmodel/LedgerCenterUiState;", "", "tasks", "", "Lcom/ledger/task/data/model/Task;", "timeRange", "Lcom/ledger/task/data/model/TimeRange;", "customStartDate", "Ljava/time/LocalDate;", "customEndDate", "filterState", "Lcom/ledger/task/data/model/LedgerFilterState;", "allCategories", "", "isExporting", "", "lastExportUri", "Landroid/net/Uri;", "exportMessage", "exportHistory", "Lcom/ledger/task/viewmodel/ExportFileInfo;", "isBackingUp", "isRestoring", "backupMessage", "showBackupPasswordDialog", "showRestorePasswordDialog", "pendingBackupUri", "pendingRestoreUri", "backupPasswordError", "isBackupPasswordProtected", "passwordHint", "isBiometricAvailable", "isBiometricEnabled", "showBiometricSetupDialog", "needsBiometricForBackup", "needsBiometricForRestore", "needsSetupPasswordForBackup", "needsBiometricForDisable", "biometricFailedCount", "", "showPasswordForDisable", "needsShowBackupLauncher", "biometricTriggerId", "showAutoBackupSettingsDialog", "autoBackupEnabled", "autoBackupFrequency", "Lcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;", "autoBackupHour", "autoBackupMinute", "autoBackupNextTime", "autoBackupFiles", "(Ljava/util/List;Lcom/ledger/task/data/model/TimeRange;Ljava/time/LocalDate;Ljava/time/LocalDate;Lcom/ledger/task/data/model/LedgerFilterState;Ljava/util/List;ZLandroid/net/Uri;Ljava/lang/String;Ljava/util/List;ZZLjava/lang/String;ZZLandroid/net/Uri;Landroid/net/Uri;Ljava/lang/String;ZLjava/lang/String;ZZZZZZZIZZIZZLcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;IILjava/lang/String;Ljava/util/List;)V", "getAllCategories", "()Ljava/util/List;", "getAutoBackupEnabled", "()Z", "getAutoBackupFiles", "getAutoBackupFrequency", "()Lcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;", "getAutoBackupHour", "()I", "getAutoBackupMinute", "getAutoBackupNextTime", "()Ljava/lang/String;", "getBackupMessage", "getBackupPasswordError", "getBiometricFailedCount", "getBiometricTriggerId", "getCustomEndDate", "()Ljava/time/LocalDate;", "getCustomStartDate", "getExportHistory", "getExportMessage", "getFilterState", "()Lcom/ledger/task/data/model/LedgerFilterState;", "getLastExportUri", "()Landroid/net/Uri;", "getNeedsBiometricForBackup", "getNeedsBiometricForDisable", "getNeedsBiometricForRestore", "getNeedsSetupPasswordForBackup", "getNeedsShowBackupLauncher", "getPasswordHint", "getPendingBackupUri", "getPendingRestoreUri", "getShowAutoBackupSettingsDialog", "getShowBackupPasswordDialog", "getShowBiometricSetupDialog", "getShowPasswordForDisable", "getShowRestorePasswordDialog", "getTasks", "getTimeRange", "()Lcom/ledger/task/data/model/TimeRange;", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component37", "component38", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class LedgerCenterUiState {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ledger.task.data.model.Task> tasks = null;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.TimeRange timeRange = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDate customStartDate = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDate customEndDate = null;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.LedgerFilterState filterState = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> allCategories = null;
    private final boolean isExporting = false;
    @org.jetbrains.annotations.Nullable
    private final android.net.Uri lastExportUri = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String exportMessage = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ledger.task.viewmodel.ExportFileInfo> exportHistory = null;
    private final boolean isBackingUp = false;
    private final boolean isRestoring = false;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String backupMessage = null;
    private final boolean showBackupPasswordDialog = false;
    private final boolean showRestorePasswordDialog = false;
    @org.jetbrains.annotations.Nullable
    private final android.net.Uri pendingBackupUri = null;
    @org.jetbrains.annotations.Nullable
    private final android.net.Uri pendingRestoreUri = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String backupPasswordError = null;
    private final boolean isBackupPasswordProtected = false;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String passwordHint = null;
    private final boolean isBiometricAvailable = false;
    private final boolean isBiometricEnabled = false;
    private final boolean showBiometricSetupDialog = false;
    private final boolean needsBiometricForBackup = false;
    private final boolean needsBiometricForRestore = false;
    private final boolean needsSetupPasswordForBackup = false;
    private final boolean needsBiometricForDisable = false;
    private final int biometricFailedCount = 0;
    private final boolean showPasswordForDisable = false;
    private final boolean needsShowBackupLauncher = false;
    private final int biometricTriggerId = 0;
    private final boolean showAutoBackupSettingsDialog = false;
    private final boolean autoBackupEnabled = false;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.backup.AutoBackupScheduler.BackupFrequency autoBackupFrequency = null;
    private final int autoBackupHour = 0;
    private final int autoBackupMinute = 0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String autoBackupNextTime = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> autoBackupFiles = null;
    
    /**
     * 台账中心 UI 状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.viewmodel.LedgerCenterUiState copy(@org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> tasks, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TimeRange timeRange, @org.jetbrains.annotations.NotNull
    java.time.LocalDate customStartDate, @org.jetbrains.annotations.NotNull
    java.time.LocalDate customEndDate, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.LedgerFilterState filterState, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> allCategories, boolean isExporting, @org.jetbrains.annotations.Nullable
    android.net.Uri lastExportUri, @org.jetbrains.annotations.Nullable
    java.lang.String exportMessage, @org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.viewmodel.ExportFileInfo> exportHistory, boolean isBackingUp, boolean isRestoring, @org.jetbrains.annotations.Nullable
    java.lang.String backupMessage, boolean showBackupPasswordDialog, boolean showRestorePasswordDialog, @org.jetbrains.annotations.Nullable
    android.net.Uri pendingBackupUri, @org.jetbrains.annotations.Nullable
    android.net.Uri pendingRestoreUri, @org.jetbrains.annotations.Nullable
    java.lang.String backupPasswordError, boolean isBackupPasswordProtected, @org.jetbrains.annotations.Nullable
    java.lang.String passwordHint, boolean isBiometricAvailable, boolean isBiometricEnabled, boolean showBiometricSetupDialog, boolean needsBiometricForBackup, boolean needsBiometricForRestore, boolean needsSetupPasswordForBackup, boolean needsBiometricForDisable, int biometricFailedCount, boolean showPasswordForDisable, boolean needsShowBackupLauncher, int biometricTriggerId, boolean showAutoBackupSettingsDialog, boolean autoBackupEnabled, @org.jetbrains.annotations.NotNull
    com.ledger.task.backup.AutoBackupScheduler.BackupFrequency autoBackupFrequency, int autoBackupHour, int autoBackupMinute, @org.jetbrains.annotations.NotNull
    java.lang.String autoBackupNextTime, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> autoBackupFiles) {
        return null;
    }
    
    /**
     * 台账中心 UI 状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 台账中心 UI 状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 台账中心 UI 状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public LedgerCenterUiState() {
        super();
    }
    
    public LedgerCenterUiState(@org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> tasks, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TimeRange timeRange, @org.jetbrains.annotations.NotNull
    java.time.LocalDate customStartDate, @org.jetbrains.annotations.NotNull
    java.time.LocalDate customEndDate, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.LedgerFilterState filterState, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> allCategories, boolean isExporting, @org.jetbrains.annotations.Nullable
    android.net.Uri lastExportUri, @org.jetbrains.annotations.Nullable
    java.lang.String exportMessage, @org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.viewmodel.ExportFileInfo> exportHistory, boolean isBackingUp, boolean isRestoring, @org.jetbrains.annotations.Nullable
    java.lang.String backupMessage, boolean showBackupPasswordDialog, boolean showRestorePasswordDialog, @org.jetbrains.annotations.Nullable
    android.net.Uri pendingBackupUri, @org.jetbrains.annotations.Nullable
    android.net.Uri pendingRestoreUri, @org.jetbrains.annotations.Nullable
    java.lang.String backupPasswordError, boolean isBackupPasswordProtected, @org.jetbrains.annotations.Nullable
    java.lang.String passwordHint, boolean isBiometricAvailable, boolean isBiometricEnabled, boolean showBiometricSetupDialog, boolean needsBiometricForBackup, boolean needsBiometricForRestore, boolean needsSetupPasswordForBackup, boolean needsBiometricForDisable, int biometricFailedCount, boolean showPasswordForDisable, boolean needsShowBackupLauncher, int biometricTriggerId, boolean showAutoBackupSettingsDialog, boolean autoBackupEnabled, @org.jetbrains.annotations.NotNull
    com.ledger.task.backup.AutoBackupScheduler.BackupFrequency autoBackupFrequency, int autoBackupHour, int autoBackupMinute, @org.jetbrains.annotations.NotNull
    java.lang.String autoBackupNextTime, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> autoBackupFiles) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> getTasks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.TimeRange component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.TimeRange getTimeRange() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate getCustomStartDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate getCustomEndDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.LedgerFilterState component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.LedgerFilterState getFilterState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getAllCategories() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean isExporting() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri getLastExportUri() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getExportMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.viewmodel.ExportFileInfo> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.viewmodel.ExportFileInfo> getExportHistory() {
        return null;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final boolean isBackingUp() {
        return false;
    }
    
    public final boolean component12() {
        return false;
    }
    
    public final boolean isRestoring() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getBackupMessage() {
        return null;
    }
    
    public final boolean component14() {
        return false;
    }
    
    public final boolean getShowBackupPasswordDialog() {
        return false;
    }
    
    public final boolean component15() {
        return false;
    }
    
    public final boolean getShowRestorePasswordDialog() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri component16() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri getPendingBackupUri() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri component17() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri getPendingRestoreUri() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getBackupPasswordError() {
        return null;
    }
    
    public final boolean component19() {
        return false;
    }
    
    public final boolean isBackupPasswordProtected() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component20() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getPasswordHint() {
        return null;
    }
    
    public final boolean component21() {
        return false;
    }
    
    public final boolean isBiometricAvailable() {
        return false;
    }
    
    public final boolean component22() {
        return false;
    }
    
    public final boolean isBiometricEnabled() {
        return false;
    }
    
    public final boolean component23() {
        return false;
    }
    
    public final boolean getShowBiometricSetupDialog() {
        return false;
    }
    
    public final boolean component24() {
        return false;
    }
    
    public final boolean getNeedsBiometricForBackup() {
        return false;
    }
    
    public final boolean component25() {
        return false;
    }
    
    public final boolean getNeedsBiometricForRestore() {
        return false;
    }
    
    public final boolean component26() {
        return false;
    }
    
    public final boolean getNeedsSetupPasswordForBackup() {
        return false;
    }
    
    public final boolean component27() {
        return false;
    }
    
    public final boolean getNeedsBiometricForDisable() {
        return false;
    }
    
    public final int component28() {
        return 0;
    }
    
    public final int getBiometricFailedCount() {
        return 0;
    }
    
    public final boolean component29() {
        return false;
    }
    
    public final boolean getShowPasswordForDisable() {
        return false;
    }
    
    public final boolean component30() {
        return false;
    }
    
    public final boolean getNeedsShowBackupLauncher() {
        return false;
    }
    
    public final int component31() {
        return 0;
    }
    
    public final int getBiometricTriggerId() {
        return 0;
    }
    
    public final boolean component32() {
        return false;
    }
    
    public final boolean getShowAutoBackupSettingsDialog() {
        return false;
    }
    
    public final boolean component33() {
        return false;
    }
    
    public final boolean getAutoBackupEnabled() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.backup.AutoBackupScheduler.BackupFrequency component34() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.backup.AutoBackupScheduler.BackupFrequency getAutoBackupFrequency() {
        return null;
    }
    
    public final int component35() {
        return 0;
    }
    
    public final int getAutoBackupHour() {
        return 0;
    }
    
    public final int component36() {
        return 0;
    }
    
    public final int getAutoBackupMinute() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component37() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getAutoBackupNextTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component38() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getAutoBackupFiles() {
        return null;
    }
}