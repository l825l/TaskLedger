package com.ledger.task.data.model;

import java.lang.System;

/**
 * 默认分类数据
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\f\u0010,\u001a\b\u0012\u0004\u0012\u00020-0%J\u0010\u0010.\u001a\u0004\u0018\u00010/2\u0006\u00100\u001a\u00020-J\u001e\u00101\u001a\u00020\u00042\u0006\u00102\u001a\u00020-\u00f8\u0001\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b3\u00104J\u001e\u00105\u001a\u00020\u00042\u0006\u00106\u001a\u00020-\u00f8\u0001\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b7\u00104R\u001c\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006R\u001c\u0010\b\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\t\u0010\u0006R\u001c\u0010\n\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u000b\u0010\u0006R\u001c\u0010\f\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\r\u0010\u0006R\u001c\u0010\u000e\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u000f\u0010\u0006R\u001c\u0010\u0010\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0011\u0010\u0006R\u001c\u0010\u0012\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0013\u0010\u0006R\u001c\u0010\u0014\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0015\u0010\u0006R\u001c\u0010\u0016\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0017\u0010\u0006R\u001c\u0010\u0018\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0019\u0010\u0006R\u001c\u0010\u001a\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u001b\u0010\u0006R\u001c\u0010\u001c\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u001d\u0010\u0006R\u001c\u0010\u001e\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u001f\u0010\u0006R\u001c\u0010 \u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b!\u0010\u0006R\u001c\u0010\"\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b#\u0010\u0006R\u001a\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00040%\u00f8\u0001\u0000\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010(\u001a\u00020)\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+\u0082\u0002\u000f\n\u0002\b\u0019\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b!\u00a8\u00068"}, d2 = {"Lcom/ledger/task/data/model/DefaultCategories;", "", "()V", "ColorDefault", "Landroidx/compose/ui/graphics/Color;", "getColorDefault-0d7_KjU", "()J", "J", "ColorDocument", "getColorDocument-0d7_KjU", "ColorEmail", "getColorEmail-0d7_KjU", "ColorFinance", "getColorFinance-0d7_KjU", "ColorHealth", "getColorHealth-0d7_KjU", "ColorInvestment", "getColorInvestment-0d7_KjU", "ColorInvoice", "getColorInvoice-0d7_KjU", "ColorLearning", "getColorLearning-0d7_KjU", "ColorMeeting", "getColorMeeting-0d7_KjU", "ColorPersonal", "getColorPersonal-0d7_KjU", "ColorProject", "getColorProject-0d7_KjU", "ColorShopping", "getColorShopping-0d7_KjU", "ColorSocial", "getColorSocial-0d7_KjU", "ColorTax", "getColorTax-0d7_KjU", "ColorWork", "getColorWork-0d7_KjU", "availableColors", "", "getAvailableColors", "()Ljava/util/List;", "defaultTree", "Lcom/ledger/task/data/model/CategoryTree;", "getDefaultTree", "()Lcom/ledger/task/data/model/CategoryTree;", "getAllCategoryNames", "", "getCategoryPath", "Lcom/ledger/task/data/model/CategoryPath;", "categoryId", "getColorById", "id", "getColorById-vNxB06k", "(Ljava/lang/String;)J", "getColorByName", "name", "getColorByName-vNxB06k", "app_debug"})
public final class DefaultCategories {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.data.model.DefaultCategories INSTANCE = null;
    private static final long ColorWork = 0L;
    private static final long ColorMeeting = 0L;
    private static final long ColorProject = 0L;
    private static final long ColorDocument = 0L;
    private static final long ColorEmail = 0L;
    private static final long ColorPersonal = 0L;
    private static final long ColorHealth = 0L;
    private static final long ColorLearning = 0L;
    private static final long ColorShopping = 0L;
    private static final long ColorSocial = 0L;
    private static final long ColorFinance = 0L;
    private static final long ColorInvoice = 0L;
    private static final long ColorTax = 0L;
    private static final long ColorInvestment = 0L;
    private static final long ColorDefault = 0L;
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<androidx.compose.ui.graphics.Color> availableColors = null;
    @org.jetbrains.annotations.NotNull
    private static final com.ledger.task.data.model.CategoryTree defaultTree = null;
    
    private DefaultCategories() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<androidx.compose.ui.graphics.Color> getAvailableColors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.CategoryTree getDefaultTree() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getAllCategoryNames() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.ledger.task.data.model.CategoryPath getCategoryPath(@org.jetbrains.annotations.NotNull
    java.lang.String categoryId) {
        return null;
    }
}