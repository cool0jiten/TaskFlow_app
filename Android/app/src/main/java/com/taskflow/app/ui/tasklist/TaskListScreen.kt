package com.taskflow.app.ui.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taskflow.app.domain.Task
import java.time.LocalDate
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskListViewModel, onTaskClick: (String) -> Unit, onAddClick: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopAppBar(title = { Text("TaskFlow") }) }, floatingActionButton = {
        FloatingActionButton(onClick = onAddClick) { Text("+") }
    }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Assignee", style = MaterialTheme.typography.labelLarge)
            FilterChips(listOf("All", "Aarav", "Priya", "Rohan"), state.assignee?.replaceFirstChar { it.uppercase() } ?: "All") { selected ->
                viewModel.setAssignee(if (selected == "All") null else selected.lowercase())
            }
            Spacer(Modifier.height(8.dp))
            Text("Status", style = MaterialTheme.typography.labelLarge)
            FilterChips(listOf("All", "Active", "Completed"), state.status?.replaceFirstChar { it.uppercase() } ?: "All") { selected ->
                viewModel.setStatus(if (selected == "All") null else selected.lowercase())
            }
            Spacer(Modifier.height(12.dp))
            when {
                state.loading -> Box(Modifier.fillMaxWidth().weight(1f), Alignment.Center) { CircularProgressIndicator() }
                state.error != null && state.tasks.isEmpty() -> ErrorPanel(state.error ?: "Error", viewModel::refresh, Modifier.weight(1f))
                state.tasks.isEmpty() -> Box(Modifier.fillMaxWidth().weight(1f), Alignment.Center) { Text("No tasks match these filters.") }
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.tasks, key = { it.id }) { task -> TaskCard(task, onToggle = { viewModel.toggle(task) }, onClick = { onTaskClick(task.id) }) }
                }
            }
            if (state.error != null && state.tasks.isNotEmpty()) Text(state.error!!, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun FilterChips(options: List<String>, selected: String, onSelected: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        options.forEach { option -> FilterChip(selected = option == selected, onClick = { onSelected(option) }, label = { Text(option) }) }
    }
}

@Composable
private fun TaskCard(task: Task, onToggle: () -> Unit, onClick: () -> Unit) {
    val overdue = task.dueDate?.let { date ->
        try { !task.completed && LocalDate.parse(date).isBefore(LocalDate.now()) } catch (_: DateTimeParseException) { false }
    } ?: false
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = task.completed, onCheckedChange = { onToggle() })
            Column(Modifier.weight(1f).clickable(onClick = onClick).padding(start = 8.dp)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text(task.dueDate ?: "No due date", style = MaterialTheme.typography.bodySmall)
                Text("${task.assigneeId.displayName} • ${task.priority.name.lowercase()}", style = MaterialTheme.typography.bodySmall)
                if (overdue) Text("Overdue", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelMedium)
                if (task.completed) Text("Completed", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            }
            Text("›", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.clickable(onClick = onClick))
        }
    }
}

@Composable
private fun ErrorPanel(message: String, retry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(message, color = MaterialTheme.colorScheme.error)
        Button(onClick = retry) { Text("Retry") }
    }
}
