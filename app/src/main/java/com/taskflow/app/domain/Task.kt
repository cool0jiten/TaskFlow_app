package com.taskflow.app.domain

enum class Priority(val apiValue: String) {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");
    companion object {
        fun from(value: String?) = entries.firstOrNull { it.apiValue.equals(value, true) } ?: MEDIUM
    }
}

enum class Assignee(val apiValue: String, val displayName: String) {
    AARAV("aarav", "Aarav"),
    PRIYA("priya", "Priya"),
    ROHAN("rohan", "Rohan");
    companion object {
        fun from(value: String?) = entries.firstOrNull { it.apiValue.equals(value, true) } ?: AARAV
    }
}

data class Task(
    val id: String,
    val title: String,
    val description: String? = null,
    val dueDate: String? = null,
    val priority: Priority = Priority.MEDIUM,
    val assigneeId: Assignee = Assignee.AARAV,
    val completed: Boolean = false
)

data class CreateTaskRequest(
    val title: String,
    val description: String?,
    val dueDate: String?,
    val priority: String,
    val assigneeId: String,
    val completed: Boolean = false
)
