package com.ledger.task.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.data.local.TaskEntity
import com.ledger.task.data.local.serializeToString
import com.ledger.task.data.local.toDomain
import com.ledger.task.domain.model.CategoryNode
import com.ledger.task.domain.model.DefaultCategories
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.Recurrence
import com.ledger.task.domain.model.RecurrenceType
import com.ledger.task.domain.model.RichContent
import com.ledger.task.domain.model.RichTextItem
import com.ledger.task.domain.model.SubTask
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.DependencyState
import com.ledger.task.domain.DependencyValidationResult
import com.ledger.task.domain.TaskDependencyValidator
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.domain.usecase.CompleteTaskUseCase
import com.ledger.task.notification.ReminderManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
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
    // 过滤后的相关任务（排除已在前置依赖中的）
    val filteredRelatedTasks: List<Task> = emptyList(),
    val dependencyState: DependencyState = DependencyState.NoDependencies,
    val dependencyValidationError: DependencyValidationResult? = null,
    val showDependencyBlockedDialog: Boolean = false,
    // 删除确认对话框
    val showRemoveConfirmDialog: Boolean = false,
    val removeTargetTaskId: Long? = null,
    val removeTargetIsPredecessor: Boolean = true,
    val isEdit: Boolean = false,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val deleted: Boolean = false,  // 删除成功标记
    val showDeleteDialog: Boolean = false,  // 删除确认对话框
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
    val availableTasksLoaded: Boolean = false,  // 标记是否已加载可用任务
    // 循环任务
    val recurrence: Recurrence? = null,
    val showRecurrenceDialog: Boolean = false,
    // 子任务
    val subTasks: List<SubTask> = emptyList()
)

