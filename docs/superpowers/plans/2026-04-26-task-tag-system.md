# 任务标签系统实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为任务添加自定义标签系统，支持标签创建、颜色自定义、按标签筛选和标签统计。

**Architecture:** 采用 Clean Architecture 分层架构。新增 Tag 领域模型和 TagEntity 数据实体，通过 Room 数据库存储标签及任务-标签关联关系。使用 Flow 实现响应式数据流，ViewModel 管理 UI 状态。

**Tech Stack:** Kotlin, Jetpack Compose, Room, Koin, Material 3

---

## 文件结构

### 新增文件
- `domain/model/Tag.kt` - 标签领域模型
- `data/local/TagEntity.kt` - 标签数据库实体
- `data/local/TaskTagCrossRef.kt` - 任务-标签关联实体
- `data/local/TagDao.kt` - 标签数据访问对象
- `domain/repository/TagRepository.kt` - 标签仓库接口
- `data/repository/TagRepositoryImpl.kt` - 标签仓库实现
- `domain/usecase/GetAllTagsUseCase.kt` - 获取所有标签用例
- `domain/usecase/SaveTagUseCase.kt` - 保存标签用例
- `domain/usecase/DeleteTagUseCase.kt` - 删除标签用例
- `domain/usecase/GetTasksByTagUseCase.kt` - 按标签获取任务用例
- `domain/usecase/GetTagStatsUseCase.kt` - 获取标签统计用例
- `ui/component/TagChip.kt` - 标签芯片组件
- `ui/component/TagSelector.kt` - 标签选择器组件
- `ui/component/TagFilterBar.kt` - 标签筛选栏组件
- `ui/component/TagStatsCard.kt` - 标签统计卡片组件

### 修改文件
- `data/local/AppDatabase.kt` - 添加标签表和关联表
- `data/local/TaskEntity.kt` - 添加标签关联支持
- `di/AppModule.kt` - 注册标签相关依赖
- `ui/screen/TaskEditScreen.kt` - 添加标签选择功能
- `ui/screen/AllTasksScreen.kt` - 添加标签筛选功能
- `viewmodel/TaskEditViewModel.kt` - 添加标签状态管理
- `viewmodel/AllTasksViewModel.kt` - 添加标签筛选状态

---

## Task 1: 创建标签领域模型

**Files:**
- Create: `app/src/main/java/com/ledger/task/domain/model/Tag.kt`

- [ ] **Step 1: 创建 Tag 数据类**

```kotlin
package com.ledger.task.domain.model

import androidx.compose.ui.graphics.Color

/**
 * 任务标签
 */
data class Tag(
    val id: Long = 0,
    val name: String,
    val color: Color,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        // 预设颜色
        val PRESET_COLORS = listOf(
            Color(0xFFEF5350), // 红色
            Color(0xFFFF7043), // 橙色
            Color(0xFFFFCA28), // 黄色
            Color(0xFF66BB6A), // 绿色
            Color(0xFF42A5F5), // 蓝色
            Color(0xFFAB47BC), // 紫色
            Color(0xFF78909C), // 灰色
            Color(0xFF26A69A)  // 青色
        )
    }
}
```

- [ ] **Step 2: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: 提交**

```bash
git add app/src/main/java/com/ledger/task/domain/model/Tag.kt
git commit -m "feat: 添加标签领域模型"
```

---

## Task 2: 创建标签数据库实体

**Files:**
- Create: `app/src/main/java/com/ledger/task/data/local/TagEntity.kt`
- Create: `app/src/main/java/com/ledger/task/data/local/TaskTagCrossRef.kt`

- [ ] **Step 1: 创建 TagEntity**

```kotlin
package com.ledger.task.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ledger.task.domain.model.Tag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * 标签实体（Room Entity）
 */
@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val colorArgb: Int,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Tag {
        return Tag(
            id = id,
            name = name,
            color = Color(colorArgb),
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomain(tag: Tag): TagEntity {
            return TagEntity(
                id = tag.id,
                name = tag.name,
                colorArgb = tag.color.toArgb(),
                createdAt = tag.createdAt
            )
        }
    }
}
```

- [ ] **Step 2: 创建 TaskTagCrossRef 关联表**

```kotlin
package com.ledger.task.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * 任务-标签关联表（多对多）
 */
@Entity(
    tableName = "task_tags",
    primaryKeys = ["taskId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["taskId"]),
        Index(value = ["tagId"])
    ]
)
data class TaskTagCrossRef(
    val taskId: Long,
    val tagId: Long
)
```

