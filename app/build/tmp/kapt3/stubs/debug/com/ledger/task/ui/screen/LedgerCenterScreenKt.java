package com.ledger.task.ui.screen;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\"\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001ar\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00032\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2$\u0010\r\u001a \u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0010H\u0003\u001aL\u0010\u0011\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\u001c\u0010\u0012\u001a\u0018\u0012\u0006\u0012\u0004\u0018\u00010\f\u0012\u0006\u0012\u0004\u0018\u00010\f\u0012\u0004\u0012\u00020\u00010\u00132\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0003H\u0003\u001az\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\f2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\b\b\u0002\u0010\"\u001a\u00020#H\u0003\u001aR\u0010$\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00032\u0012\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010&2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\u0006\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010(\u001a\u00020\u0003H\u0003\u001a^\u0010)\u001a\u00020\u00012\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\f0,2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010&2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\b\b\u0002\u0010\"\u001a\u00020#H\u0003\u001a`\u00100\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u00101\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u00102\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u00104\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\b\u00105\u001a\u0004\u0018\u0001062\u0006\u00107\u001a\u00020\u0003H\u0003\u001a0\u00108\u001a\u00020\u00012\f\u00109\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\u0006\u00107\u001a\u00020\u00032\u0006\u0010:\u001a\u00020\u00072\b\b\u0002\u0010\"\u001a\u00020#H\u0003\u001a*\u0010;\u001a\u00020\u00012\u0006\u0010<\u001a\u00020=2\u000e\b\u0002\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\b\b\u0002\u0010\"\u001a\u00020#H\u0007\u001a<\u0010?\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010&2\u0006\u0010\u0014\u001a\u00020\f2\b\u0010@\u001a\u0004\u0018\u00010\fH\u0003\u001aT\u0010A\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\u0012\u0010D\u001a\u000e\u0012\u0004\u0012\u000206\u0012\u0004\u0012\u00020\u00010&2\f\u0010E\u001a\b\u0012\u0004\u0012\u00020F0\u000bH\u0003\u001a\u0018\u0010G\u001a\u00020\u00012\u0006\u0010H\u001a\u00020\f2\u0006\u0010I\u001a\u00020\fH\u0003\u001a \u0010J\u001a\u00020\u00012\f\u0010K\u001a\b\u0012\u0004\u0012\u00020L0\u000b2\b\b\u0002\u0010\"\u001a\u00020#H\u0003\u001aZ\u0010M\u001a\u00020\u00012\u0006\u0010N\u001a\u00020O2\u0006\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u00020Q2\u0012\u0010S\u001a\u000e\u0012\u0004\u0012\u00020O\u0012\u0004\u0012\u00020\u00010&2\f\u0010T\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010U\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\b\b\u0002\u0010\"\u001a\u00020#H\u0003\u00a8\u0006V"}, d2 = {"AutoBackupSettingsDialog", "", "currentEnabled", "", "currentFrequency", "Lcom/ledger/task/backup/AutoBackupScheduler$BackupFrequency;", "currentHour", "", "currentMinute", "hasBackupPassword", "backupFiles", "", "", "onSave", "Lkotlin/Function4;", "onDismiss", "Lkotlin/Function0;", "BackupPasswordDialog", "onConfirm", "Lkotlin/Function2;", "title", "description", "isOptional", "BackupRestoreSection", "isBackingUp", "isRestoring", "isBiometricAvailable", "isBiometricEnabled", "autoBackupEnabled", "autoBackupNextTime", "onBackup", "onRestore", "onBiometricSettings", "onAutoBackupSettings", "modifier", "Landroidx/compose/ui/Modifier;", "BiometricSetupDialog", "onEnable", "Lkotlin/Function1;", "onDisable", "needsSetupPassword", "CategoryFilterSection", "categories", "selectedCategories", "", "onToggle", "onSelectAll", "onDeselectAll", "ExportDialog", "onExportCSV", "onExportExcel", "onShare", "onOpenFileLocation", "lastExportUri", "Landroid/net/Uri;", "isExporting", "ExportSection", "onExport", "taskCount", "LedgerCenterScreen", "viewModel", "Lcom/ledger/task/viewmodel/LedgerCenterViewModel;", "onNavigateToAdd", "RestorePasswordDialog", "passwordHint", "ShareDialog", "onShareNewCSV", "onShareNewExcel", "onShareExisting", "exportHistory", "Lcom/ledger/task/viewmodel/ExportFileInfo;", "StatItem", "label", "value", "TaskStatisticsSection", "tasks", "Lcom/ledger/task/data/model/Task;", "TimeRangeSelectorSection", "selectedRange", "Lcom/ledger/task/data/model/TimeRange;", "customStartDate", "Ljava/time/LocalDate;", "customEndDate", "onRangeChange", "onShowStartDatePicker", "onShowEndDatePicker", "app_debug"})
public final class LedgerCenterScreenKt {
    
