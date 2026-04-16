package com.senacelik.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_repos")
data class GitHubRepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val owner: String,
    val stars: Int,
    val url: String,
    val description: String?,
    val language: String?
)
