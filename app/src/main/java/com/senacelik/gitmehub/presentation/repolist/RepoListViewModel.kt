package com.senacelik.gitmehub.presentation.repolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senacelik.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    // The single source of truth for the UI
    private val _state = MutableStateFlow(RepoListState())
    val state: StateFlow<RepoListState> = _state.asStateFlow()

    init {
        // Start observing the local database immediately
        observeRepositories()
        // Trigger an initial search
        processIntent(RepoListIntent.Search(_state.value.searchQuery))
    }

    // The single entry point for all UI actions
    fun processIntent(intent: RepoListIntent) {
        when (intent) {
            is RepoListIntent.Search -> {
                _state.update { it.copy(searchQuery = intent.query) }
                performSearch(intent.query)
            }
            is RepoListIntent.RefreshList -> {
                performSearch(_state.value.searchQuery)
            }
        }
    }

    private fun observeRepositories() {
        viewModelScope.launch {
            // This Flow comes from Room. It updates automatically when the DB changes.
            repository.getRepositories()
                .collect { repos ->
                    _state.update { it.copy(repositories = repos) }
                }
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Fetch from network -> Save to Room -> observeRepositories() auto-updates UI
                repository.searchRepositories(query)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = e.localizedMessage ?: "Unknown error")
                }
            }
        }
    }
}
