package app.mybad.domain.usecases.user

import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: AuthorizationNetworkRepository,
) {

    suspend operator fun invoke(oldPass: String, newPass: String) = repository.changeUserPassword(
        oldPassword = oldPass,
        newPassword = newPass
    )
}
