package app.mybad.notifier.ui.screens.authorization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem

@Composable
fun StartAuthorizationScreen(
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel
) {
    Scaffold(
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                MainAuthorizationScreen(navController = navController, authVM = authVM)
            }
        }
    )
}

@Composable
fun MainAuthorizationScreen(
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel
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
            AuthorizationScreenButtonEntry(navController = navController, authVM = authVM)
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
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthorizationScreenButtonLogin(navController = navController)
        AuthorizationScreenButtonRegistration(navController = navController)
    }
}

@Composable
private fun AuthorizationScreenButtonLogin(navController: NavHostController) {
    Button(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .fillMaxWidth(),
        onClick = { navController.navigate(route = AuthorizationNavItem.Login.route) },
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
private fun AuthorizationScreenButtonRegistration(navController: NavHostController) {
    OutlinedButton(
        modifier = Modifier
            .padding(top = 8.dp, start = 15.dp, end = 15.dp, bottom = 16.dp)
            .fillMaxWidth(),
        onClick = { navController.navigate(route = AuthorizationNavItem.Registration.route) },
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
