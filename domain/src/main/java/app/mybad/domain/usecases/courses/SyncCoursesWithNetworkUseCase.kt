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

class SyncCoursesWithNetworkUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val remedyNetworkRepository: RemedyNetworkRepository,
    private val courseNetworkRepository: CourseNetworkRepository,
    private val usageNetworkRepository: UsageNetworkRepository,

    ) {
    suspend operator fun invoke(userId: Long) {
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: Start")
        if (userId != AuthToken.userId) return
        // сначала отправить все то, что есть локально и получить id с бека, потом получить все с бека и дополнить информацию.
        syncRemediesFromNetwork(userId)
        syncCoursesFromNetwork(userId)
        syncUsagesFromNetwork(userId)
        Log.d("VTTAG", "SynchronizationCourseWorker::syncCourses: End")
    }

    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    private suspend fun syncRemediesFromNetwork(userId: Long) {
        remedyNetworkRepository.getRemedies().onSuccess { remediesNet ->
            val remediesLoc = remedyRepository.getRemediesByUserId(userId).getOrThrow()
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::getCourses: remediesNet=${remediesNet.size} remediesLoc=${remediesLoc.size}"
            )
            var remediesNew: List<RemedyDomainModel> = emptyList()
            if (remediesLoc.isNotEmpty()) {
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
                remediesNew = remediesLoc.map { it.idn }.let { remediesLocIds ->
                    remediesNet.filter { remedyNet ->
                        remedyNet.idn !in remediesLocIds
                    }
                }
            } else remediesNew = remediesNet
            remediesNew.forEach { remedyNew ->
                // запишем в локальную базу и получим id, загрузим все курсы с remedyNew.idn
                remedyRepository.insertRemedy(remedyNew).onSuccess { remedyIdLoc ->
                    syncCoursesFromNetwork(remedyIdNet = remedyNew.idn, remedyIdLoc = remedyIdLoc)
                }
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }
    }

    // загрузить usagesNet и добавить в локальную базу
    private suspend fun syncUsagesFromNetwork(
        courseIdNet: Long,
        courseIdLoc: Long = 0,
        remedyIdLoc: Long = 0,
        remedyIdNet: Long = 0,
    ) {
        usageNetworkRepository.getUsagesByCourseId(
            courseId = courseIdNet,
            courseIdLoc = courseIdLoc,
            remedyIdNet = remedyIdNet,
            remedyIdLoc = remedyIdLoc,
        ).onSuccess { usagesNet ->
            if (usagesNet.isNotEmpty()) usageRepository.insertUsage(usagesNet)
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }
    }

    private suspend fun syncCoursesFromNetwork(remedyIdNet: Long, remedyIdLoc: Long = 0) {
        courseNetworkRepository.getCoursesByRemedyId(
            remedyId = remedyIdNet,
            remedyIdLoc = remedyIdLoc,
        ).onSuccess { coursesNet ->
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::syncCoursesFromNetwork: remedies idn=${remedyIdNet} id=${remedyIdLoc} size=${coursesNet.size}"
            )
            // это новые курсы
            coursesNet.forEach { courseNet ->
                Log.d(
                    "VTTAG",
                    "SynchronizationCourseWorker::syncCoursesFromNetwork: course idn=${courseNet.idn} id=${courseNet.id}"
                )
                // запишем в локальную базу, тут уже подставлен remedyIdLoc и userIdLoc
                courseRepository.insertCourse(courseNet).onSuccess { courseIdLoc ->
                    // получим с бека usages для courseNet.idn
                    syncUsagesFromNetwork(
                        courseIdNet = courseNet.idn,
                        courseIdLoc = courseIdLoc,
                        remedyIdNet = courseNet.remedyIdn,
                        remedyIdLoc = remedyIdLoc,
                    )
                }.onFailure {
                    TODO("реализовать обработку ошибок")
                }
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }

    }

    private suspend fun syncCoursesFromNetwork(userId: Long) {
        courseNetworkRepository.getCourses().onSuccess { coursesNet ->
            // локальные курсы
            val coursesLoc = courseRepository.getCoursesByUserId(userId).getOrThrow()

            var coursesNew: List<CourseDomainModel> = emptyList()
            if (coursesLoc.isNotEmpty()) {
                val coursesOld = coursesNet.map { it.idn }.let { coursesNetIds ->
                    coursesLoc.filter { course ->
                        course.idn > 0L && course.updateNetworkDate > 0 && course.idn !in coursesNetIds
                    }
                }
                deleteCourseLoc(coursesOld)
                // то что пришло с бека, но нет локально, проверить по idn и найти локальный id
                coursesNew = coursesLoc.map { it.idn }.let { courseLocIdns ->
                    coursesNet.filter { courseNew ->
                        courseNew.idn !in courseLocIdns
                    }
                }
            } else coursesNew = coursesNet
            coursesNew.forEach { courseNew ->
                remedyRepository.getRemedyByIdn(courseNew.idn).onSuccess { remedy ->
                    courseRepository.insertCourse(courseNew.copy(remedyId = remedy.id))
                        .onSuccess { courseIdLoc ->
                            syncUsagesFromNetwork(
                                courseIdNet = courseNew.idn,
                                remedyIdLoc = remedy.id,
                                courseIdLoc = courseIdLoc,
                            )
                        }
                }
            }
        }
    }

    private suspend fun syncUsagesFromNetwork(userId: Long) {
        usageNetworkRepository.getUsages().onSuccess { usagesNet ->
            val usagesLoc = usageRepository.getUsagesByUserId(userId).getOrThrow()
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
            deleteCourseLoc(remedy.id)
            remedyRepository.deleteRemedyById(remedy.id)
        }
    }

    private suspend fun deleteCourseLoc(courses: List<CourseDomainModel>) {
        if (courses.isEmpty()) return
        courses.forEach { course ->
            deleteUsageLoc(course.id)
            courseRepository.deleteCoursesById(course.id)
        }
    }

    private suspend fun deleteCourseLoc(remedyId: Long) {
        if (remedyId <= 0) return
        courseRepository.getCoursesByRemedyId(remedyId).onSuccess { courses ->
            courses.forEach { course ->
                deleteUsageLoc(course.id)
                courseRepository.deleteCoursesById(course.id)
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
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
}
