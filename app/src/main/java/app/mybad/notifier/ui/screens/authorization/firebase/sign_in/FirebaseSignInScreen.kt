package app.mybad.notifier.ui.screens.authorization.firebase.sign_in

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.SignInWithGoogle
import app.mybad.notifier.ui.common.showToast
import kotlinx.coroutines.flow.Flow

@Composable
fun FirebaseSignInScreen(
    state: FirebaseSignInContract.State,
    effectFlow: Flow<FirebaseSignInContract.Effect>? = null,
    sendEvent: (event: FirebaseSignInContract.Event) -> Unit = {},
    navigation: (navigationEffect: FirebaseSignInContract.Effect.Navigation) -> Unit = {},
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                Log.w("VTTAG", "FirebaseSignInScreen::launcher: in")
                result.data?.let {
                    Log.w("VTTAG", "FirebaseSignInScreen::launcher: ok intent")
                    sendEvent(FirebaseSignInContract.Event.SignInWithGoogle(it))
                }
            }
        }
    )
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            context.showToast(error)
        }
    }
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is FirebaseSignInContract.Effect.Navigation -> navigation(effect)
                is FirebaseSignInContract.Effect.OpenAuthPage -> {
                    launcher.launch(
                        IntentSenderRequest.Builder(effect.intent.intentSender).build()
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        SignInWithGoogle {
            sendEvent(FirebaseSignInContract.Event.OpenGoogleLoginPage)
        }
    }
}
