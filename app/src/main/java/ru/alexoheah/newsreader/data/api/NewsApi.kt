package ru.alexoheah.newsreader.data.api

import io.reactivex.Single
import retrofit2.http.GET
import ru.alexoheah.newsreader.model.news.NewsResponse

interface NewsApi {

    @GET("rss/all")
    fun getNews(): Single<NewsResponse>
}