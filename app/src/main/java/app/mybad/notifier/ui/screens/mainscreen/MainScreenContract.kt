package app.mybad.notifier.ui.screens.mainscreen

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.theme.utils.currentDateTime
import kotlinx.datetime.LocalDateTime

data class MainScreenContract(
    val remedies: List<RemedyDomainModel> = emptyList(),
    val courses: List<CourseDomainModel> = emptyList(),
    val usages: List<UsageDomainModel> = emptyList(),
    val usagesSize: Int = 0,
    val date: LocalDateTime = currentDateTime()
)
