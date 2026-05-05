package com.senacelik.gitmehub.presentation.repolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.senacelik.domain.model.GitHubRepo
import com.senacelik.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RepoListState())
    val state: StateFlow<RepoListState> = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagingDataFlow: Flow<PagingData<GitHubRepo>> = _state
        .map { it.searchQuery }
        .distinctUntilChanged()
        .debounce(500L) // Back to 500ms to avoid excessive updates while typing
        .flatMapLatest { query ->
            if (query.isBlank()) {
                // Return empty paging data if query is blank
                kotlinx.coroutines.flow.flowOf(PagingData.empty())
            } else {
                repository.getSearchResultStream(query)
            }
        }
        .cachedIn(viewModelScope)

    fun processIntent(intent: RepoListIntent) {
        when (intent) {
            is RepoListIntent.Search -> {
                _state.update { it.copy(searchQuery = intent.query) }
            }
            is RepoListIntent.ToggleStar -> {
                viewModelScope.launch {
                    repository.toggleStar(intent.repo)
                }
            }
        }
    }
}
