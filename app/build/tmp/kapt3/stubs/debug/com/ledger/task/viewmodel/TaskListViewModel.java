package com.ledger.task.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\u001e\u001a\u00020\u0011H\u0002J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\bJ\u0010\u0010\"\u001a\u00020 2\b\u0010#\u001a\u0004\u0018\u00010\u000bJ\u000e\u0010$\u001a\u00020 2\u0006\u0010%\u001a\u00020\u000fJ\u000e\u0010&\u001a\u00020 2\u0006\u0010\'\u001a\u00020(R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006)"}, d2 = {"Lcom/ledger/task/viewmodel/TaskListViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "filteredTasksFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/ledger/task/data/model/Task;", "priorityFilterFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ledger/task/data/model/Priority;", "repository", "Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "searchQueryFlow", "", "sortStateFlow", "Lcom/ledger/task/viewmodel/SortState;", "sortedTasksFlow", "statsFlow", "Lcom/ledger/task/viewmodel/StatsState;", "todayFlow", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/ledger/task/viewmodel/TaskListUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "applySort", "tasks", "sort", "deleteTask", "", "task", "onPriorityFilterChange", "priority", "onSearchQueryChange", "query", "onSort", "field", "Lcom/ledger/task/viewmodel/SortField;", "app_debug"})
public final class TaskListViewModel extends androidx.lifecycle.AndroidViewModel {
    private final com.ledger.task.data.repository.TaskRepositoryImpl repository = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> searchQueryFlow = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ledger.task.data.model.Priority> priorityFilterFlow = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ledger.task.viewmodel.SortState> sortStateFlow = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> todayFlow = null;
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.model.Task>> filteredTasksFlow = null;
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.ledger.task.data.model.Task>> sortedTasksFlow = null;
    private final kotlinx.coroutines.flow.Flow<com.ledger.task.viewmodel.StatsState> statsFlow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.TaskListUiState> uiState = null;
    
    public TaskListViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.TaskListUiState> getUiState() {
        return null;
    }
    
    public final void onSearchQueryChange(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
    }
    
    public final void onPriorityFilterChange(@org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.Priority priority) {
    }
    
    public final void onSort(@org.jetbrains.annotations.NotNull
    com.ledger.task.viewmodel.SortField field) {
    }
    
    public final void deleteTask(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Task task) {
    }
    
    private final java.util.List<com.ledger.task.data.model.Task> applySort(java.util.List<com.ledger.task.data.model.Task> tasks, com.ledger.task.viewmodel.SortState sort) {
        return null;
    }
}