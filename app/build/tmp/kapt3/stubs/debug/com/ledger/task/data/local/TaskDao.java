package com.ledger.task.data.local;

import java.lang.System;

/**
 * 任务数据访问对象
 */
@androidx.room.Dao
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0019\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\'J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\'J\u000e\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\'J\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\b\u001a\u00020\tH\'J\u0019\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJA\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00100\u00032\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00122\b\u0010\u0014\u001a\u0004\u0018\u00010\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\'\u00a2\u0006\u0002\u0010\u0017J\u0014\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00100\u0003H\'J\u0014\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00100\u0003H\'J\u001b\u0010\u001a\u001a\u0004\u0018\u00010\r2\u0006\u0010\u001b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cJ6\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00100\u00032\u0006\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\u00162\b\u0010!\u001a\u0004\u0018\u00010\u0012H\'J\u001c\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00100\u00032\u0006\u0010#\u001a\u00020\tH\'J\"\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00100\u00032\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\t0\u0010H\'J$\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00100\u00032\u0006\u0010\'\u001a\u00020\t2\u0006\u0010(\u001a\u00020\tH\'J\u0019\u0010)\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ&\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00100\u00032\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0012H\'J\u0019\u0010+\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ!\u0010,\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\t2\u0006\u0010-\u001a\u00020\u0004H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010.\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006/"}, d2 = {"Lcom/ledger/task/data/local/TaskDao;", "", "countAll", "Lkotlinx/coroutines/flow/Flow;", "", "countDone", "countInProgress", "countOverdue", "todayEpochDay", "", "delete", "", "task", "Lcom/ledger/task/data/local/TaskEntity;", "(Lcom/ledger/task/data/local/TaskEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "filterAllTasks", "", "query", "", "priorityName", "statusName", "hasImage", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;)Lkotlinx/coroutines/flow/Flow;", "getAll", "getAllCategories", "getById", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLedgerTasks", "startDateEpoch", "endDateEpoch", "includeArchived", "category", "getPriorityTasks", "nowMillis", "getTasksByIds", "ids", "getTodayTasks", "todayStartMillis", "todayEndMillis", "insert", "searchAndFilter", "update", "updateSortOrder", "sortOrder", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface TaskDao {
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM tasks ORDER BY id DESC")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.local.TaskEntity>> getAll();
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "SELECT * FROM tasks WHERE id = :id")
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.ledger.task.data.local.TaskEntity> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.local.TaskEntity task, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Update
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.local.TaskEntity task, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Delete
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.local.TaskEntity task, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT COUNT(*) FROM tasks")
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> countAll();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT COUNT(*) FROM tasks WHERE status = \'IN_PROGRESS\'")
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> countInProgress();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT COUNT(*) FROM tasks WHERE status = \'DONE\'")
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> countDone();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT COUNT(*) FROM tasks WHERE status != \'DONE\' AND deadline < :todayEpochDay")
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> countOverdue(long todayEpochDay);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "\n        SELECT * FROM tasks\n        WHERE title LIKE \'%\' || :query || \'%\'\n        AND (:priorityName IS NULL OR priority = :priorityName)\n        ORDER BY id DESC\n    ")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.local.TaskEntity>> searchAndFilter(@org.jetbrains.annotations.NotNull
    java.lang.String query, @org.jetbrains.annotations.Nullable
    java.lang.String priorityName);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "\n        SELECT * FROM tasks\n        WHERE deadline >= :todayStartMillis\n        AND deadline < :todayEndMillis\n        ORDER BY\n            CASE WHEN status = \'DONE\' THEN 1 ELSE 0 END,\n            CASE priority\n                WHEN \'HIGH\' THEN 0\n                WHEN \'MEDIUM\' THEN 1\n                WHEN \'NORMAL\' THEN 2\n                WHEN \'LOW\' THEN 3\n                ELSE 4\n            END,\n            sortOrder ASC\n    ")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.local.TaskEntity>> getTodayTasks(long todayStartMillis, long todayEndMillis);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE tasks SET sortOrder = :sortOrder WHERE id = :id")
    public abstract java.lang.Object updateSortOrder(long id, int sortOrder, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "\n        SELECT * FROM tasks\n        WHERE priority IN (\'HIGH\', \'MEDIUM\')\n        AND status != \'DONE\'\n        ORDER BY\n            CASE WHEN deadline < :nowMillis THEN 0 ELSE 1 END,\n            CASE priority\n                WHEN \'HIGH\' THEN 0\n                WHEN \'MEDIUM\' THEN 1\n                ELSE 2\n            END,\n            sortOrder ASC\n    ")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.local.TaskEntity>> getPriorityTasks(long nowMillis);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "\n        SELECT * FROM tasks\n        WHERE (:query IS NULL OR title LIKE \'%\' || :query || \'%\')\n        AND (:priorityName IS NULL OR priority = :priorityName)\n        AND (:statusName IS NULL OR status = :statusName)\n        AND (:hasImage IS NULL OR hasImage = :hasImage)\n        ORDER BY id DESC\n    ")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.local.TaskEntity>> filterAllTasks(@org.jetbrains.annotations.Nullable
    java.lang.String query, @org.jetbrains.annotations.Nullable
    java.lang.String priorityName, @org.jetbrains.annotations.Nullable
    java.lang.String statusName, @org.jetbrains.annotations.Nullable
    java.lang.Boolean hasImage);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "\n        SELECT * FROM tasks\n        WHERE (:includeArchived = 1 OR status != \'DONE\')\n        AND (:category IS NULL OR category = :category)\n        AND deadline >= :startDateEpoch\n        AND deadline <= :endDateEpoch\n        ORDER BY deadline DESC\n    ")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.local.TaskEntity>> getLedgerTasks(long startDateEpoch, long endDateEpoch, boolean includeArchived, @org.jetbrains.annotations.Nullable
    java.lang.String category);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT DISTINCT category FROM tasks WHERE category != \'\'")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<java.lang.String>> getAllCategories();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM tasks WHERE id IN (:ids)")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.local.TaskEntity>> getTasksByIds(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> ids);
}