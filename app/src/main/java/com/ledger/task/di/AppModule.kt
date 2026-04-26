package com.ledger.task.di

import androidx.room.Room
import com.ledger.task.data.local.AppDatabase
import com.ledger.task.data.local.DatabaseKeyManager
import com.ledger.task.data.local.MIGRATION_4_5
import com.ledger.task.data.local.MIGRATION_5_6
import com.ledger.task.data.local.MIGRATION_6_7
import com.ledger.task.data.local.MIGRATION_7_8
import com.ledger.task.data.local.MIGRATION_8_9
import com.ledger.task.data.local.SqlCipherSupportFactory
import com.ledger.task.data.local.TagDao
import com.ledger.task.data.repository.TagRepositoryImpl
import com.ledger.task.data.repository.TaskRepositoryImpl
import com.ledger.task.domain.repository.TagRepository
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.domain.usecase.CompleteTaskUseCase
import com.ledger.task.domain.usecase.DeleteTagUseCase
import com.ledger.task.domain.usecase.DeleteTaskUseCase
import com.ledger.task.domain.usecase.GetAllTagsUseCase
import com.ledger.task.domain.usecase.GetAllTasksUseCase
import com.ledger.task.domain.usecase.GetPredecessorTasksUseCase
import com.ledger.task.domain.usecase.GetPriorityTasksUseCase
import com.ledger.task.domain.usecase.GetRelatedTasksUseCase
import com.ledger.task.domain.usecase.GetTagStatsUseCase
import com.ledger.task.domain.usecase.GetTaskUseCase
import com.ledger.task.domain.usecase.GetTodayTasksUseCase
import com.ledger.task.domain.usecase.QuickCompleteTaskUseCase
import com.ledger.task.domain.usecase.SaveTagUseCase
import com.ledger.task.domain.usecase.SaveTaskUseCase
import com.ledger.task.domain.usecase.UndoCompleteTaskUseCase
import com.ledger.task.domain.usecase.ValidateDependencyUseCase
import com.ledger.task.viewmodel.AllTasksViewModel
import com.ledger.task.viewmodel.LedgerCenterViewModel
import com.ledger.task.viewmodel.PriorityTasksViewModel
import com.ledger.task.viewmodel.SettingsViewModel
import com.ledger.task.viewmodel.TaskEditViewModel
import com.ledger.task.viewmodel.TodayTasksViewModel
import com.ledger.task.viewmodel.UpdateViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin 依赖注入模块
 */
val appModule = module {
    // Database - 使用 SQLCipher 加密
    single {
        val context = androidContext() as android.app.Application
        val passphrase = DatabaseKeyManager.getOrCreateKey(context)
        Room.databaseBuilder(context, AppDatabase::class.java, "task_ledger")
            .openHelperFactory(SqlCipherSupportFactory.create(passphrase))
            .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9)
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().taskDao() }
    single { get<AppDatabase>().subTaskDao() }
    single { get<AppDatabase>().tagDao() }

    // Repository
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<TagRepository> { TagRepositoryImpl(get()) }
}

/**
 * UseCase 模块
 */
val useCaseModule = module {
    factory { GetTaskUseCase(get()) }
    factory { SaveTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { GetTodayTasksUseCase(get()) }
    factory { GetPriorityTasksUseCase(get()) }
    factory { GetAllTasksUseCase(get()) }
    factory { GetPredecessorTasksUseCase(get()) }
    factory { GetRelatedTasksUseCase(get()) }
    factory { QuickCompleteTaskUseCase(get()) }
    factory { UndoCompleteTaskUseCase(get()) }
    factory { CompleteTaskUseCase(get()) }
    // ValidateDependencyUseCase 需要 TaskDependencyValidator，暂时跳过

    // Tag UseCases
    factory { GetAllTagsUseCase(get()) }
    factory { SaveTagUseCase(get()) }
    factory { DeleteTagUseCase(get()) }
    factory { GetTagStatsUseCase(get()) }
}

/**
 * ViewModel 模块
 */
val viewModelModule = module {
    viewModel { TaskEditViewModel(androidContext() as android.app.Application, get(), get()) }
    viewModel { TodayTasksViewModel(get(), get()) }
    viewModel { PriorityTasksViewModel(get(), get()) }
    viewModel { AllTasksViewModel(get()) }
    viewModel { LedgerCenterViewModel(androidContext() as android.app.Application, get()) }
    viewModel { SettingsViewModel(androidContext() as android.app.Application, get(), get()) }
    viewModel { UpdateViewModel(androidContext() as android.app.Application) }
}
