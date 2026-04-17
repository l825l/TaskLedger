package com.ledger.task.notification;

import java.lang.System;

/**
 * 通知助手
 * 负责创建通知渠道和发送任务提醒通知
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ&\u0010\u000e\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/ledger/task/notification/NotificationHelper;", "", "()V", "CHANNEL_DESCRIPTION", "", "CHANNEL_ID", "CHANNEL_NAME", "cancelNotification", "", "context", "Landroid/content/Context;", "taskId", "", "createNotificationChannel", "showTaskReminder", "title", "message", "app_debug"})
public final class NotificationHelper {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.notification.NotificationHelper INSTANCE = null;
    private static final java.lang.String CHANNEL_ID = "task_reminder";
    private static final java.lang.String CHANNEL_NAME = "\u4efb\u52a1\u63d0\u9192";
    private static final java.lang.String CHANNEL_DESCRIPTION = "\u4efb\u52a1\u622a\u6b62\u65f6\u95f4\u63d0\u9192\u901a\u77e5";
    
    private NotificationHelper() {
        super();
    }
    
    /**
     * 创建通知渠道（Android 8.0+）
     */
    public final void createNotificationChannel(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    /**
     * 发送任务提醒通知
     */
    public final void showTaskReminder(@org.jetbrains.annotations.NotNull
    android.content.Context context, long taskId, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String message) {
    }
    
    /**
     * 取消任务提醒通知
     */
    public final void cancelNotification(@org.jetbrains.annotations.NotNull
    android.content.Context context, long taskId) {
    }
}