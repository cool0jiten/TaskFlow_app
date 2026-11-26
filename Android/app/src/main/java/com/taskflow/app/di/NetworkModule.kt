package com.taskflow.app.di

import com.taskflow.app.data.remote.TaskApi
import com.taskflow.app.data.repository.TaskRepositoryImpl
import com.taskflow.app.domain.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // Replace with the assessment backend URL. For emulator localhost use http://10.0.2.2:PORT/.
    private const val BASE_URL = "http://10.0.2.2:3000/"

    @Provides @Singleton fun loggingInterceptor() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    @Provides @Singleton fun client(logging: HttpLoggingInterceptor) = OkHttpClient.Builder().addInterceptor(logging).build()
    @Provides @Singleton fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build()
    @Provides @Singleton fun taskApi(retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}
