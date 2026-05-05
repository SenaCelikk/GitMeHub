package com.senacelik.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.senacelik.data.local.database.GitMeHubDatabase
import com.senacelik.data.local.entity.GitHubRepoEntity
import com.senacelik.data.local.entity.GitHubRepoWithStarredStatus
import com.senacelik.data.local.entity.RemoteKeys
import com.senacelik.data.mapper.toEntity

@OptIn(ExperimentalPagingApi::class)
class GitHubRemoteMediator(
    private val query: String,
    private val api: GithubApi,
    private val database: GitMeHubDatabase
) : RemoteMediator<Int, GitHubRepoWithStarredStatus>() {

    override fun toString(): String = "GitHubRemoteMediator(query='$query')"

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GitHubRepoWithStarredStatus>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyAtClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val apiResponse = api.searchRepositories(
                query = query,
                page = page,
                perPage = state.config.pageSize
            )

            val repos = apiResponse.items
            val endOfPaginationReached = repos.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.gitRepoDao().clearRepos()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.gitRepoDao().insertRepos(repos.map { it.toEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, GitHubRepoWithStarredStatus>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { item ->
                database.remoteKeysDao().remoteKeysRepoId(item.repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, GitHubRepoWithStarredStatus>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { item ->
                database.remoteKeysDao().remoteKeysRepoId(item.repo.id)
            }
    }

    private suspend fun getRemoteKeyAtClosestToCurrentPosition(
        state: PagingState<Int, GitHubRepoWithStarredStatus>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.repo?.id?.let { repoId ->
                database.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}
