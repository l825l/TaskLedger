package com.ledger.task.data.model;

import java.lang.System;

/**
 * 台账筛选状态
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\tH\u00c6\u0003J7\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\t2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0007H\u00d6\u0001R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000e\u00a8\u0006\u001c"}, d2 = {"Lcom/ledger/task/data/model/LedgerFilterState;", "", "startDate", "Ljava/time/LocalDate;", "endDate", "categories", "", "", "includeArchived", "", "(Ljava/time/LocalDate;Ljava/time/LocalDate;Ljava/util/Set;Z)V", "getCategories", "()Ljava/util/Set;", "getEndDate", "()Ljava/time/LocalDate;", "getIncludeArchived", "()Z", "getStartDate", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class LedgerFilterState {
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDate startDate = null;
    @org.jetbrains.annotations.NotNull
    private final java.time.LocalDate endDate = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Set<java.lang.String> categories = null;
    private final boolean includeArchived = false;
    
    /**
     * 台账筛选状态
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.LedgerFilterState copy(@org.jetbrains.annotations.NotNull
    java.time.LocalDate startDate, @org.jetbrains.annotations.NotNull
    java.time.LocalDate endDate, @org.jetbrains.annotations.NotNull
    java.util.Set<java.lang.String> categories, boolean includeArchived) {
        return null;
    }
    
    /**
     * 台账筛选状态
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 台账筛选状态
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 台账筛选状态
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public LedgerFilterState(@org.jetbrains.annotations.NotNull
    java.time.LocalDate startDate, @org.jetbrains.annotations.NotNull
    java.time.LocalDate endDate, @org.jetbrains.annotations.NotNull
    java.util.Set<java.lang.String> categories, boolean includeArchived) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate getStartDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.time.LocalDate getEndDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Set<java.lang.String> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.Set<java.lang.String> getCategories() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean getIncludeArchived() {
        return false;
    }
}