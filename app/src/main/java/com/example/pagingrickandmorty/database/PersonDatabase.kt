package com.example.pagingrickandmorty.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pagingrickandmorty.dao.KeyDao
import com.example.pagingrickandmorty.dao.PersonDao
import com.example.pagingrickandmorty.data.KeyData
import com.example.pagingrickandmorty.data.Result

@Database(entities = [Result::class , KeyData::class], version = 1, exportSchema = false)
abstract class PersonDatabase : RoomDatabase() {

    abstract fun getPersonDao(): PersonDao

    abstract fun getKeyDao(): KeyDao

}