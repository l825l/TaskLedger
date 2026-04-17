package com.ledger.task.data.model

/**
 * 任务状态（OVERDUE 为运行时衍生，不存入数据库）
 */
enum class TaskStatus(val label: String) {
    PENDING("待处理"),
    IN_PROGRESS("进行中"),
    DONE("已完成")
}

/**
 * 显示用状态（包含衍生的 OVERDUE）
 */
enum class DisplayStatus(val label: String) {
    PENDING("待处理"),
    IN_PROGRESS("进行中"),
    DONE("已完成"),
    OVERDUE("已逾期")
}
