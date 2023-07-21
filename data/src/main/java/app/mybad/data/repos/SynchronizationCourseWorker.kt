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
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.network.CourseNetworkRepository
import app.mybad.domain.repository.network.RemedyNetworkRepository
import app.mybad.domain.repository.network.UsageNetworkRepository
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
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val remedyNetworkRepository: RemedyNetworkRepository,
    private val courseNetworkRepository: CourseNetworkRepository,
    private val usageNetworkRepository: UsageNetworkRepository,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, params) {

    private val builder by lazy {
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
            create(appContext)
            syncCourses { progress ->
                setForeground(createForegroundInfo("Synchronization: $progress"))
            }
            getCourses { progress ->
                setForeground(createForegroundInfo("Synchronization: $progress"))
            }
            Log.d("VTTAG", "SynchronizationCourseWorker::doWork: End")
            Result.success()
        } catch (t: Throwable) {
            Log.d("VTTAG", "SynchronizationCourseWorker::doWork: Error", t)
            Result.retry()
        }
    }

    private fun createForegroundInfo(progress: String) =
        ForegroundInfo(NOTIFICATION_ID, getNotification(progress))

    private fun getNotification(progress: String) = builder
        .setContentText(progress)
        .addAction(android.R.drawable.ic_delete, "Cansel", getIntent())
        .build()

    private fun getIntent() = WorkManager
        .getInstance(appContext)
        .createCancelPendingIntent(id)

    // получить все с бека, нужно ли это?
    private suspend fun getCourses(callback: suspend (progress: String) -> Unit) {
        Log.d("VTTAG", "SynchronizationCourseWorker::getCourses: Start")
        // получить remedy
        callback(REMEDY_CHANNEL_INFO_REMEDY)
        val remediesNet = remedyNetworkRepository.getRemedies().getOrThrow()
        val remedies = remedyRepository.getRemediesByUserId(AuthToken.userId).getOrThrow()
        Log.d("VTTAG", "SynchronizationCourseWorker::getCourses: remediesNet=${remediesNet.size}")
        // сравнить и удалить старое и добавить новое
        remediesNet.forEach { remedy ->
            remedyRepository.deleteRemedyById(remedy.id)
            remedyRepository.insertRemedy(remedy)
            // получить courses
            callback(REMEDY_CHANNEL_INFO_COURSE)
            val coursesNet = courseNetworkRepository.getCoursesByRemedyId(remedy.id).getOrThrow()
            Log.d("VTTAG", "SynchronizationCourseWorker::getCourses: coursesNet=${coursesNet.size}")
            coursesNet.forEach { course ->
                courseRepository.deleteCourseById(course.id)
                courseRepository.insertCourse(course)
                // получить usage
                callback(REMEDY_CHANNEL_INFO_USAGE)
                val usagesNet = usageNetworkRepository.getUsagesByCourseId(course.id).getOrThrow()
                Log.d(
                    "VTTAG",
                    "SynchronizationCourseWorker::getCourses: usagesNet=${usagesNet.size}"
                )
                usagesNet.forEach { usage ->
                    usageRepository.deleteUsagesById(usage.id)
                    usageRepository.insertUsage(usage)
                }
            }
        }
        callback(REMEDY_CHANNEL_INFO_END)
        Log.d("VTTAG", "SynchronizationCourseWorker::getCourses: End")
    }

    // отправить то что у нас локально еще не было отправлено
    private suspend fun syncCourses(callback: suspend (progress: String) -> Unit) {
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: Start")
        // передать remedy
        callback(REMEDY_CHANNEL_INFO_REMEDY)
        val remedies = remedyRepository.getRemedyNotUpdateByUserId(AuthToken.userId).getOrThrow()
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: remedies=${remedies.size}")
        remedies.forEach { remedy ->
            val updatedRemedy = remedyNetworkRepository.updateRemedy(remedy)
            remedyRepository.updateRemedy(updatedRemedy)
            val courses = courseRepository.getCoursesByRemedyId(updatedRemedy.id).getOrThrow()
            callback(REMEDY_CHANNEL_INFO_COURSE)
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::syncCourses: remedyId=${remedy.id} courses=${courses.size}"
            )
            courses.forEach { course ->
                val updatedCourse = courseNetworkRepository.updateCourse(course).getOrThrow()
                courseRepository.updateCourse(updatedCourse)
                val usages = usageRepository.getUsagesByCourseId(updatedCourse.id).getOrThrow()
                Log.d(
                    "VTTAG",
                    "SynchronizationCourseWorker::syncCourses: remedyId=${remedy.id} courseId=${course.id} usages=${usages.size}"
                )
                callback(REMEDY_CHANNEL_INFO_USAGE)
                usages.forEach { usage ->
                    val updatedUsage = usageNetworkRepository.updateUsage(usage)
                    usageRepository.updateUsage(updatedUsage)
                }
            }
        }
        callback(REMEDY_CHANNEL_INFO_END)
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: End")
    }

    companion object {
        const val NOTIFICATION_ID = 145103
        const val REMEDY_CHANNEL_ID = "remedy_content"
        const val REMEDY_CHANNEL_NAME = "Notifications from Vitatracker reminder"
        const val REMEDY_CHANNEL_DESCRIPTION = "Vitatracker synchronize show"
        const val REMEDY_CHANNEL_TITLE = "Vitatracker synchronize"
        const val REMEDY_CHANNEL_GROUP = "Vitatracker"
        private const val REMEDY_CHANNEL_INFO_REMEDY = "remedy"
        private const val REMEDY_CHANNEL_INFO_COURSE = "course"
        private const val REMEDY_CHANNEL_INFO_USAGE = "usage"
        private const val REMEDY_CHANNEL_INFO_END = "end"

        fun create(context: Context) {

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
        }

        private fun createWorkRequest(): OneTimeWorkRequest {
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::startSynchronization: Start"
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
                // батарея не разряжена
                .setRequiresBatteryNotLow(true)
                // есть место на диске
                .setRequiresStorageNotLow(true)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<SynchronizationCourseWorker>()
//                .setInputData(workData)
                .apply {
                    setBackoffCriteria(BackoffPolicy.LINEAR, 60, TimeUnit.SECONDS)
                }
                .setConstraints(workConstraints)
                .build()

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
