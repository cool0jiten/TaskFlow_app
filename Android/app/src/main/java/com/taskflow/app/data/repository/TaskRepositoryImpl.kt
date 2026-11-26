package com.taskflow.app.data.repository

import com.taskflow.app.data.remote.TaskApi
import com.taskflow.app.domain.CreateTaskRequest
import com.taskflow.app.domain.Task
import com.taskflow.app.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val api: TaskApi) : TaskRepository {
    override fun observeTasks(assignee: String?, status: String?): Flow<Result<List<Task>>> = flow {
        emit(runCatching { api.getTasks(assignee, status).map { it.toDomain() } })
    }

    override suspend fun getTask(id: String): Result<Task> = runCatching {
        api.getTasks().firstOrNull { it.id?.toString() == id }?.toDomain()
            ?: throw IllegalStateException("Task not found")
    }

    override suspend fun addTask(request: CreateTaskRequest): Result<Task> = runCatching {
        api.createTask(request).toDomain()
    }

    override suspend fun toggleTask(id: String): Result<Task> = runCatching {
        api.toggleTask(id).toDomain()
    }
}
