package com.ledger.task.data.model;

import java.lang.System;

/**
 * 分类路径
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0087\b\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B!\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0006J\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J)\u0010\f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001J\t\u0010\u0012\u001a\u00020\u0004H\u00d6\u0001J\u0010\u0010\u0012\u001a\u00020\u00042\b\b\u0002\u0010\u0013\u001a\u00020\u0004R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/ledger/task/data/model/CategoryPath;", "", "path", "", "", "names", "(Ljava/util/List;Ljava/util/List;)V", "getNames", "()Ljava/util/List;", "getPath", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "separator", "Companion", "app_debug"})
public final class CategoryPath {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> path = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> names = null;
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.data.model.CategoryPath.Companion Companion = null;
    
    /**
     * 分类路径
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.CategoryPath copy(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> path, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> names) {
        return null;
    }
    
    /**
     * 分类路径
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 分类路径
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 分类路径
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public CategoryPath(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> path, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> names) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getPath() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getNames() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String toString(@org.jetbrains.annotations.NotNull
    java.lang.String separator) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a8\u0006\b"}, d2 = {"Lcom/ledger/task/data/model/CategoryPath$Companion;", "", "()V", "fromNodes", "Lcom/ledger/task/data/model/CategoryPath;", "nodes", "", "Lcom/ledger/task/data/model/CategoryNode;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.ledger.task.data.model.CategoryPath fromNodes(@org.jetbrains.annotations.NotNull
        java.util.List<com.ledger.task.data.model.CategoryNode> nodes) {
            return null;
        }
    }
}