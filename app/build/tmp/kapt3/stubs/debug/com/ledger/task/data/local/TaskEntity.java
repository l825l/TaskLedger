package com.ledger.task.data.local;

import java.lang.System;

/**
 * 任务实体（Room Entity）
 * 仅用于数据库操作，不直接暴露给 UI 层
 */
@androidx.room.Entity(tableName = "tasks")
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b%\b\u0087\b\u0018\u00002\u00020\u0001B\u008b\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0017J\u0018\u00103\u001a\u00020#2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\tH\u0002J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\u0005H\u00c6\u0003J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\u0014H\u00c6\u0003J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\u0010\u00109\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001bJ\t\u0010:\u001a\u00020\u0005H\u00c6\u0003J\t\u0010;\u001a\u00020\u0007H\u00c6\u0003J\t\u0010<\u001a\u00020\tH\u00c6\u0003J\t\u0010=\u001a\u00020\u000bH\u00c6\u0003J\t\u0010>\u001a\u00020\u0005H\u00c6\u0003J\t\u0010?\u001a\u00020\u0005H\u00c6\u0003J\t\u0010@\u001a\u00020\u000fH\u00c6\u0003J\t\u0010A\u001a\u00020\u0005H\u00c6\u0003J\u009c\u0001\u0010B\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00032\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010CJ\u0013\u0010D\u001a\u00020\u000f2\b\u0010E\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010F\u001a\u00020\u0014H\u00d6\u0001J\t\u0010G\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0015\u0010\u0016\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0019R\u0011\u0010\"\u001a\u00020#8F\u00a2\u0006\u0006\u001a\u0004\b$\u0010%R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001eR\u0011\u0010\u0010\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0019R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010\u0011\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0019R\u0011\u0010\u0012\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u0019R\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u0019\u00a8\u0006H"}, d2 = {"Lcom/ledger/task/data/local/TaskEntity;", "", "id", "", "title", "", "priority", "Lcom/ledger/task/data/model/Priority;", "deadline", "Ljava/time/LocalDateTime;", "status", "Lcom/ledger/task/data/model/TaskStatus;", "description", "category", "hasImage", "", "predecessorIds", "relatedIds", "richContent", "sortOrder", "", "createdAt", "completedAt", "(JLjava/lang/String;Lcom/ledger/task/data/model/Priority;Ljava/time/LocalDateTime;Lcom/ledger/task/data/model/TaskStatus;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;IJLjava/lang/Long;)V", "getCategory", "()Ljava/lang/String;", "getCompletedAt", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCreatedAt", "()J", "getDeadline", "()Ljava/time/LocalDateTime;", "getDescription", "displayStatus", "Lcom/ledger/task/data/model/DisplayStatus;", "getDisplayStatus", "()Lcom/ledger/task/data/model/DisplayStatus;", "getHasImage", "()Z", "getId", "getPredecessorIds", "getPriority", "()Lcom/ledger/task/data/model/Priority;", "getRelatedIds", "getRichContent", "getSortOrder", "()I", "getStatus", "()Lcom/ledger/task/data/model/TaskStatus;", "getTitle", "calculateDisplayStatus", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLjava/lang/String;Lcom/ledger/task/data/model/Priority;Ljava/time/LocalDateTime;Lcom/ledger/task/data/model/TaskStatus;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;IJLjava/lang/Long;)Lcom/ledger/task/data/local/TaskEntity;", "equals", "other", "hashCode", "toString", "app_debug"})
public final class TaskEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
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
    private final java.lang.String description = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String category = null;
    private final boolean hasImage = false;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String predecessorIds = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String relatedIds = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String richContent = null;
    private final int sortOrder = 0;
    private final long createdAt = 0L;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long completedAt = null;
    
    /**
     * 任务实体（Room Entity）
     * 仅用于数据库操作，不直接暴露给 UI 层
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.local.TaskEntity copy(long id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Priority priority, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime deadline, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TaskStatus status, @org.jetbrains.annotations.NotNull
    java.lang.String description, @org.jetbrains.annotations.NotNull
    java.lang.String category, boolean hasImage, @org.jetbrains.annotations.NotNull
    java.lang.String predecessorIds, @org.jetbrains.annotations.NotNull
    java.lang.String relatedIds, @org.jetbrains.annotations.NotNull
    java.lang.String richContent, int sortOrder, long createdAt, @org.jetbrains.annotations.Nullable
    java.lang.Long completedAt) {
        return null;
    }
    
    /**
     * 任务实体（Room Entity）
     * 仅用于数据库操作，不直接暴露给 UI 层
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 任务实体（Room Entity）
     * 仅用于数据库操作，不直接暴露给 UI 层
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 任务实体（Room Entity）
     * 仅用于数据库操作，不直接暴露给 UI 层
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public TaskEntity(long id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Priority priority, @org.jetbrains.annotations.NotNull
    java.time.LocalDateTime deadline, @org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TaskStatus status, @org.jetbrains.annotations.NotNull
    java.lang.String description, @org.jetbrains.annotations.NotNull
    java.lang.String category, boolean hasImage, @org.jetbrains.annotations.NotNull
    java.lang.String predecessorIds, @org.jetbrains.annotations.NotNull
    java.lang.String relatedIds, @org.jetbrains.annotations.NotNull
    java.lang.String richContent, int sortOrder, long createdAt, @org.jetbrains.annotations.Nullable
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
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCategory() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final boolean getHasImage() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPredecessorIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRelatedIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRichContent() {
        return null;
    }
    
    public final int component12() {
        return 0;
    }
    
    public final int getSortOrder() {
        return 0;
    }
    
    public final long component13() {
        return 0L;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long component14() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getCompletedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.DisplayStatus getDisplayStatus() {
        return null;
    }
    
    private final com.ledger.task.data.model.DisplayStatus calculateDisplayStatus(com.ledger.task.data.model.TaskStatus status, java.time.LocalDateTime deadline) {
        return null;
    }
}