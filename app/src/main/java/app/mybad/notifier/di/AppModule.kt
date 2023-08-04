package app.mybad.notifier.di

import android.app.Application
import app.mybad.data.repos.SynchronizationCourseWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppModule : Application() {

    override fun onCreate() {
        super.onCreate()
        SynchronizationCourseWorker.createNotificationChannel(this)
    }

}
