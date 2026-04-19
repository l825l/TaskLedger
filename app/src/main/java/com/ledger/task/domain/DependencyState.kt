package com.ledger.task.domain

import com.ledger.task.data.model.Task
import com.ledger.task.data.model.TaskStatus

/**
 * 依赖状态
 */
sealed interface DependencyState {
    /** 无前置依赖 */
    object NoDependencies : DependencyState
    /** 所有前置依赖已完成，可执行 */
    data class Unblocked(val completedCount: Int, val totalCount: Int) : DependencyState
    /** 存在未完成的前置依赖，被阻塞 */
    data class Blocked(val blockingTasks: List<Task>) : DependencyState

    /** 是否被阻塞 */
    val isBlocked: Boolean
        get() = this is Blocked

    /** 是否有依赖 */
    val hasDependencies: Boolean
        get() = this !is NoDependencies
}

/**
 * 依赖状态计算器
 */
object DependencyStateCalculator {

    /**
     * 计算依赖状态
     * @param predecessorTasks 前置依赖任务列表
     */
    fun calculate(predecessorTasks: List<Task>): DependencyState {
        if (predecessorTasks.isEmpty()) {
            return DependencyState.NoDependencies
        }

        val completedTasks = predecessorTasks.filter { it.status == TaskStatus.DONE }
        val blockingTasks = predecessorTasks.filter { it.status != TaskStatus.DONE }

        return if (blockingTasks.isEmpty()) {
            DependencyState.Unblocked(completedTasks.size, predecessorTasks.size)
        } else {
            DependencyState.Blocked(blockingTasks)
        }
    }

    /**
     * 计算依赖进度
     * @return Pair(已完成数, 总数)
     */
    fun calculateProgress(predecessorTasks: List<Task>): Pair<Int, Int> {
        if (predecessorTasks.isEmpty()) return Pair(0, 0)
        val completed = predecessorTasks.count { it.status == TaskStatus.DONE }
        return Pair(completed, predecessorTasks.size)
    }
}
