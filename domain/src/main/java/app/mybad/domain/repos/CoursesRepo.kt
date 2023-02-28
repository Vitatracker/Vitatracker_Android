package app.mybad.domain.repos

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import kotlinx.coroutines.flow.Flow

interface CoursesRepo {
    suspend fun getAll() : List<CourseDomainModel>
    suspend fun getAllFlow() : Flow<List<CourseDomainModel>>
    suspend fun getSingle(courseId: Long) : CourseDomainModel
    suspend fun updateSingle(courseId: Long, item: CourseDomainModel)
    suspend fun deleteSingle(courseId: Long)
    suspend fun add(item: CourseDomainModel)
}