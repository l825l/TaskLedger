package com.ledger.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// 排序字段
enum class SortField { TITLE, PRIORITY, DEADLINE, STATUS }

data class SortState(val field: SortField, val ascending: Boolean)

// 显示状态排序映射（常量）
private val DISPLAY_STATUS_ORDER = mapOf(
    DisplayStatus.OVERDUE to 0,
    DisplayStatus.IN_PROGRESS to 1,
    DisplayStatus.PENDING to 2,
    DisplayStatus.DONE to 3
)

data class StatsState(
    val total: Int = 0,
    val inProgress: Int = 0,
    val done: Int = 0,
    val overdue: Int = 0
)

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val activePriorityFilter: Priority? = null,
    val sortState: SortState = SortState(SortField.PRIORITY, ascending = true),
    val stats: StatsState = StatsState()
)

class TaskListViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val searchQueryFlow = MutableStateFlow("")
    private val priorityFilterFlow = MutableStateFlow<Priority?>(null)
    private val sortStateFlow = MutableStateFlow(SortState(SortField.PRIORITY, ascending = true))
    private val todayFlow = MutableStateFlow(LocalDate.now().toEpochDay())

    init {
        // 每天零点更新今天日期
        viewModelScope.launch {
            kotlinx.coroutines.flow.flow {
                while (true) {
                    emit(LocalDate.now().toEpochDay())
                    // 计算到明天零点的毫秒数
                    val today = LocalDate.now()
                    val tomorrow = today.plusDays(1)
                    val millisUntilMidnight = kotlin.math.min(
                        kotlin.math.max(
                            java.time.Duration.between(today.atStartOfDay(), tomorrow.atStartOfDay()).toMillis(),
                            60000L // 至少 1 分钟
                        ),
                        86400000L // 最多 24 小时
                    )
                    kotlinx.coroutines.delay(millisUntilMidnight)
                }
            }.collect { day ->
                todayFlow.value = day
            }
        }
    }

    // 搜索筛选后的任务列表
    private val filteredTasksFlow = combine(searchQueryFlow, priorityFilterFlow) { query, priority ->
        query to priority
    }.flatMapLatest { (query, priority) ->
        if (query.isBlank() && priority == null) {
            repository.getAll()
        } else {
            repository.searchAndFilter(query, priority)
        }
    }

    // 排序后的任务列表
    private val sortedTasksFlow = combine(filteredTasksFlow, sortStateFlow) { tasks, sort ->
        applySort(tasks, sort)
    }

    // 统计数据（独立 Flow，不受搜索筛选影响）
    private val statsFlow = combine(
        repository.countAll(),
        repository.countInProgress(),
        repository.countDone(),
        todayFlow.flatMapLatest { day -> repository.countOverdue(day) }
    ) { total, inProgress, done, overdue ->
        StatsState(total, inProgress, done, overdue)
    }

    // 合并 UI 状态
    val uiState: StateFlow<TaskListUiState> = combine(
        sortedTasksFlow,
        searchQueryFlow,
        priorityFilterFlow,
        sortStateFlow,
        statsFlow
    ) { tasks, query, priority, sort, stats ->
        TaskListUiState(
            tasks = tasks,
            searchQuery = query,
            activePriorityFilter = priority,
            sortState = sort,
            stats = stats
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskListUiState())

    fun onSearchQueryChange(query: String) {
        searchQueryFlow.value = query
    }

    fun onPriorityFilterChange(priority: Priority?) {
        priorityFilterFlow.value = priority
    }

    fun onSort(field: SortField) {
        val current = sortStateFlow.value
        sortStateFlow.value = if (current.field == field) {
            current.copy(ascending = !current.ascending)
        } else {
            SortState(field, ascending = true)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    private fun applySort(tasks: List<Task>, sort: SortState): List<Task> {
        return tasks.sortedWith { a, b ->
            val cmp = when (sort.field) {
                SortField.TITLE -> a.title.compareTo(b.title)
                SortField.PRIORITY -> a.priority.order.compareTo(b.priority.order)
                SortField.DEADLINE -> a.deadline.compareTo(b.deadline)
                SortField.STATUS -> {
                    DISPLAY_STATUS_ORDER[a.displayStatus]?.compareTo(DISPLAY_STATUS_ORDER[b.displayStatus] ?: 0) ?: 0
                }
            }
            if (sort.ascending) cmp else -cmp
        }
    }
}
