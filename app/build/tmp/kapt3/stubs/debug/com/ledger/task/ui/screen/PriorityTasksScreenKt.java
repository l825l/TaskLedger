package com.ledger.task.ui.screen;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a<\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001ap\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0014\u001a\u00020\u00152\u0014\b\u0002\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u00a8\u0006\u0017"}, d2 = {"PriorityTasksScreen", "", "viewModel", "Lcom/ledger/task/viewmodel/PriorityTasksViewModel;", "onNavigateToEdit", "Lkotlin/Function1;", "", "onNavigateToAdd", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "SwipeablePriorityTaskRow", "task", "Lcom/ledger/task/data/model/Task;", "index", "", "onClick", "onCompleted", "onPriorityUpgrade", "onPriorityDowngrade", "isCurrentlySwiped", "", "onSwipeStateChanged", "app_debug"})
public final class PriorityTasksScreenKt {
    
    /**
     * 重点待办屏幕
     */
    @androidx.compose.runtime.Composable
    public static final void PriorityTasksScreen(@org.jetbrains.annotations.NotNull
    com.ledger.task.viewmodel.PriorityTasksViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onNavigateToEdit, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToAdd, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 可左滑/右滑的重点任务行
     * 左滑：显示完成/取消按钮
     * 右滑：显示升降级按钮
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    private static final void SwipeablePriorityTaskRow(com.ledger.task.data.model.Task task, int index, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onCompleted, kotlin.jvm.functions.Function0<kotlin.Unit> onPriorityUpgrade, kotlin.jvm.functions.Function0<kotlin.Unit> onPriorityDowngrade, boolean isCurrentlySwiped, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onSwipeStateChanged) {
    }
}