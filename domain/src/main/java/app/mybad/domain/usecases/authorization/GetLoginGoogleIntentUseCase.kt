package app.mybad.domain.usecases.authorization

import app.mybad.domain.repository.network.AuthorizationGoogleRepository
import javax.inject.Inject

class GetLoginGoogleIntentUseCase @Inject constructor(
    private val repository: AuthorizationGoogleRepository,
) {
    suspend operator fun invoke() = repository.getAuthRequestIntent()
}
