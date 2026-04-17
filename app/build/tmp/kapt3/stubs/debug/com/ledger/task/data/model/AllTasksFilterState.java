package com.ledger.task.data.model;

import java.lang.System;

/**
 * 全部事务筛选状态
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\nH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0011J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\fH\u00c6\u0003JT\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00c6\u0001\u00a2\u0006\u0002\u0010!J\u0013\u0010\"\u001a\u00020\n2\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020%H\u00d6\u0001J\t\u0010&\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0015\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000fR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006\'"}, d2 = {"Lcom/ledger/task/data/model/AllTasksFilterState;", "", "searchQuery", "", "category", "priority", "Lcom/ledger/task/data/model/Priority;", "status", "Lcom/ledger/task/data/model/DisplayStatus;", "hasImage", "", "quickTag", "Lcom/ledger/task/data/model/QuickTag;", "(Ljava/lang/String;Ljava/lang/String;Lcom/ledger/task/data/model/Priority;Lcom/ledger/task/data/model/DisplayStatus;Ljava/lang/Boolean;Lcom/ledger/task/data/model/QuickTag;)V", "getCategory", "()Ljava/lang/String;", "getHasImage", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "getPriority", "()Lcom/ledger/task/data/model/Priority;", "getQuickTag", "()Lcom/ledger/task/data/model/QuickTag;", "getSearchQuery", "getStatus", "()Lcom/ledger/task/data/model/DisplayStatus;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(Ljava/lang/String;Ljava/lang/String;Lcom/ledger/task/data/model/Priority;Lcom/ledger/task/data/model/DisplayStatus;Ljava/lang/Boolean;Lcom/ledger/task/data/model/QuickTag;)Lcom/ledger/task/data/model/AllTasksFilterState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class AllTasksFilterState {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String searchQuery = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String category = null;
    @org.jetbrains.annotations.Nullable
    private final com.ledger.task.data.model.Priority priority = null;
    @org.jetbrains.annotations.Nullable
    private final com.ledger.task.data.model.DisplayStatus status = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Boolean hasImage = null;
    @org.jetbrains.annotations.Nullable
    private final com.ledger.task.data.model.QuickTag quickTag = null;
    
    /**
     * 全部事务筛选状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.AllTasksFilterState copy(@org.jetbrains.annotations.NotNull
    java.lang.String searchQuery, @org.jetbrains.annotations.Nullable
    java.lang.String category, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.Priority priority, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.DisplayStatus status, @org.jetbrains.annotations.Nullable
    java.lang.Boolean hasImage, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.QuickTag quickTag) {
        return null;
    }
    
    /**
     * 全部事务筛选状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 全部事务筛选状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 全部事务筛选状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public AllTasksFilterState() {
        super();
    }
    
    public AllTasksFilterState(@org.jetbrains.annotations.NotNull
    java.lang.String searchQuery, @org.jetbrains.annotations.Nullable
    java.lang.String category, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.Priority priority, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.DisplayStatus status, @org.jetbrains.annotations.Nullable
    java.lang.Boolean hasImage, @org.jetbrains.annotations.Nullable
    com.ledger.task.data.model.QuickTag quickTag) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.ledger.task.data.model.Priority component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.ledger.task.data.model.Priority getPriority() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.ledger.task.data.model.DisplayStatus component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.ledger.task.data.model.DisplayStatus getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Boolean component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Boolean getHasImage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.ledger.task.data.model.QuickTag component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.ledger.task.data.model.QuickTag getQuickTag() {
        return null;
    }
}