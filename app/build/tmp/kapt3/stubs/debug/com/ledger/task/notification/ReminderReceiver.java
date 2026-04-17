package com.ledger.task.notification;

import java.lang.System;

/**
 * 提醒广播接收器
 * 接收 AlarmManager 发送的提醒广播并显示通知
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016\u00a8\u0006\n"}, d2 = {"Lcom/ledger/task/notification/ReminderReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "Companion", "app_debug"})
public final class ReminderReceiver extends android.content.BroadcastReceiver {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.notification.ReminderReceiver.Companion Companion = null;
    private static final java.lang.String TAG = "ReminderReceiver";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_TASK_ID = "task_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_TASK_TITLE = "task_title";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_REMINDER_TYPE = "reminder_type";
    
    public ReminderReceiver() {
        super();
    }
    
    @java.lang.Override
    public void onReceive(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.content.Intent intent) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/ledger/task/notification/ReminderReceiver$Companion;", "", "()V", "EXTRA_REMINDER_TYPE", "", "EXTRA_TASK_ID", "EXTRA_TASK_TITLE", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}