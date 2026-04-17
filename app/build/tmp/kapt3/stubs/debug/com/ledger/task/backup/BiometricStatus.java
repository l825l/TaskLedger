package com.ledger.task.backup;

import java.lang.System;

/**
 * 生物识别状态
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/ledger/task/backup/BiometricStatus;", "", "(Ljava/lang/String;I)V", "AVAILABLE", "NO_HARDWARE", "HARDWARE_UNAVAILABLE", "NONE_ENROLLED", "UNKNOWN", "app_debug"})
public enum BiometricStatus {
    /*public static final*/ AVAILABLE /* = new AVAILABLE() */,
    /*public static final*/ NO_HARDWARE /* = new NO_HARDWARE() */,
    /*public static final*/ HARDWARE_UNAVAILABLE /* = new HARDWARE_UNAVAILABLE() */,
    /*public static final*/ NONE_ENROLLED /* = new NONE_ENROLLED() */,
    /*public static final*/ UNKNOWN /* = new UNKNOWN() */;
    
    BiometricStatus() {
    }
}