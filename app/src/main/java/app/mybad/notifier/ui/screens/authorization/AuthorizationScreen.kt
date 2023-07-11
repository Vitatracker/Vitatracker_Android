package app.mybad.notifier.ui.screens.authorization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import app.mybad.theme.R
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem
import app.mybad.notifier.ui.screens.common.showToast

@Composable
fun StartAuthorizationScreen(
    onLoginButtonClicked: () -> Unit,
    onRegistrationButtonClicked: () -> Unit
) {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val viewModel: AuthorizationScreenViewModel = hiltViewModel()
//    val uiEventFlow = viewModel.uiEvent
//    val uiEventFlowLifecycleAware = remember(uiEventFlow, lifecycleOwner) {
//        uiEventFlow.flowWithLifecycle(lifecycleOwner.lifecycle)
//    }

//    LaunchedEffect(key1 = "StartAuthorizationScreenKey") {
//        uiEventFlowLifecycleAware.collect { message ->
//            showToast(context, message)
//        }
//    }

    Scaffold(
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                MainAuthorizationScreen(
                    onLoginButtonClicked = onLoginButtonClicked,
                    onRegistrationButtonClicked = onRegistrationButtonClicked
                )
            }
        }
    )
}

@Composable
fun MainAuthorizationScreen(
    onLoginButtonClicked: () -> Unit,
    onRegistrationButtonClicked: () -> Unit
) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        // ScreenBackgroundImage(R.drawable.ic_background_authorization_screen)
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            AuthorizationScreenImage()
            AuthorizationScreenButtonEntry(
                onLoginButtonClicked = onLoginButtonClicked,
                onRegistrationButtonClicked = onRegistrationButtonClicked
            )
            SurfaceSignInWith(onClick = { /*TODO*/ })
        }
    }
}

@Composable
private fun AuthorizationScreenImage() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_man_doctor),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun AuthorizationScreenButtonEntry(
    onLoginButtonClicked: () -> Unit,
    onRegistrationButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthorizationScreenButtonLogin(onLoginButtonClicked)
        AuthorizationScreenButtonRegistration(onRegistrationButtonClicked)
    }
}

@Composable
private fun AuthorizationScreenButtonLogin(onLoginButtonClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .fillMaxWidth(),
        onClick = { onLoginButtonClicked() },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_login),
            modifier = Modifier,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun AuthorizationScreenButtonRegistration(onRegistrationButtonClicked: () -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .padding(top = 8.dp, start = 15.dp, end = 15.dp, bottom = 16.dp)
            .fillMaxWidth(),
        onClick = { onRegistrationButtonClicked() },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Gray
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_registration),
            modifier = Modifier,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
