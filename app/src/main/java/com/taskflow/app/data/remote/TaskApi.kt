package com.taskflow.app.data.remote

import com.taskflow.app.domain.CreateTaskRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {
    @GET("api/tasks")
    suspend fun getTasks(@Query("assignee") assignee: String? = null, @Query("status") status: String? = null): List<TaskDto>

    @POST("api/tasks")
    suspend fun createTask(@Body request: CreateTaskRequest): TaskDto

    @PATCH("api/tasks/{id}/toggle")
    suspend fun toggleTask(@Path("id") id: String): TaskDto
}
