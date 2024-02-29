package com.example.pagingrickandmorty.network

import com.example.pagingrickandmorty.data.PersonData
import retrofit2.http.GET
import retrofit2.http.Query

interface PersonApi {

    @GET("character")
    suspend fun getPersonApi(@Query("page") page: Int): PersonData
}