package com.ledger.task.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.TaskLedgerApp
import com.ledger.task.data.model.Priority
import com.ledger.task.data.model.Task
import com.ledger.task.data.model.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val tasks: List<Task> = emptyList()
)

/**
 * 重点待办 ViewModel
 */
class PriorityTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as TaskLedgerApp).repository

    private val _uiState = MutableStateFlow(PriorityTasksUiState())
    val uiState: StateFlow<PriorityTasksUiState> = _uiState.asStateFlow()

    // 使用 stateIn 将 Flow 转换为 StateFlow，避免 shareIn + collect 的低效用法
    private val priorityTasksFlow: StateFlow<List<Task>> = run {
        val nowMillis = LocalDateTime.now()
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        repository.getPriorityTasks(nowMillis)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    init {
        loadPriorityTasks()
    }

    fun loadPriorityTasks() {
        viewModelScope.launch {
            priorityTasksFlow.collect { tasks ->
                _uiState.value = PriorityTasksUiState(tasks = tasks)
            }
        }
    }

    fun onTaskCompleted(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getById(taskId)
            task?.let {
                val updatedTask = it.copy(
                    status = TaskStatus.DONE,
                    completedAt = System.currentTimeMillis()
                )
                repository.update(updatedTask)
            }
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
}
