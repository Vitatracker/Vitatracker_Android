package app.mybad.notifier.ui.screens.settings.about

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.BuildConfig
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.settings.common.SettingsAboutItem

@Composable
@Preview(showBackground = true)
fun SettingsAbout(
    modifier: Modifier = Modifier,
    onContacts: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "Версия ${BuildConfig.VERSION_NAME}",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        Text(text = stringResource(R.string.settings_current_build_year))
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        SettingsAboutItem(text = stringResource(R.string.settings_user_agreement), onSelected = {})
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        SettingsAboutItem(text = stringResource(R.string.settings_privacy_policy), onSelected = {})
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        SettingsAboutItem(text = stringResource(R.string.settings_our_contacts), onSelected = onContacts)
    }
}