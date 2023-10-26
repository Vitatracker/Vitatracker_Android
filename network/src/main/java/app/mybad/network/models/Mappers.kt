package app.mybad.network.models

import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.AuthorizationDomainModel
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.PasswordDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.SetNewPasswordDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.models.VerificationCodeDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.network.api.AuthorizationApi
import app.mybad.network.models.response.AuthorizationNetworkModel
import app.mybad.network.models.response.CourseNetworkModel
import app.mybad.network.models.response.PasswordNetworkModel
import app.mybad.network.models.response.RemedyNetworkModel
import app.mybad.network.models.response.SetNewPasswordNetworkModel
import app.mybad.network.models.response.UsageNetworkModel
import app.mybad.network.models.response.UserNetworkModel
import app.mybad.network.models.response.VerificationCodeNetworkModel
import app.mybad.utils.MILES_SECONDS
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.currentDateTimeUTCInSecond
import app.mybad.utils.systemToEpochSecond
import app.mybad.utils.systemToInstant
import app.mybad.utils.toDateTimeIso
import app.mybad.utils.toDateTimeSystem
import app.mybad.utils.toDateTimeUTCInSecond

fun UserDomainModel.mapToNet() = UserNetworkModel(
    id = idn,

    avatar = avatar,

    createdDate = createdDate.toDateTimeIso(),
    updatedDate = updatedDate.toDateTimeIso(),

    name = name,
    email = email,
    password = password,

    notUsed = notUsed,
)

fun UserNetworkModel.mapToDomain(userIdLoc: Long = 0) = UserDomainModel(
    id = userIdLoc,
    idn = id,

    avatar = avatar ?: "",

    createdDate = createdDate.toDateTimeUTCInSecond(),
    updatedDate = updatedDate.toDateTimeUTCInSecond(),

    name = name ?: "",
    email = email ?: "",
    password = password ?: "",

    notUsed = notUsed,

    // мы получаем с сервера, нам не нужно его возвращать опять на сервер, проверить
    updateNetworkDate = currentDateTimeUTCInSecond(),
)

fun CourseNetworkModel.mapToDomain(
    remedyIdLoc: Long = 0,
    courseIdLoc: Long = 0,
) = CourseDomainModel(
    id = courseIdLoc,
    idn = id,

    createdDate = createdDate.toDateTimeUTCInSecond(),
    updatedDate = updatedDate.toDateTimeUTCInSecond(),

    userId = AuthToken.userId,
    userIdn = userId ?: "",

    comment = comment ?: "",

    remedyId = remedyIdLoc,
    remedyIdn = remedyId,

    startDate = (startDate / MILES_SECONDS).toDateTimeSystem().atStartOfDay(),
    endDate = (endDate / MILES_SECONDS).toDateTimeSystem().atEndOfDay(),

    remindDate = if (remindDate > 0) (remindDate / MILES_SECONDS).toDateTimeSystem() else null,
    interval = interval,

    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,

    patternUsages = patternUsages ?: "",
    // мы получаем с сервера, нам не нужно его возвращать опять на сервер, проверить
    updateNetworkDate = currentDateTimeUTCInSecond(),
)

fun CourseDomainModel.mapToNet() = CourseNetworkModel(
    id = idn,

    createdDate = createdDate.toDateTimeIso(),
    updatedDate = updatedDate.toDateTimeIso(),

    userId = userIdn,

    comment = comment,

    remedyId = remedyIdn,

    startDate = startDate.systemToInstant().toEpochMilliseconds(),
    endDate = endDate.systemToInstant().toEpochMilliseconds(),
    remindDate = remindDate?.systemToInstant()?.toEpochMilliseconds() ?: 0,

    interval = interval,
    regime = regime,

    patternUsages = patternUsages,

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

    createdDate = createdDate.toDateTimeUTCInSecond(),
    updatedDate = updatedDate.toDateTimeUTCInSecond(),

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
    updateNetworkDate = currentDateTimeUTCInSecond(),
)

fun RemedyDomainModel.mapToNet() = RemedyNetworkModel(
    id = idn,

    createdDate = createdDate.toDateTimeIso(),
    updatedDate = updatedDate.toDateTimeIso(),

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
    courseIdLoc: Long = 0,
    usageIdLoc: Long = 0
) = UsageDomainModel(
    id = usageIdLoc,
    idn = id,

    userId = AuthToken.userId,
    userIdn = userId ?: "",

    courseId = courseIdLoc,
    courseIdn = courseId ?: 0,

    createdDate = createdDate.toDateTimeUTCInSecond(),
    updatedDate = updatedDate.toDateTimeUTCInSecond(),

    factUseTime = if (factUseTime > 0) factUseTime.toDateTimeSystem() else null,
    useTime = useTime.toDateTimeSystem(),

    quantity = quantity,

    notUsed = notUsed,

    // мы получаем с сервера, нам не нужно его возвращать опять на сервер
    updateNetworkDate = currentDateTimeUTCInSecond(),
)

fun UsageDomainModel.mapToNet() = UsageNetworkModel(
    id = idn,

    userId = userIdn,

    courseId = courseIdn,

    createdDate = createdDate.toDateTimeIso(),
    updatedDate = updatedDate.toDateTimeIso(),

    factUseTime = factUseTime?.systemToEpochSecond() ?: -1,
    useTime = useTime.systemToEpochSecond(),

    quantity = quantity,

    notUsed = notUsed,
)

@JvmName("listUnmToDomain")
fun List<UsageNetworkModel>.mapToDomain(
    courseIdLoc: Long = 0,
    usageIdLoc: Long = 0
): List<UsageDomainModel> = this.map {
    it.mapToDomain(
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
    createdDate = createdDate.toDateTimeIso(),
    updatedDate = updatedDate.toDateTimeIso(),
    email = null,
    name = null,
    notUsed = false,
    password = null,
)

fun PasswordNetworkModel.mapToDomain() = PasswordDomainModel(
    message = message ?: "${violations?.fieldName}: ${violations?.message}"
)

fun VerificationCodeNetworkModel.mapToDomain() = VerificationCodeDomainModel(
    token = token ?: "",
    message = message ?: "${violations?.fieldName}: ${violations?.message}"
)

fun SetNewPasswordNetworkModel.mapToDomain() = SetNewPasswordDomainModel(
    message = message ?: "${violations?.fieldName}: ${violations?.message}"
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
