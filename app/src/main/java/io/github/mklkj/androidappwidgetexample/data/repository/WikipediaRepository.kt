package io.github.mklkj.androidappwidgetexample.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import io.github.mklkj.androidappwidgetexample.data.api.model.WikipediaPageSummary
import io.github.mklkj.androidappwidgetexample.data.api.model.WikipediaPageSummaryJsonAdapter
import io.github.mklkj.androidappwidgetexample.data.api.service.WikipediaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WikipediaRepository @Inject constructor(
    private val wikipediaService: WikipediaService,
    private val sharedPreferences: SharedPreferences,
) {

    companion object {
        private const val PAGE_RANDOM_SUMMARY_KEY = "page_random_summary_key"
    }

    suspend fun getPageRandomSummary(): WikipediaPageSummary {
        val res = wikipediaService.getPageRandomSummary()
        sharedPreferences.edit {
            putString(
                PAGE_RANDOM_SUMMARY_KEY,
                WikipediaPageSummaryJsonAdapter(Moshi.Builder().build()).toJson(res)
            )
        }
        return res
    }

    suspend fun getCachedPageRandomSummary() = withContext(Dispatchers.IO) {
        sharedPreferences.getString(PAGE_RANDOM_SUMMARY_KEY, null)?.let {
            WikipediaPageSummaryJsonAdapter(Moshi.Builder().build()).fromJson(it)
        }
    }
}
