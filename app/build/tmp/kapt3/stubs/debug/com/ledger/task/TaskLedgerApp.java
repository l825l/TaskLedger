package com.ledger.task;

import java.lang.System;

/**
 * Application 类，提供全局依赖
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0004H\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u0006\u0010\u0015\u001a\u00020\u0014J\b\u0010\u0016\u001a\u00020\u0014H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001b\u0010\f\u001a\u00020\r8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0018"}, d2 = {"Lcom/ledger/task/TaskLedgerApp;", "Landroid/app/Application;", "()V", "_database", "Lcom/ledger/task/data/local/AppDatabase;", "appScope", "Landroidx/lifecycle/LifecycleCoroutineScope;", "getAppScope", "()Landroidx/lifecycle/LifecycleCoroutineScope;", "database", "getDatabase", "()Lcom/ledger/task/data/local/AppDatabase;", "repository", "Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "getRepository", "()Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "repository$delegate", "Lkotlin/Lazy;", "createDatabase", "onCreate", "", "resetDatabase", "seedInitialData", "Companion", "app_debug"})
public final class TaskLedgerApp extends android.app.Application {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.TaskLedgerApp.Companion Companion = null;
    private static final java.lang.String TAG = "TaskLedgerApp";
    private com.ledger.task.data.local.AppDatabase _database;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy repository$delegate = null;
    
    public TaskLedgerApp() {
        super();
    }
    
    private final androidx.lifecycle.LifecycleCoroutineScope getAppScope() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.local.AppDatabase getDatabase() {
        return null;
    }
    
    private final com.ledger.task.data.local.AppDatabase createDatabase() {
        return null;
    }
    
    /**
     * 重置数据库（用于恢复备份后重新初始化）
     */
    public final void resetDatabase() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.repository.TaskRepositoryImpl getRepository() {
        return null;
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    private final void seedInitialData() {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/ledger/task/TaskLedgerApp$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}