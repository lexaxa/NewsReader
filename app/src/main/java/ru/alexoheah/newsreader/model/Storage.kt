package ru.alexoheah.newsreader.model

import ru.alexoheah.newsreader.data.database.NewsDao
import ru.alexoheah.newsreader.model.news.*

class Storage(val newsDao: NewsDao) {

    fun insertNews(response: NewsResponse) {

        val news: List<News>? = response.channel?.items?.map { it.toStorage() }
        if (news != null) {
            newsDao.clearAndCacheNews(news)

        }
    }

    fun getNews(): NewsResponse? {
        var newsList: List<News?>? = newsDao.getNews()
        var response = NewsResponse()
        response.channel?.items = (newsList?.map { it!!.toRemote() } as MutableList<Item>?)!!
        return response
    }

    fun getNewsById(id: Int): News? {
        return newsDao.getNewsById(id)
    }

    interface StorageOwner {
        fun obtainStorage(): Storage?
    }
}