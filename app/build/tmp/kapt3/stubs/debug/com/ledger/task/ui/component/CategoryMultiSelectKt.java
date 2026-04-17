package com.ledger.task.ui.component;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000P\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001aB\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u00062\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a\u008f\u0001\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\b2\u0006\u0010\u0011\u001a\u00020\u000f2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u00062\u0006\u0010\u0018\u001a\u00020\u00042\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\bH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001a\u0010\u001b\u001a.\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u00042\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001aX\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020!2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u00062\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\b2\u0006\u0010\u0018\u001a\u00020\u00042\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0007\u001a:\u0010\"\u001a\u00020\u00012\u0006\u0010 \u001a\u00020!2\u0006\u0010\u001d\u001a\u00020\u00042\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00010%H\u0007\u0082\u0002\u000b\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006&"}, d2 = {"CategoryMultiSelect", "", "categories", "", "", "selectedCategories", "", "onToggle", "Lkotlin/Function1;", "modifier", "Landroidx/compose/ui/Modifier;", "CategoryNodeItem", "node", "Lcom/ledger/task/data/model/CategoryNode;", "isExpanded", "", "onToggleExpand", "isSelected", "onSelect", "indent", "Landroidx/compose/ui/unit/Dp;", "level", "", "expandedNodes", "selectedNodeId", "onNodeSelect", "CategoryNodeItem-PfoAEA0", "(Lcom/ledger/task/data/model/CategoryNode;ZLkotlin/jvm/functions/Function1;ZLkotlin/jvm/functions/Function1;FILjava/util/Set;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)V", "CategoryPicker", "selectedCategory", "onCategoryChange", "CategoryTreeContent", "tree", "Lcom/ledger/task/data/model/CategoryTree;", "CategoryTreeDialog", "onCategorySelect", "onDismiss", "Lkotlin/Function0;", "app_debug"})
public final class CategoryMultiSelectKt {
    
    /**
     * 分类多选器（保留原有功能用于筛选）
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void CategoryMultiSelect(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> categories, @org.jetbrains.annotations.NotNull
    java.util.Set<java.lang.String> selectedCategories, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggle, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 多级分类选择器（用于任务编辑时选择分类）
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void CategoryPicker(@org.jetbrains.annotations.NotNull
    java.lang.String selectedCategory, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCategoryChange, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * 分类树选择对话框
     */
    @androidx.compose.runtime.Composable
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    public static final void CategoryTreeDialog(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.CategoryTree tree, @org.jetbrains.annotations.NotNull
    java.lang.String selectedCategory, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCategorySelect, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    /**
     * 分类树内容
     */
    @androidx.compose.runtime.Composable
    public static final void CategoryTreeContent(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.CategoryTree tree, @org.jetbrains.annotations.NotNull
    java.util.Set<java.lang.String> expandedNodes, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggleExpand, @org.jetbrains.annotations.NotNull
    java.lang.String selectedNodeId, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNodeSelect, int level) {
    }
}