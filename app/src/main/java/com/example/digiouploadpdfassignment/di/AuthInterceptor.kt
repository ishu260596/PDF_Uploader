package com.example.digiouploadpdfassignment.di

import com.example.digiouploadpdfassignment.utils.Const
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Headers
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        request.addHeader("authorization", "Basic ${Const.getAuth()}")
        request.addHeader("Content-Type", "application/json")
        return chain.proceed(request.build())
    }
}