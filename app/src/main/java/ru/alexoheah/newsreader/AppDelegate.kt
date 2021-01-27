package ru.alexoheah.newsreader

import android.app.Application
import androidx.room.Room
import ru.alexoheah.newsreader.data.database.NewsDatabase
import ru.alexoheah.newsreader.model.Storage
import java.lang.StringBuilder

class AppDelegate: Application() {

    private lateinit var mStorage: Storage

    override fun onCreate() {
        super.onCreate()

        val database: NewsDatabase = Room.databaseBuilder(this, NewsDatabase::class.java, "news_database")
            .fallbackToDestructiveMigration()
            .build()

        mStorage = Storage(database.getNewsDao()!!)
    }

    fun getStorage(): Storage = mStorage
}