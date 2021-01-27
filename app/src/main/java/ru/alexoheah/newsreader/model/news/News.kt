package ru.alexoheah.newsreader.model.news

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class News @JvmOverloads constructor (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "link")
    val link: String = "",

    @ColumnInfo(name = "published_on")
    val publishedOn: String = "",

    @ColumnInfo(name = "author")
    val author: String = ""

)