package com.senacelik.gitmehub.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.senacelik.gitmehub.presentation.repolist.RepoListIntent
import com.senacelik.gitmehub.presentation.repolist.RepoListState
import com.senacelik.gitmehub.presentation.repolist.RepoListViewModel

@Composable
fun RepoListScreen(
    viewModel: RepoListViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val state: RepoListState by viewModel.state.collectAsState()
    val pagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems()
    var textState by remember { mutableStateOf(state.searchQuery) }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search Bar
        OutlinedTextField(
            value = textState,
            onValueChange = { 
                textState = it
                viewModel.processIntent(RepoListIntent.Search(it))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search Repositories") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            if (textState.isBlank()) {
                Text(
                    text = "Search for repositories to see results here.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id }
                    ) { index ->
                        val repo = pagingItems[index]
                        if (repo != null) {
                            RepoItem(
                                repo = repo,
                                onClick = { onNavigateToDetail(repo.url) },
                                onStarClick = { viewModel.processIntent(RepoListIntent.ToggleStar(repo)) }
                            )
                        }
                    }

                    when (val loadState = pagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                }
                            }
                        }
                        is LoadState.Error -> {
                            item {
                                Text(
                                    text = loadState.error.localizedMessage ?: "Error loading more items",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        else -> {}
                    }
                }

                if (pagingItems.loadState.refresh is LoadState.Loading || (textState.isNotBlank() && textState != state.searchQuery)) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (pagingItems.loadState.refresh is LoadState.Error) {
                    val error = (pagingItems.loadState.refresh as LoadState.Error).error
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error.localizedMessage ?: "An error occurred",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { pagingItems.retry() }) {
                            Text("Retry")
                        }
                    }
                } else if (pagingItems.itemCount == 0 && 
                           pagingItems.loadState.refresh is LoadState.NotLoading &&
                           textState == state.searchQuery) {
                    Text(
                        text = "No repositories found for '$textState'",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
