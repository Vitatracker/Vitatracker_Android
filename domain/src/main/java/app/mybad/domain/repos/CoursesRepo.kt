package app.mybad.domain.repos

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel

interface CoursesRepo {
    fun getAll() : List<CourseDomainModel>
    fun getSingle(courseId: Long) : CourseDomainModel
    fun updateSingle(courseId: Long, item: CourseDomainModel)
    fun deleteSingle(courseId: Long)
}