package app.mybad.notifier.ui.screens.settings.profile

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.common.ReUseOutlinedTextField
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.settings.common.BaseHorizontalDivider
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsProfileScreen(
    state: SettingsProfileContract.State,
    events: Flow<SettingsProfileContract.Effect>? = null,
    sendEvent: (event: SettingsProfileContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsProfileContract.Effect.Navigation) -> Unit = {},
) {

    LaunchedEffect(key1 = true) {
        events?.collect { event ->
            when (event) {
                is SettingsProfileContract.Effect.Navigation -> navigation(event)
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.navigation_settings_profile,
                onBackPressed = { sendEvent(SettingsProfileContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = 120.dp
                ),
        ) {
            SettingsProfileTop(state, sendEvent)
            SettingsProfileBottom(sendEvent)
        }
    }
}

@Composable
private fun SettingsProfileTop(
    state: SettingsProfileContract.State,
    sendEvent: (event: SettingsProfileContract.Event) -> Unit = {}
) {
    Column {
        ReUseOutlinedTextField(
            value = state.name,
            label = stringResource(id = R.string.settings_user_name),
            onValueChanged = { sendEvent(SettingsProfileContract.Event.OnUserNameChanged(it)) },
            trailingIcon = R.drawable.icon_settings_user,
            tint = MaterialTheme.colorScheme.surfaceDim,
        )
        Spacer(Modifier.height(4.dp))
        ReUseOutlinedTextField(
            value = state.email,
            enabled = false,
            label = stringResource(id = R.string.settings_user_email),
            trailingIcon = R.drawable.icon_settings_mail,
            tint = MaterialTheme.colorScheme.surfaceDim,
        )
    }
}

@Composable
private fun SettingsProfileBottom(
    sendEvent: (event: SettingsProfileContract.Event) -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingsProfileBottomElement(
            text = R.string.settings_change_password,
            icon = R.drawable.icon_settings_lock,
            tint = MaterialTheme.colorScheme.primary,
            onClick = { sendEvent(SettingsProfileContract.Event.ChangePassword) }
        )
        SettingsProfileBottomElement(
            text = R.string.settings_quit,
            icon = R.drawable.icon_settings_exit,
            tint = MaterialTheme.colorScheme.surfaceDim,
            onClick = { sendEvent(SettingsProfileContract.Event.SignOut) }
        )
        SettingsProfileBottomElement(
            text = R.string.settings_delete_account,
            onClick = { sendEvent(SettingsProfileContract.Event.DeleteAccount) }
        )
    }
}

@Composable
private fun SettingsProfileBottomElement(
    @StringRes text: Int,
    @DrawableRes icon: Int? = null,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = text),
                fontSize = 16.sp
            )
            Icon(
                imageVector = if (icon != null) ImageVector.vectorResource(icon) else Icons.Default.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (icon != null) tint else MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        BaseHorizontalDivider()
    }
}

@Preview(
    showBackground = true,
    widthDp = 720, heightDp = 1220,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreview"
)
@Preview(
    showBackground = true,
    widthDp = 320, heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewSmall"
)
@Composable
fun SettingsProfileScreenPreview() {
    MyBADTheme {
        SettingsProfileScreen(SettingsProfileContract.State())
    }
}
