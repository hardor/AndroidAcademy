package ru.profapp.androidacademy.Data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiKeyInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url().newBuilder().addQueryParameter("api-key", "325ecd93bec842ef9f627ce7132a70c5").build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}