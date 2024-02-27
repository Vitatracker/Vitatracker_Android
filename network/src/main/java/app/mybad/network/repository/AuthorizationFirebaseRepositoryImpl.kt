package app.mybad.network.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.AuthorizationFirebaseDomainModel
import app.mybad.domain.repository.network.AuthorizationFirebaseRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class AuthorizationFirebaseRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebase: Firebase,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : AuthorizationFirebaseRepository {

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            context,
            buildGoogleSignInRequest()
        )
    }

    override suspend fun getAuthRequestIntent() = withContext(dispatcher) {
        runCatching {
            googleSignInClient.signInIntent.also {
                Log.w(
                    "VTTAG",
                    "AuthorizationFirebaseRepositoryImpl::getAuthRequestIntent: intent=${it}"
                )
            }
        }
    }

    override suspend fun signInWithIntent(intent: Intent) = withContext(dispatcher) {
        runCatching {
            Log.w(
                "VTTAG",
                "AuthorizationFirebaseRepositoryImpl::signInWithIntent: in ${intent}"
            )
            GoogleSignIn.getSignedInAccountFromIntent(intent).result.let { account ->
                Log.w(
                    "VTTAG",
                    "AuthorizationFirebaseRepositoryImpl::signInWithIntent: account=${account.idToken}"
                )
                account?.let {
                    Log.w(
                        "VTTAG",
                        "AuthorizationFirebaseRepositoryImpl::signInWithIntent: account=${account.idToken}"
                    )
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    firebase.auth.signInWithCredential(credential).result.user?.let { user ->
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
            }.also {
                Log.w(
                    "VTTAG",
                    "AuthorizationFirebaseRepositoryImpl::signInWithIntent: out"
                )
            }
        }
    }

    override suspend fun signOut() = withContext(dispatcher) {
        runCatching {
            val isSignOut = googleSignInClient.signOut().isSuccessful
            if (isSignOut) firebase.auth.signOut()
            // выйти из нашего сервера

            isSignOut
        }
    }

    private fun buildGoogleSignInRequest() = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(AuthToken.GOOGLE_CLIENT_ID)
        .requestEmail()
        .build()

}
