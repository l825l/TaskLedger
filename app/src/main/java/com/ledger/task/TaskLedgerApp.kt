package com.ledger.task

import android.app.Application
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.ledger.task.data.local.AppDatabase
import com.ledger.task.data.local.DatabaseKeyManager
import com.ledger.task.data.local.MIGRATION_4_5
import com.ledger.task.data.local.MIGRATION_5_6
import com.ledger.task.data.local.SqlCipherSupportFactory
import com.ledger.task.data.local.provideSampleData
import com.ledger.task.data.local.toDomain
import com.ledger.task.data.repository.TaskRepositoryImpl
import com.ledger.task.notification.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Application 类，提供全局依赖
 */
class TaskLedgerApp : Application() {

    companion object {
        private const val TAG = "TaskLedgerApp"
    }

    // 使用 ProcessLifecycleOwner.lifecycleScope 替代自定义 CoroutineScope
    // 这确保协程与应用进程生命周期绑定，避免内存泄漏
    private val appScope get() = ProcessLifecycleOwner.get().lifecycleScope

    private var _database: AppDatabase? = null

    val database: AppDatabase
        get() = _database ?: createDatabase().also { _database = it }

    private fun createDatabase(): AppDatabase {
        // 获取加密密钥
        val passphrase = DatabaseKeyManager.getOrCreateKey(this)
        Log.i(TAG, "Database encryption initialized")

        return Room.databaseBuilder(this, AppDatabase::class.java, "task_ledger")
            .openHelperFactory(SqlCipherSupportFactory.create(passphrase))
            .addMigrations(MIGRATION_4_5, MIGRATION_5_6)
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * 重置数据库（用于恢复备份后重新初始化）
     */
    fun resetDatabase() {
        _database?.close()
        _database = null
        Log.i(TAG, "Database reset, will reinitialize with new key on next access")
    }

    val repository by lazy {
        TaskRepositoryImpl(database.taskDao())
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化通知渠道
        NotificationHelper.createNotificationChannel(this)
        seedInitialData()
    }

    private fun seedInitialData() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("data_seeded", false)) return

        appScope.launch(Dispatchers.IO) {
            provideSampleData().forEach { entity ->
                repository.insert(entity.toDomain())
            }
            prefs.edit().putBoolean("data_seeded", true).apply()
        }
    }
}
