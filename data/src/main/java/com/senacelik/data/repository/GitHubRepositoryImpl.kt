package com.senacelik.data.repository

import android.util.Log
import com.senacelik.data.local.dao.GitHubRepoDao
import com.senacelik.data.mapper.toDomain
import com.senacelik.data.mapper.toEntity
import com.senacelik.data.remote.GithubApi
import com.senacelik.domain.model.GitHubRepo
import com.senacelik.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GitHubRepositoryImpl(
    private val api: GithubApi,
    private val dao: GitHubRepoDao
) : GitHubRepository {

    override fun getRepositories(): Flow<List<GitHubRepo>> {
        return dao.observeAllRepos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshRepositories(username: String) {
        try {
            val remoteRepos = api.getUserRepositories(username)
            dao.clearRepos()
            dao.insertRepos(remoteRepos.map { it.toEntity() })
        } catch (e: Exception) {
            Log.e("GitHubRepository", "Error refreshing repos", e)
        }
    }

    override suspend fun searchRepositories(query: String) {
        try {
            Log.d("GitHubRepository", "Searching for: $query")
            val response = api.searchRepositories(query)
            Log.d("GitHubRepository", "Found ${response.items.size} repos")
            dao.clearRepos()
            dao.insertRepos(response.items.map { it.toEntity() })
        } catch (e: Exception) {
            Log.e("GitHubRepository", "Error searching repos", e)
        }
    }
}
