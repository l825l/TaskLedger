package com.ledger.task.ui.util;

import java.lang.System;

/**
 * CSV 导出工具
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J(\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\b\b\u0002\u0010\r\u001a\u00020\u0004J(\u0010\u000e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\b\b\u0002\u0010\r\u001a\u00020\u0004J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/ledger/task/ui/util/CsvExporter;", "", "()V", "escapeXml", "", "text", "exportToDownloads", "Landroid/net/Uri;", "context", "Landroid/content/Context;", "tasks", "", "Lcom/ledger/task/data/model/Task;", "fileName", "exportToExcel", "share", "", "uri", "app_debug"})
public final class CsvExporter {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.ui.util.CsvExporter INSTANCE = null;
    
    private CsvExporter() {
        super();
    }
    
    /**
     * 导出任务列表为 CSV 文件（带 BOM 头，Excel 中文兼容）
     * 保存到 Downloads 目录，返回文件 Uri
     */
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri exportToDownloads(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> tasks, @org.jetbrains.annotations.NotNull
    java.lang.String fileName) {
        return null;
    }
    
    /**
     * 分享 CSV 文件
     */
    public final void share(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.net.Uri uri) {
    }
    
    /**
     * 导出任务列表为 Excel (.xlsx) 文件
     * 使用简单的 XML 格式（Excel XML Spreadsheet）
     */
    @org.jetbrains.annotations.Nullable
    public final android.net.Uri exportToExcel(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> tasks, @org.jetbrains.annotations.NotNull
    java.lang.String fileName) {
        return null;
    }
    
    /**
     * 转义 XML 特殊字符
     */
    private final java.lang.String escapeXml(java.lang.String text) {
        return null;
    }
}