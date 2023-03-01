package app.mybad.notifier.di

import android.app.AlarmManager
import android.content.Context
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.notifications.NotificationsScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationsModule {

    @Provides
    @Singleton
    fun providesAlarmManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    @Singleton
    fun providesNotificationScheduler(
        @ApplicationContext context: Context,
        usagesRepo: UsagesRepo,
        medsRepo: MedsRepo
    ) = NotificationsScheduler(
            context = context,
            usagesRepo = usagesRepo,
            medsRepo = medsRepo
        )

}