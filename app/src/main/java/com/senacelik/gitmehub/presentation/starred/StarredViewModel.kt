package com.senacelik.gitmehub.presentation.starred

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senacelik.domain.model.GitHubRepo
import com.senacelik.domain.repository.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarredViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {

    val starredRepos: StateFlow<List<GitHubRepo>> = repository.getStarredRepos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleStar(repo: GitHubRepo) {
        viewModelScope.launch {
            repository.toggleStar(repo)
        }
    }
}