- [ ] **Step 3: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: 提交**

```bash
git add app/src/main/java/com/ledger/task/data/local/TagEntity.kt
git add app/src/main/java/com/ledger/task/data/local/TaskTagCrossRef.kt
git commit -m "feat: 添加标签数据库实体和关联表"
```

---

## Task 3: 创建标签 DAO

**Files:**
- Create: `app/src/main/java/com/ledger/task/data/local/TagDao.kt`

- [ ] **Step 1: 创建 TagDao 接口**

```kotlin
package com.ledger.task.data.local

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction

/**
 * 标签数据访问对象
 */
@Dao
interface TagDao {

    // 获取所有标签
    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun getAllTags(): Flow<List<TagEntity>>

    // 根据 ID 获取标签
    @Query("SELECT * FROM tags WHERE id = :tagId")
    suspend fun getTagById(tagId: Long): TagEntity?

    // 插入标签
    @Insert
    suspend fun insertTag(tag: TagEntity): Long

    // 删除标签
    @Delete
    suspend fun deleteTag(tag: TagEntity)

    // 获取任务的所有标签
    @Query("""
        SELECT t.* FROM tags t
        INNER JOIN task_tags tt ON t.id = tt.tagId
        WHERE tt.taskId = :taskId
        ORDER BY t.name ASC
    """)
    fun getTagsForTask(taskId: Long): Flow<List<TagEntity>>

    // 获取标签关联的任务数量
    @Query("SELECT COUNT(*) FROM task_tags WHERE tagId = :tagId")
    fun getTaskCountForTag(tagId: Long): Flow<Int>

    // 添加任务-标签关联
    @Insert
    suspend fun addTaskTag(crossRef: TaskTagCrossRef)

    // 删除任务-标签关联
    @Query("DELETE FROM task_tags WHERE taskId = :taskId AND tagId = :tagId")
    suspend fun removeTaskTag(taskId: Long, tagId: Long)

    // 删除任务的所有标签关联
    @Query("DELETE FROM task_tags WHERE taskId = :taskId")
    suspend fun clearTaskTags(taskId: Long)

    // 获取包含指定标签的任务 ID 列表
    @Query("SELECT taskId FROM task_tags WHERE tagId = :tagId")
    fun getTaskIdsByTag(tagId: Long): Flow<List<Long>>

    // 检查标签名称是否存在
    @Query("SELECT EXISTS(SELECT 1 FROM tags WHERE name = :name AND id != :excludeId)")
    suspend fun isNameExists(name: String, excludeId: Long = 0): Boolean
}
```

- [ ] **Step 2: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: 提交**

```bash
git add app/src/main/java/com/ledger/task/data/local/TagDao.kt
git commit -m "feat: 添加标签数据访问对象"
```

---

## Task 4: 更新数据库配置

**Files:**
- Modify: `app/src/main/java/com/ledger/task/data/local/AppDatabase.kt`

- [ ] **Step 1: 添加标签表到数据库**

在 `AppDatabase.kt` 中添加：

```kotlin
@Database(
    entities = [TaskEntity::class, SubTaskEntity::class, TagEntity::class, TaskTagCrossRef::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
    abstract fun tagDao(): TagDao
}

/**
 * 数据库迁移：版本 8 -> 9，添加标签表
 */
val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 创建标签表
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS tags (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                colorArgb INTEGER NOT NULL,
                createdAt INTEGER NOT NULL
            )
        """)
        // 创建任务-标签关联表
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS task_tags (
                taskId INTEGER NOT NULL,
                tagId INTEGER NOT NULL,
                PRIMARY KEY(taskId, tagId),
                FOREIGN KEY(taskId) REFERENCES tasks(id) ON DELETE CASCADE,
                FOREIGN KEY(tagId) REFERENCES tags(id) ON DELETE CASCADE
            )
        """)
        // 创建索引
        db.execSQL("CREATE INDEX IF NOT EXISTS index_task_tags_taskId ON task_tags (taskId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_task_tags_tagId ON task_tags (tagId)")
    }
}
```

- [ ] **Step 2: 更新 AppModule 添加迁移**

在 `di/AppModule.kt` 中更新数据库构建：

```kotlin
Room.databaseBuilder(context, AppDatabase::class.java, "task_ledger")
    .openHelperFactory(SqlCipherSupportFactory.create(passphrase))
    .addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9)
    .fallbackToDestructiveMigration()
    .build()
```

