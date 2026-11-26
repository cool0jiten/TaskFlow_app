package com.taskflow.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taskflow.app.ui.addtask.AddTaskScreen
import com.taskflow.app.ui.taskdetail.TaskDetailScreen
import com.taskflow.app.ui.tasklist.TaskListScreen

object Routes { const val LIST = "task_list"; const val DETAIL = "task_detail/{taskId}"; const val ADD = "add_task"; fun detail(id: String) = "task_detail/$id" }

@Composable
fun TaskFlowNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) { TaskListScreen(hiltViewModel(), onTaskClick = { navController.navigate(Routes.detail(it)) }, onAddClick = { navController.navigate(Routes.ADD) }) }
        composable(Routes.DETAIL, arguments = listOf(navArgument("taskId") { type = NavType.StringType })) { entry ->
            val id = entry.arguments?.getString("taskId").orEmpty()
            TaskDetailScreen(id, hiltViewModel(), onBack = { navController.popBackStack() })
        }
        composable(Routes.ADD) { AddTaskScreen(hiltViewModel(), onBack = { navController.popBackStack() }, onSaved = { navController.popBackStack() }) }
    }
}
