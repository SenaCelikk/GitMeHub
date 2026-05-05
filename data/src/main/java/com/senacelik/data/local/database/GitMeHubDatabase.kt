package com.senacelik.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.senacelik.data.local.dao.GitHubRepoDao
import com.senacelik.data.local.dao.RemoteKeysDao
import com.senacelik.data.local.dao.StarredRepoDao
import com.senacelik.data.local.entity.GitHubRepoEntity
import com.senacelik.data.local.entity.RemoteKeys
import com.senacelik.data.local.entity.StarredRepoEntity

@Database(
    entities = [GitHubRepoEntity::class, RemoteKeys::class, StarredRepoEntity::class],
    version = 3,
    exportSchema = false
)
abstract class GitMeHubDatabase : RoomDatabase() {
    abstract fun gitRepoDao(): GitHubRepoDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun starredRepoDao(): StarredRepoDao
}
