package com.ledger.task.viewmodel;

import java.lang.System;

/**
 * 今日待办 UI 状态
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B3\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u00a2\u0006\u0002\u0010\nJ\u000f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\bH\u00c6\u0003J\t\u0010\u0014\u001a\u00020\bH\u00c6\u0003J7\u0010\u0015\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00062\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\bH\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\rR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\f\u00a8\u0006\u001b"}, d2 = {"Lcom/ledger/task/viewmodel/TodayTasksUiState;", "", "tasks", "", "Lcom/ledger/task/data/model/Task;", "isSorting", "", "completedCount", "", "totalCount", "(Ljava/util/List;ZII)V", "getCompletedCount", "()I", "()Z", "getTasks", "()Ljava/util/List;", "getTotalCount", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "toString", "", "app_debug"})
public final class TodayTasksUiState {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ledger.task.data.model.Task> tasks = null;
    private final boolean isSorting = false;
    private final int completedCount = 0;
    private final int totalCount = 0;
    
    /**
     * 今日待办 UI 状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.viewmodel.TodayTasksUiState copy(@org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> tasks, boolean isSorting, int completedCount, int totalCount) {
        return null;
    }
    
    /**
     * 今日待办 UI 状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 今日待办 UI 状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 今日待办 UI 状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public TodayTasksUiState() {
        super();
    }
    
    public TodayTasksUiState(@org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> tasks, boolean isSorting, int completedCount, int totalCount) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> getTasks() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean isSorting() {
        return false;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int getCompletedCount() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int getTotalCount() {
        return 0;
    }
}