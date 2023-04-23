package app.mybad.notifier.ui.screens.settings.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.settings.SettingsIntent

@Composable
fun SettingsProfile(
    modifier: Modifier = Modifier,
    userModel: PersonalDomainModel = PersonalDomainModel(),
    savePersonal: (SettingsIntent) -> Unit,
    onPasswordEdit: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val editUserAvatar = remember { mutableStateOf(userModel.avatar) }
    val editUserName = remember { mutableStateOf(userModel.name) }
    val editEmail = remember { mutableStateOf(userModel.email) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        SettingsProfileTop(
            editEmail = editEmail,
            editUserName = editUserName,
            editUserAvatar = editUserAvatar
        )
        when {
            (userModel.name != editUserName.value) || (userModel.email != editEmail.value) || (userModel.avatar != editUserAvatar.value) -> SettingsProfileButtonSavable(
                onDismiss = onDismiss,
                editUserName = editUserName.value.toString(),
                editEmail = editEmail.value.toString(),
                editUserAvatar = editUserAvatar.value.toString(),
                savePersonal = savePersonal
            )

            (userModel.name == editUserName.value) || (userModel.email == editEmail.value) || (userModel.avatar == editUserAvatar.value) -> SettingsProfileBottom(
                onPasswordEdit = onPasswordEdit
            )
        }
    }
}

@Composable
private fun SettingsProfileTop(
    editEmail: MutableState<String?>,
    editUserAvatar: MutableState<String?>,
    editUserName: MutableState<String?>
) {
    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        UserImage(url = editUserAvatar.value, showEdit = true) {
            editUserAvatar.value = it
        }
        Spacer(Modifier.height(32.dp))
        SettingsProfileEditText(
            label = stringResource(id = R.string.settings_user_name),
            enabled = true,
            icon = R.drawable.icon_settings_user,
            valueString = editUserName.value.toString()
        ) {
            editUserName.value = it
        }
        Spacer(Modifier.height(24.dp))
        SettingsProfileEditText(
            label = stringResource(id = R.string.settings_user_email),
            enabled = true,
            icon = R.drawable.icon_settings_mail,
            valueString = editEmail.value.toString()
        ) {
            editEmail.value = it
        }
    }
}

@Composable
private fun SettingsProfileBottom(onPasswordEdit: () -> Unit) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsProfileBottomElement(
            text = R.string.settings_change_password,
            icon = ImageVector.vectorResource(id = R.drawable.icon_settings_lock),
            tint = MaterialTheme.colorScheme.primary
        )
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        SettingsProfileBottomElement(
            text = R.string.settings_delete_account,
            icon = ImageVector.vectorResource(id = R.drawable.icon_settings_exit),
            tint = Color.Gray
        )
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        SettingsProfileBottomElement(
            text = R.string.settings_change_password,
            icon = Icons.Default.ErrorOutline,
            tint = MaterialTheme.colorScheme.error
        )
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun SettingsProfileBottomElement(text: Int, icon: ImageVector, tint: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = text), modifier = Modifier, fontSize = 20.sp)
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = tint
        )
    }
}

@Composable
private fun SettingsProfileEditText(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean,
    icon: Int = 0,
    valueString: String,
    onEdit: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf(valueString) }

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
        trailingIcon = {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                tint = Color.Gray
            )
        }
    )
}

@Composable
private fun SettingsProfileButtonSavable(
    onDismiss: () -> Unit,
    editUserName: String = "",
    editEmail: String = "",
    editUserAvatar: String = "",
    savePersonal: (SettingsIntent) -> Unit,
    userModel: UserDomainModel = UserDomainModel()
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                onDismiss()
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
                savePersonal(
                    SettingsIntent.SetPersonal(
                        PersonalDomainModel(
                            name = editUserName,
                            age = userModel.personal.age,
                            avatar = editUserAvatar,
                            email = editEmail
                        )
                    )
                )
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
private fun SettingsProfileButtonChangePassword(
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
