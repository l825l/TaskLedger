package com.ledger.task.domain

import com.ledger.task.domain.model.Task
import com.ledger.task.domain.model.TaskStatus
import com.ledger.task.domain.repository.TaskRepository

/**
 * 依赖验证结果
 */
sealed interface DependencyValidationResult {
    /** 验证通过 */
    object Valid : DependencyValidationResult
    /** 自引用：任务不能依赖自身 */
    data class SelfReference(val taskId: Long) : DependencyValidationResult
    /** 直接循环依赖 */
    data class DirectCycle(val cyclePath: List<Long>) : DependencyValidationResult
    /** 前置任务不存在 */
    data class PredecessorNotFound(val predecessorId: Long) : DependencyValidationResult
}

/**
 * 任务依赖验证器
 * 使用 DFS 检测循环依赖
 */
class TaskDependencyValidator(private val repository: TaskRepository) {

    /**
     * 验证添加前置依赖是否合法
     * @param taskId 目标任务 ID
     * @param predecessorId 要添加的前置依赖 ID
     */
    suspend fun validatePredecessorAddition(
        taskId: Long,
        predecessorId: Long
    ): DependencyValidationResult {
        // 1. 自引用检测
        if (taskId == predecessorId) {
            return DependencyValidationResult.SelfReference(taskId)
        }

        // 2. 检查前置任务是否存在
        val predecessor = repository.getById(predecessorId)
            ?: return DependencyValidationResult.PredecessorNotFound(predecessorId)

        // 3. 获取所有任务用于构建依赖图
        val allTasks = repository.getAllNow()
        val taskMap = allTasks.associateBy { it.id }

        // 4. 模拟添加依赖后的状态，检测是否形成环
        // 从 predecessorId 开始 DFS，检查是否能到达 taskId
        // 如果能到达，说明添加后会形成环
        val visited = mutableSetOf<Long>()
        val path = mutableListOf<Long>()

        if (hasCycleFrom(predecessorId, taskId, taskMap, visited, path)) {
            return DependencyValidationResult.DirectCycle(path.toList())
        }

        return DependencyValidationResult.Valid
    }

    /**
     * DFS 检测从 startNode 出发是否能到达 targetNode
     * 如果能到达，说明添加 startNode -> targetNode 的反向边会形成环
     */
    private fun hasCycleFrom(
        currentNode: Long,
        targetNode: Long,
        taskMap: Map<Long, Task>,
        visited: MutableSet<Long>,
        path: MutableList<Long>
    ): Boolean {
        visited.add(currentNode)
        path.add(currentNode)

        val task = taskMap[currentNode] ?: return false

        // 检查当前节点的所有前置依赖
        for (predecessorId in task.predecessorIds) {
            if (predecessorId == targetNode) {
                // 找到环：从 currentNode 的前置可以到达 targetNode
                path.add(targetNode)
                return true
            }

            if (!visited.contains(predecessorId)) {
                if (hasCycleFrom(predecessorId, targetNode, taskMap, visited, path)) {
                    return true
                }
            }
        }

        path.removeAt(path.size - 1)
        return false
    }

    /**
     * 批量验证前置依赖
     */
    suspend fun validatePredecessors(
        taskId: Long,
        predecessorIds: List<Long>
    ): List<DependencyValidationResult> {
        return predecessorIds.map { validatePredecessorAddition(taskId, it) }
    }

    /**
     * 获取所有阻塞当前任务的前置依赖
     */
    suspend fun getBlockingPredecessors(taskId: Long): List<Task> {
        val task = repository.getById(taskId) ?: return emptyList()
        if (task.predecessorIds.isEmpty()) return emptyList()

        val predecessors = repository.getPredecessorTasks(taskId)
        return predecessors.filter { it.status != TaskStatus.DONE }
    }

    /**
     * 检查任务是否被阻塞
     */
    suspend fun isTaskBlocked(taskId: Long): Boolean {
        return getBlockingPredecessors(taskId).isNotEmpty()
    }
}
