package io.github.mklkj.androidappwidgetexample

import android.appwidget.AppWidgetManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAppWidgetManager(@ApplicationContext context: Context): AppWidgetManager =
        AppWidgetManager.getInstance(context)
}
