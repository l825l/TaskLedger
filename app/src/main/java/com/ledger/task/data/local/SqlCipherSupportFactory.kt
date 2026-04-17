package com.ledger.task.data.local

import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

/**
 * SQLCipher 支持工厂
 * 使用官方 SupportOpenHelperFactory 创建加密数据库
 */
object SqlCipherSupportFactory {

    init {
        // 加载 SQLCipher 本地库
        System.loadLibrary("sqlcipher")
    }

    /**
     * 创建 SQLCipher SupportOpenHelperFactory
     * @param passphrase 加密密钥
     */
    fun create(passphrase: ByteArray): SupportOpenHelperFactory {
        return SupportOpenHelperFactory(passphrase)
    }
}
