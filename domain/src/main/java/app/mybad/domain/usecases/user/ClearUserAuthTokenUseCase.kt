package app.mybad.domain.usecases.user

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class ClearUserAuthTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke() {
        if (AuthToken.userId > 0) repository.clearTokenByUserId(AuthToken.userId)
        AuthToken.clear()
    }

}
