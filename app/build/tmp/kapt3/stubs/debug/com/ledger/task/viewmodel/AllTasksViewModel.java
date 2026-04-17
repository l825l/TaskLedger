package com.ledger.task.viewmodel;

import java.lang.System;

/**
 * 全部事务 ViewModel
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00132\b\u0010\u0015\u001a\u0004\u0018\u00010\rJ\u000e\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\tJ\u0015\u0010\u0018\u001a\u00020\u00132\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u00a2\u0006\u0002\u0010\u001bJ\u0010\u0010\u001c\u001a\u00020\u00132\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eJ\u0010\u0010\u001f\u001a\u00020\u00132\b\u0010 \u001a\u0004\u0018\u00010!J\u000e\u0010\"\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\rJ\u0010\u0010$\u001a\u00020\u00132\b\u0010%\u001a\u0004\u0018\u00010&R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\'"}, d2 = {"Lcom/ledger/task/viewmodel/AllTasksViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/ledger/task/viewmodel/AllTasksUiState;", "filterStateFlow", "Lcom/ledger/task/data/model/AllTasksFilterState;", "repository", "Lcom/ledger/task/data/repository/TaskRepositoryImpl;", "searchQueryFlow", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadAllTasks", "", "onCategoryFilterChange", "category", "onFilterChange", "filter", "onHasImageFilterChange", "hasImage", "", "(Ljava/lang/Boolean;)V", "onPriorityFilterChange", "priority", "Lcom/ledger/task/data/model/Priority;", "onQuickTagFilterChange", "quickTag", "Lcom/ledger/task/data/model/QuickTag;", "onSearchChange", "query", "onStatusFilterChange", "status", "Lcom/ledger/task/data/model/DisplayStatus;", "app_debug"})
public final class AllTasksViewModel extends androidx.lifecycle.AndroidViewModel {
    private final com.ledger.task.data.repository.TaskRepositoryImpl repository = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> searchQueryFlow = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ledger.task.data.model.AllTasksFilterState> filterStateFlow = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.ledger.task.viewmodel.AllTasksUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.AllTasksUiState> uiState = null;
    
    public AllTasksViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.ledger.task.viewmodel.AllTasksUiState> getUiState() {
        return null;
    }
    
    private final void loadAllTasks() {
    }
    
    public final void onSearchChange(@org.jetbrains.annotations.NotNull
    java.lang.String query) {
    }
    
    public final void onFilterChange(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.AllTasksFilterState filter) {
    }
    
    public final void onPriorityFilterChange(@org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.Priority priority) {
    }
    
    public final void onStatusFilterChange(@org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.DisplayStatus status) {
    }
    
    public final void onHasImageFilterChange(@org.jetbrains.annotations.Nullable
    java.lang.Boolean hasImage) {
    }
    
    public final void onCategoryFilterChange(@org.jetbrains.annotations.Nullable
    java.lang.String category) {
    }
    
    public final void onQuickTagFilterChange(@org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.QuickTag quickTag) {
    }
}