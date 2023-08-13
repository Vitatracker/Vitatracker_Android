package app.mybad.domain.usecases.user

import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersCountUseCase @Inject constructor(
    private val repository: UserRepository,
) {

    suspend operator fun invoke() = repository.getNumberOfUsers()

}
