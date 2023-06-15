package app.mybad.data

import app.mybad.data.db.entity.CourseDataModel
import app.mybad.data.db.entity.MedDataModel
import app.mybad.data.db.entity.UsageCommonDataModel
import app.mybad.data.models.user.NotificationsUserDataModel
import app.mybad.data.models.user.PersonalDataModel
import app.mybad.data.models.user.RulesUserDataModel
import app.mybad.data.models.user.UserDataModel
import app.mybad.data.models.user.UserSettingsDataModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.RulesUserDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.network.models.response.NotificationSetting
import app.mybad.network.models.UserModel
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

fun UserDomainModel.mapToNetwork(): UserModel {
    return UserModel(
        id = id,
        name = personal.name.toString(),
        email = personal.email.toString(),
        password = "123456",    //все дело в бэке, не сочтите за Альцгеймер
        avatar = personal.avatar.toString(),
        notificationSettings = NotificationSetting(
            id = settings.notifications.medsId,
            userId = id,
            isEnabled = settings.notifications.isEnabled,
            isFloat = settings.notifications.isFloat,
            medicalControl = settings.notifications.medicationControl,
            nextCourseStart = settings.notifications.nextCourseStart
        ),
        notUsed = null,
        remedies = emptyList()
    )
}

fun UserModel.mapToDomain(): UserDataModel {
    return UserDataModel(
        id = id,
        personal = PersonalDataModel(name = name, avatar = avatar, email = email),
        settings = UserSettingsDataModel(
            notifications = NotificationsUserDataModel(
                isEnabled = notificationSettings!!.isEnabled,
                isFloat = notificationSettings!!.isFloat,
                medicationControl = notificationSettings!!.medicalControl,
                nextCourseStart = notificationSettings!!.nextCourseStart,
                medsId = notificationSettings!!.id
            ),
            rules = RulesUserDataModel()
        )
    )
}
