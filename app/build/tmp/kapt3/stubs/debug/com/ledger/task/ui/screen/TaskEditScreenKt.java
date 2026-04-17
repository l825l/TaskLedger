package com.ledger.task.ui.screen;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000T\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\u001a\u0080\u0001\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0003\u001a\u0010\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0006H\u0003\u001aH\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u000e\b\u0002\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0003\u001a.\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u001c2\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0003\u001aM\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0014\b\u0002\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007\u00a2\u0006\u0002\u0010%\u00a8\u0006&"}, d2 = {"CategorySelectionDialog", "", "categories", "", "Lcom/ledger/task/data/model/CategoryNode;", "selectedCategory", "", "onSelect", "Lkotlin/Function1;", "onDismiss", "Lkotlin/Function0;", "onAddCategory", "onEditCategory", "onDeleteCategory", "modifier", "Landroidx/compose/ui/Modifier;", "Label", "text", "RelatedTaskCard", "task", "Lcom/ledger/task/data/model/Task;", "relationType", "isCompleted", "", "onRemove", "onClick", "StatusSlider", "currentStatus", "Lcom/ledger/task/data/model/TaskStatus;", "onStatusChange", "TaskEditScreen", "viewModel", "Lcom/ledger/task/viewmodel/TaskEditViewModel;", "taskId", "", "onNavigateBack", "onNavigateToTask", "(Lcom/ledger/task/viewmodel/TaskEditViewModel;Ljava/lang/Long;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;)V", "app_debug"})
public final class TaskEditScreenKt {
    
    /**
     * 任务创建/编辑页
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void TaskEditScreen(@org.jetbrains.annotations.NotNull
    com.ledger.task.viewmodel.TaskEditViewModel viewModel, @org.jetbrains.annotations.Nullable
    java.lang.Long taskId, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onNavigateToTask, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    private static final void Label(java.lang.String text) {
    }
    
    /**
     * 状态滑块组件
     */
    @androidx.compose.runtime.Composable
    private static final void StatusSlider(com.ledger.task.data.model.TaskStatus currentStatus, kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.TaskStatus, kotlin.Unit> onStatusChange, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 关联任务卡片（横向滚动用）
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    private static final void RelatedTaskCard(com.ledger.task.data.model.Task task, java.lang.String relationType, boolean isCompleted, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 分类选择对话框
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    private static final void CategorySelectionDialog(java.util.List<com.ledger.task.data.model.CategoryNode> categories, java.lang.String selectedCategory, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelect, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function0<kotlin.Unit> onAddCategory, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onEditCategory, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDeleteCategory, androidx.compose.ui.Modifier modifier) {
    }
}