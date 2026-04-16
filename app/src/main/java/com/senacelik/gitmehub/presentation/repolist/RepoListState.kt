package com.senacelik.gitmehub.presentation.repolist

import com.senacelik.domain.model.GitHubRepo

// 1. STATE: What the UI displays
data class RepoListState(
    val isLoading: Boolean = false,
    val repositories: List<GitHubRepo> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "kotlin" // Default search for testing
)

// 2. INTENT: Actions the user takes on the View
sealed class RepoListIntent {
    data class Search(val query: String) : RepoListIntent()
    object RefreshList : RepoListIntent()
}
