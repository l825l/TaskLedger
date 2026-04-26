package com.ledger.task.domain.model

import androidx.compose.ui.graphics.Color

/**
 * 分类节点
 */
data class CategoryNode(
    val id: String,
    val name: String,
    val color: Color = Color.Gray,
    val parentId: String? = null,
    val children: List<CategoryNode> = emptyList()
) {
    fun isRoot(): Boolean = parentId == null
}

/**
 * 分类路径
 */
data class CategoryPath(
    val path: List<String>,  // 分类ID路径
    val names: List<String>  // 分类名称路径
) {
    fun toString(separator: String = " / "): String = names.joinToString(separator)

    companion object {
        fun fromNodes(nodes: List<CategoryNode>): CategoryPath {
            return CategoryPath(
                path = nodes.map { it.id },
                names = nodes.map { it.name }
            )
        }
    }
}

/**
 * 分类树
 */
data class CategoryTree(
    val roots: List<CategoryNode>
) {
    fun getAllNodes(): List<CategoryNode> {
        fun flatten(node: CategoryNode): List<CategoryNode> {
            return listOf(node) + node.children.flatMap(::flatten)
        }
        return roots.flatMap(::flatten)
    }

    fun findNodeById(id: String): CategoryNode? {
        val allNodes = getAllNodes()
        return allNodes.firstOrNull { it.id == id }
    }

    fun getAncestors(id: String): List<CategoryNode> {
        val allNodes = getAllNodes()
        val node = allNodes.firstOrNull { it.id == id } ?: return emptyList()
        val path = mutableListOf<CategoryNode>()
        var current = node
        while (current.parentId != null) {
            val parent = allNodes.firstOrNull { it.id == current.parentId }
            if (parent == null) break
            path.add(0, parent)
            current = parent
        }
        return path
    }
}

/**
 * 默认分类数据
 */
object DefaultCategories {
    // 预定义颜色
    val ColorWork = Color(0xFF4CAF50)      // 绿色 - 工作
    val ColorMeeting = Color(0xFF8BC34A)    // 浅绿 - 会议
    val ColorProject = Color(0xFF2E7D32)    // 深绿 - 项目
    val ColorDocument = Color(0xFF66BB6A)   // 中绿 - 文档
    val ColorEmail = Color(0xFF81C784)      // 薄荷绿 - 邮件

    val ColorPersonal = Color(0xFF2196F3)   // 蓝色 - 个人
    val ColorHealth = Color(0xFFE91E63)     // 粉红 - 健康
    val ColorLearning = Color(0xFF9C27B0)   // 紫色 - 学习
    val ColorShopping = Color(0xFFFF9800)   // 橙色 - 购物
    val ColorSocial = Color(0xFF00BCD4)     // 青色 - 社交

    val ColorFinance = Color(0xFFFFC107)    // 黄色 - 财务
    val ColorInvoice = Color(0xFFFFEB3B)    // 亮黄 - 发票
    val ColorTax = Color(0xFFFFC107)        // 黄色 - 税务
    val ColorInvestment = Color(0xFFFF9800) // 橙色 - 投资

    val ColorDefault = Color(0xFF9E9E9E)    // 灰色 - 默认

    // 可选颜色列表（用于新建分类）
    val availableColors = listOf(
        Color(0xFFE91E63),  // 粉红
        Color(0xFF9C27B0),  // 紫色
        Color(0xFF673AB7),  // 深紫
        Color(0xFF3F51B5),  // 靛蓝
        Color(0xFF2196F3),  // 蓝色
        Color(0xFF03A9F4),  // 浅蓝
        Color(0xFF00BCD4),  // 青色
        Color(0xFF009688),  // 蓝绿
        Color(0xFF4CAF50),  // 绿色
        Color(0xFF8BC34A),  // 浅绿
        Color(0xFFCDDC39),  // 黄绿
        Color(0xFFFFEB3B),  // 黄色
        Color(0xFFFFC107),  // 琥珀
        Color(0xFFFF9800),  // 橙色
        Color(0xFFFF5722),  // 深橙
        Color(0xFF795548),  // 棕色
        Color(0xFF9E9E9E),  // 灰色
        Color(0xFF607D8B)   // 蓝灰
    )

    // 默认分类树结构
    val defaultTree = CategoryTree(
        roots = listOf(
            CategoryNode(
                id = "work",
                name = "工作",
                color = ColorWork,
                children = listOf(
                    CategoryNode(id = "work-meeting", name = "会议", color = ColorMeeting, parentId = "work"),
                    CategoryNode(id = "work-project", name = "项目", color = ColorProject, parentId = "work"),
                    CategoryNode(id = "work-document", name = "文档", color = ColorDocument, parentId = "work"),
                    CategoryNode(id = "work-email", name = "邮件", color = ColorEmail, parentId = "work")
                )
            ),
            CategoryNode(
                id = "personal",
                name = "个人",
                color = ColorPersonal,
                children = listOf(
                    CategoryNode(id = "personal-health", name = "健康", color = ColorHealth, parentId = "personal"),
                    CategoryNode(id = "personal-learning", name = "学习", color = ColorLearning, parentId = "personal"),
                    CategoryNode(id = "personal-shopping", name = "购物", color = ColorShopping, parentId = "personal"),
                    CategoryNode(id = "personal-social", name = "社交", color = ColorSocial, parentId = "personal")
                )
            ),
            CategoryNode(
                id = "finance",
                name = "财务",
                color = ColorFinance,
                children = listOf(
                    CategoryNode(id = "finance-invoice", name = "发票", color = ColorInvoice, parentId = "finance"),
                    CategoryNode(id = "finance-tax", name = "税务", color = ColorTax, parentId = "finance"),
                    CategoryNode(id = "finance-investment", name = "投资", color = ColorInvestment, parentId = "finance")
                )
            )
        )
    )

    // 获取所有分类名称（扁平化）
    fun getAllCategoryNames(): List<String> = defaultTree.getAllNodes().map { it.name }

    // 根据ID获取完整路径
    fun getCategoryPath(categoryId: String): CategoryPath? {
        val pathNodes = defaultTree.getAncestors(categoryId)
        if (pathNodes.isEmpty()) return null
        return CategoryPath.fromNodes(pathNodes)
    }

    // 根据分类名称获取颜色
    fun getColorByName(name: String): Color {
        if (name.isEmpty()) return ColorDefault
        val node = defaultTree.getAllNodes().find { it.name == name }
        return node?.color ?: ColorDefault
    }

    // 根据分类ID获取颜色
    fun getColorById(id: String): Color {
        if (id.isEmpty()) return ColorDefault
        val node = defaultTree.findNodeById(id)
        return node?.color ?: ColorDefault
    }
}
