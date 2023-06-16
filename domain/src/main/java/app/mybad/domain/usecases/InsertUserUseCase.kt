package app.mybad.domain.usecases

import app.mybad.domain.repos.UsersRepository
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(
    private val repository: UsersRepository,
) {

    suspend operator fun invoke(name: String, email: String) =
        repository.insertUser(name = name, email = email)

}
