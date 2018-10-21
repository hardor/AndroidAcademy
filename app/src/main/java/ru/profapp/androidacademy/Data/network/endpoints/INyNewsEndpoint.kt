package ru.profapp.androidacademy.Data.network.endpoints

import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import ru.profapp.androidacademy.Data.network.ApiKeyInterceptor
import ru.profapp.androidacademy.Data.network.dto.NyNewsDTO


interface INyNewsEndpoint {

    @GET("{section}.json")
    fun getNews(@Path("section") section: String): Single<NyNewsDTO>
}