package app.mybad.notifier.ui.screens.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.BuildConfig
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.settings.common.BaseDivider
import app.mybad.notifier.ui.screens.settings.common.SettingsAboutItem

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
        ClickableText(text = link("https://lnkd.in/eHz-Andj"), onClick = {

        })
        BaseDivider()
        Text(
            text = stringResource(R.string.settings_contacts_front_end_team),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        ClickableText(text = link("https://lnkd.in/eKz_n5Df"), onClick = {

        })

        ClickableText(text = link("https://lnkd.in/eaSMTNie"), modifier = Modifier.padding(top = 16.dp), onClick = {

        })
        BaseDivider()
        Text(
            text = stringResource(R.string.settings_contacts_android_team),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        ClickableText(text = link("https://lnkd.in/eGmXzP6b"), onClick = {

        })

        ClickableText(text = link("https://lnkd.in/eX4htR79"), modifier = Modifier.padding(top = 16.dp), onClick = {

        })
        BaseDivider()
    }
}

@Composable
fun link(text: String): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline, fontSize = 14.sp)) {
            append(text)
        }
    }
}