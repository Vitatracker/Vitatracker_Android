package app.mybad.domain.usecases.user

import app.mybad.domain.repository.UserDataRepo
import javax.inject.Inject

class GetUserPersonalUseCase @Inject constructor(
    private val repository: UserDataRepo,
) {

    suspend operator fun invoke() = repository.getUserPersonal()

}
