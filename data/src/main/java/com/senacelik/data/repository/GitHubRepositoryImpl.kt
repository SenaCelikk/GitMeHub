package com.senacelik.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.senacelik.data.local.database.GitMeHubDatabase
import com.senacelik.data.local.entity.GitHubRepoWithStarredStatus
import com.senacelik.data.mapper.toDomain
import com.senacelik.data.mapper.toStarredEntity
import com.senacelik.data.remote.GitHubRemoteMediator
import com.senacelik.data.remote.GithubApi
import com.senacelik.domain.model.GitHubRepo
import com.senacelik.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GitHubRepositoryImpl(
    private val api: GithubApi,
    private val database: GitMeHubDatabase
) : GitHubRepository {

    override fun getSearchResultStream(query: String): Flow<PagingData<GitHubRepo>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_PAGE_SIZE
            ),
            remoteMediator = GitHubRemoteMediator(
                query = query,
                api = api,
                database = database
            ),
            pagingSourceFactory = { database.gitRepoDao().pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.repo.toDomain(isStarred = it.isStarred) }
        }
    }

    override suspend fun toggleStar(repo: GitHubRepo) {
        val isStarred = database.starredRepoDao().isStarred(repo.id)
        if (isStarred) {
            database.starredRepoDao().deleteStarredRepo(repo.toStarredEntity())
        } else {
            database.starredRepoDao().insertStarredRepo(repo.toStarredEntity())
        }
    }

    override fun getStarredRepos(): Flow<List<GitHubRepo>> {
        return database.starredRepoDao().observeStarredRepos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStarredRepoIds(): Flow<Set<Long>> {
        return database.starredRepoDao().observeStarredIds().map { it.toSet() }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}
