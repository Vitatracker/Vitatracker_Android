package app.mybad.data.repos

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.mybad.domain.repos.AuthorizationRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationRepoImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AuthorizationRepo {
    override suspend fun loginWithFacebook() {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithGoogle() {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithEmail(login: String, password: String) {
        TODO("Not yet implemented")
    }

}
