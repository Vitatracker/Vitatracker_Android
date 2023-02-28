package app.mybad.data

import app.mybad.data.models.course.CourseDataModel
import app.mybad.domain.models.course.CourseDomainModel

fun CourseDataModel.mapToDomain() : CourseDomainModel {
    return CourseDomainModel(id, creationDate, updateDate, userId, comment, medId, startDate, endDate, interval, showUsageTime, isFinished, isInfinite)
}

fun List<CourseDataModel>.mapToDomain() : List<CourseDomainModel> {
    return mutableListOf<CourseDomainModel>().apply {
        this@mapToDomain.forEach {
            this.add(it.mapToDomain())
        }
    }
}

fun CourseDomainModel.mapToData() : CourseDataModel {
    return CourseDataModel(id, creationDate, updateDate, userId, comment, medId, startDate, endDate, interval, showUsageTime, isFinished, isInfinite)
}