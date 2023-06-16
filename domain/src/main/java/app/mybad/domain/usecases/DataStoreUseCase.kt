package app.mybad.domain.usecases

import app.mybad.domain.repos.DataStoreRepository
import javax.inject.Inject

class DataStoreUseCase @Inject constructor(
    private val repository: DataStoreRepository,
) {

    val token = repository.observeToken
    val userId = repository.observeUserId
    val mail = repository.observeMail

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

    suspend fun updateMail(mail: String) {
        try {
            repository.updateMail(mail)
        } catch (ignore: Exception) {

        }
    }
    suspend fun clear() {
        try {
            updateToken("")
            updateUserId(-1)
            updateMail("")
        } catch (ignore: Exception) {

        }
    }
}

