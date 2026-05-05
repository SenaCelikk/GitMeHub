package com.senacelik.domain.repository

import androidx.paging.PagingData
import com.senacelik.domain.model.GitHubRepo
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    fun getSearchResultStream(query: String): Flow<PagingData<GitHubRepo>>
    suspend fun toggleStar(repo: GitHubRepo)
    fun getStarredRepos(): Flow<List<GitHubRepo>>
    fun getStarredRepoIds(): Flow<Set<Long>>
}
