package app.mybad.network.models

import app.mybad.domain.models.AuthorizationDomainModel
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.network.models.response.AuthorizationNetworkModel
import app.mybad.network.models.response.CourseNetworkModel
import app.mybad.network.models.response.RemedyNetworkModel
import app.mybad.network.models.response.UsageNetworkModel
import app.mybad.network.models.response.UserNetworkModel

fun UserDomainModel.mapToNet() = UserNetworkModel(
    id = idn,

    avatar = avatar,

    createdDate = createdDate,
    updatedDate = updatedDate,

    name = name,
    email = email,
    password = password,

    notUsed = notUsed,
)

fun UserNetworkModel.mapToDomain() = UserDomainModel(
    idn = id,

    avatar = avatar ?: "",

    createdDate = createdDate ?: 0L,
    updatedDate = updatedDate ?: 0L,

    name = name ?: "",
    email = email ?: "",
    password = password ?: "",

    notUsed = notUsed,
)

fun CourseNetworkModel.mapToDomain() = CourseDomainModel(
    idn = id,

    createdDate = createdDate ?: 0L,

    userIdn = userId ?: "",

    comment = comment ?: "",
    remedyId = remedyId,

    startDate = startDate,
    endDate = endDate,
    remindDate = remindDate,

    interval = interval,
    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,
)

fun CourseDomainModel.mapToNet() = CourseNetworkModel(
    id = idn,

    createdDate = createdDate,

    userId = userIdn,

    comment = comment,
    remedyId = remedyId,

    startDate = startDate,
    endDate = endDate,
    remindDate = remindDate,

    interval = interval,
    regime = regime,

    isFinished = isFinished,
    isInfinite = isInfinite,
    notUsed = notUsed,
)

fun List<CourseNetworkModel>.mapToDomain() = this.map { it.mapToDomain() }

fun RemedyNetworkModel.mapToDomain() = RemedyDomainModel(
    idn = id,

    createdDate = createdDate ?: 0,
    updatedDate = updatedDate ?: 0,

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
)

fun RemedyDomainModel.mapToDomain() = RemedyNetworkModel(
    id = idn,

    createdDate = createdDate,
    updatedDate = updatedDate,

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

fun UsageNetworkModel.mapToDomain() = UsageDomainModel(
    idn = id,

    userIdn = userId ?: "",

    courseId = courseId ?: 0,

    createdDate = createdDate ?: 0,
    updatedDate = updatedDate ?: 0,

    factUseTime = factUseTime,
    useTime = useTime,

    quantity = quantity,

    notUsed = notUsed,
)

fun UsageDomainModel.mapToNet() = UsageNetworkModel(
    id = idn,

    userId = userIdn,

    courseId = courseId,

    createdDate = createdDate,
    updatedDate = updatedDate,

    factUseTime = factUseTime,
    useTime = useTime,

    quantity = quantity,

    notUsed = notUsed,
)

fun AuthorizationNetworkModel.mapToDomain() = AuthorizationDomainModel(
    token = token,
    refreshToken = refreshToken,
)

fun UserSettingsDomainModel.mapToNet() = UserNetworkModel(
    id = "",
    avatar = null,
    createdDate = null,
    email = null,
    name = null,
    notUsed = false,
    password = null,
    updatedDate = null
)
