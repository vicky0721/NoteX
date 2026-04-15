package com.example.notex.model

import androidx.room.Entity
import androidx.room.PrimaryKey
//import kotlinx.serialization.Serializable
import java.io.Serializable



@Entity(tableName = "Note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val title: String,
    val content: String,
    val date: String,
    val color: Int = -1,

    ):Serializable