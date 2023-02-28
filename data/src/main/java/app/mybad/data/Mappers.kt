package app.mybad.data

import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.med.MedDetailsDataModel
import app.mybad.data.models.usages.UsageCommonDataModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel

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

fun UsageCommonDataModel.mapToDomain() : UsageCommonDomainModel {
    return UsageCommonDomainModel(id, medId, userId, creationTime, editTime, useTime, factUseTime)
}

fun List<UsageCommonDataModel>.mapToDomain() : List<UsageCommonDomainModel> {
    return mutableListOf<UsageCommonDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}

fun UsageCommonDomainModel.mapToData() : UsageCommonDataModel {
    return UsageCommonDataModel(id, medId, userId, creationTime, editTime, useTime, factUseTime)
}
@JvmName("ucdm_toData")
fun List<UsageCommonDomainModel>.mapToData() : List<UsageCommonDataModel> {
    return mutableListOf<UsageCommonDataModel>().apply {
        this@mapToData.forEach {
            this.add(it.mapToData())
        }
    }
}