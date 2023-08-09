package app.mybad.domain.usecases.user

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class TakeUserAuthTokenUseCase @Inject constructor(
    private val repository: UserRepository,
    private val clearUserAuthTokenUseCase: ClearUserAuthTokenUseCase,
    private val refreshAuthTokenUseCase: RefreshAuthTokenUseCase,
) {

    suspend operator fun invoke(currentDate: Long, userId: Long = -1, email: String = "") {
        try {
            val user = when {
                userId > 0 -> repository.getUserById(userId)
                email.isNotBlank() -> repository.getUserByEmail(email)
                else -> repository.getUserLastEntrance()
            }
            Log.w("VTTAG", "TakeUserAuthTokenUseCase:: Ok: userId=${user.id}")
            if (user.token.isNotBlank() && user.tokenDate > 0) {
                AuthToken.userId = user.id
                // проверить дату токена
                if (user.tokenDate <= currentDate + 600 && user.tokenRefreshDate > currentDate) {
                    // требуется обновить токен
                    refreshAuthTokenUseCase(currentDate)
                } else if (user.tokenRefreshDate <= currentDate) {
                    // требуется релогин
                    clearUserAuthTokenUseCase()
                } else {
                    AuthToken.userId = user.id
                    AuthToken.email = user.email
                    AuthToken.tokenRefreshDate = user.tokenRefreshDate
                    AuthToken.tokenRefresh = user.tokenRefresh
                    AuthToken.tokenDate = user.tokenDate
                    AuthToken.token = user.token
                }
            }
        } catch (e: Error) {
            Log.e("VTTAG", "TakeUserAuthTokenUseCase: Error", e)
        }
    }

}
