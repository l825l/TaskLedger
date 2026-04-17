package com.ledger.task.ui.component;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u001aW\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007\u00a2\u0006\u0002\u0010\u0010\u001aa\u0010\u0011\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00122\u0006\u0010\u0013\u001a\u00020\u00032\b\u0010\u0014\u001a\u0004\u0018\u0001H\u00122\u001a\u0010\u0015\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0006\u0012\u0004\u0018\u0001H\u0012\u0012\u0004\u0012\u00020\u00030\u00170\u00162\u0014\u0010\u0018\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u0001H\u0012\u0012\u0004\u0012\u00020\u00010\u00192\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0003\u00a2\u0006\u0002\u0010\u001a\u001a\u00b7\u0001\u0010\u001b\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0014\u0010\u001c\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u00020\u00010\u00192\u0014\u0010\u001d\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0012\u0004\u0012\u00020\u00010\u00192\u0014\u0010\u001e\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\u0004\u0012\u00020\u00010\u00192\u0014\u0010\u001f\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u00010\u00192\u0014\u0010 \u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\u0004\u0012\u00020\u00010\u00192\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007\u00a2\u0006\u0002\u0010!\u00a8\u0006\""}, d2 = {"ActiveFiltersChip", "", "category", "", "priority", "Lcom/ledger/task/data/model/Priority;", "status", "Lcom/ledger/task/data/model/DisplayStatus;", "hasImage", "", "quickTag", "Lcom/ledger/task/data/model/QuickTag;", "onClearAll", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "(Ljava/lang/String;Lcom/ledger/task/data/model/Priority;Lcom/ledger/task/data/model/DisplayStatus;Ljava/lang/Boolean;Lcom/ledger/task/data/model/QuickTag;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;)V", "FilterDropdown", "T", "label", "value", "options", "", "Lkotlin/Pair;", "onValueChange", "Lkotlin/Function1;", "(Ljava/lang/String;Ljava/lang/Object;Ljava/util/List;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;)V", "MultiFilterBar", "onCategoryChange", "onPriorityChange", "onStatusChange", "onHasImageChange", "onQuickTagChange", "(Ljava/lang/String;Lcom/ledger/task/data/model/Priority;Lcom/ledger/task/data/model/DisplayStatus;Ljava/lang/Boolean;Lcom/ledger/task/data/model/QuickTag;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;)V", "app_debug"})
public final class MultiFilterBarKt {
    
    /**
     * 多维筛选器组件
     * 支持分类、优先级、状态、是否含图片、快捷标签组合筛选
     */
    @androidx.compose.runtime.Composable
    public static final void MultiFilterBar(@org.jetbrains.annotations.Nullable
    java.lang.String category, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.Priority priority, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.DisplayStatus status, @org.jetbrains.annotations.Nullable
    java.lang.Boolean hasImage, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.QuickTag quickTag, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCategoryChange, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.Priority, kotlin.Unit> onPriorityChange, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.DisplayStatus, kotlin.Unit> onStatusChange, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onHasImageChange, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.ledger.task.data.model.QuickTag, kotlin.Unit> onQuickTagChange, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 单个下拉筛选器
     */
    @androidx.compose.runtime.Composable
    private static final <T extends java.lang.Object>void FilterDropdown(java.lang.String label, T value, java.util.List<? extends kotlin.Pair<? extends T, java.lang.String>> options, kotlin.jvm.functions.Function1<? super T, kotlin.Unit> onValueChange, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 当前筛选状态显示（带清除按钮）
     */
    @androidx.compose.runtime.Composable
    public static final void ActiveFiltersChip(@org.jetbrains.annotations.Nullable
    java.lang.String category, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.Priority priority, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.DisplayStatus status, @org.jetbrains.annotations.Nullable
    java.lang.Boolean hasImage, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.QuickTag quickTag, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClearAll, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
}