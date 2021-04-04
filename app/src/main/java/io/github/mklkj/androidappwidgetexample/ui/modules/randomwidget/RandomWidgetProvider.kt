package io.github.mklkj.androidappwidgetexample.ui.modules.randomwidget

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import io.github.mklkj.androidappwidgetexample.R
import io.github.mklkj.androidappwidgetexample.data.repository.WikipediaRepository
import io.github.mklkj.androidappwidgetexample.ui.MainActivity
import io.github.mklkj.androidappwidgetexample.ui.base.BaseAppWidgetProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RandomWidgetProvider : BaseAppWidgetProvider() {

    @Inject
    lateinit var wikipediaRepository: WikipediaRepository

    companion object {
        private const val UPDATE_TYPE_KEY = "button_type_key"
        private const val UPDATE_RELOAD_KEY = "button_reload_key"
        private const val UPDATE_REFRESH_KEY = "button_refresh_key"
    }

    override fun onUpdate(context: Context, appWidgetIds: IntArray, extras: Bundle?) {
        appWidgetIds.forEach {
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_random)
            launch {
                remoteViews.onWidgetUpdate(context, it, extras?.getString(UPDATE_TYPE_KEY))
                appWidgetManager.updateAppWidget(it, remoteViews)
            }
        }
    }

    private suspend fun RemoteViews.onWidgetUpdate(context: Context?, id: Int, type: String?) {
        when (type) {
            UPDATE_REFRESH_KEY -> {
                setRefreshUI()
                appWidgetManager.updateAppWidget(id, this)
                delay(1_000)
                enqueueRefreshJob()
            }
            else -> setWidgetUI(context, id)
        }
    }

    private suspend fun RemoteViews.setWidgetUI(context: Context?, widgetId: Int) {
        Timber.d("onWidgetUpdate(widgetId: $widgetId)")
        val page = wikipediaRepository.getCachedPageRandomSummary()
        setTextViewText(R.id.app_name, page?.title)

        setOnClickPendingIntent(R.id.open_button, Intent(Intent.ACTION_VIEW).let {
            it.data = page?.contentUrls?.desktop?.page?.toUri()
            PendingIntent.getActivity(context, it.data.hashCode(), it, FLAG_UPDATE_CURRENT)
        })

        setOnClickPendingIntent(R.id.app_name, Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, 0, it, 0)
        })
        setOnClickPendingIntent(
            R.id.refresh_button, context?.buttonIntent(widgetId, UPDATE_REFRESH_KEY)
        )
        setOnClickPendingIntent(
            R.id.reload_button, context?.buttonIntent(widgetId, UPDATE_RELOAD_KEY)
        )
    }

    private fun RemoteViews.setRefreshUI() {
        setTextViewText(R.id.app_name, "Refreshing...")
    }

    private fun RemoteViews.enqueueRefreshJob() {
        // TODO: configure work manager
        setTextViewText(R.id.app_name, "Success")
    }

    private fun Context.buttonIntent(id: Int, updateType: String): PendingIntent {
        val intent = Intent(this, RandomWidgetProvider::class.java).apply {
            action = ACTION_APPWIDGET_UPDATE
            putExtra(UPDATE_TYPE_KEY, updateType)
            putExtra(WIDGET_ID_KEY, arrayListOf(id).toIntArray())
        }
        return PendingIntent.getBroadcast(this, intent.hashCode(), intent, FLAG_UPDATE_CURRENT)
    }
}
