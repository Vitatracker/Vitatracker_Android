package app.mybad.domain.usecases

import app.mybad.domain.repos.UsersRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: UsersRepository,
) {

    suspend operator fun invoke(email: String): Long =
        repository.insertUser(email = email) ?: -1

}
