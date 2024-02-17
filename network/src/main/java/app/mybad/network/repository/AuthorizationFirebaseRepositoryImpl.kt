package app.mybad.network.repository

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.AuthorizationFirebaseDomainModel
import app.mybad.domain.repository.network.AuthorizationFirebaseRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class AuthorizationFirebaseRepositoryImpl @Inject constructor(
    private val oneTapClient: SignInClient,
    private val firebase: Firebase,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : AuthorizationFirebaseRepository {
    override suspend fun getAuthRequestIntent(): PendingIntent? = withContext(dispatcher) {
        try {
            val intent = oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
            Log.w(
                "VTTAG",
                "AuthorizationFirebaseRepositoryImpl::getAuthRequestIntent: intent=${intent.pendingIntent.creatorPackage}"
            )
            intent?.pendingIntent
        } catch (e: Exception) {
            Log.w(
                "VTTAG",
                "AuthorizationFirebaseRepositoryImpl::getAuthRequestIntent: error=${e.localizedMessage}"
            )
            null
        }
    }

    override suspend fun signInWithIntent(intent: Intent) = withContext(dispatcher) {
        runCatching {
            Log.w(
                "VTTAG",
                "AuthorizationFirebaseRepositoryImpl::signInWithIntent: in"
            )
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            firebase.auth.signInWithCredential(googleCredentials).await().user?.let { user ->
                Log.w(
                    "VTTAG",
                    "AuthorizationFirebaseRepositoryImpl::signInWithIntent: user=${user.email}"
                )
                AuthorizationFirebaseDomainModel(
                    userId = user.uid,
                    userName = user.displayName ?: "",
                    profilePictureUrl = user.photoUrl?.toString() ?: "",
                    userEmail = user.email ?: "",
                )
            }
        }
    }

    override suspend fun signOut() = withContext(dispatcher) {
        runCatching {
            val isSignOut = oneTapClient.signOut().isSuccessful
            if (isSignOut) firebase.auth.signOut()
            // выйти из нашего сервера

            isSignOut
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(AuthToken.GOOGLE_CLIENT_ID)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

}
