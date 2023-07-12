package app.mybad.notifier.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.theme.R

@Composable
fun SplashScreen(
    proceedToMain: () -> Unit,
    proceedToAuthorization: () -> Unit,
    viewModel: SplashScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        viewModel.effect.collect {
            when (it) {
                SplashScreenEffect.NavigateNext -> {
                    proceedToAuthorization()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle(SplashScreenState.Initial)
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_frau_doctor),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        when (screenState) {
            SplashScreenState.Authorized -> proceedToMain()
            SplashScreenState.Initial -> {}
            SplashScreenState.NotAuthorized -> {
                Spacer(modifier = Modifier.height(24.dp))
                NewUserGreeting(viewModel::onBeginClicked)
            }
        }
    }
}

@Composable
private fun NewUserGreeting(onBeginClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                append(stringResource(R.string.splash_welcome_in))
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(stringResource(id = R.string.app_name))
                }
            },
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.splash_welcome_text),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        ReUseFilledButton(
            textId = R.string.action_begin,
            onClick = { onBeginClicked() }
        )
    }
}