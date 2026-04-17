package com.ledger.task.notification;

import java.lang.System;

/**
 * 提醒管理器
 * 负责调度和取消任务提醒
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004J@\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J&\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u0018R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/ledger/task/notification/ReminderManager;", "", "()V", "REMINDER_15_MIN", "", "REMINDER_1_DAY", "REMINDER_1_HOUR", "TAG", "", "cancelReminder", "", "context", "Landroid/content/Context;", "taskId", "scheduleAlarm", "alarmManager", "Landroid/app/AlarmManager;", "title", "triggerTime", "reminderType", "requestCode", "", "scheduleReminder", "deadline", "Ljava/time/LocalDateTime;", "app_debug"})
public final class ReminderManager {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.notification.ReminderManager INSTANCE = null;
    private static final java.lang.String TAG = "ReminderManager";
    private static final long REMINDER_15_MIN = 900000L;
    private static final long REMINDER_1_HOUR = 3600000L;
    private static final long REMINDER_1_DAY = 86400000L;
    
    private ReminderManager() {
        super();
    }
    
    /**
     * 调度任务提醒
     * @param context 上下文
     * @param taskId 任务ID
     * @param title 任务标题
     * @param deadline 截止时间
     */
    public final void scheduleReminder(@org.jetbrains.annotations.NotNull
    android.content.Context context, long taskId, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime deadline) {
    }
    
    /**
     * 调度单个闹钟
     */
    private final void scheduleAlarm(android.content.Context context, android.app.AlarmManager alarmManager, long taskId, java.lang.String title, long triggerTime, java.lang.String reminderType, int requestCode) {
    }
    
    /**
     * 取消任务提醒
     */
    public final void cancelReminder(@org.jetbrains.annotations.NotNull
    android.content.Context context, long taskId) {
    }
}