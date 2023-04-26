package app.mybad.data.api

import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.user.UserDataModel

interface RemoteApi {

    // Onboarding
    fun setUserModel(model: UserDataModel)

    // Main Screen
    fun addCourse(course: CourseDataModel) // POST
    fun addMed(med: MedDataModel)

    fun hasChanges(): Boolean

    fun getUserModel(): UserDataModel
    fun getUserCourses(): List<CourseDataModel>
    fun getUserMeds(): List<MedDataModel>
}
