package app.mybad.notifier.ui.screens.settings.profile

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.notifier.ui.screens.settings.common.DecoratedTextInput
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.settings.SettingsIntent

@Composable
//@Preview(showBackground = true)
fun SettingsProfileEdit(
    modifier: Modifier = Modifier,
    userModel: PersonalDomainModel = PersonalDomainModel(),
    onSave: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        UserImage(url = userModel.avatar)
        Spacer(Modifier.height(32.dp))
        DecoratedTextInput(
            label = userModel.name.toString(),
            enabled = true
        ) { }
        Spacer(Modifier.height(24.dp))
        DecoratedTextInput(
            label = userModel.email.toString(),
            enabled = true
        ) { }
        Spacer(Modifier.height(24.dp))
        NavigationRow(
            onBack = onDismiss::invoke,
            onNext = onSave::invoke,
            backLabel = stringResource(R.string.settings_cancel),
            nextLabel = stringResource(R.string.settings_save)
        )
    }
}