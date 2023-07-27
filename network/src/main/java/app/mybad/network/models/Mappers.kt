package app.mybad.network.models

import app.mybad.domain.models.authorization.AuthorizationUserLoginDomainModel
import app.mybad.domain.models.authorization.AuthorizationUserRegistrationDomainModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.RulesUserDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.network.models.request.AuthorizationUserLogin
import app.mybad.network.models.request.AuthorizationUserRegistration
import app.mybad.network.models.response.Courses
import app.mybad.network.models.response.NotificationSetting
import app.mybad.network.models.response.Remedies
import app.mybad.network.models.response.Usages

fun Usages.mapToDomain(medId: Long, userId: Long): UsageCommonDomainModel {
    return UsageCommonDomainModel(
        id = id,
        medId = medId,
        userId = userId,
        useTime = useTime,
        factUseTime = factUseTime,
        quantity = quantity,
        isDeleted = notUsed
    )
}

fun List<Usages>.mapToDomain(medId: Long, userId: Long): List<UsageCommonDomainModel> {
    return mutableListOf<UsageCommonDomainModel>().apply {
        this@mapToDomain.forEach {
            add(it.mapToDomain(medId, userId))
        }
    }.toList()
}

fun Courses.mapToDomain(userId: Long): CourseDomainModel {
    return CourseDomainModel(
        id = id,
        medId = remedyId,
        userId = userId,
        regime = regime.toInt(),
        startDate = startDate,
        endDate = endDate,
        remindDate = remindDate,
        interval = interval,
        comment = comment,
        isFinished = isFinished,
        isInfinite = isInfinite,
    )
}

fun CourseDomainModel.mapToNet(usages: List<UsageCommonDomainModel>): Courses {
    return Courses(
        id = id,
        remedyId = medId,
        regime = regime.toLong(),
        startDate = startDate,
        endDate = endDate,
        remindDate = remindDate,
        interval = interval,
        comment = comment,
        isInfinite = isInfinite,
        isFinished = isFinished,
        usages = usages.mapToNet(id),
        notUsed = false
    )
}

fun List<Courses>.mapToDomain(userId: Long): List<CourseDomainModel> {
    return mutableListOf<CourseDomainModel>().apply {
        this@mapToDomain.forEach {
            add(it.mapToDomain(userId))
        }
    }
}

fun Remedies.mapToDomain(): MedDomainModel {
    return MedDomainModel(
        id = id,
        userId = userId,
        name = name,
        description = description,
        comment = comment,
        type = type,
        dose = dose.toInt(),
        icon = icon.toInt(),
        color = color.toInt(),
        measureUnit = measureUnit,
        beforeFood = beforeFood,
    )
}

fun UsageCommonDomainModel.mapToNet(courseId: Long): Usages {
    return Usages(
        id = id.toLong(),
        courseId = courseId,
        useTime = useTime,
        factUseTime = factUseTime,
        notUsed = isDeleted,
        quantity = quantity
    )
}

fun List<UsageCommonDomainModel>.mapToNet(courseId: Long): List<Usages> {
    return mutableListOf<Usages>().apply {
        this@mapToNet.forEach {
            add(it.mapToNet(courseId))
        }
    }
}

fun UserDomainModel.mapToNet() = UserModel(
    id = id,
    name = personal.name.toString(),
    email = personal.email.toString(),
    password = "123456",    //все дело в бэке, не сочтите за Альцгеймер //TODO("проверить что это")
    avatar = personal.avatar.toString(),
    notificationSettings = NotificationSetting(
        id = settings.notifications.medsId,
        userId = id,
        isEnabled = settings.notifications.isEnabled,
        isFloat = settings.notifications.isFloat,
        medicationControl = settings.notifications.medicationControl,
        nextCourseStart = settings.notifications.nextCourseStart,
    ),
    notUsed = null,
    remedies = emptyList(),
)

fun UserModel.mapToDomain() = UserDomainModel(
    id = id,
    personal = PersonalDomainModel(name = name, avatar = avatar, email = email),
    settings = UserSettingsDomainModel(
        notifications = NotificationsUserDomainModel(
            isEnabled = notificationSettings!!.isEnabled,
            isFloat = notificationSettings.isFloat,
            medicationControl = notificationSettings.medicationControl,
            nextCourseStart = notificationSettings.nextCourseStart,
            medsId = notificationSettings.id,
        ),
        rules = RulesUserDomainModel(),
    )
)

fun AuthorizationUserLoginDomainModel.mapToNet() = AuthorizationUserLogin(
    email = email,
    password = password,
)

fun AuthorizationUserLogin.mapToDomain() = AuthorizationUserLoginDomainModel(
    email = email,
    password = password,
)

fun AuthorizationUserRegistrationDomainModel.mapToNet() = AuthorizationUserRegistration(
    email = email,
    password = password
)

fun AuthorizationUserRegistration.mapToDomain() = AuthorizationUserRegistrationDomainModel(
    email = email,
    password = password
)
