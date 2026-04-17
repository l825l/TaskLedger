package com.ledger.task.data.model;

import java.lang.System;

/**
 * 富文本内容
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087\b\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0015\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0019\u0010\t\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\u0006\u0010\u0011\u001a\u00020\u000bJ\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0013J\t\u0010\u0015\u001a\u00020\u0013H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0017"}, d2 = {"Lcom/ledger/task/data/model/RichContent;", "", "items", "", "Lcom/ledger/task/data/model/RichTextItem;", "(Ljava/util/List;)V", "getItems", "()Ljava/util/List;", "component1", "copy", "equals", "", "other", "getQuickTags", "Lcom/ledger/task/data/model/QuickTag;", "hashCode", "", "isEmpty", "serialize", "", "toPlainText", "toString", "Companion", "app_debug"})
public final class RichContent {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.ledger.task.data.model.RichTextItem> items = null;
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.data.model.RichContent.Companion Companion = null;
    
    /**
     * 富文本内容
     */
    @org.jetbrains.annotations.NotNull
    public final com.ledger.task.data.model.RichContent copy(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.ledger.task.data.model.RichTextItem> items) {
        return null;
    }
    
    /**
     * 富文本内容
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * 富文本内容
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * 富文本内容
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public RichContent() {
        super();
    }
    
    public RichContent(@org.jetbrains.annotations.NotNull
    java.util.List<? extends com.ledger.task.data.model.RichTextItem> items) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.RichTextItem> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.RichTextItem> getItems() {
        return null;
    }
    
    public final boolean isEmpty() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String toPlainText() {
        return null;
    }
    
    /**
     * 序列化为字符串（JSON格式）
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String serialize() {
        return null;
    }
    
    /**
     * 获取所有快捷标签
     */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.ledger.task.data.model.QuickTag> getQuickTags() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ledger/task/data/model/RichContent$Companion;", "", "()V", "deserialize", "Lcom/ledger/task/data/model/RichContent;", "content", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * 从字符串反序列化
         */
        @org.jetbrains.annotations.NotNull
        public final com.ledger.task.data.model.RichContent deserialize(@org.jetbrains.annotations.NotNull
        java.lang.String content) {
            return null;
        }
    }
}