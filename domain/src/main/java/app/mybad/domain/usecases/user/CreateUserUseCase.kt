package app.mybad.domain.usecases.user

import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(name: String, email: String): Long =
        repository.insertUser(name = name, email = email) ?: -1

}
