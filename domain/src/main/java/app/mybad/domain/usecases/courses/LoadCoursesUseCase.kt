package app.mybad.domain.usecases.courses

import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class LoadCoursesUseCase @Inject constructor(
    private val medsRepo: MedsRepo,
    private val coursesRepo: CoursesRepo,
    private val usagesRepo: UsagesRepo,
) {

    suspend fun getMedsFlow(userId: Long) = medsRepo.getAllFlow(userId)
    suspend fun getCoursesFlow(userId: Long) = coursesRepo.getAllFlow(userId)
    suspend fun getUsagesFlow(userId: Long) = usagesRepo.getCommonAllFlow(userId)
}
