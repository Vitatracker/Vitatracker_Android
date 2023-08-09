package app.mybad.domain.usecases.user

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.network.AuthorizationNetworkRepository
import javax.inject.Inject

class RefreshAuthTokenUseCase @Inject constructor(
    private val authRepository: AuthorizationNetworkRepository,
    private val clearUserAuthTokenUseCase: ClearUserAuthTokenUseCase,
    private val updateUserAuthTokenUseCase: UpdateUserAuthTokenUseCase,
) {

    suspend operator fun invoke(currentDate: Long): Boolean {
        try {
            Log.e(
                "VTTAG",
                "RefreshAuthTokenUseCase: date=$currentDate tokenDate=${AuthToken.tokenDate}"
            )
            // требуется релогин
            if (AuthToken.userId == 0L || AuthToken.tokenRefreshDate <= currentDate || AuthToken.token == "") {
                clearUserAuthTokenUseCase()
                return false
            }
            // пока не требуется обновление токена
            if (AuthToken.tokenDate > currentDate + 600) return true
            // обновим токен
            authRepository.refreshToken().onSuccess { auth ->
                // обновим данные user db и AuthToken
                updateUserAuthTokenUseCase(
                    userId = AuthToken.userId,
                    token = auth.token,
                    tokenDate = auth.tokenDate,
                    tokenRefresh = auth.tokenRefresh,
                    tokenRefreshDate = auth.tokenRefreshDate
                )
            }
            return true
        } catch (e: Error) {
            Log.e("VTTAG", "RefreshAuthTokenUseCase: Error", e)
            return false
        }
    }
//{"statusCode":401,"message":"Access Denied: User is not authenticated","timestamp":1691614827122}
}
