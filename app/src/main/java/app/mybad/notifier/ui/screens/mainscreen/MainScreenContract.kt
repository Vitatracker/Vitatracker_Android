package app.mybad.notifier.ui.screens.mainscreen

import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.utils.getCurrentDateTime
import kotlinx.datetime.LocalDateTime

data class MainScreenContract(
    val meds: List<MedDomainModel> = emptyList(),
    val usages: List<UsageCommonDomainModel> = emptyList(),
    val allUsages: Int = 0,
    val date: LocalDateTime = getCurrentDateTime()
)
