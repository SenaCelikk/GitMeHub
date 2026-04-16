package com.senacelik.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.senacelik.data.local.entity.GitHubRepoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GitHubRepoDao {
    // Returns a Flow so the UI can observe database changes in real-time
    @Query("SELECT * FROM github_repos")
    fun observeAllRepos(): Flow<List<GitHubRepoEntity>>

    // Inserts fresh data and replaces existing items if IDs match
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<GitHubRepoEntity>)

    @Query("DELETE FROM github_repos")
    suspend fun clearRepos()
}