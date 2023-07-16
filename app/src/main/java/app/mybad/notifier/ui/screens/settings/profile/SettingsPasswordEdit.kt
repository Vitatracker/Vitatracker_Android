package app.mybad.notifier.ui.screens.settings.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.settings.common.DecoratedTextInput
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.theme.R

@Composable
@Preview(showBackground = true)
fun SettingsPasswordEdit(
    modifier: Modifier = Modifier,
    userModel: UserSettingsDomainModel = UserSettingsDomainModel(),
    onSave: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        UserImage(url = userModel.personal.avatar, showEdit = false) { }
        Spacer(Modifier.height(32.dp))
        DecoratedTextInput(
            label = stringResource(R.string.settings_current_password),
            enabled = true
        ) { }
        Spacer(Modifier.height(24.dp))
        DecoratedTextInput(
            label = stringResource(R.string.settings_new_password),
            enabled = true
        ) { }
        Spacer(Modifier.height(24.dp))
        DecoratedTextInput(
            label = stringResource(R.string.settings_repeat_new_password),
            enabled = true
        ) { }
        Spacer(Modifier.height(24.dp))
        NavigationRow(
            onBack = onDismiss::invoke,
            onNext = onSave::invoke,
            backLabel = stringResource(R.string.settings_cancel),
            nextLabel = stringResource(R.string.settings_save),
        )
    }
}
