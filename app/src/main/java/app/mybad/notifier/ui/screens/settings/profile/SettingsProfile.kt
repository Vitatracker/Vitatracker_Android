package app.mybad.notifier.ui.screens.settings.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.screens.reuse.ReUseOutlinedTextField
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsProfileScreen(
    state: ProfileScreenContract.State,
    events: Flow<ProfileScreenContract.Effect>? = null,
    onEventSent: (event: ProfileScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: ProfileScreenContract.Effect.Navigation) -> Unit
) {
    var showUpdateAvatarDialog by remember {
        mutableStateOf(false)
    }

//    var showUpdateAvatarDialog by remember {
//        mutableStateOf(false)
//    }

    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                ProfileScreenContract.Effect.Navigation.Back -> {
                    onNavigationRequested(ProfileScreenContract.Effect.Navigation.Back)
                }

                ProfileScreenContract.Effect.Navigation.ToChangePassword -> {
                    onNavigationRequested(ProfileScreenContract.Effect.Navigation.ToChangePassword)
                }

                ProfileScreenContract.Effect.Navigation.ToAuthorization -> {
                    onNavigationRequested(ProfileScreenContract.Effect.Navigation.ToAuthorization)
                }

                ProfileScreenContract.Effect.ShowDialog -> showUpdateAvatarDialog = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.navigation_settings_profile,
                onBackPressed = { onEventSent(ProfileScreenContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsProfileTop(state, onEventSent)
            SettingsProfileBottom(onEventSent)
        }
    }
    if (showUpdateAvatarDialog) {
        UpdateUserAvatarDialog(onDismissRequest = { showUpdateAvatarDialog = false }, onEventSent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserAvatarDialog(
    onDismissRequest: () -> Unit = {},
    onEventSent: (event: ProfileScreenContract.Event) -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.secondary,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(id = R.string.settings_profile_edit_avatar_dialog_1),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(48.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.settings_profile_edit_avatar_dialog_2),
                        fontSize = 16.sp
                    )
                    Icon(imageVector = Icons.Outlined.Image, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.settings_profile_edit_avatar_dialog_3),
                        fontSize = 16.sp
                    )
                    Icon(imageVector = Icons.Outlined.PhotoLibrary, contentDescription = null)
                }

            }
        }
    }
}

@Composable
private fun SettingsProfileTop(
    state: ProfileScreenContract.State,
    onEventSent: (event: ProfileScreenContract.Event) -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        UserImage(url = state.userAvatar) {
            onEventSent(ProfileScreenContract.Event.EditAvatar)
        }
        Spacer(Modifier.height(32.dp))
        ReUseOutlinedTextField(
            value = state.name,
            label = stringResource(id = R.string.settings_user_name),
            onValueChanged = { onEventSent(ProfileScreenContract.Event.OnUserNameChanged(it)) },
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
    onEventSent: (event: ProfileScreenContract.Event) -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        SettingsProfileBottomElement(
            text = R.string.settings_change_password,
            icon = ImageVector.vectorResource(id = R.drawable.icon_settings_lock),
            tint = MaterialTheme.colorScheme.primary,
            onClick = { onEventSent(ProfileScreenContract.Event.ChangePassword) }
        )
        SettingsProfileBottomElement(
            text = R.string.settings_quit,
            icon = ImageVector.vectorResource(id = R.drawable.icon_settings_exit),
            tint = Color.Gray,
            onClick = { onEventSent(ProfileScreenContract.Event.SignOut) }
        )
        SettingsProfileBottomElement(
            text = R.string.settings_delete_account,
            icon = Icons.Default.ErrorOutline,
            tint = MaterialTheme.colorScheme.error,
            onClick = { onEventSent(ProfileScreenContract.Event.DeleteAccount) }
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
                .padding(paddingValues = PaddingValues(start = 16.dp, end = 16.dp))
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
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    }
}
