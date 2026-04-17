package com.ledger.task.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\bK\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u00c8\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u0012\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014\u0012\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u0010\u0012\b\b\u0002\u0010 \u001a\u00020\u0010\u0012\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014\u0012\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u0014\u0012\b\b\u0002\u0010$\u001a\u00020\u0005\u0012\b\b\u0002\u0010%\u001a\u00020&\u0012\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010(\u001a\u00020\u0010\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\t\u0010P\u001a\u00020\u0012H\u00c6\u0003J\u000f\u0010Q\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u00c6\u0003J\u000f\u0010R\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u00c6\u0003J\u000f\u0010S\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014H\u00c6\u0003J\u000f\u0010T\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014H\u00c6\u0003J\t\u0010U\u001a\u00020\u0010H\u00c6\u0003J\t\u0010V\u001a\u00020\u0010H\u00c6\u0003J\t\u0010W\u001a\u00020\u0010H\u00c6\u0003J\t\u0010X\u001a\u00020\u0010H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0010H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0005H\u00c6\u0003J\t\u0010[\u001a\u00020\u0010H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0010H\u00c6\u0003J\t\u0010]\u001a\u00020\u0010H\u00c6\u0003J\u000f\u0010^\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014H\u00c6\u0003J\u000f\u0010_\u001a\b\u0012\u0004\u0012\u00020#0\u0014H\u00c6\u0003J\t\u0010`\u001a\u00020\u0005H\u00c6\u0003J\u0019\u0010a\u001a\u00020&H\u00c6\u0003\u00f8\u0001\u0002\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\bb\u0010:J\u000b\u0010c\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010d\u001a\u00020\u0010H\u00c6\u0003J\t\u0010e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010f\u001a\u00020\bH\u00c6\u0003J\t\u0010g\u001a\u00020\nH\u00c6\u0003J\t\u0010h\u001a\u00020\fH\u00c6\u0003J\u0010\u0010i\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u00102J\t\u0010j\u001a\u00020\u0005H\u00c6\u0003J\t\u0010k\u001a\u00020\u0010H\u00c6\u0003J\u00d6\u0002\u0010l\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00122\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00142\u000e\b\u0002\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u00142\b\b\u0002\u0010\u0019\u001a\u00020\u00102\b\b\u0002\u0010\u001a\u001a\u00020\u00102\b\b\u0002\u0010\u001b\u001a\u00020\u00102\b\b\u0002\u0010\u001c\u001a\u00020\u00102\b\b\u0002\u0010\u001d\u001a\u00020\u00102\b\b\u0002\u0010\u001e\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020\u00102\b\b\u0002\u0010 \u001a\u00020\u00102\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00170\u00142\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u00142\b\b\u0002\u0010$\u001a\u00020\u00052\b\b\u0002\u0010%\u001a\u00020&2\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010(\u001a\u00020\u0010H\u00c6\u0001\u00f8\u0001\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\bm\u0010nJ\u0013\u0010o\u001a\u00020\u00102\b\u0010p\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010q\u001a\u00020rH\u00d6\u0001J\t\u0010s\u001a\u00020\u0005H\u00d6\u0001R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010(\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010+R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0015\u0010\r\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u00103\u001a\u0004\b1\u00102R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u00105R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u00100R\u0013\u0010\'\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00100R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010-R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010:R\u0011\u0010\u0019\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010-R\u0011\u0010\u001a\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010-R\u001c\u0010%\u001a\u00020&\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010<\u001a\u0004\b;\u0010:R\u0011\u0010$\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u00100R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010+R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010+R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010AR\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010+R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010+R\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010ER\u0011\u0010\u001b\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u0010-R\u0011\u0010 \u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010-R\u0011\u0010\u001f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010-R\u0011\u0010\u001d\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010-R\u0011\u0010\u001e\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u0010-R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010LR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u00100R\u0011\u0010\u001c\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u0010-\u0082\u0002\u000f\n\u0002\b\u0019\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b!\u00a8\u0006t"}, d2 = {"Lcom/ledger/task/viewmodel/TaskEditUiState;", "", "id", "", "title", "", "description", "priority", "Lcom/ledger/task/data/model/Priority;", "deadline", "Ljava/time/LocalDateTime;", "status", "Lcom/ledger/task/data/model/TaskStatus;", "completedAt", "category", "hasImage", "", "richContent", "Lcom/ledger/task/data/model/RichContent;", "predecessorIds", "", "relatedIds", "predecessorTasks", "Lcom/ledger/task/data/model/Task;", "relatedTasks", "isEdit", "isSaving", "saved", "titleError", "showPredecessorDialog", "showRelatedDialog", "showCategoryDialog", "showAddCategoryDialog", "availableTasks", "categories", "Lcom/ledger/task/data/model/CategoryNode;", "newCategoryName", "newCategoryColor", "Landroidx/compose/ui/graphics/Color;", "editingCategoryId", "availableTasksLoaded", "(JLjava/lang/String;Ljava/lang/String;Lcom/ledger/task/data/model/Priority;Ljava/time/LocalDateTime;Lcom/ledger/task/data/model/TaskStatus;Ljava/lang/Long;Ljava/lang/String;ZLcom/ledger/task/data/model/RichContent;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;ZZZZZZZZLjava/util/List;Ljava/util/List;Ljava/lang/String;JLjava/lang/String;ZLkotlin/jvm/internal/DefaultConstructorMarker;)V", "getAvailableTasks", "()Ljava/util/List;", "getAvailableTasksLoaded", "()Z", "getCategories", "getCategory", "()Ljava/lang/String;", "getCompletedAt", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getDeadline", "()Ljava/time/LocalDateTime;", "getDescription", "getEditingCategoryId", "getHasImage", "getId", "()J", "getNewCategoryColor-0d7_KjU", "J", "getNewCategoryName", "getPredecessorIds", "getPredecessorTasks", "getPriority", "()Lcom/ledger/task/data/model/Priority;", "getRelatedIds", "getRelatedTasks", "getRichContent", "()Lcom/ledger/task/data/model/RichContent;", "getSaved", "getShowAddCategoryDialog", "getShowCategoryDialog", "getShowPredecessorDialog", "getShowRelatedDialog", "getStatus", "()Lcom/ledger/task/data/model/TaskStatus;", "getTitle", "getTitleError", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component26-0d7_KjU", "component27", "component28", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "copy-9IfPis8", "(JLjava/lang/String;Ljava/lang/String;Lcom/ledger/task/data/model/Priority;Ljava/time/LocalDateTime;Lcom/ledger/task/data/model/TaskStatus;Ljava/lang/Long;Ljava/lang/String;ZLcom/ledger/task/data/model/RichContent;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;ZZZZZZZZLjava/util/List;Ljava/util/List;Ljava/lang/String;JLjava/lang/String;Z)Lcom/ledger/task/viewmodel/TaskEditUiState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class TaskEditUiState {
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String description = null;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.Priority priority = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDateTime deadline = null;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.TaskStatus status = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long completedAt = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String category = null;
    private final boolean hasImage = false;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.RichContent richContent = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.Long> predecessorIds = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.Long> relatedIds = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ledger.task.data.model.Task> predecessorTasks = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ledger.task.data.model.Task> relatedTasks = null;
    private final boolean isEdit = false;
    private final boolean isSaving = false;
    private final boolean saved = false;
    private final boolean titleError = false;
    private final boolean showPredecessorDialog = false;
    private final boolean showRelatedDialog = false;
    private final boolean showCategoryDialog = false;
    private final boolean showAddCategoryDialog = false;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ledger.task.data.model.Task> availableTasks = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ledger.task.data.model.CategoryNode> categories = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String newCategoryName = null;
    private final long newCategoryColor = 0L;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String editingCategoryId = null;
    private final boolean availableTasksLoaded = false;
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    private TaskEditUiState(long id, java.lang.String title, java.lang.String description, com.ledger.task.data.model.Priority priority, java.time.LocalDateTime deadline, com.ledger.task.data.model.TaskStatus status, java.lang.Long completedAt, java.lang.String category, boolean hasImage, com.ledger.task.data.model.RichContent richContent, java.util.List<java.lang.Long> predecessorIds, java.util.List<java.lang.Long> relatedIds, java.util.List<com.ledger.task.data.model.Task> predecessorTasks, java.util.List<com.ledger.task.data.model.Task> relatedTasks, boolean isEdit, boolean isSaving, boolean saved, boolean titleError, boolean showPredecessorDialog, boolean showRelatedDialog, boolean showCategoryDialog, boolean showAddCategoryDialog, java.util.List<com.ledger.task.data.model.Task> availableTasks, java.util.List<com.ledger.task.data.model.CategoryNode> categories, java.lang.String newCategoryName, long newCategoryColor, java.lang.String editingCategoryId, boolean availableTasksLoaded) {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.Priority component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.Priority getPriority() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime getDeadline() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.TaskStatus component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.TaskStatus getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getCompletedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCategory() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    public final boolean getHasImage() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.RichContent component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.RichContent getRichContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.Long> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.Long> getPredecessorIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.Long> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.Long> getRelatedIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> getPredecessorTasks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> getRelatedTasks() {
        return null;
    }
    
    public final boolean component15() {
        return false;
    }
    
    public final boolean isEdit() {
        return false;
    }
    
    public final boolean component16() {
        return false;
    }
    
    public final boolean isSaving() {
        return false;
    }
    
    public final boolean component17() {
        return false;
    }
    
    public final boolean getSaved() {
        return false;
    }
    
    public final boolean component18() {
        return false;
    }
    
    public final boolean getTitleError() {
        return false;
    }
    
    public final boolean component19() {
        return false;
    }
    
    public final boolean getShowPredecessorDialog() {
        return false;
    }
    
    public final boolean component20() {
        return false;
    }
    
    public final boolean getShowRelatedDialog() {
        return false;
    }
    
    public final boolean component21() {
        return false;
    }
    
    public final boolean getShowCategoryDialog() {
        return false;
    }
    
    public final boolean component22() {
        return false;
    }
    
    public final boolean getShowAddCategoryDialog() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> component23() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.Task> getAvailableTasks() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.CategoryNode> component24() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.CategoryNode> getCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component25() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getNewCategoryName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component27() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getEditingCategoryId() {
        return null;
    }
    
    public final boolean component28() {
        return false;
    }
    
    public final boolean getAvailableTasksLoaded() {
        return false;
    }
}