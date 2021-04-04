package io.github.mklkj.androidappwidgetexample.data.api.service

import io.github.mklkj.androidappwidgetexample.data.api.model.WikipediaPageSummary
import retrofit2.http.GET

interface WikipediaService {

    @GET("page/random/summary")
    suspend fun getPageRandomSummary(): WikipediaPageSummary
}
