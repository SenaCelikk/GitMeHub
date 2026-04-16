package com.senacelik.domain.repository

import com.senacelik.domain.model.GitHubRepo
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    // UI observes this for real-time updates from Room
    fun getRepositories(): Flow<List<GitHubRepo>>

    // Triggered to fetch fresh data from the network
    suspend fun refreshRepositories(username: String)

    // New: Search for repositories by query
    suspend fun searchRepositories(query: String)
}
