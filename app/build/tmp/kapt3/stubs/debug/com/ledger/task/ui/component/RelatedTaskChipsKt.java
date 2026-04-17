package com.ledger.task.ui.component;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\u001a(\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a<\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00030\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u00a8\u0006\u0010"}, d2 = {"RelatedTaskChip", "", "task", "Lcom/ledger/task/data/model/Task;", "onClose", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "RelatedTasksContainer", "title", "", "tasks", "", "onRemove", "Lkotlin/Function1;", "", "app_debug"})
public final class RelatedTaskChipsKt {
    
    /**
     * 关联任务小卡片
     */
    @androidx.compose.runtime.Composable
    public static final void RelatedTaskChip(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Task task, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 关联事项组件（前置依赖 + 相关任务合并）
     */
    @androidx.compose.runtime.Composable
    public static final void RelatedTasksContainer(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.util.List<com.ledger.task.data.model.Task> tasks, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onRemove, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
}