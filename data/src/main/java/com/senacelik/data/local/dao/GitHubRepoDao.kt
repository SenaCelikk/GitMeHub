package com.senacelik.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.senacelik.data.local.entity.GitHubRepoEntity
import com.senacelik.data.local.entity.GitHubRepoWithStarredStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GitHubRepoDao {
    @Query("SELECT * FROM github_repos")
    fun observeAllRepos(): Flow<List<GitHubRepoEntity>>

    @Query(
        """
        SELECT github_repos.*, starred_repos.id IS NOT NULL as isStarred 
        FROM github_repos 
        LEFT JOIN starred_repos ON github_repos.id = starred_repos.id
        """
    )
    fun pagingSource(): PagingSource<Int, GitHubRepoWithStarredStatus>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<GitHubRepoEntity>)

    @Query("DELETE FROM github_repos")
    suspend fun clearRepos()
}
