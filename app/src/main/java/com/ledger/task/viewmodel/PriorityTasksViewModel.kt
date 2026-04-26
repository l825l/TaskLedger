package com.ledger.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TagRepository
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.domain.usecase.CompleteTaskUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 重点待办 UI 状态
 */
data class PriorityTasksUiState(
    val tasks: List<Task> = emptyList(),
    // 任务标签映射：taskId -> (标签名称, 标签颜色ARGB值)
    val taskTags: Map<Long, Pair<String, Int>> = emptyMap()
)

/**
 * 重点待办 ViewModel
 */
class PriorityTasksViewModel(
    private val repository: TaskRepository,
    private val tagRepository: TagRepository,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {

    // 直接将 Flow 转换为 StateFlow，避免中间 MutableStateFlow
    val uiState: StateFlow<PriorityTasksUiState> = run {
        val nowMillis = LocalDateTime.now()
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        repository.getPriorityTasks(nowMillis)
            .map { tasks ->
                // 批量获取任务标签
                val taskIds = tasks.map { it.id }
                val taskTagsList = tagRepository.getFirstTagForTasks(taskIds)
                val taskTagsMap = taskTagsList.associate { info ->
                    info.taskId to Pair(info.tagName, info.tagColorArgb)
                }

                PriorityTasksUiState(tasks = tasks, taskTags = taskTagsMap)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PriorityTasksUiState()
            )
    }

    fun onTaskCompleted(taskId: Long) {
        viewModelScope.launch {
            completeTaskUseCase(taskId)
        }
    }

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

    fun onPriorityUpgrade(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getById(taskId)
            task?.let {
                val updatedPriority = when (it.priority) {
                    Priority.HIGH -> Priority.HIGH // 已经是紧急，不能再升级
                    Priority.MEDIUM -> Priority.HIGH // 高 -> 紧急
                    Priority.NORMAL -> Priority.MEDIUM
                    Priority.LOW -> Priority.NORMAL
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
}
