package app.mybad.domain.usecases.user

import app.mybad.domain.repository.UserDataRepo
import kotlinx.coroutines.delay
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: UserDataRepo,
) {

    suspend operator fun invoke(oldPass: String, newPass: String) = runCatching {
        delay(2000L)
        // TODO do password change
    }

}
