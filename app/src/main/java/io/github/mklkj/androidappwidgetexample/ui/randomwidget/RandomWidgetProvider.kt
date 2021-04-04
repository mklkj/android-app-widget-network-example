package io.github.mklkj.androidappwidgetexample.ui.randomwidget

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
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

    companion object {
        private const val BUTTON_TYPE_KEY = "button_type_key"
        private const val BUTTON_REFRESH_KEY = "button_refresh_key"
        private const val BUTTON_RELOAD_KEY = "button_reload_key"
        private const val WIDGET_ID_KEY = "widget_id_key"

        private const val WIDGET_ID_DEFAULT = -1
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val widgetId = intent?.getIntExtra(WIDGET_ID_KEY, WIDGET_ID_DEFAULT)
        if (widgetId != WIDGET_ID_DEFAULT && intent?.action == ACTION_APPWIDGET_UPDATE) {
            launch {
                val remoteViews = RemoteViews(context?.packageName, R.layout.appwidget_random)
                remoteViews.onWidgetUpdate(context, widgetId!!)
                AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews)
            }
        }
    }

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
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
            R.id.refresh_button, context?.buttonIntent(0, widgetId, BUTTON_REFRESH_KEY)
        )
        setOnClickPendingIntent(
            R.id.reload_button, context?.buttonIntent(0, widgetId, BUTTON_RELOAD_KEY)
        )
    }

    private fun Context.buttonIntent(code: Int, id: Int, buttonType: String): PendingIntent {
        return PendingIntent.getBroadcast(
            this, code,
            Intent(this, RandomWidgetProvider::class.java).apply {
                action = ACTION_APPWIDGET_UPDATE
                putExtra(BUTTON_TYPE_KEY, buttonType)
                putExtra(WIDGET_ID_KEY, id)
            }, FLAG_UPDATE_CURRENT
        )
    }
}
