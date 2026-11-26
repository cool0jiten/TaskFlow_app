package com.taskflow.app.ui.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.domain.Assignee
import com.taskflow.app.domain.CreateTaskRequest
import com.taskflow.app.domain.Priority
import com.taskflow.app.domain.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTaskUiState(val title: String = "", val description: String = "", val dueDate: String? = null, val priority: Priority = Priority.MEDIUM, val assignee: Assignee = Assignee.AARAV, val saving: Boolean = false, val error: String? = null, val titleError: String? = null, val saved: Boolean = false)

@HiltViewModel
class AddTaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _state = MutableStateFlow(AddTaskUiState())
    val state: StateFlow<AddTaskUiState> = _state.asStateFlow()
    fun title(value: String) { _state.update { it.copy(title = value, titleError = null, error = null) } }
    fun description(value: String) { _state.update { it.copy(description = value) } }
    fun date(value: String?) { _state.update { it.copy(dueDate = value) } }
    fun priority(value: Priority) { _state.update { it.copy(priority = value) } }
    fun assignee(value: Assignee) { _state.update { it.copy(assignee = value) } }
    fun save() {
        val s = _state.value
        if (s.title.isBlank()) { _state.update { it.copy(titleError = "Title is required") }; return }
        viewModelScope.launch {
            _state.update { it.copy(saving = true, error = null) }
            val request = CreateTaskRequest(s.title.trim(), s.description.trim().ifBlank { null }, s.dueDate, s.priority.apiValue, s.assignee.apiValue)
            repository.addTask(request).fold(
                { _state.update { it.copy(saving = false, saved = true) } },
                { e -> _state.update { it.copy(saving = false, error = e.message ?: "Failed to create task") } }
            )
        }
    }
}
