package com.senacelik.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.senacelik.data.local.entity.StarredRepoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StarredRepoDao {
    @Query("SELECT * FROM starred_repos")
    fun observeStarredRepos(): Flow<List<StarredRepoEntity>>

    @Query("SELECT id FROM starred_repos")
    fun observeStarredIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStarredRepo(repo: StarredRepoEntity)

    @Delete
    suspend fun deleteStarredRepo(repo: StarredRepoEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM starred_repos WHERE id = :id)")
    suspend fun isStarred(id: Long): Boolean
}
