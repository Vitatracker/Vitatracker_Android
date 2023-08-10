package app.mybad.notifier.ui.screens.settings.about_team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsAboutOurTeamScreen(
    state: SettingsAboutOurTeamScreenContract.State,
    events: Flow<SettingsAboutOurTeamScreenContract.Effect>? = null,
    onEventSent: (event: SettingsAboutOurTeamScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: SettingsAboutOurTeamScreenContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                SettingsAboutOurTeamScreenContract.Effect.Navigation.Back -> {
                    onNavigationRequested(SettingsAboutOurTeamScreenContract.Effect.Navigation.Back)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.settings_about_our_team,
                onBackPressed = { onEventSent(SettingsAboutOurTeamScreenContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            val appName: String = stringResource(id = R.string.app_name)
            Text(
                text = stringResource(R.string.settings_about_our_team_hello),
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = String.format(stringResource(R.string.settings_about_our_team_body_1), appName),
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_about_our_team_body_2),
                fontSize = 16.sp
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = String.format(stringResource(R.string.settings_about_our_team_end), appName),
                fontSize = 16.sp
            )
        }
    }
}