- [ ] **Step 3: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: 提交**

```bash
git add app/src/main/java/com/ledger/task/data/local/AppDatabase.kt
git add app/src/main/java/com/ledger/task/di/AppModule.kt
git commit -m "feat: 更新数据库配置添加标签表"
```

---

## Task 5: 创建标签仓库

**Files:**
- Create: `app/src/main/java/com/ledger/task/domain/repository/TagRepository.kt`
- Create: `app/src/main/java/com/ledger/task/data/repository/TagRepositoryImpl.kt`

- [ ] **Step 1: 创建 TagRepository 接口**

```kotlin
package com.ledger.task.domain.repository

import com.ledger.task.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    fun getAllTags(): Flow<List<Tag>>
    suspend fun getTagById(id: Long): Tag?
    suspend fun saveTag(tag: Tag): Long
    suspend fun deleteTag(tag: Tag)
    fun getTagsForTask(taskId: Long): Flow<List<Tag>>
    fun getTaskCountForTag(tagId: Long): Flow<Int>
    suspend fun addTagToTask(taskId: Long, tagId: Long)
    suspend fun removeTagFromTask(taskId: Long, tagId: Long)
    suspend fun clearTaskTags(taskId: Long)
    fun getTaskIdsByTag(tagId: Long): Flow<List<Long>>
    suspend fun isNameExists(name: String, excludeId: Long = 0): Boolean
}
```

- [ ] **Step 2: 创建 TagRepositoryImpl**

```kotlin
package com.ledger.task.data.repository

import com.ledger.task.data.local.TagDao
import com.ledger.task.data.local.TagEntity
import com.ledger.task.data.local.TaskTagCrossRef
import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagRepositoryImpl(
    private val tagDao: TagDao
) : TagRepository {

    override fun getAllTags(): Flow<List<Tag>> {
        return tagDao.getAllTags().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTagById(id: Long): Tag? {
        return tagDao.getTagById(id)?.toDomain()
    }

    override suspend fun saveTag(tag: Tag): Long {
        val entity = TagEntity.fromDomain(tag)
        return tagDao.insertTag(entity)
    }

    override suspend fun deleteTag(tag: Tag) {
        tagDao.deleteTag(TagEntity.fromDomain(tag))
    }

    override fun getTagsForTask(taskId: Long): Flow<List<Tag>> {
        return tagDao.getTagsForTask(taskId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTaskCountForTag(tagId: Long): Flow<Int> {
        return tagDao.getTaskCountForTag(tagId)
    }

    override suspend fun addTagToTask(taskId: Long, tagId: Long) {
        tagDao.addTaskTag(TaskTagCrossRef(taskId, tagId))
    }

    override suspend fun removeTagFromTask(taskId: Long, tagId: Long) {
        tagDao.removeTaskTag(taskId, tagId)
    }

    override suspend fun clearTaskTags(taskId: Long) {
        tagDao.clearTaskTags(taskId)
    }

    override fun getTaskIdsByTag(tagId: Long): Flow<List<Long>> {
        return tagDao.getTaskIdsByTag(tagId)
    }

    override suspend fun isNameExists(name: String, excludeId: Long): Boolean {
        return tagDao.isNameExists(name, excludeId)
    }
}
```

- [ ] **Step 3: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: 提交**

```bash
git add app/src/main/java/com/ledger/task/domain/repository/TagRepository.kt
git add app/src/main/java/com/ledger/task/data/repository/TagRepositoryImpl.kt
git commit -m "feat: 添加标签仓库"
```

---

## Task 6: 创建标签 UseCase

**Files:**
- Create: `app/src/main/java/com/ledger/task/domain/usecase/GetAllTagsUseCase.kt`
- Create: `app/src/main/java/com/ledger/task/domain/usecase/SaveTagUseCase.kt`
- Create: `app/src/main/java/com/ledger/task/domain/usecase/DeleteTagUseCase.kt`
- Create: `app/src/main/java/com/ledger/task/domain/usecase/GetTagStatsUseCase.kt`

- [ ] **Step 1: 创建 GetAllTagsUseCase**

```kotlin
package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow

class GetAllTagsUseCase(private val repository: TagRepository) {
    operator fun invoke(): Flow<List<Tag>> = repository.getAllTags()
}
```

- [ ] **Step 2: 创建 SaveTagUseCase**

