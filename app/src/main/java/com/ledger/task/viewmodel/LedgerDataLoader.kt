package com.ledger.task.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ledger.task.domain.model.LedgerFilterState
import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TimeRange
import com.ledger.task.domain.repository.TaskRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * 台账数据加载器
 * 负责加载和筛选台账数据
 */
class LedgerDataLoader(
    private val application: Application,
    private val getState: () -> LedgerCenterUiState,
    private val updateState: (LedgerCenterUiState) -> Unit,
    private val repository: TaskRepository
) {
    companion object {
        private const val TAG = "LedgerDataLoader"
    }

    // Flow 收集任务
    private var loadLedgerJob: Job? = null
    private var loadCategoriesJob: Job? = null

    /**
     * 取消所有收集任务
     */
    fun cancelJobs() {
        loadLedgerJob?.cancel()
        loadCategoriesJob?.cancel()
    }

    /**
     * 加载台账数据
     */
    fun loadLedgerData(viewModelScope: kotlinx.coroutines.CoroutineScope) {
        val state = getState().filterState

        // 取消之前的收集任务
        loadLedgerJob?.cancel()
        loadCategoriesJob?.cancel()

        loadLedgerJob = viewModelScope.launch {
            // 将日期转换为毫秒（与数据库中的 deadline 字段格式一致）
            val startMillis = state.startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endMillis = state.endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            repository.getLedgerTasks(
                startMillis,
                endMillis,
                state.includeArchived,
                if (state.categories.isEmpty()) null else state.categories.firstOrNull()
            ).collect { tasks ->
                // 如果选择了多个分类，在内存中过滤
                val filteredTasks = if (state.categories.isEmpty() || state.categories.size == 1) {
                    tasks
                } else {
                    tasks.filter { it.category in state.categories }
                }

                updateState(getState().copy(tasks = filteredTasks))
            }
        }

        // 加载所有分类
        loadCategoriesJob = viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                updateState(getState().copy(allCategories = categories))
            }
        }
    }

    /**
     * 时间范围变更
     */
    fun onTimeRangeChange(range: TimeRange) {
        val today = LocalDate.now()
        val (startDate, endDate) = when (range) {
            TimeRange.THIS_WEEK -> {
                val start = today.with(DayOfWeek.MONDAY)
                val end = today.with(DayOfWeek.SUNDAY)
                start to end
            }
            TimeRange.THIS_MONTH -> {
                val start = today.withDayOfMonth(1)
                val end = today.with(TemporalAdjusters.lastDayOfMonth())
                start to end
            }
            TimeRange.CUSTOM -> {
                getState().customStartDate to getState().customEndDate
            }
        }
        updateState(getState().copy(
            timeRange = range,
            filterState = getState().filterState.copy(
                startDate = startDate,
                endDate = endDate
            )
        ))
    }

    /**
     * 自定义日期范围变更
     */
    fun onCustomDateRangeChange(startDate: LocalDate, endDate: LocalDate): Boolean {
        // 验证日期范围
        if (startDate.isAfter(endDate)) {
            updateState(getState().copy(exportMessage = "开始日期不能晚于结束日期"))
            return false
        }

        updateState(getState().copy(
            customStartDate = startDate,
            customEndDate = endDate
        ))
        return true
    }

    /**
     * 切换分类选择
     */
    fun onCategoryToggle(category: String) {
        val currentCategories = getState().filterState.categories
        val newCategories = if (currentCategories.contains(category)) {
            currentCategories - category
        } else {
            currentCategories + category
        }
        updateState(getState().copy(
            filterState = getState().filterState.copy(categories = newCategories)
        ))
    }

    /**
     * 全选分类
     */
    fun onSelectAllCategories() {
        val allCategories = getState().allCategories.toSet()
        updateState(getState().copy(
            filterState = getState().filterState.copy(categories = allCategories)
        ))
    }

    /**
     * 取消全选分类
     */
    fun onDeselectAllCategories() {
        updateState(getState().copy(
            filterState = getState().filterState.copy(categories = emptySet())
        ))
    }

    /**
     * 是否包含归档任务变更
     */
    fun onIncludeArchivedChange(include: Boolean) {
        updateState(getState().copy(
            filterState = getState().filterState.copy(includeArchived = include)
        ))
    }
}
