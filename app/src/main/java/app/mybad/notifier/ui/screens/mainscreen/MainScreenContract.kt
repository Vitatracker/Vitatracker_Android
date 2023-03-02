package app.mybad.notifier.ui.screens.mainscreen

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import java.time.LocalDate
import java.time.LocalDateTime

data class MainScreenContract(
    val usages: List<UsageCommonDomainModel> = emptyList(),
    var date: LocalDateTime = LocalDateTime.now()
)
