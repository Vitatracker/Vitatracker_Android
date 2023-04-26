package app.mybad.data.repos

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.mybad.domain.repos.AuthorizationRepo
import app.mybad.network.models.AuthorizationUserNetwork
import app.mybad.network.repos.repo.AuthorizationNetworkRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationRepoImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val authorizationNetworkRepo: AuthorizationNetworkRepo
) : AuthorizationRepo {

    override suspend fun loginWithFacebook() {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithGoogle() {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithEmail(login: String, password: String) {
        authorizationNetworkRepo.loginUser(
            authorizationUserNetwork = AuthorizationUserNetwork(
                email = login,
                password = password
            )
        )
    }

}
