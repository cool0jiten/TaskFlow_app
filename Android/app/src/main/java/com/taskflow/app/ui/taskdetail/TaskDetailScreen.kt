package com.taskflow.app.ui.taskdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(taskId: String, viewModel: TaskDetailViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(taskId) { viewModel.load(taskId) }
    Scaffold(topBar = { TopAppBar(title = { Text("Task Details") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (state.loading) CircularProgressIndicator()
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            state.task?.let { task ->
                Text(task.title, style = MaterialTheme.typography.headlineSmall)
                DetailLine("Description", task.description?.takeIf { it.isNotBlank() } ?: "No description")
                DetailLine("Due date", task.dueDate ?: "No due date")
                DetailLine("Priority", task.priority.name)
                DetailLine("Assignee", task.assigneeId.displayName)
                DetailLine("Status", if (task.completed) "Completed" else "Active")
                Button(onClick = viewModel::toggle, enabled = !state.updating, modifier = Modifier.fillMaxWidth()) {
                    if (state.updating) CircularProgressIndicator(Modifier.size(18.dp)) else Text(if (task.completed) "Mark as Active" else "Mark as Complete")
                }
            }
        }
    }
}
@Composable private fun DetailLine(label: String, value: String) { Column { Text(label, style = MaterialTheme.typography.labelLarge); Text(value, style = MaterialTheme.typography.bodyLarge) } }