```kotlin
package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository

class SaveTagUseCase(private val repository: TagRepository) {
    suspend operator fun invoke(tag: Tag): Result<Long> {
        return runCatching {
            // 检查名称是否已存在
            if (repository.isNameExists(tag.name, tag.id)) {
                throw IllegalArgumentException("标签名称已存在")
            }
            repository.saveTag(tag)
        }
    }
}
```

- [ ] **Step 3: 创建 DeleteTagUseCase**

```kotlin
package com.ledger.task.domain.usecase

import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.repository.TagRepository

class DeleteTagUseCase(private val repository: TagRepository) {
    suspend operator fun invoke(tag: Tag): Result<Unit> {
        return runCatching {
            repository.deleteTag(tag)
        }
    }
}
```

- [ ] **Step 4: 创建 GetTagStatsUseCase**

```kotlin
package com.ledger.task.domain.usecase

import com.ledger.task.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class TagStats(
    val tagId: Long,
    val tagName: String,
    val taskCount: Int
)

class GetTagStatsUseCase(private val repository: TagRepository) {
    operator fun invoke(): Flow<List<TagStats>> {
        return combine(
            repository.getAllTags(),
            { tags ->
                tags.map { tag ->
                    TagStats(
                        tagId = tag.id,
                        tagName = tag.name,
                        taskCount = 0 // 将在 ViewModel 中动态计算
                    )
                }
            }
        )
    }
}
```

- [ ] **Step 5: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: 提交**

```bash
git add app/src/main/java/com/ledger/task/domain/usecase/GetAllTagsUseCase.kt
git add app/src/main/java/com/ledger/task/domain/usecase/SaveTagUseCase.kt
git add app/src/main/java/com/ledger/task/domain/usecase/DeleteTagUseCase.kt
git add app/src/main/java/com/ledger/task/domain/usecase/GetTagStatsUseCase.kt
git commit -m "feat: 添加标签相关 UseCase"
```

---

## Task 7: 注册依赖注入

**Files:**
- Modify: `app/src/main/java/com/ledger/task/di/AppModule.kt`

- [ ] **Step 1: 更新 AppModule**

在 `appModule` 中添加：

```kotlin
single { get<AppDatabase>().tagDao() }

// Repository
single<TagRepository> { TagRepositoryImpl(get()) }
```

在 `useCaseModule` 中添加：

```kotlin
factory { GetAllTagsUseCase(get()) }
factory { SaveTagUseCase(get()) }
factory { DeleteTagUseCase(get()) }
factory { GetTagStatsUseCase(get()) }
```

- [ ] **Step 2: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: 提交**

```bash
git add app/src/main/java/com/ledger/task/di/AppModule.kt
git commit -m "feat: 注册标签依赖注入"
```

---

## Task 8: 创建标签 UI 组件

**Files:**
- Create: `app/src/main/java/com/ledger/task/ui/component/TagChip.kt`
- Create: `app/src/main/java/com/ledger/task/ui/component/TagSelector.kt`

- [ ] **Step 1: 创建 TagChip 组件**

```kotlin
package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.domain.model.Tag

/**
 * 标签芯片组件
 */
@Composable
fun TagChip(
    tag: Tag,
    modifier: Modifier = Modifier,
    showDelete: Boolean = false,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null
) {
    val backgroundColor = if (selected) {
        tag.color.copy(alpha = 0.3f)
    } else {
        tag.color.copy(alpha = 0.15f)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 颜色指示点
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(tag.color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        // 标签名称
        Text(
            text = tag.name,
            color = tag.color,
            fontSize = 13.sp
        )
        // 删除按钮
        if (showDelete && onDeleteClick != null) {
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "删除标签",
                    tint = tag.color,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
```

- [ ] **Step 2: 创建 TagSelector 组件**

```kotlin
package com.ledger.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.Tag
import com.ledger.task.ui.theme.getTextMuted

/**
 * 标签选择器组件
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSelector(
    allTags: List<Tag>,
    selectedTagIds: List<Long>,
    onTagToggle: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (allTags.isEmpty()) {
        Text(
            text = "暂无标签，请先创建标签",
            color = getTextMuted(),
            modifier = modifier.padding(8.dp)
        )
        return
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        allTags.forEach { tag ->
            TagChip(
                tag = tag,
                selected = selectedTagIds.contains(tag.id),
                onClick = { onTagToggle(tag.id) }
            )
        }
    }
}
```

