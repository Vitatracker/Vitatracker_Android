package app.mybad.domain.usecases.authorization

import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import javax.inject.Inject

class RecoveryPasswordUseCase @Inject constructor(
    private val repository: AuthorizationNetworkRepository,
) {

    suspend operator fun invoke(email: String) = repository.restorePassword(email)

}
