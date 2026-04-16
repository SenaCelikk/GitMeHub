package com.senacelik.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepoDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("stargazers_count") val stars: Int,
    @SerialName("description") val description: String? = null,
    @SerialName("html_url") val url: String,
    @SerialName("language") val language: String? = null,
    @SerialName("owner") val owner: OwnerDto
)

@Serializable
data class OwnerDto(
    @SerialName("login") val login: String
)
