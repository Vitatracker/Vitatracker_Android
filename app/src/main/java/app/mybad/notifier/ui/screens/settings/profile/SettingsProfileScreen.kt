package app.mybad.notifier.ui.screens.settings.profile

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.common.ReUseOutlinedTextField
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.settings.common.BaseHorizontalDivider
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsProfileScreen(
    state: SettingsProfileContract.State,
    events: Flow<SettingsProfileContract.Effect>? = null,
    sendEvent: (event: SettingsProfileContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsProfileContract.Effect.Navigation) -> Unit
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
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ReUseOutlinedTextField(
            value = state.name,
            label = stringResource(id = R.string.settings_user_name),
            onValueChanged = { sendEvent(SettingsProfileContract.Event.OnUserNameChanged(it)) },
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_settings_user),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        )
        Spacer(Modifier.height(4.dp))
        ReUseOutlinedTextField(
            value = state.email,
            enabled = false,
            label = stringResource(id = R.string.settings_user_email),
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_settings_mail),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
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
            icon = ImageVector.vectorResource(id = R.drawable.icon_settings_lock),
            tint = MaterialTheme.colorScheme.primary,
            onClick = { sendEvent(SettingsProfileContract.Event.ChangePassword) }
        )
        SettingsProfileBottomElement(
            text = R.string.settings_quit,
            icon = ImageVector.vectorResource(id = R.drawable.icon_settings_exit),
            tint = Color.Gray,
            onClick = { sendEvent(SettingsProfileContract.Event.SignOut) }
        )
        SettingsProfileBottomElement(
            text = R.string.settings_delete_account,
            icon = Icons.Default.ErrorOutline,
            tint = MaterialTheme.colorScheme.error,
            onClick = { sendEvent(SettingsProfileContract.Event.DeleteAccount) }
        )
    }
}

@Composable
private fun SettingsProfileBottomElement(
    text: Int,
    icon: ImageVector,
    tint: Color,
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
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = tint
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        BaseHorizontalDivider()
    }
}
