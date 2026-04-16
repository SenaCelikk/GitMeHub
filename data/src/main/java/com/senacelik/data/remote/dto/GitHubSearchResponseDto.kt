package com.senacelik.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubSearchResponseDto(
    @SerialName("items") val items: List<GitHubRepoDto>
)
