package com.ledger.task.ui.component;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u001aT\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\u0018\u0010\t\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a&\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0003\u00a8\u0006\u0012"}, d2 = {"TaskRelationDialog", "", "title", "", "availableTasks", "", "Lcom/ledger/task/data/model/Task;", "selectedTaskIds", "", "onConfirm", "Lkotlin/Function1;", "onDismiss", "Lkotlin/Function0;", "TaskRelationItem", "task", "isSelected", "", "onClick", "app_debug"})
public final class TaskRelationDialogKt {
    
    /**
     * 关联任务选择对话框
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void TaskRelationDialog(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> availableTasks, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> selectedTaskIds, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.util.List<java.lang.Long>, kotlin.Unit> onConfirm, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    /**
     * 关联任务项
     */
    @androidx.compose.runtime.Composable
    private static final void TaskRelationItem(com.ledger.task.data.model.Task task, boolean isSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}