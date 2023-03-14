package app.mybad.notifier.ui.screens.settings.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.notifier.R

@Composable
@Preview(showBackground = true)
fun SettingsProfile(
    modifier: Modifier = Modifier,
    userModel: PersonalDomainModel = PersonalDomainModel(),
    savePersonal: () -> Unit ={},
    declinePersonal: () -> Unit ={},
    onAvatarEdit: () -> Unit = {},
    onPasswordEdit: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {

    val editUserName = remember { mutableStateOf(userModel.name) }
    val editEmail = remember { mutableStateOf(userModel.email) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        UserImage(url = userModel.avatar, showEdit = true, onEdit = onAvatarEdit::invoke)
        Spacer(Modifier.height(32.dp))
        SettingsProfileEditText(
            label = stringResource(id = R.string.settings_user_name),
            enabled = true,
            icon = Icons.Default.AccountCircle
        ) {
            editUserName.value = it
        }
        Spacer(Modifier.height(24.dp))
        SettingsProfileEditText(
            label = stringResource(id = R.string.settings_user_email),
            enabled = true,
            icon = Icons.Default.Email
        ) {
            editEmail.value = it
        }
        Spacer(Modifier.height(24.dp))
        when {
            userModel.name != editUserName.value -> SettingsProfileButtonSaveable(
                onDismiss = onDismiss,
                savePersonal = savePersonal,
                declinePersonal = declinePersonal
            )
            userModel.email != editEmail.value -> SettingsProfileButtonSaveable(
                onDismiss = onDismiss,
                savePersonal = savePersonal,
                declinePersonal = declinePersonal
            )
            userModel.email == editEmail.value -> SettingsProfileButtonChangePassword(
                onPasswordEdit = onPasswordEdit
            )
            userModel.name == editUserName.value -> SettingsProfileButtonChangePassword(
                onPasswordEdit = onPasswordEdit
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsProfileEditText(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean,
    icon: ImageVector,
    onEdit: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        placeholder = {
            Text(text = label, modifier = Modifier)
        },
        value = value,
        onValueChange = {
            onEdit(it)
            value = it
        },
        minLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.clearFocus() }
        ),
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}


@Composable
fun SettingsProfileButtonSaveable(
    onDismiss: () -> Unit,
    savePersonal: () -> Unit,
    declinePersonal: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = {
                onDismiss()
                declinePersonal()
            },
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.settings_cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Button(
            onClick = {
                onDismiss()
                savePersonal()
            },
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.settings_save),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SettingsProfileButtonChangePassword(
    onPasswordEdit: () -> Unit = {},
) {
    ElevatedButton(
        onClick = onPasswordEdit::invoke,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.settings_change_password),
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}