package app.mybad.data.repos

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import app.mybad.domain.usecases.courses.SynchronizationCourseUseCase
import app.mybad.utils.currentDateTimeInSecond
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Named

@HiltWorker
class SynchronizationCourseWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val synchronizationCourseUseCase: SynchronizationCourseUseCase,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, params) {

    private val notificationBuilder by lazy {
        NotificationCompat.Builder(appContext, REMEDY_CHANNEL_ID)
            .setContentTitle(REMEDY_CHANNEL_TITLE)
            .setGroup(REMEDY_CHANNEL_GROUP)
            .setGroupSummary(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSilent(true)
            .setSmallIcon(android.R.drawable.ic_popup_sync)
    }

    override suspend fun doWork(): Result = withContext(dispatcher) {
        log("doWork: Start")
        synchronizationCourseUseCase(currentDateTimeInSecond()) { info ->
            setInfo(info)
        }.onSuccess {
            return@withContext Result.success()
        }.onFailure {
            log("doWork: Error", it)
            return@withContext Result.retry()
        }
        return@withContext Result.success()
    }

    private suspend fun setInfo(progress: String) {
        setForeground(createForegroundInfo("Synchronization: $progress"))
    }

    private fun createForegroundInfo(progress: String) =
        ForegroundInfo(NOTIFICATION_ID, getNotification(progress))

    private fun getNotification(progress: String) = notificationBuilder
        .setContentText(progress)
        .addAction(android.R.drawable.ic_delete, "Cansel", getIntent())
        .build()

    private fun getIntent() = WorkManager
        .getInstance(appContext)
        .createCancelPendingIntent(id)

    companion object {
        const val NOTIFICATION_ID = 145103
        const val REMEDY_CHANNEL_ID = "remedy_content"
        const val REMEDY_CHANNEL_NAME = "Notifications from Vitatracker reminder"
        const val REMEDY_CHANNEL_DESCRIPTION = "Vitatracker synchronize show"
        const val REMEDY_CHANNEL_TITLE = "Vitatracker synchronize"
        const val REMEDY_CHANNEL_GROUP = "Vitatracker"

        private const val WORK_NAME = "work_synchronization_remedy"
        private const val WORK_TAG = "work_synchronization_remedy_tag"
        private const val WORK_REPEAT_TIME = 40L
        private const val WORK_FLEX_TIME = 20L
        private const val WORK_INITIAL_TIME = 30L
        private const val WORK_DELAY_TIME = 60L

        fun createNotificationChannel(context: Context) {

            val channel = NotificationChannel(
                REMEDY_CHANNEL_ID,
                REMEDY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = REMEDY_CHANNEL_DESCRIPTION
            }
            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }

        fun WorkManager.start() {
            log("WorkManager.start: Start")
            this.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                createPeriodicWorkRequest()
            )
            log("WorkManager.start: End")
        }

        fun WorkManager.cancel() {
            log("WorkManager.cancel")
            this.cancelUniqueWork(WORK_NAME)
            this.cancelAllWorkByTag(WORK_TAG)
        }

        private fun createPeriodicWorkRequest(): PeriodicWorkRequest {
            log("createPeriodicWorkRequest: Start")
            // критерии запуска синхронизации
            val workConstraints = Constraints.Builder()
                // наличие интернета
                //CONNECTED — WiFi или Mobile Data
                //UNMETERD — только WiFi
                //METERED — только Mobile Data
                //NOT_ROAMING — интернет не должен быть роуминговым;
                //NOT_REQUIRED — интернет не нужен.
                .setRequiredNetworkType(NetworkType.CONNECTED)
                // батарея заряжена
//                .setRequiresBatteryNotLow(true)
                // есть место на диске
//                .setRequiresStorageNotLow(true)
                .build()

            val workRequest = PeriodicWorkRequest.Builder(
                SynchronizationCourseWorker::class.java,
                WORK_REPEAT_TIME,
                TimeUnit.MINUTES,
                WORK_FLEX_TIME,
                TimeUnit.MINUTES,
            )
                .setConstraints(workConstraints)
                .addTag(WORK_TAG)
                .setInitialDelay(WORK_INITIAL_TIME, TimeUnit.SECONDS)
                .apply {
                    setBackoffCriteria(BackoffPolicy.LINEAR, WORK_DELAY_TIME, TimeUnit.SECONDS)
                }
                .build()

            log("createPeriodicWorkRequest: End")
            return workRequest
        }

        private fun log(text: String, error: Throwable? = null) {
            Log.d("VTTAG", "SynchronizationCourseWorker::$text", error)
        }
    }

}
