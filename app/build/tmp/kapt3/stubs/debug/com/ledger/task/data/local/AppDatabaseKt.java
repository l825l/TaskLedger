package com.ledger.task.data.local;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u001a\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\"\u0011\u0010\u0000\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u0011\u0010\u0004\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0003\u00a8\u0006\t"}, d2 = {"MIGRATION_4_5", "Landroidx/room/migration/Migration;", "getMIGRATION_4_5", "()Landroidx/room/migration/Migration;", "MIGRATION_5_6", "getMIGRATION_5_6", "provideSampleData", "", "Lcom/ledger/task/data/local/TaskEntity;", "app_debug"})
public final class AppDatabaseKt {
    
    /**
     * 数据库迁移：版本 4 -> 5，添加 completedAt 字段
     */
    @org.jetbrains.annotations.NotNull
    private static final androidx.room.migration.Migration MIGRATION_4_5 = null;
    
    /**
     * 数据库迁移：版本 5 -> 6，启用加密
     * 注意：此迁移在 SQLCipher 上下文中执行，数据已通过加密密钥访问
     */
    @org.jetbrains.annotations.NotNull
    private static final androidx.room.migration.Migration MIGRATION_5_6 = null;
    
    @org.jetbrains.annotations.NotNull
    public static final androidx.room.migration.Migration getMIGRATION_4_5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final androidx.room.migration.Migration getMIGRATION_5_6() {
        return null;
    }
    
    /**
     * 预置示例数据
     */
    @org.jetbrains.annotations.NotNull
    public static final java.util.List<com.ledger.task.data.local.TaskEntity> provideSampleData() {
        return null;
    }
}