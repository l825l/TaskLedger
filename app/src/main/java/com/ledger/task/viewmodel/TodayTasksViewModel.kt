package com.ledger.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TagRepository
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.domain.usecase.CompleteTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * 今日待办 UI 状态
 */
data class TodayTasksUiState(
    val tasks: List<Task> = emptyList(),
    val isSorting: Boolean = false,
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    // 任务标签映射：taskId -> (标签名称, 标签颜色ARGB值)
    val taskTags: Map<Long, Pair<String, Int>> = emptyMap()
)

/**
 * 今日待办 ViewModel
 */
class TodayTasksViewModel(
    private val repository: TaskRepository,
    private val tagRepository: TagRepository,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {

    private val _isSorting = MutableStateFlow(false)

    // 直接将 Flow 转换为 StateFlow，避免中间 MutableStateFlow
    val uiState: StateFlow<TodayTasksUiState> = run {
        val today = LocalDate.now()
        val todayStart = today.atTime(LocalTime.MIN)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val todayEnd = today.plusDays(1).atTime(LocalTime.MIN)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        repository.getTodayTasks(todayStart, todayEnd)
            .map { tasks ->
                // 批量获取任务标签
                val taskIds = tasks.map { it.id }
                val taskTagsList = tagRepository.getFirstTagForTasks(taskIds)
                val taskTagsMap = taskTagsList.associate { info ->
                    info.taskId to Pair(info.tagName, info.tagColorArgb)
                }

                TodayTasksUiState(
                    tasks = tasks,
                    isSorting = _isSorting.value,
                    completedCount = tasks.count { it.status == TaskStatus.DONE },
                    totalCount = tasks.size,
                    taskTags = taskTagsMap
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TodayTasksUiState()
            )
    }

    fun onTaskCompleted(taskId: Long) {
        viewModelScope.launch {
            completeTaskUseCase(taskId)
        }
    }

    fun onTaskUndoComplete(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getById(taskId)
            task?.let {
                val updatedTask = it.copy(
                    status = TaskStatus.PENDING,
                    completedAt = null
                )
                repository.update(updatedTask)
            }
        }
    }

    /**
     * 优先级降级（统一方法，onTaskPriorityDowngrade 是别名）
     */
    fun onPriorityDowngrade(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getById(taskId)
            task?.let {
                val updatedPriority = when (it.priority) {
                    Priority.HIGH -> Priority.MEDIUM
                    Priority.MEDIUM -> Priority.NORMAL
                    Priority.NORMAL -> Priority.LOW
                    Priority.LOW -> Priority.LOW
                }
                val updatedTask = it.copy(priority = updatedPriority)
                repository.update(updatedTask)
            }
        }
    }

    /**
     * onTaskPriorityDowngrade 是 onPriorityDowngrade 的别名
     * 保持向后兼容
     */
    fun onTaskPriorityDowngrade(taskId: Long) = onPriorityDowngrade(taskId)

    fun onPriorityUpgrade(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getById(taskId)
            task?.let {
                val updatedPriority = when (it.priority) {
                    Priority.LOW -> Priority.NORMAL
                    Priority.NORMAL -> Priority.MEDIUM
                    Priority.MEDIUM -> Priority.HIGH
                    Priority.HIGH -> Priority.HIGH
                }
                val updatedTask = it.copy(priority = updatedPriority)
                repository.update(updatedTask)
            }
        }
    }

    /**
     * 删除任务
     */
    fun onTaskDelete(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getById(taskId) ?: return@launch
            repository.delete(task)
        }
    }

    fun reorderTasks(fromIndex: Int, toIndex: Int) {
        val currentTasks = uiState.value.tasks.toMutableList()
        if (fromIndex in currentTasks.indices && toIndex in currentTasks.indices && fromIndex != toIndex) {
            // 先更新 UI
            _isSorting.value = true
            val task = currentTasks.removeAt(fromIndex)
            currentTasks.add(toIndex, task)

            // 保存排序到数据库
            viewModelScope.launch {
                currentTasks.forEachIndexed { index, t ->
                    repository.updateSortOrder(t.id, index)
                }
                _isSorting.value = false
            }
        }
    }

    /**
     * 在同优先级内重新排序
     */
    fun reorderTasksInPriority(tasks: List<Task>, fromIndex: Int, toIndex: Int) {
        if (fromIndex !in tasks.indices || toIndex !in tasks.indices || fromIndex == toIndex) {
            return
        }

        val fromTask = tasks[fromIndex]
        val toTask = tasks[toIndex]

        // 检查是否同优先级
        if (fromTask.priority != toTask.priority) {
            return
        }

        // 获取同优先级的任务列表
        val samePriorityTasks = tasks.filter { it.priority == fromTask.priority }
        val fromPriorityIndex = samePriorityTasks.indexOfFirst { it.id == fromTask.id }
        val toPriorityIndex = samePriorityTasks.indexOfFirst { it.id == toTask.id }

        if (fromPriorityIndex == -1 || toPriorityIndex == -1) return

        // 重新排序同优先级任务
        val reorderedTasks = samePriorityTasks.toMutableList()
        val task = reorderedTasks.removeAt(fromPriorityIndex)
        reorderedTasks.add(toPriorityIndex, task)

        // 保存排序到数据库
        viewModelScope.launch {
            reorderedTasks.forEachIndexed { index, t ->
                repository.updateSortOrder(t.id, index)
            }
        }
    }
}
