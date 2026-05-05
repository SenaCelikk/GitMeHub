package com.senacelik.gitmehub.presentation.repolist

data class RepoListState(
    val searchQuery: String = ""
)

sealed class RepoListIntent {
    data class Search(val query: String) : RepoListIntent()
    data class ToggleStar(val repo: com.senacelik.domain.model.GitHubRepo) : RepoListIntent()
}
