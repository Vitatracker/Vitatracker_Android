package app.mybad.data.repos

import app.mybad.domain.models.usages.UsageDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel
import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject
import javax.inject.Singleton

val usages = listOf(
    UsageDomainModel(1677182682L),
    UsageDomainModel(1677182683L),
    UsageDomainModel(1677254684L),
    UsageDomainModel(1677269085L),
    UsageDomainModel(1677355486L),
    UsageDomainModel(1677341087L),
    UsageDomainModel(1677427488L),
)
val usages1 = listOf(
    UsageDomainModel(1677182662L),
    UsageDomainModel(1677182663L),
    UsageDomainModel(1677254664L),
    UsageDomainModel(1677269065L),
    UsageDomainModel(1677355466L),
    UsageDomainModel(1677341067L),
    UsageDomainModel(1677427468L),
)
val usages2 = listOf(
    UsageDomainModel(1677182681L),
    UsageDomainModel(1677182682L),
    UsageDomainModel(1677254683L),
    UsageDomainModel(1677269084L),
    UsageDomainModel(1677355485L),
)

@Singleton
class UsagesRepoImpl @Inject constructor() : UsagesRepo {

    override fun getAll(): List<UsagesDomainModel> {
        return listOf(
            UsagesDomainModel(medId = 1L, usages = usages),
            UsagesDomainModel(medId = 2L, usages = usages1),
            UsagesDomainModel(medId = 3L, usages = usages2),
        )
    }

    override fun getSingle(medId: Long): UsagesDomainModel {
        return UsagesDomainModel()
    }

    override fun updateSingle(medId: Long, item: UsagesDomainModel) {
        TODO("Not yet implemented")
    }

    override fun deleteSingle(medId: Long) {
        TODO("Not yet implemented")
    }
}