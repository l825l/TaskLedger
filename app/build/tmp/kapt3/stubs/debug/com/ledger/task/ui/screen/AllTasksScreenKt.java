package com.ledger.task.ui.screen;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000V\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u001a<\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a\u00d7\u0001\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00052\u0014\u0010\u0018\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\r\u0012\u0004\u0012\u00020\u00010\u00052\u0014\u0010\u0019\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0010\u0012\u0004\u0012\u00020\u00010\u00052\u0014\u0010\u001a\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0012\u0012\u0004\u0012\u00020\u00010\u00052\u0014\u0010\u001b\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0014\u0012\u0004\u0012\u00020\u00010\u00052\u0014\u0010\u001c\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0016\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u00a2\u0006\u0002\u0010\u001e\u001a0\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0003\u00a8\u0006%"}, d2 = {"AllTasksScreen", "", "viewModel", "Lcom/ledger/task/viewmodel/AllTasksViewModel;", "onNavigateToEdit", "Lkotlin/Function1;", "", "onNavigateToAdd", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "FilterPanel", "searchQuery", "", "category", "priority", "Lcom/ledger/task/data/model/Priority;", "status", "Lcom/ledger/task/data/model/DisplayStatus;", "hasImage", "", "quickTag", "Lcom/ledger/task/data/model/QuickTag;", "onSearchChange", "onCategoryChange", "onPriorityChange", "onStatusChange", "onHasImageChange", "onQuickTagChange", "onClearAllFilters", "(Ljava/lang/String;Ljava/lang/String;Lcom/ledger/task/data/model/Priority;Lcom/ledger/task/data/model/DisplayStatus;Ljava/lang/Boolean;Lcom/ledger/task/data/model/QuickTag;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)V", "TaskRow", "task", "Lcom/ledger/task/data/model/Task;", "index", "", "onClick", "app_debug"})
public final class AllTasksScreenKt {
    
    /**
     * 全部事务屏幕
     */
    @androidx.compose.runtime.Composable
    public static final void AllTasksScreen(@org.jetbrains.annotations.NotNull
    com.ledger.task.viewmodel.AllTasksViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onNavigateToEdit, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToAdd, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    private static final void FilterPanel(java.lang.String searchQuery, java.lang.String category, com.ledger.task.data.model.Priority priority, com.ledger.task.data.model.DisplayStatus status, java.lang.Boolean hasImage, com.ledger.task.data.model.QuickTag quickTag, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSearchChange, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCategoryChange, kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.Priority, kotlin.Unit> onPriorityChange, kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.DisplayStatus, kotlin.Unit> onStatusChange, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onHasImageChange, kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.QuickTag, kotlin.Unit> onQuickTagChange, kotlin.jvm.functions.Function0<kotlin.Unit> onClearAllFilters) {
    }
    
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    private static final void TaskRow(com.ledger.task.data.model.Task task, int index, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
}