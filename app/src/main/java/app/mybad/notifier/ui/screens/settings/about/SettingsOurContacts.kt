package app.mybad.notifier.ui.screens.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.theme.R
import app.mybad.notifier.ui.screens.reuse.HyperLinkText
import app.mybad.notifier.ui.screens.settings.common.BaseDivider

@Composable
fun SettingsOurContacts(modifier: Modifier = Modifier) {
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
        BaseDivider()
        Text(
            text = stringResource(R.string.settings_contacts_front_end_team),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        HyperLinkText("https://lnkd.in/eKz_n5Df")
        HyperLinkText("https://lnkd.in/eaSMTNie", modifier = Modifier.padding(top = 16.dp))
        BaseDivider()
        Text(
            text = stringResource(R.string.settings_contacts_android_team),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        HyperLinkText("https://lnkd.in/eGmXzP6b")
        HyperLinkText("https://lnkd.in/eX4htR79", modifier = Modifier.padding(top = 16.dp))
        BaseDivider()
    }
}