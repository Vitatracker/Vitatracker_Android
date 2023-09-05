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
    suspend operator fun invoke(userId: Long) {
        log("sendCoursesToNetwork: Start")
        if (userId != AuthToken.userId) return
        // передать и получить id с бека: remedies->courses->usages
        sendRemedies(userId)
        // передать и получить id с бека: courses->usages
        sendCourses(userId)
        // передать и получить id с бека: usages
        sendUsages(userId)
        log("sendCoursesToNetwork: End")
    }

    private suspend fun sendRemedies(userId: Long) {
        remedyRepository.getRemedyNotUpdateByUserId(userId).onSuccess { remedies ->
            log("sendRemedies: remedies=${remedies.size}")
            remedies.forEach { remedy ->
                log("sendRemedies: remedyNetwork remedy=${remedy.id} ${remedy.idn}")
                remedyNetworkRepository.updateRemedy(remedy).onSuccess { updatedRemedy ->
                    log("sendCourses: to net ok remedy update=${updatedRemedy.updateNetworkDate}")
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
        courseRepository.getCoursesByRemedyId(remedy.id).onSuccess { courses ->
            log("sendCourses: remedyId=${remedy.id} courses=${courses.size}")
            courses.forEach { course ->
                courseNetworkRepository.updateCourse(
                    course.copy(
                        remedyIdn = remedy.idn,
                    )
                ).onSuccess { updatedCourse ->
                    log("sendCourses: to net ok courses update=${updatedCourse.updateNetworkDate}")
                    courseRepository.updateCourse(updatedCourse).onFailure {
                        TODO("реализовать обработку ошибок")
                    }
                    sendUsages(updatedCourse)
                }.onFailure {
//                TODO("реализовать обработку ошибок")
                }
            }
        }.onFailure {
//                TODO("реализовать обработку ошибок")
        }
    }


    private suspend fun sendCourses(userId: Long) {
        courseRepository.getCoursesNotUpdateByUserId(userId).onSuccess { courses ->
            log("sendCourses: courses=${courses.size}")
            courses.forEach { course ->
                remedyRepository.getRemedyById(course.remedyId).onSuccess { remedy ->
                    if (remedy.idn > 0) {
                        courseNetworkRepository.updateCourse(
                            course.copy(
                                remedyIdn = remedy.idn
                            )
                        ).onSuccess { updatedCourse ->
                            log("sendCourses: to net ok courses update=${updatedCourse.updateNetworkDate}")
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
        }.onFailure {
//                        TODO("реализовать обработку ошибок")
        }
    }

    // usage без coursa не может быть
    private suspend fun sendUsages(course: CourseDomainModel) {
        usageRepository.getUsagesByCourseId(course.id).onSuccess { usages ->
            log("sendUsages: courseId=${course.id} usages=${usages.size}")
            usages.forEach { usage ->
                usageNetworkRepository.updateUsage(
                    usage.copy(
                        courseIdn = course.idn,
                        remedyIdn = course.remedyIdn,
                    )
                ).onSuccess { updatedUsage ->
                    log("sendCourses: to net ok usage update=${updatedUsage.updateNetworkDate}")
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

    private suspend fun sendUsages(userId: Long) {
        usageRepository.getUsagesNotUpdateByUserId(userId).onSuccess { usages ->
            log("sendUsages: usages=${usages.size}")
            usages.forEach { usage ->
                courseRepository.getCourseById(usage.courseId).onSuccess { course ->
                    if (course.remedyIdn > 0 && course.idn > 0) {
                        usageNetworkRepository.updateUsage(
                            usage.copy(
                                remedyIdn = course.remedyIdn,
                                courseIdn = course.idn,
                            )
                        ).onSuccess { updatedUsage ->
                            log("sendCourses: to net ok usage update=${updatedUsage.updateNetworkDate}")
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

    private fun log(text: String) {
        Log.d("VTTAG", "SendCoursesToNetworkUseCase::$text")
    }

}
