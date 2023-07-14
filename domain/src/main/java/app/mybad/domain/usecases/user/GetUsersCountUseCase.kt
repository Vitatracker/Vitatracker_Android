package app.mybad.domain.usecases.user

import app.mybad.domain.repos.UserDataRepo
import javax.inject.Inject

class GetUsersCountUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute() = userDataRepo.getUsersCount()
}