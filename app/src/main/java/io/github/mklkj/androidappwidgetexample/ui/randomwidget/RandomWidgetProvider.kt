package io.github.mklkj.androidappwidgetexample.ui.randomwidget

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import io.github.mklkj.androidappwidgetexample.R
import io.github.mklkj.androidappwidgetexample.ui.MainActivity

class RandomWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach {
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_random)
            remoteViews.onWidgetUpdate(context)
            appWidgetManager.updateAppWidget(it, remoteViews)
        }
    }

    private fun RemoteViews.onWidgetUpdate(context: Context) {
        // open app
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
        // todo: open in browser
        setOnClickPendingIntent(R.id.open_button, Intent(Intent.ACTION_VIEW).let {
            it.data = "https://google.com".toUri()
            PendingIntent.getActivity(context, 0, it, FLAG_CANCEL_CURRENT)
        })
    }
}
