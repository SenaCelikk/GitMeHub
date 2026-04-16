package com.senacelik.data.remote

import com.senacelik.data.remote.dto.GitHubRepoDto
import com.senacelik.data.remote.dto.GitHubSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("users/{username}/repos")
    suspend fun getUserRepositories(
        @Path("username") username: String
    ): List<GitHubRepoDto>

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc"
    ): GitHubSearchResponseDto
}
