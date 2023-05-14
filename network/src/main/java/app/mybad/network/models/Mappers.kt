package app.mybad.network.models

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.network.models.response.Courses
import app.mybad.network.models.response.Remedies
import app.mybad.network.models.response.Usages

fun Usages.mapToDomain(medId: Long, userId: Long): UsageCommonDomainModel {
    return UsageCommonDomainModel(
        id = id.toInt(),
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
