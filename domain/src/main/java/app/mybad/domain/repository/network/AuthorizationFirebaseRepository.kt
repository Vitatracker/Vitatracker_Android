package app.mybad.domain.repository.network

import android.content.Intent
import app.mybad.domain.models.AuthorizationFirebaseDomainModel

interface AuthorizationFirebaseRepository {
    suspend fun getAuthRequestIntent(): Result<Intent>
    suspend fun signInWithIntent(intent: Intent): Result<AuthorizationFirebaseDomainModel?>
    suspend fun signOut(): Result<Boolean>
}
