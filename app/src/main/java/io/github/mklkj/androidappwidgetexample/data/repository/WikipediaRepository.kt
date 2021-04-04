package io.github.mklkj.androidappwidgetexample.data.repository

import io.github.mklkj.androidappwidgetexample.data.api.service.WikipediaService
import javax.inject.Inject

class WikipediaRepository @Inject constructor(
    private val wikipediaService: WikipediaService,
) {

    suspend fun getPageRandomSummary() = wikipediaService.getPageRandomSummary()
}
