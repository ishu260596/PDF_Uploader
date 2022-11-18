package com.example.digiouploadpdfassignment.di

import com.example.digiouploadpdfassignment.models.remote.PDFUploadApiService
import com.example.digiouploadpdfassignment.utils.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit.Builder  {
        return Retrofit.Builder().baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun getPDFUploadService(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient): PDFUploadApiService {
        return retrofitBuilder.client(okHttpClient).build().create(PDFUploadApiService::class.java)
    }
}