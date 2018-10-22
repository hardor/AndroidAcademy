package ru.profapp.androidacademy.Data.network

import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.profapp.androidacademy.Data.network.dto.NyNewsDTO
import ru.profapp.androidacademy.Data.network.endpoints.INyNewsEndpoint

object RestApi {

    fun getNews(section: String): Single<Response<NyNewsDTO>> {
        return instance.getNews(section)

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