    /**
     * 台账中心屏幕
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void LedgerCenterScreen(@org.jetbrains.annotations.NotNull
    com.ledger.task.viewmodel.LedgerCenterViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToAdd, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 时间范围选择器区域
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    private static final void TimeRangeSelectorSection(com.ledger.task.data.model.TimeRange selectedRange, java.time.LocalDate customStartDate, java.time.LocalDate customEndDate, kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.TimeRange, kotlin.Unit> onRangeChange, kotlin.jvm.functions.Function0<kotlin.Unit> onShowStartDatePicker, kotlin.jvm.functions.Function0<kotlin.Unit> onShowEndDatePicker, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 分类筛选区域
     */
    @androidx.compose.runtime.Composable
    private static final void CategoryFilterSection(java.util.List<java.lang.String> categories, java.util.Set<java.lang.String> selectedCategories, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggle, kotlin.jvm.functions.Function0<kotlin.Unit> onSelectAll, kotlin.jvm.functions.Function0<kotlin.Unit> onDeselectAll, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 任务统计区域
     */
    @androidx.compose.runtime.Composable
    private static final void TaskStatisticsSection(java.util.List<com.ledger.task.data.model.Task> tasks, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    private static final void StatItem(java.lang.String label, java.lang.String value) {
    }
    
    /**
     * 导出区域
     */
    @androidx.compose.runtime.Composable
    private static final void ExportSection(kotlin.jvm.functions.Function0<kotlin.Unit> onExport, boolean isExporting, int taskCount, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 导出选项对话框
     */
    @androidx.compose.runtime.Composable
    private static final void ExportDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function0<kotlin.Unit> onExportCSV, kotlin.jvm.functions.Function0<kotlin.Unit> onExportExcel, kotlin.jvm.functions.Function0<kotlin.Unit> onShare, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenFileLocation, android.net.Uri lastExportUri, boolean isExporting) {
    }
    
    /**
     * 分享选项对话框
     */
    @androidx.compose.runtime.Composable
    private static final void ShareDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function0<kotlin.Unit> onShareNewCSV, kotlin.jvm.functions.Function0<kotlin.Unit> onShareNewExcel, kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> onShareExisting, java.util.List<com.ledger.task.viewmodel.ExportFileInfo> exportHistory) {
    }
    
    /**
     * 备份恢复区域
     */
    @androidx.compose.runtime.Composable
    private static final void BackupRestoreSection(boolean isBackingUp, boolean isRestoring, boolean isBiometricAvailable, boolean isBiometricEnabled, boolean autoBackupEnabled, java.lang.String autoBackupNextTime, kotlin.jvm.functions.Function0<kotlin.Unit> onBackup, kotlin.jvm.functions.Function0<kotlin.Unit> onRestore, kotlin.jvm.functions.Function0<kotlin.Unit> onBiometricSettings, kotlin.jvm.functions.Function0<kotlin.Unit> onAutoBackupSettings, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 备份密码对话框
     */
    @androidx.compose.runtime.Composable
    private static final void BackupPasswordDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onConfirm, java.lang.String title, java.lang.String description, boolean isOptional) {
    }
    
    /**
     * 恢复密码对话框
     */
    @androidx.compose.runtime.Composable
    private static final void RestorePasswordDialog(kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onConfirm, java.lang.String title, java.lang.String passwordHint) {
    }
    
    /**
     * 生物识别设置对话框
     */
    @androidx.compose.runtime.Composable
    private static final void BiometricSetupDialog(boolean isBiometricEnabled, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onEnable, kotlin.jvm.functions.Function0<kotlin.Unit> onDisable, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, boolean hasBackupPassword, boolean needsSetupPassword) {
    }
    
    /**
     * 自动备份设置对话框
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    private static final void AutoBackupSettingsDialog(boolean currentEnabled, com.ledger.task.backup.AutoBackupScheduler.BackupFrequency currentFrequency, int currentHour, int currentMinute, boolean hasBackupPassword, java.util.List<java.lang.String> backupFiles, kotlin.jvm.functions.Function4<? super java.lang.Boolean, ? super com.ledger.task.backup.AutoBackupScheduler.BackupFrequency, ? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}