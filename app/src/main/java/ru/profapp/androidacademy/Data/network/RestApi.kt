package ru.profapp.androidacademy.Data.network

import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.profapp.androidacademy.Data.network.endpoints.INyNewsEndpoint
import ru.profapp.androidacademy.Models.NewsItem
import java.text.SimpleDateFormat
import java.util.*

object RestApi {


    fun getNews(section: String): Single<List<NewsItem>> {
        return instance.getNews(section).map {


            return@map it.results?.map { r ->
                NewsItem(
                        r.title,
                        r.multimedia?.lastOrNull()?.url,
                        r.subsection,
                        Date(),
                     //   SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", Locale.getDefault()).parse(r.publishedDate),
                        r.abstract
                )
            }

        }
    }

    private var instance: INyNewsEndpoint = create()

    private fun create(): INyNewsEndpoint {
        val httpClient = OkHttpClient().newBuilder().addInterceptor(ApiKeyInterceptor())

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.nytimes.com/svc/topstories/v2/")
                .client(httpClient.build())
                .build()
        return retrofit.create(INyNewsEndpoint::class.java)
    }

}