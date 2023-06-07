package app.mybad.domain.usecases.usages

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.UsagesRepo
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.absoluteValue

class UpdateAllUsagesInCourseUseCase @Inject constructor(
    private val usagesRepo: UsagesRepo
) {

    suspend operator fun invoke(usagesPattern: List<Pair<Long, Int>>, med: MedDomainModel, course: CourseDomainModel) {
        usagesRepo.deleteUsagesByMedId(med.id)
        val usages = generateCommonUsages(
            usagesByDay = usagesPattern,
            medId = med.id,
            userId = med.userId,
            startDate = course.startDate,
            endDate = course.endDate,
            regime = course.regime
        )
        usagesRepo.addUsages(usages)
    }

    private fun generateCommonUsages(
        usagesByDay: List<Pair<Long, Int>>,
        medId: Long,
        userId: Long,
        startDate: Long,
        endDate: Long,
        regime: Int,
    ): List<UsageCommonDomainModel> {
        val now = Instant.now().epochSecond
        val startLocalDate = LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.UTC).toLocalDate()
        val endLocalDate = LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.UTC).toLocalDate()
        val interval = ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt().absoluteValue
        return mutableListOf<UsageCommonDomainModel>().apply {
            repeat(interval) { position ->
                if (position % (regime + 1) == 0) {
                    usagesByDay.forEach {
                        val time = (it.first)
                        if (time > now) {
                            this.add(
                                UsageCommonDomainModel(
                                    medId = medId,
                                    userId = userId,
                                    creationTime = now,
                                    useTime = time,
                                    quantity = it.second
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
