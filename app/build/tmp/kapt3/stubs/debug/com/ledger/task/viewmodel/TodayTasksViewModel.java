package com.ledger.task.viewmodel;

import java.lang.System;

/**
 * 今日待办 ViewModel
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u0016\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bJ$\u0010\u001d\u001a\u00020\u00112\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006!"}, d2 = {"Lcom/ledger/task/viewmodel/TodayTasksViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ledger/task/viewmodel/TodayTasksUiState;", "loadJob", "Lkotlinx/coroutines/Job;", "repository", "Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadTodayTasks", "", "onPriorityDowngrade", "taskId", "", "onPriorityUpgrade", "onTaskCompleted", "onTaskPriorityDowngrade", "onTaskUndoComplete", "reorderTasks", "fromIndex", "", "toIndex", "reorderTasksInPriority", "tasks", "", "Lcom/ledger/task/data/model/Task;", "app_debug"})
public final class TodayTasksViewModel extends androidx.lifecycle.AndroidViewModel {
    private final com.ledger.task.data.repository.TaskRepositoryImpl repository = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ledger.task.viewmodel.TodayTasksUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.TodayTasksUiState> uiState = null;
    private kotlinx.coroutines.Job loadJob;
    
    public TodayTasksViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.TodayTasksUiState> getUiState() {
        return null;
    }
    
    public final void loadTodayTasks() {
    }
    
    public final void onTaskCompleted(long taskId) {
    }
    
    public final void onTaskUndoComplete(long taskId) {
    }
    
    /**
     * 优先级降级（统一方法，onTaskPriorityDowngrade 是别名）
     */
    public final void onPriorityDowngrade(long taskId) {
    }
    
    /**
     * onTaskPriorityDowngrade 是 onPriorityDowngrade 的别名
     * 保持向后兼容
     */
    public final void onTaskPriorityDowngrade(long taskId) {
    }
    
    public final void onPriorityUpgrade(long taskId) {
    }
    
    public final void reorderTasks(int fromIndex, int toIndex) {
    }
    
    /**
     * 在同优先级内重新排序
     */
    public final void reorderTasksInPriority(@org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> tasks, int fromIndex, int toIndex) {
    }
}