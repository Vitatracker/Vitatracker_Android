package app.mybad.network.repos

import app.mybad.data.api.AuthorizationApiRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationApiImpl @Inject constructor() : AuthorizationApiRepo {

    override fun checkLogin(login: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun createUser(login: String, passwordHash: String) {
        TODO("Not yet implemented")
    }

    override fun authUser(login: String, passwordHash: String) {
        TODO("Not yet implemented")
    }

    override fun restorePassword(login: String) {
        TODO("Not yet implemented")
    }

    override fun setNewPassword(token: String, passwordHash: String) {
        TODO("Not yet implemented")
    }


}