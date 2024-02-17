package app.mybad.domain.usecases.authorization

import android.content.Intent
import app.mybad.domain.repository.network.AuthorizationFirebaseRepository
import javax.inject.Inject

class SignInFirebaseGoogleAuthUseCase @Inject constructor(
    private val repository: AuthorizationFirebaseRepository,
) {
    suspend operator fun invoke(intent: Intent) = repository.signInWithIntent(intent)
}
