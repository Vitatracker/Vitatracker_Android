package app.mybad.domain.usecases.authorization

import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import javax.inject.Inject

class RegistrationUserUseCase @Inject constructor(
    private val repository: AuthorizationNetworkRepository,
) {

    suspend operator fun invoke(
        login: String,
        password: String,
        userName: String = "",
    ) = repository.registrationUser(
        login = login,
        password = password,
        userName = userName,
    )

}
