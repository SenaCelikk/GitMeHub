package com.senacelik.data.mapper

import com.senacelik.data.local.entity.GitHubRepoEntity
import com.senacelik.data.local.entity.StarredRepoEntity
import com.senacelik.data.remote.dto.GitHubRepoDto
import com.senacelik.domain.model.GitHubRepo

// Network -> Database
fun GitHubRepoDto.toEntity() = GitHubRepoEntity(
    id = id,
    name = name,
    url = url, // Map it here
    owner = owner.login,
    stars = stars,
    description = description,
    language = language
)

// Database -> Domain
fun GitHubRepoEntity.toDomain(isStarred: Boolean = false) = GitHubRepo(
    id = id,
    name = name,
    url = url, // Map it here
    owner = owner,
    stargazersCount = stars,
    description = description,
    language = language,
    isStarred = isStarred
)

// Domain -> Starred Entity
fun GitHubRepo.toStarredEntity() = StarredRepoEntity(
    id = id,
    name = name,
    owner = owner,
    stars = stargazersCount,
    url = url,
    description = description,
    language = language
)

// Starred Entity -> Domain
fun StarredRepoEntity.toDomain() = GitHubRepo(
    id = id,
    name = name,
    owner = owner,
    url = url,
    stargazersCount = stars,
    description = description,
    language = language,
    isStarred = true
)
