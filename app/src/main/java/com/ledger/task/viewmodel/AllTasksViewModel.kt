package com.ledger.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.domain.model.AllTasksFilterState
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.QuickTag
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 全部事务 UI 状态
 */
data class AllTasksUiState(
    val tasks: List<Task> = emptyList(),
    val filterState: AllTasksFilterState = AllTasksFilterState(),
    val searchQuery: String = ""
)

/**
 * 全部事务 ViewModel
 */
class AllTasksViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")
    private val filterStateFlow = MutableStateFlow(AllTasksFilterState())

    private val _uiState = MutableStateFlow(AllTasksUiState())
    val uiState: StateFlow<AllTasksUiState> = _uiState.asStateFlow()

    init {
        loadAllTasks()
    }

    private fun loadAllTasks() {
        viewModelScope.launch {
            combine(
                searchQueryFlow,
                filterStateFlow,
                repository.getAll()
            ) { query, filter, allTasks ->
                val now = LocalDateTime.now()
                val nowMillis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                val filtered = allTasks.filter { task ->
                    val matchesSearch = query.isBlank() || task.title.contains(query, ignoreCase = true)
                    val matchesCategory = filter.category == null || task.category == filter.category
                    val matchesPriority = filter.priority == null || task.priority == filter.priority
                    val matchesHasImage = filter.hasImage == null || task.hasImage == filter.hasImage

                    // 快捷标签筛选
                    val matchesQuickTag = filter.quickTag == null || task.richContent.getQuickTags().contains(filter.quickTag)

                    // 状态筛选逻辑（支持已逾期）
                    val matchesStatus = when (filter.status) {
                        null -> true
                        DisplayStatus.OVERDUE -> task.status != TaskStatus.DONE && task.deadline.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < nowMillis
                        DisplayStatus.PENDING -> task.status == TaskStatus.PENDING && task.deadline.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= nowMillis
                        DisplayStatus.IN_PROGRESS -> task.status == TaskStatus.IN_PROGRESS && task.deadline.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= nowMillis
                        DisplayStatus.DONE -> task.status == TaskStatus.DONE
                    }

                    matchesSearch && matchesCategory && matchesPriority && matchesStatus && matchesHasImage && matchesQuickTag
                }.sortedWith(
                    compareBy<Task> { it.deadline.toLocalDate() }
                        .thenBy { task ->
                            when (task.priority) {
                                Priority.HIGH -> 0
                                Priority.MEDIUM -> 1
                                Priority.NORMAL -> 2
                                Priority.LOW -> 3
                            }
                        }
                )

                AllTasksUiState(
                    tasks = filtered,
                    filterState = filter,
                    searchQuery = query
                )
            }.collect { result ->
                _uiState.value = result
            }
        }
    }

    fun onSearchChange(query: String) {
        searchQueryFlow.value = query
    }

    fun onFilterChange(filter: AllTasksFilterState) {
        filterStateFlow.value = filter
    }

    fun onPriorityFilterChange(priority: Priority?) {
        filterStateFlow.value = filterStateFlow.value.copy(priority = priority)
    }

    fun onStatusFilterChange(status: DisplayStatus?) {
        filterStateFlow.value = filterStateFlow.value.copy(status = status)
    }

    fun onHasImageFilterChange(hasImage: Boolean?) {
        filterStateFlow.value = filterStateFlow.value.copy(hasImage = hasImage)
    }

    fun onCategoryFilterChange(category: String?) {
        filterStateFlow.value = filterStateFlow.value.copy(category = category)
    }

    fun onQuickTagFilterChange(quickTag: QuickTag?) {
        filterStateFlow.value = filterStateFlow.value.copy(quickTag = quickTag)
    }
}
