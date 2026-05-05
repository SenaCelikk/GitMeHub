package com.senacelik.domain.model

data class GitHubRepo(
    val id: Long,
    val name: String,
    val owner: String,
    val url: String,
    val stargazersCount: Int,
    val description: String? = null,
    val language: String? = null,
    val isStarred: Boolean = false
)
