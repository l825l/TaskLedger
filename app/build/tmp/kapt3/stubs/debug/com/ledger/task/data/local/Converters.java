package com.ledger.task.data.local;

import java.lang.System;

/**
 * Room 类型转换器
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u000bH\u0007J\u0010\u0010\f\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\rH\u0007J\u0010\u0010\u000e\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u000fH\u0007J\u0010\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0004H\u0007J\u0010\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\nH\u0007J\u0010\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0005\u001a\u00020\nH\u0007J\u0010\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0005\u001a\u00020\nH\u0007\u00a8\u0006\u0015"}, d2 = {"Lcom/ledger/task/data/local/Converters;", "", "()V", "fromLocalDate", "", "value", "Ljava/time/LocalDate;", "fromLocalDateTime", "Ljava/time/LocalDateTime;", "fromPriority", "", "Lcom/ledger/task/data/model/Priority;", "fromRichContent", "Lcom/ledger/task/data/model/RichContent;", "fromTaskStatus", "Lcom/ledger/task/data/model/TaskStatus;", "toLocalDate", "toLocalDateTime", "toPriority", "toRichContent", "toTaskStatus", "app_debug"})
public final class Converters {
    
    public Converters() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.TypeConverter
    public final java.lang.String fromPriority(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.Priority value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.TypeConverter
    public final com.ledger.task.data.model.Priority toPriority(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.TypeConverter
    public final java.lang.String fromTaskStatus(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.TaskStatus value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.TypeConverter
    public final com.ledger.task.data.model.TaskStatus toTaskStatus(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @androidx.room.TypeConverter
    public final long fromLocalDate(@org.jetbrains.annotations.NotNull
    java.time.LocalDate value) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.TypeConverter
    public final java.time.LocalDate toLocalDate(long value) {
        return null;
    }
    
    @androidx.room.TypeConverter
    public final long fromLocalDateTime(@org.jetbrains.annotations.NotNull
    java.time.LocalDateTime value) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.TypeConverter
    public final java.time.LocalDateTime toLocalDateTime(long value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.TypeConverter
    public final java.lang.String fromRichContent(@org.jetbrains.annotations.NotNull
    com.ledger.task.data.model.RichContent value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.TypeConverter
    public final com.ledger.task.data.model.RichContent toRichContent(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
}