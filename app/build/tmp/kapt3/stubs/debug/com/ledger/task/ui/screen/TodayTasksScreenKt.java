package com.ledger.task.ui.screen;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000N\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u00fe\u0001\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00052\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00010\u000f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u000f2\u0018\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u00152\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\u00152\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\u00152\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u00152\b\b\u0002\u0010\u001a\u001a\u00020\n2\u0014\b\u0002\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u000f2\b\b\u0002\u0010\u001c\u001a\u00020\u001dH\u0003\u001a<\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020 2\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00010\u000f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\u00152\b\b\u0002\u0010\u001c\u001a\u00020\u001dH\u0007\u00a8\u0006#"}, d2 = {"SwipeableTaskRow", "", "task", "Lcom/ledger/task/data/model/Task;", "taskIndex", "", "totalItems", "itemHeight", "", "isDragging", "", "dragOffsetY", "samePriorityStartIndex", "samePriorityEndIndex", "onDragStart", "Lkotlin/Function1;", "", "onDragUpdate", "onDragEnd", "Lkotlin/Function2;", "onClick", "Lkotlin/Function0;", "onCompleted", "onUndoComplete", "onPriorityUpgrade", "onPriorityDowngrade", "isCurrentlySwiped", "onSwipeStateChanged", "modifier", "Landroidx/compose/ui/Modifier;", "TodayTasksScreen", "viewModel", "Lcom/ledger/task/viewmodel/TodayTasksViewModel;", "onNavigateToEdit", "onNavigateToAdd", "app_debug"})
public final class TodayTasksScreenKt {
    
    /**
     * 今日待办屏幕
     */
    @androidx.compose.runtime.Composable
    public static final void TodayTasksScreen(@org.jetbrains.annotations.NotNull
    com.ledger.task.viewmodel.TodayTasksViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onNavigateToEdit, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToAdd, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 可左滑/右滑的任务行
     * 左滑：显示完成/取消按钮（已完成任务显示撤销）
     * 右滑：显示升降级按钮
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    private static final void SwipeableTaskRow(com.ledger.task.data.model.Task task, int taskIndex, int totalItems, float itemHeight, boolean isDragging, float dragOffsetY, int samePriorityStartIndex, int samePriorityEndIndex, kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onDragStart, kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onDragUpdate, kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Integer, kotlin.Unit> onDragEnd, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onCompleted, kotlin.jvm.functions.Function0<kotlin.Unit> onUndoComplete, kotlin.jvm.functions.Function0<kotlin.Unit> onPriorityUpgrade, kotlin.jvm.functions.Function0<kotlin.Unit> onPriorityDowngrade, boolean isCurrentlySwiped, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onSwipeStateChanged, androidx.compose.ui.Modifier modifier) {
    }
}