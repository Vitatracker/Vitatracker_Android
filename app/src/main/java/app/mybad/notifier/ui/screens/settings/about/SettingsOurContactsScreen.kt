package app.mybad.notifier.ui.screens.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.common.HyperLinkText
import app.mybad.notifier.ui.screens.settings.common.BaseHorizontalDivider
import app.mybad.theme.R

@Composable
fun SettingsOurContactsScreen(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_contacts_back_end_team),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        HyperLinkText("https://lnkd.in/eHz-Andj")
        BaseHorizontalDivider()
        Text(
            text = stringResource(R.string.settings_contacts_front_end_team),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        HyperLinkText("https://lnkd.in/eKz_n5Df")
        Spacer(modifier = Modifier.height(16.dp))
        HyperLinkText("https://lnkd.in/eaSMTNie")
        BaseHorizontalDivider()
        Text(
            text = stringResource(R.string.settings_contacts_android_team),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        HyperLinkText(text = "https://lnkd.in/eGmXzP6b")
        Spacer(modifier = Modifier.height(16.dp))
        HyperLinkText(text = "https://lnkd.in/eX4htR79")
        BaseHorizontalDivider()
    }
}
