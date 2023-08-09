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
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import app.mybad.domain.usecases.courses.CheckCoursesLocalUseCase
import app.mybad.domain.usecases.courses.SendCoursesDeletedToNetworkUseCase
import app.mybad.domain.usecases.courses.SendCoursesToNetworkUseCase
import app.mybad.domain.usecases.courses.SyncCoursesWithNetworkUseCase
import app.mybad.domain.usecases.user.RefreshAuthTokenUseCase
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
    private val refreshAuthTokenUseCase: RefreshAuthTokenUseCase,
    private val sendCoursesDeletedToNetworkUseCase: SendCoursesDeletedToNetworkUseCase,
    private val sendCoursesToNetworkUseCase: SendCoursesToNetworkUseCase,
    private val syncCoursesWithNetworkUseCase: SyncCoursesWithNetworkUseCase,
    private val checkCoursesLocalUseCase: CheckCoursesLocalUseCase,
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
        try {
            Log.d("VTTAG", "SynchronizationCourseWorker::doWork: Start")
            // проверим токен на окончание и если нужно обновить
            if (refreshAuthTokenUseCase(currentDateTimeInSecond())) {
                // удалим, что удалено в локальной базе
                setInfo(REMEDY_CHANNEL_INFO_DELETED)
                sendCoursesDeletedToNetworkUseCase()
                // отправим новое на бек
                setInfo(REMEDY_CHANNEL_INFO_COURSES)
                sendCoursesToNetworkUseCase()
                // синхронизируем локальную базу и бек
                setInfo(REMEDY_CHANNEL_INFO_SYNC)
                syncCoursesWithNetworkUseCase()
                // проверить целостность базы локальной
                setInfo(REMEDY_CHANNEL_INFO_CHECK)
                checkCoursesLocalUseCase()
                setInfo(REMEDY_CHANNEL_INFO_END)
                Log.d("VTTAG", "SynchronizationCourseWorker::doWork: End")
            }
            Result.success()
        } catch (t: Error) {
            Log.d("VTTAG", "SynchronizationCourseWorker::doWork: Error", t)
            Result.retry()
        }
    }

    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
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
        private const val REMEDY_CHANNEL_INFO_COURSES = "courses"
        private const val REMEDY_CHANNEL_INFO_SYNC = "synchronization"
        private const val REMEDY_CHANNEL_INFO_CHECK = "check"
        private const val REMEDY_CHANNEL_INFO_DELETED = "deleted"
        private const val REMEDY_CHANNEL_INFO_END = "end"

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
            Log.d("VTTAG", "SynchronizationCourseWorker::WorkManager.start: Start")
            this.enqueueUniqueWork(
                REMEDY_CHANNEL_ID,
                ExistingWorkPolicy.KEEP,
                createWorkRequest()
            )
            Log.d("VTTAG", "SynchronizationCourseWorker::WorkManager.start: End")
        }

        private fun createWorkRequest(): OneTimeWorkRequest {
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::createWorkRequest: Start"
            )
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
                .setRequiresBatteryNotLow(true)
                // есть место на диске
//                .setRequiresStorageNotLow(true)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<SynchronizationCourseWorker>()
//                .setInputData(workData)
                .apply {
                    setBackoffCriteria(BackoffPolicy.LINEAR, 60, TimeUnit.SECONDS)
                }
                .setConstraints(workConstraints)
                .build()

            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::startSynchronization: End"
            )
            return workRequest
            /*
            //    private val workManager = WorkManager.getInstance(context)

                        workManager.enqueueUniqueWork(
                            SynchronizationCourseWorker.REMEDY_CHANNEL_ID,
                            ExistingWorkPolicy.KEEP,
                            workRequest
                        )
            */

        }
    }

}
