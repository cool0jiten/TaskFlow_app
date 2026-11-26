package com.taskflow.app.ui.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.domain.Task
import com.taskflow.app.domain.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskDetailUiState(val task: Task? = null, val loading: Boolean = false, val error: String? = null, val updating: Boolean = false)
@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _state = MutableStateFlow(TaskDetailUiState())
    val state: StateFlow<TaskDetailUiState> = _state.asStateFlow()
    fun load(id: String) { viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        repository.getTask(id).fold({ task -> _state.update { it.copy(task = task, loading = false) } }, { e -> _state.update { it.copy(loading = false, error = e.message ?: "Task not found") } })
    } }
    fun toggle() {
        val current = _state.value.task ?: return
        _state.update { it.copy(task = current.copy(completed = !current.completed), updating = true, error = null) }
        viewModelScope.launch { repository.toggleTask(current.id).fold(
            { task -> _state.update { it.copy(task = task, updating = false) } },
            { e -> _state.update { it.copy(task = current, updating = false, error = e.message ?: "Update failed") } }
        ) }
    }
}