class TaskEditViewModel(
    application: Application,
    private val repository: TaskRepository,
    private val completeTaskUseCase: CompleteTaskUseCase
) : AndroidViewModel(application) {

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
            val predecessorTasks = getTasksByIds(task.predecessorIds).sortedByPriority()
            val relatedTasks = getTasksByIds(task.relatedIds).sortedByPriority()
            // 过滤相关任务：排除已在前置依赖中的
            val predecessorIdsSet = task.predecessorIds.toSet()
            val filteredRelatedTasks = relatedTasks.filter { it.id !in predecessorIdsSet }
            val dependencyState = com.ledger.task.domain.DependencyStateCalculator.calculate(predecessorTasks)
            // 加载子任务
            val subTasks = repository.getSubTasksNow(taskId)
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
                filteredRelatedTasks = filteredRelatedTasks,
                dependencyState = dependencyState,
                recurrence = task.recurrence,
                subTasks = subTasks,
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
            val newTasks = getTasksByIds(newIds).sortedByPriority()
            val dependencyState = com.ledger.task.domain.DependencyStateCalculator.calculate(newTasks)
            // 更新过滤后的相关任务
            val newPredecessorIdsSet = newIds.toSet()
            val filteredRelatedTasks = _uiState.value.relatedTasks.filter { it.id !in newPredecessorIdsSet }
            _uiState.value = _uiState.value.copy(
                predecessorIds = newIds,
                predecessorTasks = newTasks,
                filteredRelatedTasks = filteredRelatedTasks,
                dependencyState = dependencyState
            )
        }
    }

    fun onRelatedIdsChange(newIds: List<Long>) {
        viewModelScope.launch {
            val newTasks = getTasksByIds(newIds).sortedByPriority()
            // 过滤相关任务：排除已在前置依赖中的
            val predecessorIdsSet = _uiState.value.predecessorIds.toSet()
            val filteredRelatedTasks = newTasks.filter { it.id !in predecessorIdsSet }
            _uiState.value = _uiState.value.copy(
                relatedIds = newIds,
                relatedTasks = newTasks,
                filteredRelatedTasks = filteredRelatedTasks
            )
        }
    }

    /**
     * 按优先级排序任务列表
     */
    private fun List<Task>.sortedByPriority(): List<Task> {
        return this.sortedBy { task ->
            when (task.priority) {
                com.ledger.task.domain.model.Priority.HIGH -> 0
                com.ledger.task.domain.model.Priority.MEDIUM -> 1
                com.ledger.task.domain.model.Priority.NORMAL -> 2
                com.ledger.task.domain.model.Priority.LOW -> 3
            }
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
                completedAt = state.completedAt,
                recurrence = state.recurrence?.serializeToString(),
                isRecurringInstance = false,
                parentRecurringId = null
            )
            val task = entity.toDomain()

            val taskId: Long = if (state.isEdit) {
                val result = repository.update(task)
                result.fold(
                    onSuccess = { state.id },
                    onFailure = {
                        _uiState.value = _uiState.value.copy(isSaving = false)
                        return@launch
                    }
                )
            } else {
                val newId = repository.insert(task)
                if (newId <= 0) {
                    _uiState.value = _uiState.value.copy(isSaving = false)
                    return@launch
                }
                newId
            }

            // 调度或取消提醒
            if (state.status != TaskStatus.DONE) {
                ReminderManager.scheduleReminderIfEnabled(
                    context = getApplication(),
                    taskId = taskId,
                    title = state.title,
                    deadline = state.deadline
                )
            } else {
                ReminderManager.cancelReminder(getApplication(), taskId)
            }

            _uiState.value = _uiState.value.copy(isSaving = false, saved = true)
            clearAvailableTasksCache()
        }
    }

    // ==================== 循环任务相关方法 ====================

    /**
     * 显示循环设置对话框
     */
    fun onShowRecurrenceDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showRecurrenceDialog = show)
    }

    /**
     * 设置循环类型
     */
    fun onRecurrenceTypeChange(type: RecurrenceType) {
        val current = _uiState.value.recurrence
        _uiState.value = _uiState.value.copy(
            recurrence = Recurrence(
                type = type,
                interval = current?.interval ?: 1,
                endDate = current?.endDate,
                daysOfWeek = if (type == RecurrenceType.WEEKLY) {
                    current?.daysOfWeek ?: setOf(DayOfWeek.MONDAY)
                } else null
            )
        )
    }

    /**
     * 设置循环间隔
     */
    fun onRecurrenceIntervalChange(interval: Int) {
        val current = _uiState.value.recurrence ?: return
        _uiState.value = _uiState.value.copy(
            recurrence = current.copy(interval = interval.coerceIn(1, 99))
        )
    }

    /**
     * 设置循环结束日期
     */
    fun onRecurrenceEndDateChange(date: LocalDateTime?) {
        val current = _uiState.value.recurrence ?: return
        _uiState.value = _uiState.value.copy(
            recurrence = current.copy(endDate = date)
        )
    }

    /**
     * 切换周几
     */
    fun onDayOfWeekToggle(day: DayOfWeek) {
        val current = _uiState.value.recurrence ?: return
        val currentDays = current.daysOfWeek ?: emptySet()
        val newDays = if (day in currentDays) {
            currentDays - day
        } else {
            currentDays + day
        }
        _uiState.value = _uiState.value.copy(
            recurrence = current.copy(daysOfWeek = newDays.takeIf { it.isNotEmpty() })
        )
    }

    /**
     * 清除循环设置
     */
    fun onClearRecurrence() {
        _uiState.value = _uiState.value.copy(
            recurrence = null,
            showRecurrenceDialog = false
        )
    }

    /**
     * 确认循环设置
     */
    fun onConfirmRecurrence() {
        _uiState.value = _uiState.value.copy(showRecurrenceDialog = false)
    }

    // ==================== 子任务相关方法 ====================

    /**
     * 加载子任务
     */
    fun loadSubTasks(parentId: Long) {
        viewModelScope.launch {
            val subTasks = repository.getSubTasksNow(parentId)
            _uiState.value = _uiState.value.copy(subTasks = subTasks)
        }
    }

    /**
     * 添加子任务
     */
    fun onAddSubTask(title: String) {
        val parentId = _uiState.value.id
        if (parentId <= 0) return // 新任务尚未保存，无法添加子任务

        viewModelScope.launch {
            val subTask = SubTask(
                parentId = parentId,
                title = title,
                sortOrder = _uiState.value.subTasks.size
            )
            repository.insertSubTask(subTask)
            loadSubTasks(parentId)
        }
    }

    /**
     * 切换子任务完成状态
     */
    fun onToggleSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.updateSubTask(subTask.toggle())
            loadSubTasks(subTask.parentId)
        }
    }

    /**
     * 删除子任务
     */
    fun onDeleteSubTask(subTask: SubTask) {
        viewModelScope.launch {
            repository.deleteSubTask(subTask.id)
            loadSubTasks(subTask.parentId)
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
            val newTasks = getTasksByIds(newIds).sortedByPriority()
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
     * 显示删除确认对话框
     */
    fun onShowRemoveConfirmDialog(taskId: Long, isPredecessor: Boolean) {
        _uiState.value = _uiState.value.copy(
            showRemoveConfirmDialog = true,
            removeTargetTaskId = taskId,
            removeTargetIsPredecessor = isPredecessor
        )
    }

    /**
     * 取消删除
     */
    fun onDismissRemoveConfirmDialog() {
        _uiState.value = _uiState.value.copy(
            showRemoveConfirmDialog = false,
            removeTargetTaskId = null
        )
    }

    /**
     * 确认删除关联任务
     */
    fun onConfirmRemoveTask() {
        val state = _uiState.value
        val taskId = state.removeTargetTaskId ?: return
        val isPredecessor = state.removeTargetIsPredecessor

        if (isPredecessor) {
            val newIds = state.predecessorIds.filter { it != taskId }
            onPredecessorIdsChange(newIds)
        } else {
            val newIds = state.relatedIds.filter { it != taskId }
            onRelatedIdsChange(newIds)
        }

        _uiState.value = _uiState.value.copy(
            showRemoveConfirmDialog = false,
            removeTargetTaskId = null
        )
    }

    /**
     * 快速完成关联任务
     */
    fun onQuickCompleteTask(taskId: Long) {
        viewModelScope.launch {
            completeTaskUseCase(taskId)
                .onSuccess {
                    // 刷新前置依赖列表以更新依赖状态
                    val currentId = _uiState.value.id
                    if (currentId > 0) {
                        loadTask(currentId)
                    }
                }
        }
    }

    /**
     * 撤销完成关联任务
     */
    fun onQuickUndoCompleteTask(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getById(taskId) ?: return@launch
            val updatedTask = task.copy(
                status = TaskStatus.PENDING,
                completedAt = null
            )
            repository.update(updatedTask)
                .onSuccess {
                    // 刷新前置依赖列表以更新依赖状态
                    val currentId = _uiState.value.id
                    if (currentId > 0) {
                        loadTask(currentId)
                    }
                }
        }
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
        viewModelScope.launch {
            val newTasks = getTasksByIds(newIds).sortedByPriority()
            // 过滤相关任务：排除已在前置依赖中的
            val predecessorIdsSet = _uiState.value.predecessorIds.toSet()
            val filteredRelatedTasks = newTasks.filter { it.id !in predecessorIdsSet }
            _uiState.value = _uiState.value.copy(
                relatedIds = newIds,
                relatedTasks = newTasks,
                filteredRelatedTasks = filteredRelatedTasks,
                showRelatedDialog = false
            )
        }
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

    // 删除相关方法
    fun onShowDeleteDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDeleteDialog = show)
    }

    fun delete() {
        val state = _uiState.value
        if (!state.isEdit || state.id <= 0) return

        viewModelScope.launch {
            val task = repository.getById(state.id) ?: return@launch
            // 取消提醒
            ReminderManager.cancelReminder(getApplication(), state.id)
            // 删除任务
            repository.delete(task)
            _uiState.value = state.copy(deleted = true, showDeleteDialog = false)
        }
    }
}
