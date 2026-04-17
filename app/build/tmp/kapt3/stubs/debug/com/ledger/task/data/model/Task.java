package com.ledger.task.data.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b4\b\u0087\b\u0018\u00002\u00020\u0001B\u009f\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015\u0012\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u001bJ\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0013H\u00c6\u0003J\u000f\u00109\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015H\u00c6\u0003J\u000f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015H\u00c6\u0003J\t\u0010;\u001a\u00020\u0018H\u00c6\u0003J\t\u0010<\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010=\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001fJ\t\u0010>\u001a\u00020\u0005H\u00c6\u0003J\t\u0010?\u001a\u00020\u0007H\u00c6\u0003J\t\u0010@\u001a\u00020\tH\u00c6\u0003J\t\u0010A\u001a\u00020\u000bH\u00c6\u0003J\t\u0010B\u001a\u00020\rH\u00c6\u0003J\t\u0010C\u001a\u00020\u0005H\u00c6\u0003J\t\u0010D\u001a\u00020\u0005H\u00c6\u0003J\t\u0010E\u001a\u00020\u0011H\u00c6\u0003J\u00b2\u0001\u0010F\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u00152\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00030\u00152\b\b\u0002\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u00032\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010GJ\u0013\u0010H\u001a\u00020\u00112\b\u0010I\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010J\u001a\u00020\u0018H\u00d6\u0001J\t\u0010K\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0015\u0010\u001a\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010 \u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u0019\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001dR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\"R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010,R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00103R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u00105R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010\u001d\u00a8\u0006L"}, d2 = {"Lcom/ledger/task/data/model/Task;", "", "id", "", "title", "", "priority", "Lcom/ledger/task/data/model/Priority;", "deadline", "Ljava/time/LocalDateTime;", "status", "Lcom/ledger/task/data/model/TaskStatus;", "displayStatus", "Lcom/ledger/task/data/model/DisplayStatus;", "description", "category", "hasImage", "", "richContent", "Lcom/ledger/task/data/model/RichContent;", "predecessorIds", "", "relatedIds", "sortOrder", "", "createdAt", "completedAt", "(JLjava/lang/String;Lcom/ledger/task/data/model/Priority;Ljava/time/LocalDateTime;Lcom/ledger/task/data/model/TaskStatus;Lcom/ledger/task/data/model/DisplayStatus;Ljava/lang/String;Ljava/lang/String;ZLcom/ledger/task/data/model/RichContent;Ljava/util/List;Ljava/util/List;IJLjava/lang/Long;)V", "getCategory", "()Ljava/lang/String;", "getCompletedAt", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCreatedAt", "()J", "getDeadline", "()Ljava/time/LocalDateTime;", "getDescription", "getDisplayStatus", "()Lcom/ledger/task/data/model/DisplayStatus;", "getHasImage", "()Z", "getId", "getPredecessorIds", "()Ljava/util/List;", "getPriority", "()Lcom/ledger/task/data/model/Priority;", "getRelatedIds", "getRichContent", "()Lcom/ledger/task/data/model/RichContent;", "getSortOrder", "()I", "getStatus", "()Lcom/ledger/task/data/model/TaskStatus;", "getTitle", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLjava/lang/String;Lcom/ledger/task/data/model/Priority;Ljava/time/LocalDateTime;Lcom/ledger/task/data/model/TaskStatus;Lcom/ledger/task/data/model/DisplayStatus;Ljava/lang/String;Ljava/lang/String;ZLcom/ledger/task/data/model/RichContent;Ljava/util/List;Ljava/util/List;IJLjava/lang/Long;)Lcom/ledger/task/data/model/Task;", "equals", "other", "hashCode", "toString", "app_debug"})
public final class Task {
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.Priority priority = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDateTime deadline = null;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.TaskStatus status = null;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.DisplayStatus displayStatus = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String description = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String category = null;
    private final boolean hasImage = false;
    @org.jetbrains.annotations.NotNull
    private final com.ledger.task.data.model.RichContent richContent = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.Long> predecessorIds = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.Long> relatedIds = null;
    private final int sortOrder = 0;
    private final long createdAt = 0L;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long completedAt = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.Task copy(long id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Priority priority, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime deadline, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TaskStatus status, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.DisplayStatus displayStatus, @org.jetbrains.annotations.NotNull
    java.lang.String description, @org.jetbrains.annotations.NotNull
    java.lang.String category, boolean hasImage, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.RichContent richContent, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> predecessorIds, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> relatedIds, int sortOrder, long createdAt, @org.jetbrains.annotations.Nullable
    java.lang.Long completedAt) {
        return null;
    }
    
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
    
    public Task(long id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Priority priority, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime deadline, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TaskStatus status, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.DisplayStatus displayStatus, @org.jetbrains.annotations.NotNull
    java.lang.String description, @org.jetbrains.annotations.NotNull
    java.lang.String category, boolean hasImage, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.RichContent richContent, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> predecessorIds, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> relatedIds, int sortOrder, long createdAt, @org.jetbrains.annotations.Nullable
    java.lang.Long completedAt) {
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
    public final com.ledger.task.data.model.Priority component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.Priority getPriority() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDateTime getDeadline() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.TaskStatus component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.TaskStatus getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.DisplayStatus component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.DisplayStatus getDisplayStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
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
    
    public final int component13() {
        return 0;
    }
    
    public final int getSortOrder() {
        return 0;
    }
    
    public final long component14() {
        return 0L;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long component15() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getCompletedAt() {
        return null;
    }
}