package io.github.mklkj.androidappwidgetexample.ui.modules.randomwidget

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import io.github.mklkj.androidappwidgetexample.R
import io.github.mklkj.androidappwidgetexample.data.repository.WikipediaRepository
import io.github.mklkj.androidappwidgetexample.toPrintableString
import io.github.mklkj.androidappwidgetexample.ui.MainActivity
import io.github.mklkj.androidappwidgetexample.ui.base.BaseAppWidgetProvider
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RandomWidgetProvider : BaseAppWidgetProvider() {

    @Inject
    lateinit var wikipediaRepository: WikipediaRepository

    companion object {
        private const val BUTTON_TYPE_KEY = "button_type_key"
        private const val BUTTON_REFRESH_KEY = "button_refresh_key"
        private const val BUTTON_RELOAD_KEY = "button_reload_key"
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray, extras: Bundle?
    ) {
        appWidgetIds.forEach {
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_random)
            launch {
                remoteViews.onWidgetUpdate(context, it)
                appWidgetManager.updateAppWidget(it, remoteViews)
            }
        }
    }

    private suspend fun RemoteViews.onWidgetUpdate(context: Context?, widgetId: Int) {
        Timber.d("onWidgetUpdate(widgetId: $widgetId)")
        val page = wikipediaRepository.getCachedPageRandomSummary()
        setTextViewText(R.id.app_name, page?.title)

        setOnClickPendingIntent(R.id.open_button, Intent(Intent.ACTION_VIEW).let {
            it.data = page?.contentUrls?.desktop?.page?.toUri()
            PendingIntent.getActivity(context, 0, it, FLAG_CANCEL_CURRENT)
        })

        setOnClickPendingIntent(R.id.app_name, Intent(context, MainActivity::class.java).let {
            PendingIntent.getActivity(context, 0, it, 0)
        })
        setOnClickPendingIntent(
            R.id.refresh_button, context?.buttonIntent(widgetId, BUTTON_REFRESH_KEY)
        )
        setOnClickPendingIntent(
            R.id.reload_button, context?.buttonIntent(widgetId, BUTTON_RELOAD_KEY)
        )
    }

    private fun Context.buttonIntent(id: Int, buttonType: String): PendingIntent {
        val intent = Intent(this, RandomWidgetProvider::class.java).apply {
            action = ACTION_APPWIDGET_UPDATE
            putExtra(BUTTON_TYPE_KEY, buttonType)
            putExtra(WIDGET_ID_KEY, arrayListOf(id).toIntArray())
        }
        return PendingIntent.getBroadcast(this, intent.hashCode(), intent, FLAG_UPDATE_CURRENT)
    }
}
