package app.mybad.domain.usecases.user

import app.mybad.domain.repository.UserRepository
import javax.inject.Inject

class CheckDarkThemeUseCase @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(userId: Long) = repository.isDarkTheme(userId)

}
