package com.taskflow.app.data.remote

import com.taskflow.app.domain.Assignee
import com.taskflow.app.domain.CreateTaskRequest
import com.taskflow.app.domain.Priority
import com.taskflow.app.domain.Task

data class TaskDto(
    val id: Any? = null,
    val title: String = "",
    val description: String? = null,
    val dueDate: String? = null,
    val priority: String = "medium",
    val assigneeId: String = "aarav",
    val completed: Boolean = false
) {
    fun toDomain() = Task(
        id = id?.toString() ?: "",
        title = title,
        description = description,
        dueDate = dueDate,
        priority = Priority.from(priority),
        assigneeId = Assignee.from(assigneeId),
        completed = completed
    )
}

fun Task.toCreateRequest() = CreateTaskRequest(
    title = title.trim(), description = description?.trim()?.takeIf { it.isNotEmpty() },
    dueDate = dueDate, priority = priority.apiValue, assigneeId = assigneeId.apiValue,
    completed = completed
)
