package app.mybad.domain.usecases

import app.mybad.domain.repos.UsersRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: UsersRepository,
) {

    suspend operator fun invoke(name: String, email: String): Long =
        repository.insertUser(name = name, email = email) ?: -1

}
