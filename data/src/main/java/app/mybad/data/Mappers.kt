package app.mybad.data

import app.mybad.data.db.entity.CourseDataModel
import app.mybad.data.db.entity.MedDataModel
import app.mybad.data.db.entity.UsageCommonDataModel
import app.mybad.data.db.entity.UserLocalDataModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.RulesUserDomainModel
import app.mybad.domain.models.user.UserLocalDomainModel
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
    medicationControl = medicationControl,
    nextCourseStart = nextCourseStart,
    medsId = emptyList(),
)

fun UserRulesDataModel.mapToDomain() = RulesUserDomainModel(
    canEdit = canEdit,
    canAdd = canAdd,
    canShare = canShare,
    canInvite = canInvite,
)
