package com.example.pagingrickandmorty.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagingrickandmorty.data.KeyData

@Dao
interface KeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllKeys(keyData: List<KeyData>)

    @Query("DELETE FROM keyDao")
    suspend fun deleteKeys()

    @Query("SELECT * FROM keyDao WHERE id =:id")
    fun getKeys(id : Int) : KeyData

}