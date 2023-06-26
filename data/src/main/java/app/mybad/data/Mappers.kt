package app.mybad.data

import app.mybad.data.db.entity.CourseDataModel
import app.mybad.data.db.entity.MedDataModel
import app.mybad.data.db.entity.UsageCommonDataModel
import app.mybad.data.db.entity.UserLocalDataModel
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
import app.mybad.domain.models.user.UserLocalDomainModel
import app.mybad.network.models.UserModel
import app.mybad.network.models.response.NotificationSetting
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel

fun UserLocalDataModel.mapToDomain() = UserLocalDomainModel(
    id = id,
    name = name,
    email = email,
)

fun CourseDataModel.mapToDomain() = CourseDomainModel(
    id = id,
    creationDate = creationDate,
    updateDate = updateDate,
    userId = userId,
    comment = comment,
    medId = medId,
    startDate = startDate,
    endDate = endDate,
    interval = interval,
    remindDate = remindDate,
    regime = regime,
    showUsageTime = showUsageTime,
    isFinished = isFinished,
    isInfinite = isInfinite,
)

@JvmName("listCdmToDomain")
fun List<CourseDataModel>.mapToDomain() = this.map { it.mapToDomain() }

fun CourseDomainModel.mapToData() = CourseDataModel(
    id = id,
    creationDate = creationDate,
    updateDate = updateDate,
    userId = userId,
    comment = comment,
    medId = medId,
    startDate = startDate,
    endDate = endDate,
    interval = interval,
    remindDate = remindDate,
    regime = regime,
    showUsageTime = showUsageTime,
    isFinished = isFinished,
    isInfinite = isInfinite,
)

fun MedDataModel.mapToDomain() = MedDomainModel(
    id = id,
    creationDate = creationDate,
    updateDate = updateDate,
    userId = userId,
    name = name,
    description = description,
    comment = comment,
    type = type,
    icon = icon,
    color = color,
    dose = dose,
    measureUnit = measureUnit,
    photo = photo,
    beforeFood = beforeFood,
)

@JvmName("listMdmToDomain")
fun List<MedDataModel>.mapToDomain() = this.map { it.mapToDomain() }

fun MedDomainModel.mapToData() = MedDataModel(
    id = id,
    creationDate = creationDate,
    updateDate = updateDate,
    userId = userId,
    name = name,
    description = description,
    comment = comment,
    type = type,
    icon = icon,
    color = color,
    dose = dose,
    measureUnit = measureUnit,
    photo = photo,
    beforeFood = beforeFood,
)

fun UsageCommonDataModel.mapToDomain() = UsageCommonDomainModel(
    id = id,
    medId = medId,
    userId = userId,
    creationTime = creationTime,
    editTime = editTime,
    useTime = useTime,
    factUseTime = factUseTime,
    quantity = quantity,
)

@JvmName("listUcdmToDomain")
fun List<UsageCommonDataModel>.mapToDomain() = this.map { it.mapToDomain() }

fun UsageCommonDomainModel.mapToData() = UsageCommonDataModel(
    id = id,
    medId = medId,
    userId = userId,
    creationTime = creationTime,
    editTime = editTime,
    useTime = useTime,
    factUseTime = factUseTime,
    quantity = quantity,
)

@JvmName("ucdm_toData")
fun List<UsageCommonDomainModel>.mapToData() = this.map { it.mapToData() }

@JvmName("Settings_toDomain")
fun UserPersonalDataModel.mapToDomain() = PersonalDomainModel(
    name = name,
    age = age,
    avatar = avatar,
    email = email,
)

fun UserNotificationsDataModel.mapToDomain() = NotificationsUserDomainModel(
    isEnabled = isEnabled,
    isFloat = isFloat,
    medicationControl = medicalControl,
    nextCourseStart = nextCourseStart,
    medsId = emptyList(),
)

fun UserRulesDataModel.mapToDomain() = RulesUserDomainModel(
    canEdit = canEdit,
    canAdd = canAdd,
    canShare = canShare,
    canInvite = canInvite,
)

fun UserDomainModel.mapToNetwork() = UserModel(
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
        medicalControl = settings.notifications.medicationControl,
        nextCourseStart = settings.notifications.nextCourseStart,
    ),
    notUsed = null,
    remedies = emptyList(),
)

fun UserModel.mapToDomain() = UserDataModel(
    id = id,
    personal = PersonalDataModel(name = name, avatar = avatar, email = email),
    settings = UserSettingsDataModel(
        notifications = NotificationsUserDataModel(
            isEnabled = notificationSettings!!.isEnabled,
            isFloat = notificationSettings!!.isFloat,
            medicationControl = notificationSettings!!.medicalControl,
            nextCourseStart = notificationSettings!!.nextCourseStart,
            medsId = notificationSettings!!.id,
        ),
        rules = RulesUserDataModel(),
    )
)
