package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.theme.R

@Composable
@Preview(showBackground = true)
fun AddMedSuccessScreen(
    onGo: () -> Unit = {}
) {

    var canPress by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        StartScreenImage(Modifier.padding(top = 110.dp))
        StartScreenBottom {
            if (canPress) {
                canPress = false
                onGo()
            }
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
            .padding(16.dp),
        painter = painterResource(R.drawable.done_png),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun StartScreenBottom(
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.add_course_congratulations),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(50.dp))
        ReUseFilledButton(
            textId = R.string.start_screen_go,
            onClick = onClick,
        )
    }
}
