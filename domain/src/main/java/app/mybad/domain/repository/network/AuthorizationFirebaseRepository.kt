package app.mybad.domain.repository.network

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import app.mybad.domain.models.AuthorizationFirebaseDomainModel

interface AuthorizationFirebaseRepository {
    suspend fun getAuthRequestIntent(): PendingIntent?
    suspend fun signInWithIntent(intent: Intent): Result<AuthorizationFirebaseDomainModel?>
    suspend fun signOut(): Result<Boolean>
}
