package com.ledger.task.viewmodel;

import java.lang.System;

/**
 * 台账中心 ViewModel
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u00ac\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010$\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0007\u0018\u0000 t2\u00020\u0001:\u0001tB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\u0006\u0010\u001f\u001a\u00020 J\u0006\u0010!\u001a\u00020 J\u0006\u0010\"\u001a\u00020 J\u0006\u0010#\u001a\u00020 J\u0006\u0010$\u001a\u00020 J\u001c\u0010%\u001a\u00020 2\b\u0010&\u001a\u0004\u0018\u00010\u001e2\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010\u001eJ\u0010\u0010(\u001a\u00020 2\b\u0010&\u001a\u0004\u0018\u00010\u001eJ\u000e\u0010)\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010*\u001a\u00020+2\u0006\u0010&\u001a\u00020\u001eJ\u0006\u0010,\u001a\u00020 J\u0006\u0010-\u001a\u00020 J\u0016\u0010.\u001a\u00020 2\u0006\u0010/\u001a\u0002002\u0006\u0010&\u001a\u00020\u001eJ\u0006\u00101\u001a\u00020 J\u0006\u00102\u001a\u00020 J\u0006\u00103\u001a\u00020 J\u0006\u00104\u001a\u00020 J\u0006\u00105\u001a\u00020 J\u0006\u00106\u001a\u00020 J\u0006\u00107\u001a\u00020 J\u0006\u00108\u001a\u00020 J\f\u00109\u001a\b\u0012\u0004\u0012\u00020\u001e0\u0019J\u0006\u0010:\u001a\u00020\tJ\u0006\u0010;\u001a\u00020\u000eJ+\u0010<\u001a\u000e\u0012\u0004\u0012\u00020>\u0012\u0004\u0012\u00020\u001e0=2\f\u0010?\u001a\b\u0012\u0004\u0012\u00020@0\u0019H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010AJ\u0006\u0010B\u001a\u00020+J\b\u0010C\u001a\u00020 H\u0002J\b\u0010D\u001a\u00020 H\u0002J\u0006\u0010E\u001a\u00020 J\u0006\u0010F\u001a\u00020 J\u0006\u0010G\u001a\u00020 J\u0006\u0010H\u001a\u00020 J\u000e\u0010I\u001a\u00020 2\u0006\u0010J\u001a\u00020\u001eJ\u0016\u0010K\u001a\u00020 2\u0006\u0010L\u001a\u00020M2\u0006\u0010N\u001a\u00020MJ\u0006\u0010O\u001a\u00020 J\u000e\u0010P\u001a\u00020 2\u0006\u0010Q\u001a\u00020+J\u0006\u0010R\u001a\u00020 J\u000e\u0010S\u001a\u00020 2\u0006\u0010T\u001a\u00020UJ\u0006\u0010V\u001a\u00020 J\u0006\u0010W\u001a\u00020 J\b\u0010X\u001a\u00020 H\u0002J\u0006\u0010Y\u001a\u00020 J\u001a\u0010Z\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020\u001c2\b\u0010&\u001a\u0004\u0018\u00010\u001eH\u0002J\u0006\u0010[\u001a\u00020 J\u0006\u0010\\\u001a\u00020 J\u0006\u0010]\u001a\u00020 J\u0006\u0010^\u001a\u00020 J\u0010\u0010_\u001a\u00020 2\u0006\u0010`\u001a\u00020aH\u0002J\u000e\u0010b\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020\u001cJ&\u0010c\u001a\u00020 2\u0006\u0010d\u001a\u00020+2\u0006\u0010e\u001a\u00020f2\u0006\u0010g\u001a\u00020h2\u0006\u0010i\u001a\u00020hJ\u000e\u0010j\u001a\u00020+2\u0006\u0010&\u001a\u00020\u001eJ\u0016\u0010k\u001a\u00020 2\f\u0010l\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019H\u0002J\u000e\u0010m\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019H\u0002J\u000e\u0010n\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010o\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010p\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020\u001cJ\u0006\u0010q\u001a\u00020 J\u0006\u0010r\u001a\u00020 J\u0006\u0010s\u001a\u00020 R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n \u0011*\u0004\u0018\u00010\u00100\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006u"}, d2 = {"Lcom/ledger/task/viewmodel/LedgerCenterViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ledger/task/viewmodel/LedgerCenterUiState;", "biometricAuthManager", "Lcom/ledger/task/backup/BiometricAuthManager;", "loadCategoriesJob", "Lkotlinx/coroutines/Job;", "loadLedgerJob", "passwordStorage", "Lcom/ledger/task/backup/BackupPasswordStorage;", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "repository", "Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addToExportHistory", "", "Lcom/ledger/task/viewmodel/ExportFileInfo;", "uri", "Landroid/net/Uri;", "extension", "", "cancelBackupPasswordDialog", "", "cancelDisableBiometric", "cancelRestorePasswordDialog", "clearBackupMessage", "clearExportMessage", "confirmBackup", "password", "passwordHint", "confirmRestore", "createBackup", "disableBiometricWithPassword", "", "dismissAutoBackupSettingsDialog", "dismissBiometricSetupDialog", "enableBiometricAccess", "activity", "Landroidx/fragment/app/FragmentActivity;", "exportAndShare", "exportAndShareCSV", "exportAndShareEmail", "exportAndShareExcel", "exportAndShareWechat", "exportCSV", "exportExcel", "fallbackToPasswordForRestore", "getAutoBackupFiles", "getBiometricAuthManager", "getPasswordStorage", "getRelatedTaskSummaries", "", "", "tasks", "Lcom/ledger/task/data/model/Task;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hasBackupPassword", "loadBiometricAndAutoBackupSettings", "loadExportHistory", "loadLedgerData", "onBackupLauncherShown", "onBiometricFailedForDisable", "onBiometricFailedForRestore", "onCategoryToggle", "category", "onCustomDateRangeChange", "startDate", "Ljava/time/LocalDate;", "endDate", "onDeselectAllCategories", "onIncludeArchivedChange", "include", "onSelectAllCategories", "onTimeRangeChange", "range", "Lcom/ledger/task/data/model/TimeRange;", "openExportFileLocation", "performBackupAfterBiometricSetup", "performBackupDirectly", "performDisableBiometricAccess", "performRestore", "performRestoreWithBiometric", "refreshExportHistory", "requestCreateBackup", "requestDisableBiometricAccess", "restartApp", "context", "Landroid/content/Context;", "restoreFromBackup", "saveAutoBackupSettings", "enabled", "frequency", "Lcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;", "hour", "", "minute", "saveBackupPassword", "saveExportHistory", "history", "scanDownloadDirectoryForExportFiles", "shareExistingFile", "shareToEmail", "shareToWechat", "showAutoBackupSettingsDialog", "showBiometricSetupDialog", "testAutoBackup", "Companion", "app_debug"})
public final class LedgerCenterViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.viewmodel.LedgerCenterViewModel.Companion Companion = null;
    private static final java.lang.String TAG = "LedgerCenterViewModel";
    private static final java.lang.String PREFS_NAME = "export_history";
    private static final java.lang.String KEY_HISTORY = "history";
    private static final int MAX_HISTORY = 50;
    private final com.ledger.task.data.repository.TaskRepositoryImpl repository = null;
    private final android.content.SharedPreferences prefs = null;
    private final com.ledger.task.backup.BackupPasswordStorage passwordStorage = null;
    private final com.ledger.task.backup.BiometricAuthManager biometricAuthManager = null;
    private kotlinx.coroutines.Job loadLedgerJob;
    private kotlinx.coroutines.Job loadCategoriesJob;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ledger.task.viewmodel.LedgerCenterUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.LedgerCenterUiState> uiState = null;
    
    public LedgerCenterViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.LedgerCenterUiState> getUiState() {
        return null;
    }
    
    /**
     * 加载生物识别和自动备份设置
     */
    private final void loadBiometricAndAutoBackupSettings() {
    }
    
    /**
     * 从 SharedPreferences 加载导出历史
     */
    private final void loadExportHistory() {
    }
    
    /**
     * 刷新导出历史（扫描下载目录，过滤已删除文件）
     */
    public final void refreshExportHistory() {
    }
    
    /**
     * 扫描下载目录中的导出文件
     */
    private final java.util.List<com.ledger.task.viewmodel.ExportFileInfo> scanDownloadDirectoryForExportFiles() {
        return null;
    }
    
    /**
     * 保存导出历史到 SharedPreferences
     */
    private final void saveExportHistory(java.util.List<com.ledger.task.viewmodel.ExportFileInfo> history) {
    }
    
    public final void loadLedgerData() {
    }
    
    public final void onTimeRangeChange(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TimeRange range) {
    }
    
    public final void onCustomDateRangeChange(@org.jetbrains.annotations.NotNull
    java.time.LocalDate startDate, @org.jetbrains.annotations.NotNull
    java.time.LocalDate endDate) {
    }
    
    public final void onCategoryToggle(@org.jetbrains.annotations.NotNull
    java.lang.String category) {
    }
    
    public final void onSelectAllCategories() {
    }
    
    public final void onDeselectAllCategories() {
    }
    
    public final void onIncludeArchivedChange(boolean include) {
    }
    
    /**
     * 导出为 CSV（在 IO 线程执行）
     */
    public final void exportCSV() {
    }
    
    /**
     * 导出为 Excel（在 IO 线程执行）
     */
    public final void exportExcel() {
    }
    
    /**
     * 添加到导出历史
     */
    private final java.util.List<com.ledger.task.viewmodel.ExportFileInfo> addToExportHistory(android.net.Uri uri, java.lang.String extension) {
        return null;
    }
    
    /**
     * 分享到微信（如果没有导出文件则先导出）
     */
    public final void shareToWechat(@org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    /**
     * 分享到邮件
     */
    public final void shareToEmail(@org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    /**
     * 导出并分享到微信
     */
    public final void exportAndShareWechat() {
    }
    
    /**
     * 导出并分享到邮件
     */
    public final void exportAndShareEmail() {
    }
    
    /**
     * 导出为 CSV 并分享
     */
    public final void exportAndShareCSV() {
    }
    
    /**
     * 导出为 Excel 并分享
     */
    public final void exportAndShareExcel() {
    }
    
    /**
     * 导出并分享到其他应用（保留旧方法兼容）
     */
    public final void exportAndShare() {
    }
    
    /**
     * 分享已导出的文件
     */
    public final void shareExistingFile(@org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    /**
     * 清除导出消息
     */
    public final void clearExportMessage() {
    }
    
    /**
     * 打开导出文件所在位置
     */
    public final void openExportFileLocation() {
    }
    
    /**
     * 获取关联事项摘要（批量查询优化）
     * 使用 suspend 函数确保协程正确执行
     */
    private final java.lang.Object getRelatedTaskSummaries(java.util.List<com.ledger.task.data.model.Task> tasks, kotlin.coroutines.Continuation<? super java.util.Map<java.lang.Long, java.lang.String>> continuation) {
        return null;
    }
    
    /**
     * 请求创建备份（先检测密码）
     */
    public final void requestCreateBackup() {
    }
    
    /**
     * 备份文件保存对话框已显示
     */
    public final void onBackupLauncherShown() {
    }
    
    /**
     * 创建备份（文件已选择）
     */
    public final void createBackup(@org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    /**
     * 直接执行备份（生物识别已启用时）
     */
    private final void performBackupDirectly() {
    }
    
    /**
     * 生物识别设置完成后，显示备份保存对话框
     */
    public final void performBackupAfterBiometricSetup() {
    }
    
    /**
     * 确认创建备份（带密码和密码提示）
     */
    public final void confirmBackup(@org.jetbrains.annotations.Nullable
    java.lang.String password, @org.jetbrains.annotations.Nullable
    java.lang.String passwordHint) {
    }
    
    /**
     * 取消备份密码对话框
     */
    public final void cancelBackupPasswordDialog() {
    }
    
    /**
     * 从备份恢复（检查是否需要密码，优先使用生物识别）
     */
    public final void restoreFromBackup(@org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    /**
     * 生物识别验证成功后执行恢复
     */
    public final void performRestoreWithBiometric() {
    }
    
    /**
     * 生物识别验证失败，检查是否需要回退到密码输入
     */
    public final void onBiometricFailedForRestore() {
    }
    
    /**
     * 生物识别错误，回退到密码输入
     */
    public final void fallbackToPasswordForRestore() {
    }
    
    /**
     * 确认恢复（带密码）
     */
    public final void confirmRestore(@org.jetbrains.annotations.Nullable
    java.lang.String password) {
    }
    
    /**
     * 执行恢复
     */
    private final void performRestore(android.net.Uri uri, java.lang.String password) {
    }
    
    /**
     * 取消恢复密码对话框
     */
    public final void cancelRestorePasswordDialog() {
    }
    
    /**
     * 重启应用
     */
    private final void restartApp(android.content.Context context) {
    }
    
    /**
     * 清除备份消息
     */
    public final void clearBackupMessage() {
    }
    
    /**
     * 获取生物识别管理器（用于 UI 层调用认证）
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.backup.BiometricAuthManager getBiometricAuthManager() {
        return null;
    }
    
    /**
     * 获取密码存储（用于 UI 层访问）
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.backup.BackupPasswordStorage getPasswordStorage() {
        return null;
    }
    
    /**
     * 显示生物识别设置对话框
     */
    public final void showBiometricSetupDialog() {
    }
    
    /**
     * 关闭生物识别设置对话框
     */
    public final void dismissBiometricSetupDialog() {
    }
    
    /**
     * 启用生物识别快捷访问
     * 需要先通过生物识别认证，然后才能加密密码
     * @param activity FragmentActivity 用于显示生物识别对话框
     * @param password 备份密码
     */
    public final void enableBiometricAccess(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull
    java.lang.String password) {
    }
    
    /**
     * 请求禁用生物识别快捷访问（需要生物识别验证）
     */
    public final void requestDisableBiometricAccess() {
    }
    
    /**
     * 生物识别验证成功后禁用生物识别
     */
    public final void performDisableBiometricAccess() {
    }
    
    /**
     * 生物识别验证失败，检查是否需要回退到密码输入
     */
    public final void onBiometricFailedForDisable() {
    }
    
    /**
     * 密码验证关闭生物识别
     */
    public final boolean disableBiometricWithPassword(@org.jetbrains.annotations.NotNull
    java.lang.String password) {
        return false;
    }
    
    /**
     * 取消禁用生物识别
     */
    public final void cancelDisableBiometric() {
    }
    
    /**
     * 保存备份密码（用于自动备份）
     */
    public final boolean saveBackupPassword(@org.jetbrains.annotations.NotNull
    java.lang.String password) {
        return false;
    }
    
    /**
     * 检查是否已设置备份密码
     */
    public final boolean hasBackupPassword() {
        return false;
    }
    
    /**
     * 显示自动备份设置对话框
     */
    public final void showAutoBackupSettingsDialog() {
    }
    
    /**
     * 关闭自动备份设置对话框
     */
    public final void dismissAutoBackupSettingsDialog() {
    }
    
    /**
     * 保存自动备份设置
     */
    public final void saveAutoBackupSettings(boolean enabled, @org.jetbrains.annotations.NotNull
    com.ledger.task.backup.AutoBackupScheduler.BackupFrequency frequency, int hour, int minute) {
    }
    
    /**
     * 立即测试自动备份
     */
    public final void testAutoBackup() {
    }
    
    /**
     * 获取自动备份文件列表（从下载目录）
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getAutoBackupFiles() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/ledger/task/viewmodel/LedgerCenterViewModel$Companion;", "", "()V", "KEY_HISTORY", "", "MAX_HISTORY", "", "PREFS_NAME", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}