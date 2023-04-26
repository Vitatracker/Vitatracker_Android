package app.mybad.data

import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.usages.UsageCommonDataModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.RulesUserDomainModel
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel

fun CourseDataModel.mapToDomain(): CourseDomainModel {
    return CourseDomainModel(
        id,
        creationDate,
        updateDate,
        userId,
        comment,
        medId,
        startDate,
        endDate,
        interval,
        remindDate,
        regime,
        showUsageTime,
        isFinished,
        isInfinite
    )
}

@JvmName("listCdmToDomain")
fun List<CourseDataModel>.mapToDomain(): List<CourseDomainModel> {
    return mutableListOf<CourseDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}

fun CourseDomainModel.mapToData(): CourseDataModel {
    return CourseDataModel(
        id,
        creationDate,
        updateDate,
        userId,
        comment,
        medId,
        startDate,
        endDate,
        interval,
        remindDate,
        regime,
        showUsageTime,
        isFinished,
        isInfinite
    )
}

fun MedDataModel.mapToDomain(): MedDomainModel {
    return MedDomainModel(
        id,
        creationDate,
        updateDate,
        userId,
        name,
        description,
        comment,
        type,
        icon,
        color,
        dose,
        measureUnit,
        photo,
        beforeFood
    )
}

@JvmName("listMdmToDomain")
fun List<MedDataModel>.mapToDomain(): List<MedDomainModel> {
    return mutableListOf<MedDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}

fun MedDomainModel.mapToData(): MedDataModel {
    return MedDataModel(
        id,
        creationDate,
        updateDate,
        userId,
        name,
        description,
        comment,
        type,
        icon,
        color,
        dose,
        measureUnit,
        photo,
        beforeFood
    )
}

fun UsageCommonDataModel.mapToDomain(): UsageCommonDomainModel {
    return UsageCommonDomainModel(
        id,
        medId,
        userId,
        creationTime,
        editTime,
        useTime,
        factUseTime,
        quantity
    )
}

fun List<UsageCommonDataModel>.mapToDomain(): List<UsageCommonDomainModel> {
    return mutableListOf<UsageCommonDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}

fun UsageCommonDomainModel.mapToData(): UsageCommonDataModel {
    return UsageCommonDataModel(
        id,
        medId,
        userId,
        creationTime,
        editTime,
        useTime,
        factUseTime,
        quantity
    )
}

@JvmName("ucdm_toData")
fun List<UsageCommonDomainModel>.mapToData(): List<UsageCommonDataModel> {
    return mutableListOf<UsageCommonDataModel>().apply {
        this@mapToData.forEach {
            this.add(it.mapToData())
        }
    }
}

@JvmName("Settings_toDomain")
fun UserPersonalDataModel.mapToDomain(): PersonalDomainModel {
    return PersonalDomainModel(name, age, avatar, email)
}

fun UserNotificationsDataModel.mapToDomain(): NotificationsUserDomainModel {
    return NotificationsUserDomainModel(
        isEnabled,
        isFloat,
        medicalControl,
        nextCourseStart,
        emptyList()
    )
}

fun UserRulesDataModel.mapToDomain(): RulesUserDomainModel {
    return RulesUserDomainModel(canEdit, canAdd, canShare, canInvite)
}
