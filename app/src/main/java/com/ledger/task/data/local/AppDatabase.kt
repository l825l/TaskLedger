package com.ledger.task.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Room 数据库
 * 版本 8：添加子任务表
 */
@Database(
    entities = [TaskEntity::class, SubTaskEntity::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
}

/**
 * 数据库迁移：版本 4 -> 5，添加 completedAt 字段
 */
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE tasks ADD COLUMN completedAt INTEGER")
    }
}

/**
 * 数据库迁移：版本 5 -> 6，启用加密
 * 注意：此迁移在 SQLCipher 上下文中执行，数据已通过加密密钥访问
 */
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 加密迁移由 SQLCipher 自动处理
        // 此处无需额外操作，数据库已通过加密密钥打开
    }
}

/**
 * 数据库迁移：版本 6 -> 7，添加循环任务字段
 */
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE tasks ADD COLUMN recurrence TEXT")
        db.execSQL("ALTER TABLE tasks ADD COLUMN isRecurringInstance INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE tasks ADD COLUMN parentRecurringId INTEGER")
    }
}

/**
 * 数据库迁移：版本 7 -> 8，添加子任务表
 */
val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS sub_tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                parentId INTEGER NOT NULL,
                title TEXT NOT NULL,
                isCompleted INTEGER NOT NULL DEFAULT 0,
                sortOrder INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY (parentId) REFERENCES tasks(id) ON DELETE CASCADE
            )
        """)
        db.execSQL("CREATE INDEX IF NOT EXISTS index_sub_tasks_parentId ON sub_tasks (parentId)")
    }
}

/**
 * 预置示例数据
 */
fun provideSampleData(): List<TaskEntity> {
    val today = LocalDate.now()
    val now = LocalDateTime.now()
    return listOf(
        TaskEntity(title = "完成季度财务报表审计", priority = Priority.HIGH, deadline = today.plusDays(2).atTime(now.toLocalTime()), status = TaskStatus.IN_PROGRESS),
        TaskEntity(title = "客户合同续签审批流程", priority = Priority.HIGH, deadline = today.minusDays(1).atTime(now.toLocalTime()), status = TaskStatus.IN_PROGRESS),
        TaskEntity(title = "新员工入职培训材料更新", priority = Priority.MEDIUM, deadline = today.plusDays(7).atTime(now.toLocalTime()), status = TaskStatus.PENDING),
        TaskEntity(title = "服务器安全漏洞修复", priority = Priority.HIGH, deadline = today.minusDays(3).atTime(now.toLocalTime()), status = TaskStatus.PENDING),
        TaskEntity(title = "产品需求评审会议纪要", priority = Priority.LOW, deadline = today.plusDays(12).atTime(now.toLocalTime()), status = TaskStatus.DONE),
        TaskEntity(title = "供应商资质年审材料提交", priority = Priority.MEDIUM, deadline = today.plusDays(5).atTime(now.toLocalTime()), status = TaskStatus.IN_PROGRESS),
        TaskEntity(title = "办公设备采购申请", priority = Priority.LOW, deadline = today.plusDays(17).atTime(now.toLocalTime()), status = TaskStatus.PENDING),
        TaskEntity(title = "年度考核评分表汇总", priority = Priority.MEDIUM, deadline = today.plusDays(9).atTime(now.toLocalTime()), status = TaskStatus.IN_PROGRESS),
        TaskEntity(title = "项目验收报告交付", priority = Priority.HIGH, deadline = today.plusDays(3).atTime(now.toLocalTime()), status = TaskStatus.IN_PROGRESS),
        TaskEntity(title = "内部制度文件修订发布", priority = Priority.LOW, deadline = today.plusDays(27).atTime(now.toLocalTime()), status = TaskStatus.DONE),
        TaskEntity(title = "客户投诉处理跟踪", priority = Priority.HIGH, deadline = today.minusDays(5).atTime(now.toLocalTime()), status = TaskStatus.PENDING),
        TaskEntity(title = "数据备份系统升级部署", priority = Priority.MEDIUM, deadline = today.plusDays(15).atTime(now.toLocalTime()), status = TaskStatus.PENDING)
    )
}
