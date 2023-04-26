package app.mybad.domain.usecases.auth

import app.mybad.domain.repos.DataStoreRepo
import javax.inject.Inject

class AuthorizationLoadTokenUseCase @Inject constructor(
    private val dataStore: DataStoreRepo
) {

    suspend fun execute() = dataStore.getToken()

}