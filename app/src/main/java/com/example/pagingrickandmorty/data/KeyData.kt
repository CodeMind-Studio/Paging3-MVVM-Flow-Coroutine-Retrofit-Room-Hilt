package com.example.pagingrickandmorty.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keyDao")
data class KeyData(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nextPage: Int?,
    val prevPage: Int?
)
