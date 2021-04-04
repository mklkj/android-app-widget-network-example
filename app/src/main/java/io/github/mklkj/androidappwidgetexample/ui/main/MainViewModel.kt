package io.github.mklkj.androidappwidgetexample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mklkj.androidappwidgetexample.data.api.model.WikipediaPageSummary
import io.github.mklkj.androidappwidgetexample.data.repository.WikipediaRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wikipediaRepository: WikipediaRepository
) : ViewModel() {

    private val _page = MutableLiveData<WikipediaPageSummary>()
    val page: LiveData<WikipediaPageSummary> = _page

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                _page.value = wikipediaRepository.getPageRandomSummary()
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }
}
