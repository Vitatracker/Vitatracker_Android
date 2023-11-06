package app.mybad.notifier.di

import android.app.Application
import app.mybad.notifications.channel.NotificationInfoChannel
import app.mybad.notifications.channel.NotificationTrackerChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppModule : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationTrackerChannel.create(applicationContext)
        NotificationInfoChannel.create(applicationContext)
    }

}
