package app.mybad.domain.usecases.user

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class GetUserPersonalDbUseCase @Inject constructor(
    private val repository: UserRepository,
) {

    suspend operator fun invoke() = repository.getUserPersonal(AuthToken.userId)

}
