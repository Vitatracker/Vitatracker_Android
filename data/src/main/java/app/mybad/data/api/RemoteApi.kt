package app.mybad.data.api

import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.usages.UsagesDataModel
import app.mybad.data.models.user.UserDataModel

interface RemoteApi {

    //Auth
    fun checkLogin(login: String) : Boolean
    fun createUser(login: String, passwordHash: String) : String        //API key for user
    fun authUser(login: String, passwordHash: String) : String          //API key for user
    fun restorePassword(login: String) : Boolean
    fun setNewPassword(token: String, passwordHash: String) : Boolean   // token will be sent via email (as link) when user trying to restore his password

    //Onboarding
    fun setUserModel(model: UserDataModel) : Boolean

    //Main Screen
    fun addCourse(
        course: CourseDataModel,
        med: MedDataModel,
        usagesDataModel: UsagesDataModel
    ) : Boolean

    fun getUserModel() : UserDataModel
    fun getUserCourses() : List<CourseDataModel>
    fun getUserMeds() : List<MedDataModel>
    fun getUserUsages() : List<UsagesDataModel>

}