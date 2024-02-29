package com.example.pagingrickandmorty.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagingrickandmorty.data.Result

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun savePerson(person: List<Result>)

    @Query("DELETE FROM personData")
    suspend fun deleteAll()

    @Query("SELECT * FROM personData")
    fun getPerson(): PagingSource<Int, Result>

}