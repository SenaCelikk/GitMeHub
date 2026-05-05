package com.senacelik.data.di

import android.content.Context
import androidx.room.Room
import com.senacelik.data.local.dao.GitHubRepoDao
import com.senacelik.data.local.dao.RemoteKeysDao
import com.senacelik.data.local.dao.StarredRepoDao
import com.senacelik.data.local.database.GitMeHubDatabase
import com.senacelik.data.remote.GithubApi
import com.senacelik.data.repository.GitHubRepositoryImpl
import com.senacelik.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GitMeHubDatabase {
        return Room.databaseBuilder(
            context,
            GitMeHubDatabase::class.java,
            "gitmehub_db"
        )
        .fallbackToDestructiveMigration() // Handle database changes during development
        .build()
    }

    @Provides
    @Singleton
    fun provideGitHubRepoDao(database: GitMeHubDatabase): GitHubRepoDao {
        return database.gitRepoDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: GitMeHubDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }

    @Provides
    @Singleton
    fun provideStarredRepoDao(database: GitMeHubDatabase): StarredRepoDao {
        return database.starredRepoDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("User-Agent", "GitMeHub-App")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideGithubApi(okHttpClient: OkHttpClient): GithubApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(GithubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubRepository(
        api: GithubApi,
        database: GitMeHubDatabase
    ): GitHubRepository {
        return GitHubRepositoryImpl(api, database)
    }
}
