package com.ledger.task.backup;

import java.lang.System;

/**
 * 备份信息
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J=\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u000bR\u0013\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\r\u00a8\u0006\u001c"}, d2 = {"Lcom/ledger/task/backup/BackupInfo;", "", "version", "", "timestamp", "", "isValid", "", "isPasswordProtected", "passwordHint", "(Ljava/lang/String;JZZLjava/lang/String;)V", "()Z", "getPasswordHint", "()Ljava/lang/String;", "getTimestamp", "()J", "getVersion", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class BackupInfo {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String version = null;
    private final long timestamp = 0L;
    private final boolean isValid = false;
    private final boolean isPasswordProtected = false;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String passwordHint = null;
    
    /**
     * 备份信息
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.backup.BackupInfo copy(@org.jetbrains.annotations.NotNull
    java.lang.String version, long timestamp, boolean isValid, boolean isPasswordProtected, @org.jetbrains.annotations.Nullable
    java.lang.String passwordHint) {
        return null;
    }
    
    /**
     * 备份信息
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 备份信息
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 备份信息
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public BackupInfo(@org.jetbrains.annotations.NotNull
    java.lang.String version, long timestamp, boolean isValid, boolean isPasswordProtected, @org.jetbrains.annotations.Nullable
    java.lang.String passwordHint) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getVersion() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final long getTimestamp() {
        return 0L;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean isValid() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean isPasswordProtected() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getPasswordHint() {
        return null;
    }
}