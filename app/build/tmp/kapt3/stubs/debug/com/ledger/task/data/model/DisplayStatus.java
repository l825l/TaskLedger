package com.ledger.task.data.model;

import java.lang.System;

/**
 * 显示用状态（包含衍生的 OVERDUE）
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\u000b"}, d2 = {"Lcom/ledger/task/data/model/DisplayStatus;", "", "label", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getLabel", "()Ljava/lang/String;", "PENDING", "IN_PROGRESS", "DONE", "OVERDUE", "app_debug"})
public enum DisplayStatus {
    /*public static final*/ PENDING /* = new PENDING(null) */,
    /*public static final*/ IN_PROGRESS /* = new IN_PROGRESS(null) */,
    /*public static final*/ DONE /* = new DONE(null) */,
    /*public static final*/ OVERDUE /* = new OVERDUE(null) */;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String label = null;
    
    DisplayStatus(java.lang.String label) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLabel() {
        return null;
    }
}