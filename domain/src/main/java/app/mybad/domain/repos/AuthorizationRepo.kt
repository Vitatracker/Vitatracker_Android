package app.mybad.domain.repos

interface AuthorizationRepo {

    suspend fun loginWithFacebook()

    suspend fun loginWithGoogle()

    suspend fun loginWithEmail(login: String, password: String)

    suspend fun registrationUser(login: String, password: String, name: String)

}
