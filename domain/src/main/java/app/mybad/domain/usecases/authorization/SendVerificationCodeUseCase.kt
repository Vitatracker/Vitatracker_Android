package app.mybad.domain.usecases.authorization

import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import javax.inject.Inject

class SendVerificationCodeUseCase @Inject constructor(
    private val repository: AuthorizationNetworkRepository,
) {

    suspend operator fun invoke(code: Int) = repository.sendVerificationCode(code)
}
