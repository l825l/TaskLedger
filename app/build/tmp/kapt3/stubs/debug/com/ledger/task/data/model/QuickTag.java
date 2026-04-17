package com.ledger.task.data.model;

import java.lang.System;

/**
 * 快捷标签枚举
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u0086\u0001\u0018\u0000 \u00102\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0010B\u001a\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006R\u001c\u0010\u0004\u001a\u00020\u0005\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000f\u0082\u0002\u000f\n\u0002\b\u0019\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b!\u00a8\u0006\u0011"}, d2 = {"Lcom/ledger/task/data/model/QuickTag;", "", "label", "", "color", "Landroidx/compose/ui/graphics/Color;", "(Ljava/lang/String;ILjava/lang/String;J)V", "getColor-0d7_KjU", "()J", "J", "getLabel", "()Ljava/lang/String;", "WAITING_FEEDBACK", "NEED_MEETING", "PENDING_REVIEW", "BLOCKED", "Companion", "app_debug"})
public enum QuickTag {
    /*public static final*/ WAITING_FEEDBACK /* = new WAITING_FEEDBACK(null, 0L) */,
    /*public static final*/ NEED_MEETING /* = new NEED_MEETING(null, 0L) */,
    /*public static final*/ PENDING_REVIEW /* = new PENDING_REVIEW(null, 0L) */,
    /*public static final*/ BLOCKED /* = new BLOCKED(null, 0L) */;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String label = null;
    private final long color = 0L;
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.data.model.QuickTag.Companion Companion = null;
    
    QuickTag(java.lang.String label, long color) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLabel() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ledger/task/data/model/QuickTag$Companion;", "", "()V", "fromLabel", "Lcom/ledger/task/data/model/QuickTag;", "label", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final com.ledger.task.data.model.QuickTag fromLabel(@org.jetbrains.annotations.NotNull
        java.lang.String label) {
            return null;
        }
    }
}