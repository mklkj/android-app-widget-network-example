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

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _openInBrowser = MutableLiveData<String?>()
    val openInBrowser: LiveData<String?> = _openInBrowser

    init {
        loadData()
    }

    private fun launchWithCatch(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                block()
            } catch (e: Throwable) {
                Timber.e(e)
                _message.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    private fun loadData() {
        launchWithCatch {
            _page.value = wikipediaRepository.getCachedPageRandomSummary()
        }
    }

    fun reload() {
        launchWithCatch {
            _page.value = wikipediaRepository.getPageRandomSummary()
        }
    }

    fun openInBrowser() {
        _openInBrowser.value = _page.value?.contentUrls?.desktop?.page
    }
}
