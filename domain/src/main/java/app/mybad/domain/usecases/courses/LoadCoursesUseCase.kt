package app.mybad.domain.usecases.courses

import android.util.Log
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LoadCoursesUseCase @Inject constructor(
    private val coursesRepo: CoursesRepo,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo,
) {

    operator fun invoke(
        userId: Long
    ): Flow<Triple<List<CourseDomainModel>, List<MedDomainModel>, List<UsageCommonDomainModel>>> =
        combine(
            coursesRepo.getAllFlow(userId),
            medsRepo.getAllFlow(userId),
            usagesRepo.getCommonAllFlow(userId),
            ::Triple
        ).onEach  {(courses, meds, usages) ->
            Log.w("VTTAG", "LoadCoursesUseCase: courses=${courses.size} meds=${meds.size} usages=${usages.size}")
        }

}
