package ru.alexoheah.newsreader.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

import ru.alexoheah.newsreader.model.news.News

@Database(
    entities = [News::class],
    version = 4
)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao?
}
