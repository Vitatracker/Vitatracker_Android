package app.mybad.domain.usecases.authorization

import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import net.openid.appauth.TokenRequest
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthorizationNetworkRepository,
) {

    suspend operator fun invoke(tokenRequest: TokenRequest) = repository.loginWithGoogle(tokenRequest)

}
