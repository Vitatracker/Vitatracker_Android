package app.mybad.notifier.ui.screens.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.BuildConfig
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.theme.R
import app.mybad.notifier.ui.screens.settings.common.SettingsAboutItem
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsAbout(
    state: SettingsAboutScreenContract.State,
    events: Flow<SettingsAboutScreenContract.Effect>? = null,
    onEventSent: (event: SettingsAboutScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: SettingsAboutScreenContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                SettingsAboutScreenContract.Effect.Navigation.Back -> {
                    onNavigationRequested(SettingsAboutScreenContract.Effect.Navigation.Back)
                }

                SettingsAboutScreenContract.Effect.Navigation.AboutOurTeam -> {
                    onNavigationRequested(SettingsAboutScreenContract.Effect.Navigation.AboutOurTeam)
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.settings_about,
                onBackPressed = { onEventSent(SettingsAboutScreenContract.Event.ActionBack) }
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
                text = String.format(stringResource(R.string.settings_version), BuildConfig.VERSION_NAME),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
            Text(text = stringResource(R.string.settings_current_build_year))
            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SettingsAboutItem(
                text = stringResource(R.string.settings_user_agreement),
                onSelected = {
                    onEventSent(SettingsAboutScreenContract.Event.UserAgreementClicked)
                }
            )
            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SettingsAboutItem(
                text = stringResource(R.string.settings_privacy_policy),
                onSelected = {
                    onEventSent(SettingsAboutScreenContract.Event.PrivacyPolicyClicked)
                }
            )
            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            SettingsAboutItem(
                text = stringResource(R.string.settings_about_our_team),
                onSelected = { onEventSent(SettingsAboutScreenContract.Event.AboutOurTeam) }
            )
        }
    }
}