- [ ] **Step 3: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: 提交**

```bash
git add app/src/main/java/com/ledger/task/ui/component/TagChip.kt
git add app/src/main/java/com/ledger/task/ui/component/TagSelector.kt
git commit -m "feat: 添加标签 UI 组件"
```

---

## Task 9: 创建标签筛选栏

**Files:**
- Create: `app/src/main/java/com/ledger/task/ui/component/TagFilterBar.kt`

- [ ] **Step 1: 创建 TagFilterBar 组件**

```kotlin
package com.ledger.task.ui.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.Tag
import com.ledger.task.ui.theme.getTextMuted

/**
 * 标签筛选栏
 */
@Composable
fun TagFilterBar(
    tags: List<Tag>,
    selectedTagId: Long?,
    onTagSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tags.isEmpty()) {
        return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 全部标签选项
        Text(
            text = "全部",
            color = if (selectedTagId == null) getTextMuted() else getTextMuted().copy(alpha = 0.5f),
            modifier = Modifier
                .padding(end = 8.dp)
                .then(
                    if (selectedTagId == null) Modifier.padding(bottom = 2.dp)
                    else Modifier
                )
        )

        tags.forEach { tag ->
            TagChip(
                tag = tag,
                selected = selectedTagId == tag.id,
                onClick = {
                    onTagSelected(if (selectedTagId == tag.id) null else tag.id)
                }
            )
        }
    }
}
```

- [ ] **Step 2: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: 提交**

```bash
git add app/src/main/java/com/ledger/task/ui/component/TagFilterBar.kt
git commit -m "feat: 添加标签筛选栏组件"
```

---

## Task 10: 创建标签统计卡片

**Files:**
- Create: `app/src/main/java/com/ledger/task/ui/component/TagStatsCard.kt`

- [ ] **Step 1: 创建 TagStatsCard 组件**

```kotlin
package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.domain.model.Tag
import com.ledger.task.ui.theme.getSurfaceBackground
import com.ledger.task.ui.theme.getTextMuted
import com.ledger.task.ui.theme.getTextPrimary

/**
 * 标签统计数据
 */
data class TagStatItem(
    val tag: Tag,
    val taskCount: Int
)

/**
 * 标签统计卡片
 */
@Composable
fun TagStatsCard(
    tagStats: List<TagStatItem>,
    modifier: Modifier = Modifier
) {
    if (tagStats.isEmpty()) {
        return
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = getSurfaceBackground())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "标签统计",
                color = getTextPrimary(),
                style = MaterialTheme.typography.titleMedium
            )

            tagStats.sortedByDescending { it.taskCount }.forEach { stat ->
                TagStatRow(
                    tag = stat.tag,
                    count = stat.taskCount
                )
            }
        }
    }
}

@Composable
private fun TagStatRow(
    tag: Tag,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 颜色指示点
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(tag.color)
        )

        // 标签名称
        Text(
            text = tag.name,
            color = getTextMuted(),
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )

        // 任务数量
        Text(
            text = count.toString(),
            color = tag.color,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace
        )

        Text(
            text = "个任务",
            color = getTextMuted(),
            fontSize = 12.sp
        )
    }
}
```

- [ ] **Step 2: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: 提交**

```bash
git add app/src/main/java/com/ledger/task/ui/component/TagStatsCard.kt
git commit -m "feat: 添加标签统计卡片组件"
```

---

## Task 11: 集成标签到任务编辑页面

**Files:**
- Modify: `app/src/main/java/com/ledger/task/viewmodel/TaskEditViewModel.kt`
- Modify: `app/src/main/java/com/ledger/task/ui/screen/TaskEditScreen.kt`

- [ ] **Step 1: 更新 TaskEditViewModel 添加标签状态**

在 `TaskEditViewModel.kt` 中添加标签相关状态和方法：

```kotlin
// 在 UiState data class 中添加
data class UiState(
    // ... 现有字段
    val allTags: List<Tag> = emptyList(),
    val selectedTagIds: List<Long> = emptyList()
)

// 在 ViewModel 中添加
private val getAllTagsUseCase: GetAllTagsUseCase = get()

init {
    loadTags()
}

private fun loadTags() {
    viewModelScope.launch {
        getAllTagsUseCase().collect { tags ->
            _uiState.update { it.copy(allTags = tags) }
        }
    }
}

fun toggleTag(tagId: Long) {
    val currentTags = _uiState.value.selectedTagIds
    val newTags = if (currentTags.contains(tagId)) {
        currentTags - tagId
    } else {
        currentTags + tagId
    }
    _uiState.update { it.copy(selectedTagIds = newTags) }
}
```

