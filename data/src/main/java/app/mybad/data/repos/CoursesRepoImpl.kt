package app.mybad.data.repos

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.repos.CoursesRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoursesRepoImpl @Inject constructor() : CoursesRepo {

    override fun getAll(): List<CourseDomainModel> {
        return listOf(
            CourseDomainModel(id=1L, medId = 1L, startDate = 0L, endDate = 11000000L),
            CourseDomainModel(id=2L, medId = 2L, startDate = 0L, endDate = 12000000L),
            CourseDomainModel(id=3L, medId = 3L, startDate = 0L, endDate = 13000000L),
        )
    }

    override fun getSingle(courseId: Long): CourseDomainModel {
        return CourseDomainModel()
    }

    override fun updateSingle(courseId: Long, item: CourseDomainModel) {
    }

    override fun deleteSingle(courseId: Long) {
    }
}