package com.ledger.task.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ledger.task.ui.component.BottomNavigation
import com.ledger.task.ui.screen.AllTasksScreen
import com.ledger.task.ui.screen.LedgerCenterScreen
import com.ledger.task.ui.screen.PriorityTasksScreen
import com.ledger.task.ui.screen.SettingsScreen
import com.ledger.task.ui.screen.SplashScreen
import com.ledger.task.ui.screen.TaskEditScreen
import com.ledger.task.ui.screen.TodayTasksScreen
import com.ledger.task.viewmodel.AllTasksViewModel
import com.ledger.task.viewmodel.LedgerCenterViewModel
import com.ledger.task.viewmodel.PriorityTasksViewModel
import com.ledger.task.viewmodel.TaskEditViewModel
import com.ledger.task.viewmodel.TodayTasksViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * 主页面路由列表
 */
private val mainPages = listOf(
    "today_tasks",
    "priority_tasks",
    "all_tasks",
    "ledger_center"
)

/**
 * 应用导航
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStack = navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)
    val currentRoute = backStack.value?.destination?.route ?: "splash"

    // 开屏页面、任务编辑页和设置页不显示底部导航
    val showBottomBar = currentRoute != "splash"
        && !currentRoute.startsWith("task_edit")
        && !currentRoute.startsWith("settings")

    // Pager 状态
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    androidx.compose.material3.Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(
                    currentRoute = mainPages.getOrNull(pagerState.currentPage) ?: "today_tasks",
                    onNavigate = { route ->
                        val targetIndex = mainPages.indexOf(route)
                        if (targetIndex >= 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(targetIndex)
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "splash"
            ) {
                // 开屏动画页面
                composable("splash") {
                    SplashScreen(
                        onAnimationComplete = {
                            navController.navigate("main") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    )
                }

                // 主页面（使用 HorizontalPager 实现滑动切换）
                composable("main") {
                    HorizontalPager(
                        count = mainPages.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (mainPages[page]) {
                            "today_tasks" -> {
                                val viewModel: TodayTasksViewModel = koinViewModel()
                                TodayTasksScreen(
                                    viewModel = viewModel,
                                    onNavigateToEdit = { taskId ->
                                        navController.navigate("task_edit/$taskId")
                                    },
                                    onNavigateToAdd = {
                                        navController.navigate("task_edit/0")
                                    }
                                )
                            }
                            "priority_tasks" -> {
                                val viewModel: PriorityTasksViewModel = koinViewModel()
                                PriorityTasksScreen(
                                    viewModel = viewModel,
                                    onNavigateToEdit = { taskId ->
                                        navController.navigate("task_edit/$taskId")
                                    },
                                    onNavigateToAdd = {
                                        navController.navigate("task_edit/0")
                                    }
                                )
                            }
                            "all_tasks" -> {
                                val viewModel: AllTasksViewModel = koinViewModel()
                                AllTasksScreen(
                                    viewModel = viewModel,
                                    onNavigateToEdit = { taskId ->
                                        navController.navigate("task_edit/$taskId")
                                    },
                                    onNavigateToAdd = {
                                        navController.navigate("task_edit/0")
                                    }
                                )
                            }
                            "ledger_center" -> {
                                val viewModel: LedgerCenterViewModel = koinViewModel()
                                LedgerCenterScreen(
                                    viewModel = viewModel,
                                    onNavigateToAdd = {
                                        navController.navigate("task_edit/0")
                                    },
                                    onNavigateToSettings = {
                                        navController.navigate("settings")
                                    },
                                    onNavigateToSettingsForBiometric = {
                                        navController.navigate("settings?enableBiometric=true")
                                    }
                                )
                            }
                        }
                    }
                }

                // 设置页面
                composable(
                    route = "settings?enableBiometric={enableBiometric}",
                    arguments = listOf(
                        navArgument("enableBiometric") { type = NavType.BoolType; defaultValue = false }
                    )
                ) { entry ->
                    val enableBiometric = entry.arguments?.getBoolean("enableBiometric") ?: false
                    SettingsScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        enableBiometricOnStart = enableBiometric
                    )
                }

                // 任务编辑页面
                composable(
                    route = "task_edit/{taskId}",
                    arguments = listOf(
                        navArgument("taskId") { type = NavType.LongType; defaultValue = 0L }
                    )
                ) { entry ->
                    val taskId = entry.arguments?.getLong("taskId") ?: 0L
                    val editViewModel: TaskEditViewModel = koinViewModel()
                    TaskEditScreen(
                        viewModel = editViewModel,
                        taskId = taskId,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToTask = { targetTaskId ->
                            navController.navigate("task_edit/$targetTaskId")
                        }
                    )
                }
            }
        }
    }
}
