package app.mybad.notifier.ui.screens.settings.about_team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsAboutTeamScreen(
    state: SettingsAboutTeamContract.State,
    effectFlow: Flow<SettingsAboutTeamContract.Effect>? = null,
    sendEvent: (event: SettingsAboutTeamContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsAboutTeamContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsAboutTeamContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.settings_about_our_team,
                onBackPressed = { sendEvent(SettingsAboutTeamContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,
                    bottom = 32.dp
                ),
        ) {
            val appName: String = stringResource(id = R.string.app_name)
            Text(
                text = stringResource(R.string.settings_about_our_team_hello),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_about_our_team_body_1, appName),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.settings_about_our_team_end, appName),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
