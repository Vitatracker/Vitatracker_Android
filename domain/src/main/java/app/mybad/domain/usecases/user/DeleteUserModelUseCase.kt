package app.mybad.domain.usecases.user

import app.mybad.domain.repository.UserDataRepo
import javax.inject.Inject

class DeleteUserModelUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(id: String) {
        userDataRepo.deleteUser(id = id)
    }
}
