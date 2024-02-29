package com.example.pagingrickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagingrickandmorty.data.Result
import com.example.pagingrickandmorty.database.PersonDatabase
import com.example.pagingrickandmorty.network.PersonApi
import com.example.pagingrickandmorty.repo.PersonMediatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class PersonViewModel @Inject constructor(
    private val api: PersonApi,
    private val personDatabase: PersonDatabase
) : ViewModel() {

    fun getPerson(): Flow<PagingData<Result>> =
        Pager(
            config = PagingConfig(10, enablePlaceholders = false),
            remoteMediator = PersonMediatorRepository(api, personDatabase),
            pagingSourceFactory = {
                personDatabase.getPersonDao().getPerson()
            }
        ).flow.cachedIn(viewModelScope)
}