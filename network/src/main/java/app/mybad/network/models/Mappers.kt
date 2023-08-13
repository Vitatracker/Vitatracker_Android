package app.mybad.network.models

import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.AuthorizationDomainModel
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.network.api.AuthorizationApi
import app.mybad.network.models.response.AuthorizationNetworkModel
import app.mybad.network.models.response.CourseNetworkModel
import app.mybad.network.models.response.RemedyNetworkModel
import app.mybad.network.models.response.UsageNetworkModel
import app.mybad.network.models.response.UserNetworkModel
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.toDateTimeIsoDisplay
import app.mybad.utils.toEpochSecond
import app.mybad.utils.toLocalDateTime

fun UserDomainModel.mapToNet() = UserNetworkModel(
    id = idn,

    avatar = avatar,

    createdDate = createdDate.toDateTimeIsoDisplay(),
    updatedDate = updatedDate.toDateTimeIsoDisplay(),

    name = name,
    email = email,
    password = password,

    notUsed = notUsed,
)

fun UserNetworkModel.mapToDomain(userIdLoc: Long = 0) = UserDomainModel(
    id = userIdLoc,
    idn = id,

    avatar = avatar ?: "",

    createdDate = createdDate.toLocalDateTime().toEpochSecond(),
    updatedDate = updatedDate.toLocalDateTime().toEpochSecond(),

    name = name ?: "",
    email = email ?: "",
    password = password ?: "",

    notUsed = notUsed,

    // мы получаем с сервера, нам не нужно его возвращать опять на сервер, проверить
    updateNetworkDate = currentDateTimeInSecond(),
)

fun CourseNetworkModel.mapToDomain(
    remedyIdLoc: Long = 0,
    courseIdLoc: Long = 0,
) = CourseDomainModel(
    id = courseIdLoc,
    idn = id,

    createdDate = createdDate.toLocalDateTime().toEpochSecond(),
    updatedDate = updatedDate.toLocalDateTime().toEpochSecond(),

    userId = AuthToken.userId,
    userIdn = userId ?: "",

    comment = comment ?: "",

    remedyId = remedyIdLoc,
    remedyIdn = remedyId,

    startDate = startDate,
    endDate = endDate,
    remindDate = remindDate,

    interval = interval,
    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,

    // мы получаем с сервера, нам не нужно его возвращать опять на сервер, проверить
    updateNetworkDate = currentDateTimeInSecond(),
)

fun CourseDomainModel.mapToNet() = CourseNetworkModel(
    id = idn,

    createdDate = createdDate.toDateTimeIsoDisplay(),
    updatedDate = updatedDate.toDateTimeIsoDisplay(),

    userId = userIdn,

    comment = comment,

    remedyId = remedyIdn,

    startDate = startDate,
    endDate = endDate,
    remindDate = remindDate,

    interval = interval,
    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,
)

@JvmName("listCnmToDomain")
fun List<CourseNetworkModel>.mapToDomain(remedyIdLoc: Long = 0): List<CourseDomainModel> =
    this.map { it.mapToDomain(remedyIdLoc = remedyIdLoc) }

fun RemedyNetworkModel.mapToDomain(remedyIdLoc: Long = 0) = RemedyDomainModel(
    id = remedyIdLoc,
    idn = id,

    createdDate = createdDate.toLocalDateTime().toEpochSecond(),
    updatedDate = updatedDate.toLocalDateTime().toEpochSecond(),

    userId = AuthToken.userId,
    userIdn = userId ?: "",

    name = name,
    description = description,
    comment = comment,

    type = type,
    icon = icon,
    color = color,
    dose = dose,
    measureUnit = measureUnit,
    beforeFood = beforeFood,
    notUsed = notUsed,

    // мы получаем с сервера, нам не нужно его возвращать опять на сервер, проверить
    updateNetworkDate = currentDateTimeInSecond(),
)

fun RemedyDomainModel.mapToNet() = RemedyNetworkModel(
    id = idn,

    createdDate = createdDate.toDateTimeIsoDisplay(),
    updatedDate = updatedDate.toDateTimeIsoDisplay(),

    userId = userIdn,

    name = name,
    description = description,
    comment = comment,

    type = type,
    icon = icon,
    color = color,
    dose = dose,
    measureUnit = measureUnit,
    beforeFood = beforeFood,
    notUsed = notUsed,
)

@JvmName("listRnmToDomain")
fun List<RemedyNetworkModel>.mapToDomain(): List<RemedyDomainModel> = this.map { it.mapToDomain() }

fun UsageNetworkModel.mapToDomain(
    remedyIdLoc: Long = 0,
    remedyIdNet: Long = 0,
    courseIdLoc: Long = 0,
    usageIdLoc: Long = 0
) = UsageDomainModel(
    id = usageIdLoc,
    idn = id,

    userId = AuthToken.userId,
    userIdn = userId ?: "",

    courseId = courseIdLoc,
    courseIdn = courseId ?: 0,

    remedyId = remedyIdLoc,
    remedyIdn = if (remedyIdNet > 0) remedyIdNet else remedyId ?: 0,

    createdDate = createdDate.toLocalDateTime().toEpochSecond(),
    updatedDate = updatedDate.toLocalDateTime().toEpochSecond(),

    factUseTime = factUseTime,
    useTime = useTime,

    quantity = quantity,

    notUsed = notUsed,

    // мы получаем с сервера, нам не нужно его возвращать опять на сервер, проверить
    updateNetworkDate = currentDateTimeInSecond(),
)

fun UsageDomainModel.mapToNet() = UsageNetworkModel(
    id = idn,

    userId = userIdn,

    courseId = courseIdn,

    remedyId = remedyIdn,

    createdDate = createdDate.toDateTimeIsoDisplay(),
    updatedDate = updatedDate.toDateTimeIsoDisplay(),

    timestamp = createdDate.toDateTimeIsoDisplay(),// не понятно что это, без этого на сервер не отправлялось

    factUseTime = factUseTime,
    useTime = useTime,

    quantity = quantity,

    notUsed = notUsed,
)

@JvmName("listUnmToDomain")
fun List<UsageNetworkModel>.mapToDomain(
    remedyIdLoc: Long = 0,
    remedyIdNet: Long = 0,
    courseIdLoc: Long = 0,
    usageIdLoc: Long = 0
): List<UsageDomainModel> = this.map {
    it.mapToDomain(
        remedyIdLoc = remedyIdLoc,
        remedyIdNet = remedyIdNet,
        courseIdLoc = courseIdLoc,
        usageIdLoc = usageIdLoc,
    )
}

fun AuthorizationNetworkModel.mapToDomain() = AuthorizationDomainModel(
    token = token,
    tokenDate = AuthorizationApi.decodeTokenDateExp(token),
    tokenRefresh = refreshToken,
    tokenRefreshDate = AuthorizationApi.decodeTokenDateExp(refreshToken),
)

fun UserSettingsDomainModel.mapToNet() = UserNetworkModel(
    id = "",
    avatar = null,
    createdDate = createdDate.toDateTimeIsoDisplay(),
    updatedDate = updatedDate.toDateTimeIsoDisplay(),
    email = null,
    name = null,
    notUsed = false,
    password = null,
)

/*
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
*/
