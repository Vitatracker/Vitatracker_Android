package app.mybad.data.repos

import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.repos.UserDataRepo
import javax.inject.Singleton

@Singleton
class UserDataRepoImpl (

) : UserDataRepo {

    override fun getUserData(): UserDomainModel {
        return UserDomainModel(id = "userid")
    }

    override fun updateUserData(user: UserDomainModel) {
    }

    override fun deleteUser(userId: String) {
    }
}