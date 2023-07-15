package app.mybad.notifier.ui.screens.settings.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.notifier.ui.screens.reuse.NavigateBackIconButton
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.screens.settings.SettingsIntent
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsProfile(
    userModel: PersonalDomainModel = PersonalDomainModel(),
    savePersonal: (SettingsIntent) -> Unit = {},
    onChangePasswordClicked: () -> Unit = {},
    onChangeAccountClicked: () -> Unit = {},
    onDeleteAccountClicked: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val editUserAvatar = remember { mutableStateOf(userModel.avatar) }
    val editUserName = remember { mutableStateOf(userModel.name) }
    val editEmail = remember { mutableStateOf(userModel.email) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { TitleText(textStringRes = R.string.navigation_settings_profile) },
                navigationIcon = { NavigateBackIconButton(onBackPressed) }
            )
        }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SettingsProfileTop(
                editEmail = editEmail,
                editUserName = editUserName,
                editUserAvatar = editUserAvatar
            )
            SettingsProfileBottom(
                onPasswordEdit = onChangePasswordClicked,
                onChangeAccountClicked = onChangeAccountClicked,
                onDeleteAccountClicked = onDeleteAccountClicked
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
private fun SettingsProfileBottom(
    onPasswordEdit: () -> Unit,
    onChangeAccountClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsProfileBottomElement(
            text = R.string.settings_change_password,
            icon = ImageVector.vectorResource(id = R.drawable.icon_settings_lock),
            tint = MaterialTheme.colorScheme.primary,
            onClick = { onPasswordEdit() }
        )
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        SettingsProfileBottomElement(
            text = R.string.settings_quit,
            icon = ImageVector.vectorResource(id = R.drawable.icon_settings_exit),
            tint = Color.Gray,
            onClick = onChangeAccountClicked
        )
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        SettingsProfileBottomElement(
            text = R.string.settings_delete_account,
            icon = Icons.Default.ErrorOutline,
            tint = MaterialTheme.colorScheme.error,
            onClick = onDeleteAccountClicked
        )
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
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
            Text(
                text = label,
                fontSize = 16.sp)
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
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                tint = Color.Gray
            )
        }
    )
}
