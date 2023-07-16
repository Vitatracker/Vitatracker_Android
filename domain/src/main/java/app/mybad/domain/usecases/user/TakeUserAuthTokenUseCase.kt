package app.mybad.domain.usecases.user

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class TakeUserAuthTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(currentDate: Long, userId:Long = -1, email: String = "") {
        try {
            val user = when {
                userId > 0 -> repository.getUserById(userId)
                email.isNotBlank() -> repository.getUserByEmail(email)
                else -> repository.getUserLastEntrance()
            }
            Log.w("VTTAG", "TakeUserAuthTokenUseCase:: Ok: userId=${user.id}")
            if (user.token.isNotBlank() && user.tokenDate > 0) {
                // проверить дату токена
                if (user.tokenDate < currentDate) {
                    // обновить токен
                } else if (user.tokenRefreshDate < currentDate) {
                    // релогин
                } else {
                    // проверить дуту рефреш токена
                    AuthToken.userId = user.id
                    AuthToken.token = user.token
                    AuthToken.tokenRefresh = user.tokenRefresh
                    AuthToken.email = user.email
                }
            }
        } catch (e: Error) {
            Log.e("VTTAG", "TakeUserAuthTokenUseCase: Error", e)
        }
    }

}
