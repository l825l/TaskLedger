package com.ledger.task

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.ledger.task.data.local.provideSampleData
import com.ledger.task.data.local.toDomain
import com.ledger.task.di.appModule
import com.ledger.task.di.useCaseModule
import com.ledger.task.di.viewModelModule
import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository
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
    private val tagRepository: TagRepository by inject()

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
        seedDefaultTags()
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

    /**
     * 初始化默认标签
     */
    private fun seedDefaultTags() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("default_tags_seeded", false)) return

        appScope.launch(Dispatchers.IO) {
            val defaultTags = listOf(
                "工作" to Tag.PRESET_COLORS[0], // 红色
                "生活" to Tag.PRESET_COLORS[3], // 绿色
                "学习" to Tag.PRESET_COLORS[4], // 蓝色
                "重要" to Tag.PRESET_COLORS[1]  // 橙色
            )

            defaultTags.forEach { (name, color) ->
                val tag = Tag(name = name, color = color)
                tagRepository.saveTag(tag)
            }

            prefs.edit().putBoolean("default_tags_seeded", true).apply()
        }
    }
}
