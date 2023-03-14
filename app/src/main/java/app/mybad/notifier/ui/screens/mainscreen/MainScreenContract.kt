package app.mybad.notifier.ui.screens.mainscreen

import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import java.time.LocalDateTime

data class MainScreenContract(
    val meds: List<MedDomainModel> = emptyList(),
    val usages: List<UsageCommonDomainModel> = emptyList(),
    val date: LocalDateTime = LocalDateTime.now()
)
