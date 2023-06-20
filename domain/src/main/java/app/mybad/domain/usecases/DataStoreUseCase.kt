package app.mybad.domain.usecases

import app.mybad.domain.repos.DataStoreRepository
import javax.inject.Inject

class DataStoreUseCase @Inject constructor(
    private val repository: DataStoreRepository,
) {

    val token = repository.observeToken
    val userId = repository.observeUserId
    val email = repository.observeEmail

    suspend fun updateToken(token: String) {
        try {
            repository.updateToken(token)
        } catch (ignore: Exception) {

        }
    }

    suspend fun updateUserId(userId: Long) {
        try {
            repository.updateUserId(userId)
        } catch (ignore: Exception) {

        }
    }

    suspend fun updateEmail(email: String) {
        try {
            repository.updateEmail(email)
        } catch (ignore: Exception) {

        }
    }
    suspend fun clear() {
        try {
            updateToken("")
            updateUserId(-1)
            updateEmail("")
        } catch (ignore: Exception) {

        }
    }
}

