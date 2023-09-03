package app.mybad.domain.usecases.user

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserNameDbUseCase @Inject constructor(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(userName: String) = repository.updateName(AuthToken.userId, userName)

}
