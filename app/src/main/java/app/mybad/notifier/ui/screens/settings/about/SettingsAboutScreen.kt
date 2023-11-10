package app.mybad.notifier.ui.screens.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.settings.common.BaseHorizontalDivider
import app.mybad.notifier.utils.packageVersionName
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow


@Composable
fun SettingsAboutScreen(
    state: SettingsAboutContract.State,
    effectFlow: Flow<SettingsAboutContract.Effect>? = null,
    sendEvent: (event: SettingsAboutContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsAboutContract.Effect.Navigation) -> Unit
) {
    val context = LocalContext.current
    val version = remember {context.packageVersionName}

    LaunchedEffect(SIDE_EFFECTS_KEY) {
//        version = context.packageVersionName
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsAboutContract.Effect.Navigation -> navigation(effect)
            }
        }
    }
    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.settings_about,
                onBackPressed = { sendEvent(SettingsAboutContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_version, version), //BuildConfig.VERSION_NAME
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
            Text(text = stringResource(R.string.settings_current_build_year))
            BaseHorizontalDivider()
            AboutItem(
                text = stringResource(R.string.settings_user_agreement),
                onSelected = {
                    sendEvent(SettingsAboutContract.Event.UserAgreementClicked)
                }
            )
            BaseHorizontalDivider()
            AboutItem(
                text = stringResource(R.string.settings_privacy_policy),
                onSelected = {
                    sendEvent(SettingsAboutContract.Event.PrivacyPolicyClicked)
                }
            )
            BaseHorizontalDivider()
            AboutItem(
                text = stringResource(R.string.settings_about_our_team),
                onSelected = { sendEvent(SettingsAboutContract.Event.AboutOurTeam) }
            )
        }
    }
}
