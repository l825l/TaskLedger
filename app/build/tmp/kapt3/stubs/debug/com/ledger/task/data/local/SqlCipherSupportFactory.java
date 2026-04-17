package com.ledger.task.data.local;

import java.lang.System;

/**
 * SQLCipher 支持工厂
 * 使用官方 SupportOpenHelperFactory 创建加密数据库
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/ledger/task/data/local/SqlCipherSupportFactory;", "", "()V", "create", "Lnet/zetetic/database/sqlcipher/SupportOpenHelperFactory;", "passphrase", "", "app_debug"})
public final class SqlCipherSupportFactory {
    @org.jetbrains.annotations.NotNull
    public static final com.ledger.task.data.local.SqlCipherSupportFactory INSTANCE = null;
    
    private SqlCipherSupportFactory() {
        super();
    }
    
    /**
     * 创建 SQLCipher SupportOpenHelperFactory
     * @param passphrase 加密密钥
     */
    @org.jetbrains.annotations.NotNull
    public final net.zetetic.database.sqlcipher.SupportOpenHelperFactory create(@org.jetbrains.annotations.NotNull
    byte[] passphrase) {
        return null;
    }
}