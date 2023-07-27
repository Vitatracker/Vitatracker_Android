package app.mybad.domain.usecases.authorization

import app.mybad.domain.repos.AuthorizationRepo
import javax.inject.Inject

class RegistrationUserUseCase @Inject constructor(
    private val repository: AuthorizationRepo,
) {

    suspend operator fun invoke(
        login: String,
        password: String
    ) = repository.registrationUser(
        login = login,
        password = password
    )

}
