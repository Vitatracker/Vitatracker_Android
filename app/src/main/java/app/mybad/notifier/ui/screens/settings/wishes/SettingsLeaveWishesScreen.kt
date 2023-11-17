package app.mybad.notifier.ui.screens.settings.wishes

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsLeaveWishesScreen(
    state: SettingsLeaveWishesContract.State,
    effectFlow: Flow<SettingsLeaveWishesContract.Effect>? = null,
    sendEvent: (event: SettingsLeaveWishesContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsLeaveWishesContract.Effect.Navigation) -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect {effect->
            when (effect) {
                is SettingsLeaveWishesContract.Effect.Navigation -> navigation(effect)

                SettingsLeaveWishesContract.Effect.SendMail -> {
                    context.sendMail(
                        mail = R.string.wishes_text_email.toString(),
                        subject = "Leave wishes"
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.settings_leave_your_wishes,
                onBackPressed = { sendEvent(SettingsLeaveWishesContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = 16.dp
                ),
        ) {
            Text(
                text = stringResource(id = R.string.wishes_text_header),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.wishes_text_main),
                softWrap = true,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(8.dp))
            ClickableText(
                text = AnnotatedString(text = stringResource(id = R.string.wishes_text_email)),
                onClick = {
                    sendEvent(SettingsLeaveWishesContract.Event.SendMail)
                },
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
            )
        }
    }
}

fun Context.sendMail(mail: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email" // or "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO: Handle case where no email app is available
    }
}
