package com.taskflow.app.ui.tasklist

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

data class TaskListUiState(
    val tasks: List<Task> = emptyList(), val loading: Boolean = false,
    val error: String? = null, val assignee: String? = null, val status: String? = null
)

@HiltViewModel
class TaskListViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _state = MutableStateFlow(TaskListUiState())
    val state: StateFlow<TaskListUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun setAssignee(value: String?) {
        _state.update { it.copy(assignee = value) }; refresh()
    }

    fun setStatus(value: String?) {
        _state.update { it.copy(status = value) }; refresh()
    }

    fun refresh() {
        val filters = _state.value
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            repository.observeTasks(filters.assignee, filters.status).collect { result ->
                result.fold(
                    onSuccess = { tasks ->
                        _state.update {
                            it.copy(
                                tasks = tasks,
                                loading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                loading = false,
                                error = e.message ?: "Unable to load tasks"
                            )
                        }
                    }
                )
            }
        }
    }

    fun toggle(task: Task) {
        // Optimistic update; revert to the previous value if the API request fails.
        val old = task
        _state.update { s -> s.copy(tasks = s.tasks.map { if (it.id == task.id) it.copy(completed = !it.completed) else it }) }
        viewModelScope.launch {
            repository.toggleTask(task.id).onFailure {
                _state.update { s ->
                    s.copy(
                        tasks = s.tasks.map { if (it.id == old.id) old else it },
                        error = it.message ?: "Could not update task"
                    )
                }
            }.onSuccess { updated ->
                _state.update { s -> s.copy(tasks = s.tasks.map { if (it.id == updated.id) updated else it }) }
            }
        }
    }
}
