package app.mybad.domain.usecases.courses

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.network.CourseNetworkRepository
import app.mybad.domain.repository.network.RemedyNetworkRepository
import app.mybad.domain.repository.network.UsageNetworkRepository
import javax.inject.Inject

class SendCoursesToNetworkUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val remedyNetworkRepository: RemedyNetworkRepository,
    private val courseNetworkRepository: CourseNetworkRepository,
    private val usageNetworkRepository: UsageNetworkRepository,

    ) {
    /*
    //   отправить то что у нас локально еще не было отправлено, пройтись в цикле:
    //   remedies->courses->usages,
    //   courses->usages
     */
    suspend operator fun invoke() {
        Log.d("VTTAG", "SynchronizationCourseWorker::sendCoursesToNetwork: Start")
        // передать и получить id с бека: remedies->courses->usages
        sendRemedies()
        // передать и получить id с бека: courses->usages
        sendCourses()
        // передать и получить id с бека: usages
        sendUsages()
        Log.d("VTTAG", "SynchronizationCourseWorker::sendCoursesToNetwork: End")
    }

    private suspend fun sendRemedies() {
        remedyRepository.getRemedyNotUpdateByUserId(AuthToken.userId).onSuccess { remedies ->
            Log.d("VTTAG", "SynchronizationCourseWorker::sendRemedies: remedies=${remedies.size}")
            remedies.forEach { remedy ->
                Log.d(
                    "VTTAG",
                    "SynchronizationCourseWorker::sendRemedies: remedyNetwork remedy=${remedy.id}"
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
        val courses = courseRepository.getCoursesByRemedyId(remedy.id).getOrNull()
        Log.d(
            "VTTAG",
            "SynchronizationCourseWorker::sendCourses: remedyId=${remedy.id} courses=${courses?.size}"
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
//                TODO("реализовать обработку ошибок")
            }
        }
    }

    private suspend fun sendCourses() {
        val courses = courseRepository.getCoursesNotUpdateByUserId(AuthToken.userId).getOrNull()
        Log.d(
            "VTTAG",
            "SynchronizationCourseWorker::sendCourses: courses=${courses?.size}"
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
//                        TODO("реализовать обработку ошибок")
                    }
                }
            }
        }
    }

    // usage без coursa не может быть
    private suspend fun sendUsages(course: CourseDomainModel) {
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
}
