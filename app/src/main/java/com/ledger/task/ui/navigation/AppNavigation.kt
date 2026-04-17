package com.ledger.task.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ledger.task.ui.component.BottomNavigation
import com.ledger.task.ui.screen.AllTasksScreen
import com.ledger.task.ui.screen.LedgerCenterScreen
import com.ledger.task.ui.screen.PriorityTasksScreen
import com.ledger.task.ui.screen.SplashScreen
import com.ledger.task.ui.screen.TaskEditScreen
import com.ledger.task.ui.screen.TodayTasksScreen
import com.ledger.task.viewmodel.AllTasksViewModel
import com.ledger.task.viewmodel.LedgerCenterViewModel
import com.ledger.task.viewmodel.PriorityTasksViewModel
import com.ledger.task.viewmodel.TaskEditViewModel
import com.ledger.task.viewmodel.TodayTasksViewModel

/**
 * 应用导航
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStack = navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)
    val currentRoute = backStack.value?.destination?.route ?: "splash"

    // 开屏页面不显示底部导航
    val showBottomBar = currentRoute != "splash"

    androidx.compose.material3.Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("today_tasks") { inclusive = true }
                            launchSingleTop = true
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
                            navController.navigate("today_tasks") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    )
                }
                composable("today_tasks") {
                    val viewModel: TodayTasksViewModel = viewModel()
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
                composable("priority_tasks") {
                    val viewModel: PriorityTasksViewModel = viewModel()
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
                composable("all_tasks") {
                    val viewModel: AllTasksViewModel = viewModel()
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
                composable("ledger_center") {
                    val viewModel: LedgerCenterViewModel = viewModel()
                    LedgerCenterScreen(
                        viewModel = viewModel,
                        onNavigateToAdd = {
                            navController.navigate("task_edit/0")
                        }
                    )
                }
                composable(
                    route = "task_edit/{taskId}",
                    arguments = listOf(
                        navArgument("taskId") { type = NavType.LongType; defaultValue = 0L }
                    )
                ) { entry ->
                    val taskId = entry.arguments?.getLong("taskId") ?: 0L
                    val editViewModel: TaskEditViewModel = viewModel()
                    TaskEditScreen(
                        viewModel = editViewModel,
                        taskId = taskId,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToTask = { targetTaskId ->
                            navController.navigate("task_edit/$targetTaskId") {
                                popUpTo("task_edit/{taskId}") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}
