package com.example.pagingrickandmorty.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pagingrickandmorty.data.KeyData
import com.example.pagingrickandmorty.data.Result
import com.example.pagingrickandmorty.database.PersonDatabase
import com.example.pagingrickandmorty.network.PersonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PersonMediatorRepository @Inject constructor(
    private val personApi: PersonApi,
    private val personDatabase: PersonDatabase
) : RemoteMediator<Int, Result>() {
    private val personDao = personDatabase.getPersonDao()
    private val keyDao = personDatabase.getKeyDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val key = getKeyClosedToThePosition(state)
                    key?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val keys = getFirstRemoteKey(state)
                    val prevPage = keys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = keys != null
                    )
                    prevPage
                }

                LoadType.APPEND -> {
                    val keys = getLastRemoteKeys(state)
                    val nextPage = keys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = keys != null
                    )
                    nextPage
                }
            }
            val response = personApi.getPersonApi(currentPage)
            val endOfPaginationReached = response.info.pages == currentPage
            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1
            personDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    keyDao.deleteKeys()
                    personDao.deleteAll()
                }

                personDao.savePerson(response.results)
                val keys = response.results.map {
                    KeyData(
                        id = it.id,
                        nextPage = nextPage,
                        prevPage = prevPage
                    )
                }
                keyDao.saveAllKeys(keys)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyClosedToThePosition(state: PagingState<Int, Result>): KeyData? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let {
                state.closestItemToPosition(it)?.id?.let { result ->
                    keyDao.getKeys(result)
                }
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Result>): KeyData? {
        return withContext(Dispatchers.IO) {
            state.pages.firstOrNull {
                it.data.isNotEmpty()
            }?.data?.firstOrNull()?.let {
                keyDao.getKeys(it.id)
            }
        }
    }

    private suspend fun getLastRemoteKeys(state: PagingState<Int, Result>): KeyData? {
        return withContext(Dispatchers.IO) {
            state.pages.lastOrNull {
                it.data.isNotEmpty()
            }?.data?.lastOrNull()?.let {
                keyDao.getKeys(it.id)
            }
        }
    }
}