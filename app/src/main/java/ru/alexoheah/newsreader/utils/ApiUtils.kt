package ru.alexoheah.newsreader.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import ru.alexoheah.newsreader.BuildConfig
import ru.alexoheah.newsreader.data.api.ApiKeyInterceptor
import ru.alexoheah.newsreader.data.api.NewsApi
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiUtils {

    companion object {

        val NETWORK_EXCEPTIONS = listOf<Class<*>>(
            UnknownHostException::class.java,
            SocketTimeoutException::class.java,
            ConnectException::class.java
        )
        private var sClient: OkHttpClient? = null
        private var sRetrofit: Retrofit? = null
        private var sApi: NewsApi? = null

        fun getApiService(): NewsApi {
            return sApi ?: getRetrofit().create(NewsApi::class.java)
        }

        private fun getRetrofit(): Retrofit {
            sRetrofit = sRetrofit ?: Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL) // need for interceptors
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
                .build()
            return sRetrofit as Retrofit
        }

        private fun getClient(): OkHttpClient {
            if (sClient == null) {
                val builder = OkHttpClient().newBuilder()
                builder.addInterceptor(ApiKeyInterceptor())
                if (!BuildConfig.BUILD_TYPE.contains("release")) {
                    builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
                sClient = builder.build()
            }
            return sClient!!
        }

    }
}