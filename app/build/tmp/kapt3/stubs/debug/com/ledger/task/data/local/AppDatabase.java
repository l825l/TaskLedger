package com.ledger.task.data.local;

import java.lang.System;

/**
 * Room 数据库
 * 版本 6：启用 SQLCipher 加密
 */
@androidx.room.TypeConverters(value = {com.ledger.task.data.local.Converters.class})
@androidx.room.Database(entities = {com.ledger.task.data.local.TaskEntity.class}, version = 6, exportSchema = false)
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0005"}, d2 = {"Lcom/ledger/task/data/local/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "taskDao", "Lcom/ledger/task/data/local/TaskDao;", "app_debug"})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract com.ledger.task.data.local.TaskDao taskDao();
}