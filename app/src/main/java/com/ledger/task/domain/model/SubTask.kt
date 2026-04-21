package com.ledger.task.domain.model

/**
 * 子任务
 */
data class SubTask(
    val id: Long = 0,
    val parentId: Long,           // 父任务ID
    val title: String,
    val isCompleted: Boolean = false,
    val sortOrder: Int = 0
) {
    /**
     * 切换完成状态
     */
    fun toggle(): SubTask = copy(isCompleted = !isCompleted)
}
