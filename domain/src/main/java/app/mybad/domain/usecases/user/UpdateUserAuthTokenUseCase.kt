package app.mybad.domain.usecases.user

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserAuthTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        userId: Long,
        token: String,
        tokenDate: Long,
        tokenRefresh: String,
        tokenRefreshDate: Long,
    ) {
        if (userId > 0) {
            val user = repository.updateTokenByUserId(
                userId = userId,
                token = token,
                tokenDate = tokenDate,
                tokenRefresh = tokenRefresh,
                tokenRefreshDate = tokenRefreshDate,
            )
            Log.w("VTTAG", "UpdateUserAuthTokenUseCase:: Ok: userId=${user.id} token=${user.token}")
            AuthToken.userId = user.id
            AuthToken.token = user.token
            AuthToken.tokenRefresh = user.tokenRefresh
            AuthToken.email = user.email
        }
    }

}
