package app.mybad.domain.usecases.authorization

import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import javax.inject.Inject

class SetNewPasswordUseCase @Inject constructor(
    private val repository: AuthorizationNetworkRepository,
) {
    suspend operator fun invoke(
        token: String,
        password: String,
        email: String
    ) = repository.setNewUserPassword(
        token = token,
        password = password,
        email = email
    )
}
