package app.mybad.data.repos

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.repos.CoursesRepo

class CoursesRepoImpl : CoursesRepo {

    override fun getAll(): List<CourseDomainModel> {
        TODO("Not yet implemented")
    }

    override fun getSingle(courseId: Long): CourseDomainModel {
        TODO("Not yet implemented")
    }

    override fun updateSingle(courseId: Long, item: CourseDomainModel) {
        TODO("Not yet implemented")
    }

    override fun deleteSingle(courseId: Long) {
        TODO("Not yet implemented")
    }
}