package com.nrin31266.ecommercemultivendor.di

import com.nrin31266.ecommercemultivendor.network.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("API_LOG", message)
    }
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY // Log chi tiết request và response
    return loggingInterceptor
}
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.8:8080/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Dùng MoshiConverterFactory
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}


