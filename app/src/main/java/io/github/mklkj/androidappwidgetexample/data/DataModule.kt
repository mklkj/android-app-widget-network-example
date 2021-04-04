package io.github.mklkj.androidappwidgetexample.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.mklkj.androidappwidgetexample.data.api.service.WikipediaService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://pl.wikipedia.org/api/rest_v1/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideWikipediaService(retrofit: Retrofit): WikipediaService = retrofit.create()
}
