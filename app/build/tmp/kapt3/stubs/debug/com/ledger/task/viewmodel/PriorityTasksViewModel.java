package com.ledger.task.viewmodel;

import java.lang.System;

/**
 * 重点待办 ViewModel
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0018"}, d2 = {"Lcom/ledger/task/viewmodel/PriorityTasksViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ledger/task/viewmodel/PriorityTasksUiState;", "priorityTasksFlow", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/ledger/task/data/model/Task;", "repository", "Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "uiState", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadPriorityTasks", "", "onPriorityDowngrade", "taskId", "", "onPriorityUpgrade", "onTaskCompleted", "app_debug"})
public final class PriorityTasksViewModel extends androidx.lifecycle.AndroidViewModel {
    private final com.ledger.task.data.repository.TaskRepositoryImpl repository = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ledger.task.viewmodel.PriorityTasksUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.PriorityTasksUiState> uiState = null;
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.ledger.task.data.model.Task>> priorityTasksFlow = null;
    
    public PriorityTasksViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.PriorityTasksUiState> getUiState() {
        return null;
    }
    
    public final void loadPriorityTasks() {
    }
    
    public final void onTaskCompleted(long taskId) {
    }
    
    public final void onPriorityDowngrade(long taskId) {
    }
    
    public final void onPriorityUpgrade(long taskId) {
    }
}