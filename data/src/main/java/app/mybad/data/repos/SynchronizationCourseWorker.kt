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
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
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
            if (true) {
                Log.d("VTTAG", "SynchronizationCourseWorker::doWork: Start")
                // отправим новое на бек
                sendCoursesToNetwork()
                // синхронизируем локальную базу и бек
                //syncCourses()
                Log.d("VTTAG", "SynchronizationCourseWorker::doWork: End")
            }
            Result.success()
        } catch (t: Throwable) {
            Log.d("VTTAG", "SynchronizationCourseWorker::doWork: Error", t)
            Result.retry()
        }
    }

    // сначала отправить все то, что есть локально и получить id с бека, потом получить все с бека и дополнить информацию.
    private suspend fun syncCourses() {
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: Start")
        syncRemediesFromNetwork()
        syncCoursesFromNetwork()
        syncUsagesFromNetwork()
        // проверить целостность локальной базы по id
        // remedies->courses->usages
        // courses->usages
        // usages
        // TODO("сделать проверку целостности базы")
        setInfo(REMEDY_CHANNEL_INFO_END)
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: End")
    }

    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    private suspend fun syncRemediesFromNetwork() {
        setInfo(REMEDY_CHANNEL_INFO_REMEDY)
        remedyNetworkRepository.getRemedies().onSuccess { remediesNet ->
            val remediesLoc = remedyRepository.getRemediesByUserId(AuthToken.userId).getOrThrow()
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::getCourses: remediesNet=${remediesNet.size} remediesLoc=${remediesLoc.size}"
            )
            // remedies которых нет на беке, после отправки их туда и
            // то, что нужно удалить, потому что были удалены удаленно
            // но проверять была ли отправка на бек idn > 0 и updateNetworkDate > 0
            val remediesOld = remediesNet.map { it.idn }.let { remediesNetIds ->
                remediesLoc.filter { remedy ->
                    remedy.idn > 0 && remedy.updateNetworkDate > 0 && remedy.idn !in remediesNetIds
                }
            }
            // удаляем старые записи remedies и нужно удалить courses и usages
            deleteRemediesLoc(remediesOld)
            // то что пришло с бека, но нет локально и ему еще нужно назначить id local
            val remediesNew = remediesLoc.map { it.idn }.let { remediesLocIds ->
                remediesNet.filter { remedyNet ->
                    remedyNet.idn !in remediesLocIds
                }
            }
            remediesNew.forEach { remedyNew ->
                // запишем в локальную базу и получим id, загрузим все курсы с remedyNew.idn
                remedyRepository.insertRemedy(remedyNew).onSuccess { remedyIdLoc ->
                    remedyIdLoc?.let {
                        syncCoursesFromNetwork(remedyIdNet = remedyNew.idn, remedyIdLoc = it)
                    }
                }
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }
    }

    private suspend fun syncCoursesFromNetwork(remedyIdNet: Long, remedyIdLoc: Long = 0) {
        courseNetworkRepository.getCoursesByRemedyId(
            remedyId = remedyIdNet,
            remedyIdLoc = remedyIdLoc,
        ).onSuccess { coursesNet ->
            // это новые курсы
            coursesNet.forEach { courseNet ->
                // запишем в локальную базу, тут уже подставлен remedyIdLoc и userIdLoc
                courseRepository.insertCourse(courseNet).onSuccess { courseIdLoc ->
                    courseIdLoc?.let {
                        // получим с бека usages для courseNet.idn
                        syncUsagesFromNetwork(
                            courseIdNet = courseNet.idn,
                            remedyIdLoc = remedyIdLoc,
                            courseIdLoc = it
                        )
                    }
                }.onFailure {
                    TODO("реализовать обработку ошибок")
                }
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }

    }

    // загрузить usagesNet и добавить в локальную базу
    private suspend fun syncUsagesFromNetwork(
        courseIdNet: Long,
        remedyIdLoc: Long = 0,
        courseIdLoc: Long = 0
    ) {
        usageNetworkRepository.getUsagesByCourseId(
            courseId = courseIdNet,
            remedyIdLoc = remedyIdLoc,
            courseIdLoc = courseIdLoc,
        ).onSuccess { usagesNet ->
            if (usagesNet.isNotEmpty()) usageRepository.insertUsage(usagesNet)
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }
    }

    private suspend fun syncCoursesFromNetwork() {
        courseNetworkRepository.getCourses().onSuccess { coursesNet ->
            // локальные курсы
            val coursesLoc = courseRepository.getCoursesByUserId(AuthToken.userId).getOrThrow()
            val coursesOld = coursesNet.map { it.idn }.let { coursesNetIds ->
                coursesLoc.filter { course ->
                    course.idn > 0L && course.updateNetworkDate > 0 && course.idn !in coursesNetIds
                }
            }
            deleteCourseLoc(coursesOld)
            // то что пришло с бека, но нет локально, проверить по idn и найти локальный id
            val coursesNew = coursesLoc.map { it.idn }.let { courseLocIdns ->
                coursesNet.filter { courseNew ->
                    courseNew.idn !in courseLocIdns
                }
            }
            coursesNew.forEach { courseNew ->
                remedyRepository.getRemedyByIdn(courseNew.idn).onSuccess { remedy ->
                    courseRepository.insertCourse(courseNew.copy(remedyId = remedy.id))
                        .onSuccess { courseIdLoc ->
                            courseIdLoc?.let {
                                syncUsagesFromNetwork(
                                    courseIdNet = courseNew.idn,
                                    remedyIdLoc = remedy.id,
                                    courseIdLoc = it,
                                )
                            }
                        }
                }
            }
        }
    }

    private suspend fun syncUsagesFromNetwork() {
        usageNetworkRepository.getUsages().onSuccess { usagesNet ->
            val usagesLoc = usageRepository.getUsagesByUserId(AuthToken.userId).getOrThrow()
            val usagesOld = usagesNet.map { it.idn }.let { usagesNetIds ->
                usagesLoc.filter { usage ->
                    usage.idn > 0 && usage.updateNetworkDate > 0 && usage.idn !in usagesNetIds
                }
            }
            if (usagesOld.isNotEmpty()) usageRepository.deleteUsages(usagesOld)
            // то что пришло с бека, но нет локально, проверить по idn и найти локальный id
            val usagesNew = usagesLoc.map { it.idn }.let { usageLocIdns ->
                usagesNet.filter { usageNet ->
                    usageNet.idn !in usageLocIdns
                }
            }
            usagesNew.forEach { usageNew ->
                courseRepository.getCourseByIdn(usageNew.idn).onSuccess { course ->
                    usageRepository.insertUsage(usageNew.copy(courseId = course.id))
                }
            }
        }
    }

    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    private suspend fun deleteRemediesLoc(remedies: List<RemedyDomainModel>) {
        if (remedies.isEmpty()) return
        remedies.forEach { remedy ->
            remedyRepository.deleteRemedyById(remedy.id)
            deleteCourseLoc(remedy.id)
        }
    }

    private suspend fun deleteCourseLoc(courses: List<CourseDomainModel>) {
        if (courses.isEmpty()) return
        courses.forEach { course ->
            courseRepository.deleteCourseById(course.id)
            deleteUsageLoc(course.id)
        }
    }

    private suspend fun deleteCourseLoc(remedyId: Long) {
        if (remedyId <= 0) return
        val courses = courseRepository.getCoursesByRemedyId(remedyId).getOrThrow()
        courses.forEach { course ->
            courseRepository.deleteCourseById(course.id)
            deleteUsageLoc(course.id)
        }

    }

    private suspend fun deleteUsageLoc(courseId: Long) {
        if (courseId <= 0) return
        usageRepository.getUsagesByCourseId(courseId).onSuccess { usages ->
            usageRepository.deleteUsages(usages)
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }
    }

    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    // отправить то что у нас локально еще не было отправлено, пройтись в цикле:
    // remedies->courses->usages,
    // courses->usages
    private suspend fun sendCoursesToNetwork() {
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: Start")
        // передать и получить id с бека: remedies->courses->usages
        sendRemedies()
        // передать и получить id с бека: courses->usages
        sendCourses()
        sendUsages()
        setInfo(REMEDY_CHANNEL_INFO_END)
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: End")
    }

    private suspend fun sendRemedies() {
        setInfo(REMEDY_CHANNEL_INFO_REMEDY)
        remedyRepository.getRemedyNotUpdateByUserId(AuthToken.userId).onSuccess { remedies ->
            Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: remedies=${remedies.size}")
            remedies.forEach { remedy ->
                Log.d(
                    "VTTAG",
                    "SynchronizationCourseWorker::syncCourses: remedyNetwork remedy=${remedy.id}"
                )
                remedyNetworkRepository.updateRemedy(remedy).onSuccess { updatedRemedy ->
                    remedyRepository.updateRemedy(updatedRemedy).onFailure {
                        TODO("реализовать обработку ошибок")
                    }
                    // передать и получить id с бека: courses->usages
                    sendCourses(updatedRemedy)

                }.onFailure {
                    TODO("реализовать обработку ошибок")
                }
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }
    }

    // курс без remedyId может быть, если remedies уже ранее оправлялся на бек
    private suspend fun sendCourses(remedy: RemedyDomainModel) {
        setInfo(REMEDY_CHANNEL_INFO_COURSE)
        val courses = courseRepository.getCoursesByRemedyId(remedy.id).getOrNull()
        Log.d(
            "VTTAG",
            "SynchronizationCourseWorker::syncCourses: remedyId=${remedy.id} courses=${courses?.size}"
        )
        courses?.forEach { course ->
            courseNetworkRepository.updateCourse(
                course.copy(
                    remedyIdn = remedy.idn,
                )
            ).onSuccess { updatedCourse ->
                courseRepository.updateCourse(updatedCourse).onFailure {
                    TODO("реализовать обработку ошибок")
                }
                sendUsages(updatedCourse)
            }.onFailure {
                TODO("реализовать обработку ошибок")
            }
        }
    }

    private suspend fun sendCourses() {
        setInfo(REMEDY_CHANNEL_INFO_COURSE)
        val courses = courseRepository.getCoursesNotUpdateByUserId(AuthToken.userId).getOrNull()
        Log.d(
            "VTTAG",
            "SynchronizationCourseWorker::syncCourses: courses=${courses?.size}"
        )
        courses?.forEach { course ->
            remedyRepository.getRemedyById(course.remedyId).onSuccess { remedy ->
                if (remedy.idn > 0) {
                    courseNetworkRepository.updateCourse(
                        course.copy(
                            remedyIdn = remedy.idn
                        )
                    ).onSuccess { updatedCourse ->
                        courseRepository.updateCourse(updatedCourse).onFailure {
                            TODO("реализовать обработку ошибок")
                        }
                        sendUsages(updatedCourse)
                    }.onFailure {
                        TODO("реализовать обработку ошибок")
                    }
                }
            }
        }
    }

    // usage без coursa не может быть
    private suspend fun sendUsages(course: CourseDomainModel) {
        setInfo(REMEDY_CHANNEL_INFO_USAGE)
        usageRepository.getUsagesByCourseId(course.id).onSuccess { usages ->
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::sendUsages: courseId=${course.id} usages=${usages.size}"
            )
            usages.forEach { usage ->
                usageNetworkRepository.updateUsage(
                    usage.copy(
                        courseIdn = course.idn,
                        remedyIdn = course.remedyIdn,
                    )
                ).onSuccess { updatedUsage ->
                    usageRepository.updateUsage(updatedUsage).onFailure {
                        TODO("реализовать обработку ошибок")
                    }
                }.onFailure {
                    TODO("реализовать обработку ошибок")
                }
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }
    }

    private suspend fun sendUsages() {
        setInfo(REMEDY_CHANNEL_INFO_USAGE)
        usageRepository.getUsagesNotUpdateByUserId(AuthToken.userId).onSuccess { usages ->
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::sendUsages: usages=${usages.size}"
            )
            usages.forEach { usage ->
                courseRepository.getCourseById(usage.courseId).onSuccess { course ->
                    if (course.remedyIdn > 0 && course.idn > 0) {
                        usageNetworkRepository.updateUsage(
                            usage.copy(
                                remedyIdn = course.remedyIdn,
                                courseIdn = course.idn,
                            )
                        ).onSuccess { updatedUsage ->
                            usageRepository.updateUsage(updatedUsage).onFailure {
                                TODO("реализовать обработку ошибок")
                            }
                        }.onFailure {
                            TODO("реализовать обработку ошибок")
                        }
                    }
                }
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
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
        private const val REMEDY_CHANNEL_INFO_REMEDY = "remedy"
        private const val REMEDY_CHANNEL_INFO_COURSE = "course"
        private const val REMEDY_CHANNEL_INFO_USAGE = "usage"
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
//                .setRequiresBatteryNotLow(true)
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
