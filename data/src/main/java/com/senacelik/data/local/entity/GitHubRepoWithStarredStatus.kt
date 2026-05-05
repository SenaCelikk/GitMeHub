package com.senacelik.data.local.entity

import androidx.room.Embedded

data class GitHubRepoWithStarredStatus(
    @Embedded val repo: GitHubRepoEntity,
    val isStarred: Boolean
)
