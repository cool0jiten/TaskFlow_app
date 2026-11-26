package com.taskflow.app.ui.addtask

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taskflow.app.domain.Assignee
import com.taskflow.app.domain.Priority
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(viewModel: AddTaskViewModel, onBack: () -> Unit, onSaved: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(state.saved) { if (state.saved) onSaved() }
    Scaffold(topBar = { TopAppBar(title = { Text("Add Task") }, navigationIcon = { TextButton(onClick = onBack) { Text("Cancel") } }) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = state.title, onValueChange = viewModel::title, label = { Text("Title *") }, isError = state.titleError != null, supportingText = { state.titleError?.let { Text(it) } }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = state.description, onValueChange = viewModel::description, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            Text("Due date")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {
                    val initial = state.dueDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() } ?: LocalDate.now()
                    DatePickerDialog(context, { _, year, month, day -> viewModel.date(LocalDate.of(year, month + 1, day).toString()) }, initial.year, initial.monthValue - 1, initial.dayOfMonth).show()
                }) { Text(state.dueDate ?: "Select date") }
                if (state.dueDate != null) TextButton(onClick = { viewModel.date(null) }) { Text("Clear") }
            }
            Text("Priority")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { Priority.entries.forEach { p -> FilterChip(selected = state.priority == p, onClick = { viewModel.priority(p) }, label = { Text(p.name) }) } }
            Text("Assignee")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { Assignee.entries.forEach { a -> FilterChip(selected = state.assignee == a, onClick = { viewModel.assignee(a) }, label = { Text(a.displayName) }) } }
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Button(onClick = viewModel::save, enabled = !state.saving, modifier = Modifier.fillMaxWidth()) { if (state.saving) CircularProgressIndicator(Modifier.size(18.dp)) else Text("Save Task") }
        }
    }
}
