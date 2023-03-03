package app.mybad.domain.repos

interface AuthorizationRepo {

    suspend fun loginWithFacebook()

    suspend fun loginWithGoogle()

    suspend fun loginWithEmail()

}