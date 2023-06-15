package app.mybad.data.api

import app.mybad.data.db.entity.CourseDataModel
import app.mybad.data.db.entity.MedDataModel
import app.mybad.data.models.user.UserDataModel

interface RemoteApi {

    //Auth
    fun checkLogin(login: String) : Boolean
    fun createUser(login: String, passwordHash: String)                 //API key for user POST
    fun authUser(login: String, passwordHash: String)                   //API key for user
    fun restorePassword(login: String)
    fun setNewPassword(token: String, passwordHash: String)             //token will be sent via email (as link) when user trying to restore his password

    //Onboarding
    fun setUserModel(model: UserDataModel)

    //Main Screen
    fun addCourse(course: CourseDataModel)                              //POST
    fun addMed(med: MedDataModel)

    fun hasChanges() : Boolean

    fun getUserModel() : UserDataModel
    fun getUserCourses() : List<CourseDataModel>
    fun getUserMeds() : List<MedDataModel>

}
