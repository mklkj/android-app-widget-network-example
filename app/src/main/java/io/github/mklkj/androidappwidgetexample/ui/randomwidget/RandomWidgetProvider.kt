package io.github.mklkj.androidappwidgetexample.ui.randomwidget

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import io.github.mklkj.androidappwidgetexample.R
import io.github.mklkj.androidappwidgetexample.data.repository.WikipediaRepository
import io.github.mklkj.androidappwidgetexample.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class RandomWidgetProvider : AppWidgetProvider(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var wikipediaRepository: WikipediaRepository


    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach {
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_random)
            launch {
                remoteViews.onWidgetUpdate(context)
                appWidgetManager.updateAppWidget(it, remoteViews)
            }
        }
    }

    private suspend fun RemoteViews.onWidgetUpdate(context: Context) {
        val page = wikipediaRepository.getCachedPageRandomSummary()
        setTextViewText(R.id.app_name, page?.title)

        setOnClickPendingIntent(R.id.open_button, Intent(Intent.ACTION_VIEW).let {
            it.data = page?.contentUrls?.desktop?.page?.toUri()
            PendingIntent.getActivity(context, 0, it, FLAG_CANCEL_CURRENT)
        })

        setOnClickPendingIntent(R.id.app_name, Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, 0, it, 0)
        })
        // todo: refresh from network
        setOnClickPendingIntent(R.id.refresh_button, Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, 0, it, 0)
        })
        // todo: reload from sharedpref
        setOnClickPendingIntent(R.id.reload_button, Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, 0, it, 0)
        })
    }
}
