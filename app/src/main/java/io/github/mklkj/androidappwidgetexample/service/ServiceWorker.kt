package io.github.mklkj.androidappwidgetexample.service

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.mklkj.androidappwidgetexample.data.repository.WikipediaRepository
import io.github.mklkj.androidappwidgetexample.ui.base.BaseAppWidgetProvider.Companion.WIDGET_ID_KEY
import io.github.mklkj.androidappwidgetexample.ui.modules.randomwidget.RandomWidgetProvider
import io.github.mklkj.androidappwidgetexample.ui.modules.randomwidget.RandomWidgetProvider.Companion.UPDATE_REFRESH_ERROR_KEY
import io.github.mklkj.androidappwidgetexample.ui.modules.randomwidget.RandomWidgetProvider.Companion.UPDATE_RELOAD_KEY
import io.github.mklkj.androidappwidgetexample.ui.modules.randomwidget.RandomWidgetProvider.Companion.UPDATE_TYPE_KEY
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

@HiltWorker
class ServiceWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val wikipediaRepository: WikipediaRepository,
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val WORKER_WIDGET_IDS = "worker_app_widget_ids"
    }

    override suspend fun doWork() = coroutineScope {
        return@coroutineScope try {
            wikipediaRepository.getPageRandomSummary()
            updateWidget(UPDATE_RELOAD_KEY)
            Result.success()
        } catch (e: Throwable) {
            Timber.e(e)
            updateWidget(UPDATE_REFRESH_ERROR_KEY)
            Result.failure()
        }
    }

    private fun updateWidget(status: String) {
        context.sendBroadcast(Intent(context, RandomWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(UPDATE_TYPE_KEY, status)
            putExtra(WIDGET_ID_KEY, workerParameters.inputData.getIntArray(WORKER_WIDGET_IDS))
        })
    }
}