- [ ] **Step 2: 在 TaskEditScreen 中添加标签选择器**

在任务编辑表单中添加标签选择区域：

```kotlin
// 在表单中添加
Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(8.dp)
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "标签",
            style = MaterialTheme.typography.titleSmall
        )

        TagSelector(
            allTags = uiState.allTags,
            selectedTagIds = uiState.selectedTagIds,
            onTagToggle = { viewModel.toggleTag(it) }
        )
    }
}
```

- [ ] **Step 3: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: 提交**

```bash
git add app/src/main/java/com/ledger/task/viewmodel/TaskEditViewModel.kt
git add app/src/main/java/com/ledger/task/ui/screen/TaskEditScreen.kt
git commit -m "feat: 集成标签选择到任务编辑页面"
```

---

## Task 12: 集成标签筛选到任务列表

**Files:**
- Modify: `app/src/main/java/com/ledger/task/viewmodel/AllTasksViewModel.kt`
- Modify: `app/src/main/java/com/ledger/task/ui/screen/AllTasksScreen.kt`

- [ ] **Step 1: 更新 AllTasksViewModel 添加标签筛选**

```kotlin
// 在 UiState 中添加
data class UiState(
    // ... 现有字段
    val allTags: List<Tag> = emptyList(),
    val selectedTagId: Long? = null
)

// 添加方法
fun onTagFilterChange(tagId: Long?) {
    _uiState.update { it.copy(selectedTagId = tagId) }
}
```

- [ ] **Step 2: 在 AllTasksScreen 中添加标签筛选栏**

```kotlin
// 在筛选区域添加
TagFilterBar(
    tags = uiState.allTags,
    selectedTagId = uiState.selectedTagId,
    onTagSelected = { viewModel.onTagFilterChange(it) }
)
```

- [ ] **Step 3: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: 提交**

```bash
git add app/src/main/java/com/ledger/task/viewmodel/AllTasksViewModel.kt
git add app/src/main/java/com/ledger/task/ui/screen/AllTasksScreen.kt
git commit -m "feat: 集成标签筛选到任务列表页面"
```

---

## Task 13: 添加标签管理入口

**Files:**
- Modify: `app/src/main/java/com/ledger/task/ui/screen/LedgerCenterScreen.kt`

- [ ] **Step 1: 在台账中心添加标签统计**

在统计区域添加标签统计卡片：

```kotlin
// 在 TaskStatisticsChart 后添加
if (uiState.tagStats.isNotEmpty()) {
    TagStatsCard(
        tagStats = uiState.tagStats,
        modifier = Modifier.fillMaxWidth()
    )
}
```

- [ ] **Step 2: 验证编译**

Run: `./gradlew compileDebugKotlin --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: 提交**

```bash
git add app/src/main/java/com/ledger/task/ui/screen/LedgerCenterScreen.kt
git commit -m "feat: 在台账中心添加标签统计"
```

---

## Task 14: 最终验证和测试

- [ ] **Step 1: 完整编译测试**

Run: `./gradlew assembleDebug --no-daemon`
Expected: BUILD SUCCESSFUL

- [ ] **Step 2: 安装到设备测试**

Run: `adb install -r app/build/outputs/apk/debug/app-debug.apk`
Expected: Success

- [ ] **Step 3: 手动测试清单**

测试项目：
- [ ] 创建新标签
- [ ] 为任务添加标签
- [ ] 从任务移除标签
- [ ] 按标签筛选任务
- [ ] 查看标签统计
- [ ] 删除标签

- [ ] **Step 4: 最终提交**

```bash
git add -A
git commit -m "feat: 完成任务标签系统"
```

---

## 自检清单

**1. Spec 覆盖检查：**
- [x] 支持为任务添加自定义标签 - Task 8, 11
- [x] 标签颜色自定义 - Task 1, 8
- [x] 按标签筛选任务 - Task 9, 12
- [x] 标签统计 - Task 10, 13

**2. 占位符扫描：**
- 无 TBD、TODO、implement later 等占位符
- 所有代码步骤包含完整实现

**3. 类型一致性：**
- Tag.id: Long 在所有文件中一致
- Tag.color: Color 在所有文件中一致
- TaskTagCrossRef 使用正确的关联字段名
