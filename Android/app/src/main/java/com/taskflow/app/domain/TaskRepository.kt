package com.taskflow.app.domain

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(assignee: String? = null, status: String? = null): Flow<Result<List<Task>>>
    suspend fun getTask(id: String): Result<Task>
    suspend fun addTask(request: CreateTaskRequest): Result<Task>
    suspend fun toggleTask(id: String): Result<Task>
}
