package com.ledger.task.data.repository;

import java.lang.System;

/**
 * 任务仓库实现
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J!\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ!\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0016J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0016J\u000e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0016J\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0013\u001a\u00020\bH\u0016J\u0019\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u001c\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00190\u000e2\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00190\u000eH\u0016J\u0014\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u00190\u000eH\u0016J\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00160\u0019H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 J\u001b\u0010!\u001a\u0004\u0018\u00010\u00162\u0006\u0010\"\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J6\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00190\u000e2\u0006\u0010%\u001a\u00020\b2\u0006\u0010&\u001a\u00020\b2\u0006\u0010\'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010\u001eH\u0016J\u001f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00160\u00192\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u001c\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00190\u000e2\u0006\u0010,\u001a\u00020\bH\u0016J\u001f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00160\u00192\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J%\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00160\u00192\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\b0\u0019H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100J$\u00101\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00190\u000e2\u0006\u00102\u001a\u00020\b2\u0006\u00103\u001a\u00020\bH\u0016J\u0019\u00104\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J!\u00105\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ!\u00106\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ&\u00107\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00190\u000e2\u0006\u00108\u001a\u00020\u001e2\b\u00109\u001a\u0004\u0018\u00010:H\u0016J\u0019\u0010;\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0016H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J!\u0010<\u001a\u00020\u00062\u0006\u0010\"\u001a\u00020\b2\u0006\u0010=\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010>R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006?"}, d2 = {"Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "Lcom/ledger/task/data/repository/TaskRepository;", "dao", "Lcom/ledger/task/data/local/TaskDao;", "(Lcom/ledger/task/data/local/TaskDao;)V", "addPredecessor", "", "taskId", "", "predecessorId", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addRelatedTask", "relatedId", "countAll", "Lkotlinx/coroutines/flow/Flow;", "", "countDone", "countInProgress", "countOverdue", "todayEpochDay", "delete", "task", "Lcom/ledger/task/data/model/Task;", "(Lcom/ledger/task/data/model/Task;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "filterAllTasks", "", "filter", "Lcom/ledger/task/data/model/AllTasksFilterState;", "getAll", "getAllCategories", "", "getAllNow", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLedgerTasks", "startDateEpoch", "endDateEpoch", "includeArchived", "", "category", "getPredecessorTasks", "getPriorityTasks", "nowMillis", "getRelatedTasks", "getTasksByIds", "ids", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTodayTasks", "todayStartMillis", "todayEndMillis", "insert", "removePredecessor", "removeRelatedTask", "searchAndFilter", "query", "priority", "Lcom/ledger/task/data/model/Priority;", "update", "updateSortOrder", "sortOrder", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class TaskRepositoryImpl implements com.ledger.task.data.repository.TaskRepository {
    private final com.ledger.task.data.local.TaskDao dao = null;
    
    public TaskRepositoryImpl(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.local.TaskDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.model.Task>> getAll() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object getAllNow(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.ledger.task.data.model.Task>> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.ledger.task.data.model.Task> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Task task, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Task task, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object delete(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Task task, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.lang.Integer> countAll() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.lang.Integer> countInProgress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.lang.Integer> countDone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.lang.Integer> countOverdue(long todayEpochDay) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.model.Task>> searchAndFilter(@org.jetbrains.annotations.NotNull
    java.lang.String query, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.Priority priority) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.model.Task>> getTodayTasks(long todayStartMillis, long todayEndMillis) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.model.Task>> getPriorityTasks(long nowMillis) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.model.Task>> filterAllTasks(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.AllTasksFilterState filter) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.model.Task>> getLedgerTasks(long startDateEpoch, long endDateEpoch, boolean includeArchived, @org.jetbrains.annotations.Nullable
    java.lang.String category) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<java.util.List<java.lang.String>> getAllCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object getPredecessorTasks(long taskId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.ledger.task.data.model.Task>> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object getRelatedTasks(long taskId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.ledger.task.data.model.Task>> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object addPredecessor(long taskId, long predecessorId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object removePredecessor(long taskId, long predecessorId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object addRelatedTask(long taskId, long relatedId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object removeRelatedTask(long taskId, long relatedId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object getTasksByIds(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> ids, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.ledger.task.data.model.Task>> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.Object updateSortOrder(long id, int sortOrder, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
}