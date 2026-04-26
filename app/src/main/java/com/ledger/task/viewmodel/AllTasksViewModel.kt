package com.ledger.task.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.task.domain.model.AllTasksFilterState
import com.ledger.task.domain.model.DisplayStatus
import com.ledger.task.domain.model.Priority
import com.ledger.task.domain.model.QuickTag
import com.ledger.task.domain.model.Tag
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TagRepository
import com.ledger.task.domain.repository.TaskRepository
import com.ledger.task.domain.usecase.GetAllTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 全部事务 UI 状态
 */
data class AllTasksUiState(
    val tasks: List<Task> = emptyList(),
    val filterState: AllTasksFilterState = AllTasksFilterState(),
    val searchQuery: String = "",
    val allTags: List<Tag> = emptyList(),
    // 任务标签映射：taskId -> (标签名称, 标签颜色)
    val taskTags: Map<Long, Pair<String, Color>> = emptyMap()
)

/**
 * 全部事务 ViewModel
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class AllTasksViewModel(
    private val repository: TaskRepository,
    private val tagRepository: TagRepository,
    private val getAllTagsUseCase: GetAllTagsUseCase
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
                getAllTagsUseCase()
            ) { query, filter, tags ->
                Triple(query, filter, tags)
            }.flatMapLatest { (query, filter, tags) ->
                // 获取标签筛选对应的任务ID列表
                val tagTaskIdsFlow = if (filter.tagId != null) {
                    tagRepository.getTaskIdsByTag(filter.tagId)
                } else {
                    flowOf(null)
                }

                combine(
                    tagTaskIdsFlow,
                    repository.getAll()
                ) { taskIds, allTasks ->
                    val now = LocalDateTime.now()
                    val nowMillis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                    val filtered = allTasks.filter { task ->
                        val matchesSearch = query.isBlank() || task.title.contains(query, ignoreCase = true)
                        val matchesCategory = filter.category == null || task.category == filter.category
                        val matchesPriority = filter.priority == null || task.priority == filter.priority
                        val matchesHasImage = filter.hasImage == null || task.hasImage == filter.hasImage

                        // 快捷标签筛选
                        val matchesQuickTag = filter.quickTag == null || task.richContent.getQuickTags().contains(filter.quickTag)

                        // 标签筛选
                        val matchesTag = taskIds == null || taskIds.contains(task.id)

                        // 状态筛选逻辑（支持已逾期）
                        val matchesStatus = when (filter.status) {
                            null -> true
                            DisplayStatus.OVERDUE -> task.status != TaskStatus.DONE && task.deadline.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < nowMillis
                            DisplayStatus.PENDING -> task.status == TaskStatus.PENDING && task.deadline.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= nowMillis
                            DisplayStatus.IN_PROGRESS -> task.status == TaskStatus.IN_PROGRESS && task.deadline.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= nowMillis
                            DisplayStatus.DONE -> task.status == TaskStatus.DONE
                        }

                        matchesSearch && matchesCategory && matchesPriority && matchesStatus && matchesHasImage && matchesQuickTag && matchesTag
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

                    // 加载每个任务的第一个标签（用于显示在任务卡片上）
                    val taskTagsMap = mutableMapOf<Long, Pair<String, Color>>()
                    filtered.forEach { task ->
                        // 获取任务的标签（只取第一个用于显示）
                        val taskTags = tagRepository.getTagsForTask(task.id).firstOrNull() ?: emptyList()
                        if (taskTags.isNotEmpty()) {
                            val firstTag = taskTags.first()
                            taskTagsMap[task.id] = Pair(firstTag.name, firstTag.color)
                        }
                    }

                    AllTasksUiState(
                        tasks = filtered,
                        filterState = filter,
                        searchQuery = query,
                        allTags = tags,
                        taskTags = taskTagsMap
                    )
                }
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

    fun onTagFilterChange(tagId: Long?) {
        filterStateFlow.value = filterStateFlow.value.copy(tagId = tagId)
    }
}
