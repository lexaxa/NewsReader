package ru.alexoheah.newsreader.data.database

import androidx.room.*

import ru.alexoheah.newsreader.model.news.News

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: List<News?>?)

    @Query(value = "select * from news")
    fun getNews(): List<News>?

    @Query(value = "select * from news where id = :id")
    fun getNewsById(id: Int): News?

    @Query("DELETE FROM news")
    fun clearAllNews()

    @Transaction
    fun clearAndCacheNews(news: List<News>) {
        clearAllNews()
        insertNews(news)
    }

}