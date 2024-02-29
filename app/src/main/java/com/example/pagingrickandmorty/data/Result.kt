package com.example.pagingrickandmorty.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personData")
data class Result(
    val created: String,
    val gender: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val image: String,
    val name: String,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)