package app.mybad.data

import app.mybad.data.db.models.CourseModel
import app.mybad.data.db.models.PatternUsageModel
import app.mybad.data.db.models.RemedyModel
import app.mybad.data.db.models.UsageModel
import app.mybad.data.db.models.UserModel
import app.mybad.data.models.user.PersonalDataModel
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.models.user.NotificationSettingDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserPersonalDomainModel
import app.mybad.domain.models.user.UserRulesDomainModel
import app.mybad.utils.currentDateTimeInSecond
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel

fun UserModel.mapToDomain() = UserDomainModel(
    id = id,
    idn = idn,

    avatar = avatar,

    createdDate = createdDate,
    updatedDate = updatedDate,

    name = name,
    email = email,
    password = password,

    notUsed = notUsed,

    token = token,
    tokenDate = tokenDate,

    tokenRefresh = tokenRefresh,
    tokenRefreshDate = tokenRefreshDate,

    updateNetworkDate = updateNetworkDate,
    updateLocalDate = updateLocalDate
)

@JvmName("listUrmToDomain")
fun List<UserModel>.mapToDomain() = this.map { it.mapToDomain() }

fun CourseModel.mapToDomain() = CourseDomainModel(
    id = id,
    idn = idn,

    createdDate = createdDate,
    updatedDate = updateDate,

    userId = userId,
    userIdn = userIdn,

    comment = comment,

    remedyId = remedyId,
    remedyIdn = remedyIdn,

    startDate = startDate,
    endDate = endDate,
    remindDate = remindDate,

    interval = interval,
    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,

    updateNetworkDate = updateNetworkDate,
    updateLocalDate = updateLocalDate,
)

@JvmName("listCmToDomain")
fun List<CourseModel>.mapToDomain() = this.map { it.mapToDomain() }

@JvmName("listCmToData")
fun List<CourseDomainModel>.mapToData() = this.map { it.mapToData() }

fun CourseDomainModel.mapToData() = CourseModel(
    id = id,
    idn = idn,

    createdDate = createdDate,
    updateDate = currentDateTimeInSecond(),

    userId = userId,
    userIdn = userIdn,

    comment = comment,

    remedyId = remedyId,
    remedyIdn = remedyIdn,

    startDate = startDate,
    endDate = endDate,
    remindDate = remindDate,

    interval = interval,
    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,

    updateNetworkDate = 0, // при обновлении для передачи на сервер
    updateLocalDate = updateLocalDate,
)

fun RemedyModel.mapToDomain() = RemedyDomainModel(
    id = id,
    idn = idn,

    createdDate = creationDate,
    updatedDate = updateDate,

    userId = userId,
    userIdn = userIdn,

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
    notUsed = notUsed,

    updateNetworkDate = updateNetworkDate,
    updateLocalDate = updateLocalDate,
)

@JvmName("listRmToDomain")
fun List<RemedyModel>.mapToDomain() = this.map { it.mapToDomain() }

@JvmName("listRmToData")
fun List<RemedyDomainModel>.mapToData() = this.map { it.mapToData() }

fun RemedyDomainModel.mapToData() = RemedyModel(
    id = id,
    idn = idn,

    creationDate = createdDate,
    updateDate = currentDateTimeInSecond(),

    userId = userId,
    userIdn = userIdn,

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
    notUsed = notUsed,

    updateNetworkDate = 0, // при обновлении для передачи на сервер
    updateLocalDate = updateLocalDate,
)

fun UsageModel.mapToDomain() = UsageDomainModel(
    id = id,
    idn = idn,

    userId = userId,
    userIdn = userIdn,

    courseId = courseId,
    courseIdn = courseIdn,

    remedyId = remedyId,
    remedyIdn = remedyIdn,

    createdDate = creationDate,
    updatedDate = updatedDate,

    factUseTime = factUseTime,
    useTime = useTime,

    quantity = quantity,

    isDeleted = isDeleted,
    notUsed = notUsed,

    updateNetworkDate = updateNetworkDate,
    updateLocalDate = updateLocalDate,
)

fun UsageDomainModel.mapToData() = UsageModel(
    id = id,
    idn = idn,

    userId = userId,
    userIdn = userIdn,

    courseId = courseId,
    courseIdn = courseIdn,

    remedyId = remedyId,
    remedyIdn = remedyIdn,

    creationDate = createdDate,
    updatedDate = currentDateTimeInSecond(),

    factUseTime = factUseTime,
    useTime = useTime,

    quantity = quantity,

    isDeleted = isDeleted,
    notUsed = notUsed,

    updateNetworkDate = 0, // при обновлении для передачи на сервер
    updateLocalDate = updateLocalDate,
)

@JvmName("listUmToDomain")
fun List<UsageModel>.mapToDomain() = this.map { it.mapToDomain() }

@JvmName("listUdmToData")
fun List<UsageDomainModel>.mapToData() = this.map { it.mapToData() }

//----------------------------------------
fun PatternUsageModel.mapToDomain() = PatternUsageDomainModel(
    id = id,
    idn = idn,

    userId = userId,
    userIdn = userIdn,

    courseId = courseId,
    courseIdn = courseIdn,

    remedyId = remedyId,
    remedyIdn = remedyIdn,

    createdDate = creationDate,
    updatedDate = updatedDate,

    timeInMinutes = timeInMinutes,
    quantity = quantity,

    updateNetworkDate = updateNetworkDate,
    updateLocalDate = updateLocalDate,
)

fun PatternUsageDomainModel.mapToData() = PatternUsageModel(
    id = id,
    idn = idn,

    userId = userId,
    userIdn = userIdn,

    courseId = courseId,
    courseIdn = courseIdn,

    remedyId = remedyId,
    remedyIdn = remedyIdn,

    creationDate = if (createdDate > 0) createdDate else currentDateTimeInSecond(),
    updatedDate = currentDateTimeInSecond(),

    timeInMinutes = timeInMinutes,
    quantity = quantity,

    updateNetworkDate = updateNetworkDate,
    updateLocalDate = updateLocalDate,
)

@JvmName("listPUmToDomain")
fun List<PatternUsageModel>.mapToDomain() = this.map { it.mapToDomain() }

@JvmName("listPUdmToData")
fun List<PatternUsageDomainModel>.mapToData() = this.map { it.mapToData() }

//------------------------
fun UserPersonalDataModel.mapToDomain() = UserPersonalDomainModel(
    name = name,
    age = age,
    avatar = avatar,
    email = email,
)

fun PersonalDataModel.mapToDomain() = UserPersonalDomainModel(
    name = name,
    age = age,
    avatar = avatar,
    email = email,
)

fun UserNotificationsDataModel.mapToDomain() = NotificationSettingDomainModel(
    isEnabled = isEnabled,
    isFloat = isFloat,
    medicationControl = medicationControl,
    nextCourseStart = nextCourseStart,
    medsId = emptyList(),
)

fun UserRulesDataModel.mapToDomain() = UserRulesDomainModel(
    canEdit = canEdit,
    canAdd = canAdd,
    canShare = canShare,
    canInvite = canInvite,
)
