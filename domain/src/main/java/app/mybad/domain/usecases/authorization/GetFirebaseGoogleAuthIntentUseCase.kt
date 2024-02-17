package app.mybad.domain.usecases.authorization

import app.mybad.domain.repository.network.AuthorizationFirebaseRepository
import javax.inject.Inject

class GetFirebaseGoogleAuthIntentUseCase @Inject constructor(
    private val repository: AuthorizationFirebaseRepository,
) {
    suspend operator fun invoke() = repository.getAuthRequestIntent()
}
