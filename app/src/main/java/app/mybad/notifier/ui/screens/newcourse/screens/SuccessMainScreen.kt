package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.ScreenBackgroundImage
import app.mybad.notifier.ui.theme.Typography

@Composable
@Preview(showBackground = true)
fun SuccessMainScreen(
    modifier: Modifier = Modifier,
    onGo: () -> Unit = {}
) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        ScreenBackgroundImage()
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StartScreenImage(Modifier.padding(top = 110.dp))
            StartScreenBottom { onGo() }
        }
    }
}

@Composable
private fun StartScreenImage(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .fillMaxWidth()
            .padding(36.dp),
        painter = painterResource(R.drawable.done_png),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun StartScreenBottom(
    onGo: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.add_course_congratulations),
            style = Typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onGo::invoke,
            shape = MaterialTheme.shapes.small,
            contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(id = R.string.start_screen_go),
                fontWeight = FontWeight.Bold
            )
        }
    }
}