package app.mybad.domain.repos

import app.mybad.domain.models.user.UserDomainModel

interface UserDataRepo {
    fun getUserData() : UserDomainModel
    fun updateUserData(user: UserDomainModel)
    fun deleteUser(userId: String)
}