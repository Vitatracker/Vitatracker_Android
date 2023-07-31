package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.theme.R

@Composable
@Preview(showBackground = true)
fun SuccessMainScreen(
    onGo: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .padding(top = 110.dp)
                .fillMaxWidth()
                .padding(16.dp),
            painter = painterResource(R.drawable.done_png),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        StartScreenBottom {
            onGo()
        }
    }
}

@Composable
private fun StartScreenBottom(
    onGo: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.add_course_congratulations),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(50.dp))
        ReUseFilledButton(
            modifier = Modifier.fillMaxWidth(),
            textId = R.string.start_screen_go
        ) {
            onGo()
        }
    }
}
