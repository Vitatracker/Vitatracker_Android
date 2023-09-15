package app.mybad.data

import app.mybad.data.db.models.CourseModel
import app.mybad.data.db.models.CourseWithParamsModel
import app.mybad.data.db.models.PatternUsageModel
import app.mybad.data.db.models.PatternUsageWithNameAndDateModel
import app.mybad.data.db.models.RemedyModel
import app.mybad.data.db.models.UsageModel
import app.mybad.data.db.models.UsageWithNameAndDateModel
import app.mybad.data.db.models.UserModel
import app.mybad.data.models.user.PersonalDataModel
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.models.user.NotificationSettingDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserPersonalDomainModel
import app.mybad.domain.models.user.UserRulesDomainModel
import app.mybad.utils.changeTimeOfSystem
import app.mybad.utils.changeTimeToSystemTimeInMinutes
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.timeInMinutes
import app.mybad.utils.toSystemTimeInMinutes
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
)

@JvmName("listUrmToDomain")
fun List<UserModel>.mapToDomain() = this.map { it.mapToDomain() }

fun CourseWithParamsModel.mapToDomain() = CourseDisplayDomainModel(
    id = id,
    idn = idn,
    userId = userId,
    userIdn = userIdn,
    remedyId = remedyId,
    remedyIdn = remedyIdn,

    startDate = startDate,
    endDate = endDate,
    isInfinite = isInfinite,

    regime = regime,
    isFinished = isFinished,
    notUsed = notUsed,
    patternUsages = patternUsages,
    comment = comment,

    remindDate = remindDate,
    interval = interval,

    createdDate = createdDate,
    updatedDate = updateDate,
    updateNetworkDate = updateNetworkDate,

    name = name ?: "",
    description = description ?: "",
    type = type,
    icon = icon,
    color = color,
    dose = dose,
    beforeFood = beforeFood,
    measureUnit = measureUnit,
    photo = photo
)

@JvmName("listCPmToDomain")
fun List<CourseWithParamsModel>.mapToDomain() = this.map { it.mapToDomain() }

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

    patternUsages = patternUsages,

    updateNetworkDate = updateNetworkDate,
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

    patternUsages = patternUsages,

    updateNetworkDate = updateNetworkDate,
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

    updateNetworkDate = updateNetworkDate,
)

fun UsageModel.mapToDomain() = UsageDomainModel(
    id = id,
    idn = idn,

    userId = userId,
    userIdn = userIdn,

    courseId = courseId,
    courseIdn = courseIdn,

    useTime = useTime,
    factUseTime = factUseTime,
    quantity = quantity,

    isDeleted = isDeleted,
    notUsed = notUsed,

    createdDate = creationDate,
    updatedDate = updatedDate,

    updateNetworkDate = updateNetworkDate,
)

fun UsageDomainModel.mapToData() = UsageModel(
    id = id,
    idn = idn,

    userId = userId,
    userIdn = userIdn,

    courseId = courseId,
    courseIdn = courseIdn,

    useTime = useTime,
    factUseTime = factUseTime,
    quantity = quantity,

    isDeleted = isDeleted,
    notUsed = notUsed,

    creationDate = createdDate,
    updatedDate = currentDateTimeInSecond(),

    updateNetworkDate = updateNetworkDate,
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

    createdDate = creationDate,
    updatedDate = updatedDate,

    timeInMinutes = timeInMinutes,
    quantity = quantity,

    updateNetworkDate = updateNetworkDate,
)

fun PatternUsageDomainModel.mapToData() = PatternUsageModel(
    id = id,
    idn = idn,

    userId = userId,
    userIdn = userIdn,

    courseId = courseId,
    courseIdn = courseIdn,

    creationDate = if (createdDate > 0) createdDate else currentDateTimeInSecond(),
    updatedDate = currentDateTimeInSecond(),

    timeInMinutes = timeInMinutes,
    quantity = quantity,

    updateNetworkDate = updateNetworkDate,
)

@JvmName("listPUmToDomain")
fun List<PatternUsageModel>.mapToDomain() = this.map { it.mapToDomain() }

@JvmName("listPUdmToData")
fun List<PatternUsageDomainModel>.mapToData() = this.map { it.mapToData() }

//----------------------------------------
fun PatternUsageWithNameAndDateModel.mapToDomain(date: Long) = UsageDisplayDomainModel(
    id = id,
    courseId = courseId,
    userId = userId,

    isPattern = true, // pattern
    timeInMinutes = timeInMinutes,// UTC время в минутах, для сортировки и сопоставления с паттерном
    useTime = date.changeTimeOfSystem(minutes = timeInMinutes),// для правильного отображения
    sortedDate = date.changeTimeToSystemTimeInMinutes(timeInMinutes), // дата и время с учетом часового пояса, для правильной сортировки
    quantity = quantity,

    courseIdn = courseIdn,

    remedyId = remedyId,

    startDate = startDate,
    endDate = endDate,
    isInfinite = isInfinite,

    regime = regime,
    showUsageTime = showUsageTime,
    isFinished = isFinished,
    notUsed = notUsed,

    name = name ?: "",
    description = description ?: "",
    type = type,
    icon = icon,
    color = color,
    dose = dose,
    beforeFood = beforeFood,
    measureUnit = measureUnit,
    photo = photo,
)

@JvmName("listPUNDmToDomain")
fun List<PatternUsageWithNameAndDateModel>.mapToDomain(date: Long) =
    this.map { it.mapToDomain(date) }

fun UsageWithNameAndDateModel.mapToDomain() = UsageDisplayDomainModel(
    id = id,
    courseId = courseId,
    userId = userId,

    isPattern = false, // usage
    sortedDate = useTime.toSystemTimeInMinutes(), // дата и время с учетом часового пояса, для правильной сортировки

    timeInMinutes = useTime.timeInMinutes(),// UTC время в минутах, для сортировки и сопоставления с паттерном
    quantity = quantity,
    useTime = useTime, // какое-то конкретное UTC время приема препарата, которое формируется из паттерна и текущей даты
    factUseTime = factUseTime,

    courseIdn = courseIdn,

    remedyId = remedyId,

    startDate = startDate,
    endDate = endDate,
    isInfinite = isInfinite,

    regime = regime,
    showUsageTime = showUsageTime,
    isFinished = isFinished,
    notUsed = notUsed,

    name = name ?: "",
    description = description ?: "",
    type = type,
    icon = icon,
    color = color,
    dose = dose,
    beforeFood = beforeFood,
    measureUnit = measureUnit,
    photo = photo,
)

@JvmName("listUNDmToDomain")
fun List<UsageWithNameAndDateModel>.mapToDomain() = this.map { it.mapToDomain() }

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
