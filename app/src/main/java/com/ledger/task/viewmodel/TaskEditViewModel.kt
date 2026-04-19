package com.ledger.task.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.TaskLedgerApp
import com.ledger.task.data.local.TaskEntity
import com.ledger.task.data.local.toDomain
import com.ledger.task.data.model.CategoryNode
import com.ledger.task.data.model.DefaultCategories
import com.ledger.task.data.model.Priority
import com.ledger.task.data.model.RichContent
import com.ledger.task.data.model.RichTextItem
import com.ledger.task.data.model.Task
import com.ledger.task.data.model.TaskStatus
import com.ledger.task.domain.DependencyState
import com.ledger.task.domain.DependencyValidationResult
import com.ledger.task.domain.TaskDependencyValidator
import com.ledger.task.notification.ReminderManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class TaskEditUiState(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val deadline: LocalDateTime = LocalDateTime.now().plusDays(7),
    val status: TaskStatus = TaskStatus.PENDING,
    val completedAt: Long? = null,  // 完成时间（毫秒时间戳）
    val category: String = "",
    val hasImage: Boolean = false,
    val richContent: RichContent = RichContent(emptyList()),
    val predecessorIds: List<Long> = emptyList(),
    val relatedIds: List<Long> = emptyList(),
    val predecessorTasks: List<Task> = emptyList(),
    val relatedTasks: List<Task> = emptyList(),
    val dependencyState: DependencyState = DependencyState.NoDependencies,
    val dependencyValidationError: DependencyValidationResult? = null,
    val showDependencyBlockedDialog: Boolean = false,
    val isEdit: Boolean = false,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val titleError: Boolean = false,
    val showPredecessorDialog: Boolean = false,
    val showRelatedDialog: Boolean = false,
    val showCategoryDialog: Boolean = false,
    val showAddCategoryDialog: Boolean = false,
    val availableTasks: List<Task> = emptyList(),
    val categories: List<CategoryNode> = DefaultCategories.defaultTree.getAllNodes(),
    val newCategoryName: String = "",
    val newCategoryColor: Color = DefaultCategories.ColorDefault,
    val editingCategoryId: String? = null,
    val availableTasksLoaded: Boolean = false  // 标记是否已加载可用任务
)

class TaskEditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as TaskLedgerApp).repository
    private val dependencyValidator = TaskDependencyValidator(repository)

    private val _uiState = MutableStateFlow(TaskEditUiState())
    val uiState: StateFlow<TaskEditUiState> = _uiState.asStateFlow()

    // 缓存可用任务列表，避免重复查询数据库
    private var cachedAvailableTasks: List<Task>? = null

    fun loadTask(taskId: Long) {
        if (taskId <= 0) {
            _uiState.value = TaskEditUiState(isEdit = false)
            return
        }
        viewModelScope.launch {
            val task = repository.getById(taskId) ?: return@launch
            val predecessorTasks = getTasksByIds(task.predecessorIds)
            val relatedTasks = getTasksByIds(task.relatedIds)
            val dependencyState = com.ledger.task.domain.DependencyStateCalculator.calculate(predecessorTasks)
            _uiState.value = TaskEditUiState(
                id = task.id,
                title = task.title,
                description = task.description,
                priority = task.priority,
                deadline = task.deadline,
                status = task.status,
                completedAt = task.completedAt,
                category = task.category,
                hasImage = task.hasImage,
                richContent = task.richContent,
                predecessorIds = task.predecessorIds,
                relatedIds = task.relatedIds,
                predecessorTasks = predecessorTasks,
                relatedTasks = relatedTasks,
                dependencyState = dependencyState,
                isEdit = true
            )
        }
    }

    private suspend fun getTasksByIds(ids: List<Long>): List<Task> {
        if (ids.isEmpty()) return emptyList()
        return repository.getTasksByIds(ids)
    }

    fun onTitleChange(title: String) {
        _uiState.value = _uiState.value.copy(title = title, titleError = false)
    }

    fun onDescriptionChange(desc: String) {
        _uiState.value = _uiState.value.copy(description = desc)
    }

    fun onPriorityChange(priority: Priority) {
        _uiState.value = _uiState.value.copy(priority = priority)
    }

    fun onDeadlineChange(deadline: LocalDateTime) {
        _uiState.value = _uiState.value.copy(deadline = deadline)
    }

    fun onDeadlineDateChange(date: LocalDate) {
        val currentTime = _uiState.value.deadline.toLocalTime()
        _uiState.value = _uiState.value.copy(deadline = LocalDateTime.of(date, currentTime))
    }

    fun onDeadlineTimeChange(time: LocalTime) {
        val currentDate = _uiState.value.deadline.toLocalDate()
        _uiState.value = _uiState.value.copy(deadline = LocalDateTime.of(currentDate, time))
    }

    fun onStatusChange(status: TaskStatus) {
        // 如果状态变为已完成，且之前没有完成时间，则设置当前时间
        val completedAt = if (status == TaskStatus.DONE && _uiState.value.status != TaskStatus.DONE) {
            _uiState.value.completedAt ?: System.currentTimeMillis()
        } else if (status != TaskStatus.DONE) {
            null
        } else {
            _uiState.value.completedAt
        }
        _uiState.value = _uiState.value.copy(status = status, completedAt = completedAt)
    }

    fun onCompletedAtChange(completedAt: Long?) {
        _uiState.value = _uiState.value.copy(completedAt = completedAt)
    }

    fun onRichContentChange(richContent: RichContent) {
        _uiState.value = _uiState.value.copy(richContent = richContent)
    }

    fun onPredecessorIdsChange(newIds: List<Long>) {
        viewModelScope.launch {
            val newTasks = getTasksByIds(newIds)
            val dependencyState = com.ledger.task.domain.DependencyStateCalculator.calculate(newTasks)
            _uiState.value = _uiState.value.copy(
                predecessorIds = newIds,
                predecessorTasks = newTasks,
                dependencyState = dependencyState
            )
        }
    }

    fun onRelatedIdsChange(newIds: List<Long>) {
        viewModelScope.launch {
            val newTasks = getTasksByIds(newIds)
            _uiState.value = _uiState.value.copy(
                relatedIds = newIds,
                relatedTasks = newTasks
            )
        }
    }

    fun onPredecessorAdd(predecessorId: Long) {
        val newIds = _uiState.value.predecessorIds + predecessorId
        _uiState.value = _uiState.value.copy(predecessorIds = newIds)
    }

    fun onPredecessorRemove(predecessorId: Long) {
        val newIds = _uiState.value.predecessorIds.filter { it != predecessorId }
        _uiState.value = _uiState.value.copy(predecessorIds = newIds)
    }

    fun onRelatedAdd(relatedId: Long) {
        val newIds = _uiState.value.relatedIds + relatedId
        _uiState.value = _uiState.value.copy(relatedIds = newIds)
    }

    fun onRelatedRemove(relatedId: Long) {
        val newIds = _uiState.value.relatedIds.filter { it != relatedId }
        _uiState.value = _uiState.value.copy(relatedIds = newIds)
    }

    fun save() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.value = state.copy(titleError = true)
            return
        }
        if (state.isSaving) return

        // 根据 richContent 中是否包含图片来自动设置 hasImage
        val hasImage = state.richContent.items.any { it is RichTextItem.Image }

        _uiState.value = state.copy(isSaving = true)
        viewModelScope.launch {
            val entity = TaskEntity(
                id = if (state.isEdit) state.id else 0,
                title = state.title,
                description = state.description,
                priority = state.priority,
                deadline = state.deadline,
                status = state.status,
                category = state.category,
                hasImage = hasImage,
                richContent = state.richContent.serialize(),
                predecessorIds = state.predecessorIds.joinToString(","),
                relatedIds = state.relatedIds.joinToString(","),
                completedAt = state.completedAt
            )
            val task = entity.toDomain()
            val taskId = if (state.isEdit) {
                repository.update(task)
                state.id
            } else {
                repository.insert(task)
            }

            // 调度或取消提醒
            if (state.status != TaskStatus.DONE && taskId > 0) {
                ReminderManager.scheduleReminder(
                    context = getApplication(),
                    taskId = taskId,
                    title = state.title,
                    deadline = state.deadline
                )
            } else if (state.status == TaskStatus.DONE && taskId > 0) {
                ReminderManager.cancelReminder(getApplication(), taskId)
            }

            _uiState.value = _uiState.value.copy(isSaving = false, saved = true)
            clearAvailableTasksCache()
        }
    }

    /**
     * 加载可用任务列表（使用缓存避免重复查询）
     */
    fun loadAvailableTasks() {
        // 如果已有缓存，直接使用
        val cached = cachedAvailableTasks
        if (cached != null) {
            val currentId = _uiState.value.id
            val available = cached.filter { it.id != currentId }
            _uiState.value = _uiState.value.copy(availableTasks = available)
            return
        }

        // 首次加载
        viewModelScope.launch {
            val tasks = repository.getAll().firstOrNull() ?: emptyList()
            cachedAvailableTasks = tasks
            val currentId = _uiState.value.id
            val available = tasks.filter { it.id != currentId }
            _uiState.value = _uiState.value.copy(
                availableTasks = available,
                availableTasksLoaded = true
            )
        }
    }

    /**
     * 清除缓存（在保存新任务后调用）
     */
    fun clearAvailableTasksCache() {
        cachedAvailableTasks = null
    }

    fun onShowPredecessorDialog(show: Boolean) {
        if (show) loadAvailableTasks()
        _uiState.value = _uiState.value.copy(showPredecessorDialog = show)
    }

    fun onShowRelatedDialog(show: Boolean) {
        if (show) loadAvailableTasks()
        _uiState.value = _uiState.value.copy(showRelatedDialog = show)
    }

    fun onPredecessorSelect(taskId: Long) {
        val newIds = _uiState.value.predecessorIds + taskId
        _uiState.value = _uiState.value.copy(predecessorIds = newIds)
    }

    fun onRelatedSelect(taskId: Long) {
        val newIds = _uiState.value.relatedIds + taskId
        _uiState.value = _uiState.value.copy(relatedIds = newIds)
    }

    fun onDialogConfirmPredecessors(newIds: List<Long>) {
        viewModelScope.launch {
            // 验证每个前置依赖
            val currentTaskId = _uiState.value.id
            var hasError = false
            var errorResult: DependencyValidationResult? = null

            for (predecessorId in newIds) {
                val result = dependencyValidator.validatePredecessorAddition(currentTaskId, predecessorId)
                if (result != DependencyValidationResult.Valid) {
                    hasError = true
                    errorResult = result
                    break
                }
            }

            if (hasError) {
                _uiState.value = _uiState.value.copy(
                    dependencyValidationError = errorResult
                )
                return@launch
            }

            // 验证通过，更新状态
            val newTasks = getTasksByIds(newIds)
            val dependencyState = com.ledger.task.domain.DependencyStateCalculator.calculate(newTasks)
            _uiState.value = _uiState.value.copy(
                predecessorIds = newIds,
                predecessorTasks = newTasks,
                dependencyState = dependencyState,
                showPredecessorDialog = false,
                dependencyValidationError = null
            )
        }
    }

    fun onDismissDependencyError() {
        _uiState.value = _uiState.value.copy(dependencyValidationError = null)
    }

    fun onShowDependencyBlockedDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDependencyBlockedDialog = show)
    }

    /**
     * 带验证的状态变更
     * 如果任务被阻塞，不允许改为进行中或已完成
     */
    fun onStatusChangeWithValidation(newStatus: TaskStatus) {
        val currentState = _uiState.value

        // 如果要开始或完成任务，检查前置依赖
        if (newStatus == TaskStatus.IN_PROGRESS || newStatus == TaskStatus.DONE) {
            if (currentState.dependencyState.isBlocked) {
                _uiState.value = currentState.copy(showDependencyBlockedDialog = true)
                return
            }
        }

        onStatusChange(newStatus)
    }

    fun onDialogConfirmRelated(newIds: List<Long>) {
        _uiState.value = _uiState.value.copy(
            relatedIds = newIds,
            showRelatedDialog = false
        )
    }

    // 分类相关方法
    fun onShowCategoryDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showCategoryDialog = show)
    }

    fun onCategoryChange(category: String) {
        _uiState.value = _uiState.value.copy(
            category = category,
            showCategoryDialog = false
        )
    }

    fun onShowAddCategoryDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showAddCategoryDialog = show,
            newCategoryName = "",
            newCategoryColor = DefaultCategories.availableColors.first(),
            editingCategoryId = null
        )
    }

    fun onNewCategoryNameChange(name: String) {
        _uiState.value = _uiState.value.copy(newCategoryName = name)
    }

    fun onNewCategoryColorChange(color: Color) {
        _uiState.value = _uiState.value.copy(newCategoryColor = color)
    }

    fun onAddCategory() {
        val state = _uiState.value
        if (state.newCategoryName.isBlank()) return

        val newCategory = CategoryNode(
            id = "custom-${System.currentTimeMillis()}",
            name = state.newCategoryName.trim(),
            color = state.newCategoryColor
        )
        val newCategories = state.categories + newCategory
        _uiState.value = state.copy(
            categories = newCategories,
            showAddCategoryDialog = false,
            newCategoryName = "",
            newCategoryColor = DefaultCategories.availableColors.first()
        )
    }

    fun onEditCategory(categoryId: String) {
        val state = _uiState.value
        val category = state.categories.find { it.id == categoryId } ?: return
        _uiState.value = state.copy(
            showAddCategoryDialog = true,
            newCategoryName = category.name,
            newCategoryColor = category.color,
            editingCategoryId = categoryId
        )
    }

    fun onUpdateCategory() {
        val state = _uiState.value
        if (state.newCategoryName.isBlank() || state.editingCategoryId == null) return

        val updatedCategories = state.categories.map {
            if (it.id == state.editingCategoryId) {
                it.copy(name = state.newCategoryName.trim(), color = state.newCategoryColor)
            } else {
                it
            }
        }
        _uiState.value = state.copy(
            categories = updatedCategories,
            showAddCategoryDialog = false,
            newCategoryName = "",
            newCategoryColor = DefaultCategories.availableColors.first(),
            editingCategoryId = null
        )
    }

    fun onDeleteCategory(categoryId: String) {
        val state = _uiState.value
        val updatedCategories = state.categories.filter { it.id != categoryId }
        val newCategory = if (state.category == categoryId) "" else state.category
        _uiState.value = state.copy(
            categories = updatedCategories,
            category = newCategory
        )
    }
}
