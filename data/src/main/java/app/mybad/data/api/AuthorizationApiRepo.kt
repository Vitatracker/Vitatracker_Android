package app.mybad.data.api

interface AuthorizationApiRepo {

    fun checkLogin(login: String): Boolean
    fun createUser(login: String, passwordHash: String) // API key for user POST
    fun authUser(login: String, passwordHash: String) // API key for user
    fun restorePassword(login: String)
    fun setNewPassword(
        token: String,
        passwordHash: String
    ) // token will be sent via email (as link) when user trying to restore his password

}