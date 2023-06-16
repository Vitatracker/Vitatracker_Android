package app.mybad.domain.usecases

import app.mybad.domain.repos.UsersRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val repository: UsersRepository,
) {

    suspend operator fun invoke(email: String) = repository.getUserId(email)

}
