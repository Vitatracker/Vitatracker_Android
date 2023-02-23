package app.mybad.notifier.ui.screens.settings.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.notifier.ui.screens.settings.common.DecoratedTextInput
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.notifier.R

@Composable
@Preview(showBackground = true)

fun SettingsProfile(
    modifier: Modifier = Modifier,
    userModel: UserDomainModel = UserDomainModel(),
    onAvatarEdit: () -> Unit = {},
    onPasswordEdit: () -> Unit = {},
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        UserImage(url = userModel.personal.avatar, showEdit = true, onEdit = onAvatarEdit::invoke)
        Spacer(Modifier.height(32.dp))
        DecoratedTextInput(
            label = userModel.id,
            enabled = false
        ) {}
        Spacer(Modifier.height(24.dp))
        DecoratedTextInput(
            label = userModel.id,
            enabled = false
        ) {}
        Spacer(Modifier.height(24.dp))
        ElevatedButton(
            onClick = onPasswordEdit::invoke,
            modifier = Modifier.fillMaxWidth()
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
}