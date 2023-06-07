package app.mybad.notifier.ui.screens.reuse

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun HyperLinkText(link: String, modifier: Modifier = Modifier) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline, fontSize = 14.sp)) {
            append(link)
        }
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(text = annotatedString, modifier = modifier, onClick = {
        uriHandler.openUri(annotatedString.text)
    })
}