package com.ledger.task

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.ledger.task.data.local.provideSampleData
import com.ledger.task.data.local.toDomain
import com.ledger.task.di.appModule
import com.ledger.task.di.useCaseModule
import com.ledger.task.di.viewModelModule
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.notification.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application 类，初始化 Koin 依赖注入
 */
class TaskLedgerApp : Application() {

    companion object {
        private const val TAG = "TaskLedgerApp"
    }

    private val appScope get() = ProcessLifecycleOwner.get().lifecycleScope
    private val repository: TaskRepository by inject()

    override fun onCreate() {
        super.onCreate()

        // 初始化 Koin
        startKoin {
            androidContext(this@TaskLedgerApp)
            modules(appModule, useCaseModule, viewModelModule)
        }

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
