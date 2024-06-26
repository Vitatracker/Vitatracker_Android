package app.mybad.data

import app.mybad.data.db.models.CourseModel
import app.mybad.data.db.models.CourseWithParamsModel
import app.mybad.data.db.models.NotificationModel
import app.mybad.data.db.models.PatternUsageModel
import app.mybad.data.db.models.PatternUsageWithNameAndDateModel
import app.mybad.data.db.models.RemedyModel
import app.mybad.data.db.models.UsageModel
import app.mybad.data.db.models.UsageWithNameAndDateModel
import app.mybad.data.db.models.UserModel
import app.mybad.data.models.user.PersonalDataModel
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.NotificationDomainModel
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserPersonalDomainModel
import app.mybad.utils.currentDateTimeInSeconds
import app.mybad.utils.systemToEpochSecond
import app.mybad.utils.timeCorrect
import app.mybad.utils.timeCorrectToSystem
import app.mybad.utils.timeInMinutes
import app.mybad.utils.toDateTimeSystem
import app.mybad.utils.toDateTimeUTC

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

    startDate = startDate.toDateTimeSystem(),
    endDate = endDate.toDateTimeSystem(),
    isInfinite = isInfinite,

    regime = regime,
    isFinished = isFinished,
    notUsed = notUsed,
    patternUsages = patternUsages,
    comment = comment,

    remindDate = if (remindDate > 0) remindDate.toDateTimeSystem() else null,
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

    startDate = startDate.toDateTimeSystem(),
    endDate = endDate.toDateTimeSystem(),
    remindDate = if (remindDate > 0) remindDate.toDateTimeSystem() else null,

    interval = interval,
    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,

    patternUsages = patternUsages, // UTC

    updateNetworkDate = updateNetworkDate,
)

@JvmName("listCmToDomain")
fun List<CourseModel>.mapToDomain() = this.map { it.mapToDomain() }

@JvmName("listCmToData")
fun List<CourseDomainModel>.mapToData() = this.map { it.mapToData() }

fun CourseDomainModel.mapToData() = CourseModel(
    id = id,
    idn = idn,

    createdDate = if (createdDate > 0) createdDate else currentDateTimeInSeconds(),
    updateDate = currentDateTimeInSeconds(),

    userId = userId,
    userIdn = userIdn,

    comment = comment,

    remedyId = remedyId,
    remedyIdn = remedyIdn,

    startDate = startDate.systemToEpochSecond(), // UTC
    endDate = endDate.systemToEpochSecond(),
    remindDate = remindDate?.systemToEpochSecond() ?: 0,

    interval = interval,
    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,

    patternUsages = patternUsages, // UTC

    updateNetworkDate = updateNetworkDate,
)

fun RemedyModel.mapToDomain() = RemedyDomainModel(
    id = id,
    idn = idn,

    createdDate = createdDate,
    updatedDate = updatedDate,

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

    createdDate = if (createdDate > 0) createdDate else currentDateTimeInSeconds(),
    updatedDate = currentDateTimeInSeconds(),

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

    useTime = useTime.toDateTimeSystem(),
    factUseTime = factUseTime?.toDateTimeSystem(),
    quantity = quantity,

    isDeleted = isDeleted,
    notUsed = notUsed,

    createdDate = createdDate,
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

    useTime = useTime.systemToEpochSecond(), // UTC
    factUseTime = factUseTime?.systemToEpochSecond(), // UTC
    quantity = quantity,

    isDeleted = isDeleted,
    notUsed = notUsed,

    createdDate = if (createdDate > 0) createdDate else currentDateTimeInSeconds(),
    updatedDate = currentDateTimeInSeconds(),

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

    createdDate = createdDate,
    updatedDate = updatedDate,

    timeInMinutes = timeInMinutes.timeCorrectToSystem(), // UTC - 720 и коректировать к часовому поясу
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

    createdDate = if (createdDate > 0) createdDate else currentDateTimeInSeconds(),
    updatedDate = currentDateTimeInSeconds(),

    timeInMinutes = timeInMinutes.timeCorrect(), // UTC + 720
    quantity = quantity,

    updateNetworkDate = updateNetworkDate,
)

@JvmName("listPUmToDomain")
fun List<PatternUsageModel>.mapToDomain() = this.map { it.mapToDomain() }

@JvmName("listPUdmToData")
fun List<PatternUsageDomainModel>.mapToData() = this.map { it.mapToData() }

//----------------------------------------
fun PatternUsageWithNameAndDateModel.mapToDomain(data: Long) = UsageDisplayDomainModel(
    id = id,
    courseId = courseId,
    userId = userId,

    isPattern = true, // pattern

    timeInMinutes = timeInMinutes.timeCorrectToSystem(),
    useTime = data.timeCorrectToSystem(timeInMinutes),// тут фиктивная дата, подменится во вьюмодели

    quantity = quantity,

    courseIdn = courseIdn,

    remedyId = remedyId,

    startDate = startDate.toDateTimeSystem(), // с учетом часового пояса
    endDate = endDate.toDateTimeSystem(), // с учетом часового пояса
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
fun List<PatternUsageWithNameAndDateModel>.mapToDomain(data: Long) =
    this.map { it.mapToDomain(data) }

fun UsageWithNameAndDateModel.mapToDomain() = UsageDisplayDomainModel(
    id = id,
    courseId = courseId,
    userId = userId,

    isPattern = false, // usage
    timeInMinutes = useTime.toDateTimeUTC().timeInMinutes().timeCorrectToSystem(),
    quantity = quantity,
    useTime = useTime.toDateTimeSystem(), // с учетом часового пояса
    factUseTime = factUseTime?.toDateTimeSystem(),

    courseIdn = courseIdn,

    remedyId = remedyId,

    startDate = startDate.toDateTimeSystem(), // с учетом часового пояса
    endDate = endDate.toDateTimeSystem(), // с учетом часового пояса
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

fun PersonalDataModel.mapToDomain() = UserPersonalDomainModel(
    name = name,
    age = age,
    avatar = avatar,
    email = email,
)

fun NotificationModel.mapToDomain() = NotificationDomainModel(
    id = id,
    userId = userId,
    isEnabled = isEnabled,
    type = type,
    typeId = typeId,
    time = time,
)

fun NotificationDomainModel.mapToData() = NotificationModel(
    id = id,
    userId = userId,
    isEnabled = isEnabled,
    type = type,
    typeId = typeId,
    date = date,
    time = time,
)

@JvmName("listNDmToDomain")
fun List<NotificationModel>.mapToDomain() = this.map { it.mapToDomain() }
