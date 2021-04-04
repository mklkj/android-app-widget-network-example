package io.github.mklkj.androidappwidgetexample.service

import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.github.mklkj.androidappwidgetexample.service.ServiceWorker.Companion.WORKER_WIDGET_IDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceManager @Inject constructor(
    private val workManager: WorkManager
) {

    fun startOneTimeWorkerForRefreshPageOnWidget(appWidgetIds: IntArray) {
        val worker = OneTimeWorkRequestBuilder<ServiceWorker>()
            .setInputData(Data.Builder().putIntArray(WORKER_WIDGET_IDS, appWidgetIds).build())
            .build()
        workManager.enqueueUniqueWork("refresh", ExistingWorkPolicy.REPLACE, worker)
    }
}
