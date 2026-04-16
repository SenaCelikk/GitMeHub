package com.senacelik.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.senacelik.data.local.dao.GitHubRepoDao
import com.senacelik.data.local.entity.GitHubRepoEntity

@Database(
    entities = [GitHubRepoEntity::class],
    version = 1,
    exportSchema = true
)
abstract class GitMeHubDatabase : RoomDatabase() {

    // Room will automatically generate the implementation for this
    abstract fun gitRepoDao(): GitHubRepoDao

}