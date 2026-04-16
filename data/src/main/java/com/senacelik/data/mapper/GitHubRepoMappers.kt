package com.senacelik.data.mapper

import com.senacelik.data.local.entity.GitHubRepoEntity
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
fun GitHubRepoEntity.toDomain() = GitHubRepo(
    id = id,
    name = name,
    url = url, // Map it here
    owner = owner,
    stargazersCount = stars,
    description = description,
    language = language
)