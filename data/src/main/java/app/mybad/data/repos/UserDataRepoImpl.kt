package app.mybad.data.repos

import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.repos.UserDataRepo

class UserDataRepoImpl : UserDataRepo {

    override fun getUserData(): UserDomainModel {
        TODO("Not yet implemented")
    }

    override fun updateUserData(user: UserDomainModel) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }
}