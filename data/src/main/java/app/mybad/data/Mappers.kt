package app.mybad.data

import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.med.MedDetailsDataModel
import app.mybad.data.models.usages.UsageDataModel
import app.mybad.data.models.usages.UsagesDataModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel

fun CourseDataModel.mapToDomain() : CourseDomainModel {
    return CourseDomainModel(id, creationDate, updateDate, userId, comment, medId, startDate, endDate, interval, showUsageTime, isFinished, isInfinite)
}
@JvmName("listCdmToDomain")
fun List<CourseDataModel>.mapToDomain() : List<CourseDomainModel> {
    return mutableListOf<CourseDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}
fun CourseDomainModel.mapToData() : CourseDataModel {
    return CourseDataModel(id, creationDate, updateDate, userId, comment, medId, startDate, endDate, interval, showUsageTime, isFinished, isInfinite)
}

fun UsagesDataModel.mapToDomain() : UsagesDomainModel {
    return UsagesDomainModel(medId, creationDate, userId, needControl, usages.mapToDomain())
}

@JvmName("listUsdmToDomain")
fun List<UsagesDataModel>.mapToDomain() : List<UsagesDomainModel> {
    return mutableListOf<UsagesDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}
fun UsageDataModel.mapToDomain() : UsageDomainModel {
    return UsageDomainModel(timeToUse, usedTime)
}
@JvmName("listUdmToDomain")
fun List<UsageDataModel>.mapToDomain() : List<UsageDomainModel> {
    return mutableListOf<UsageDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}

fun UsagesDomainModel.mapToData() : UsagesDataModel {
    return UsagesDataModel(medId, creationDate, userId, needControl, usages.mapToData())
}

fun UsageDomainModel.mapToData() : UsageDataModel {
    return UsageDataModel(timeToUse, usedTime)
}

fun List<UsageDomainModel>.mapToData() : List<UsageDataModel> {
    return mutableListOf<UsageDataModel>().apply {
        this@mapToData.forEach {
            this.add(it.mapToData())
        }
    }
}

fun MedDetailsDataModel.mapToDomain() : MedDetailsDomainModel {
    return MedDetailsDomainModel(type, icon, dose, measureUnit, photo, beforeFood)
}
fun MedDataModel.mapToDomain() : MedDomainModel {
    return MedDomainModel(id, creationDate, updateDate, userId, name, description, comment, details.mapToDomain())
}

@JvmName("listMdmToDomain")
fun List<MedDataModel>.mapToDomain() : List<MedDomainModel> {
    return mutableListOf<MedDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}
fun MedDetailsDomainModel.mapToData() : MedDetailsDataModel {
    return MedDetailsDataModel(type, icon, dose, measureUnit, photo, beforeFood)
}
fun MedDomainModel.mapToData() : MedDataModel {
    return MedDataModel(id, creationDate, updateDate, userId, name, description, comment, details.mapToData())
}