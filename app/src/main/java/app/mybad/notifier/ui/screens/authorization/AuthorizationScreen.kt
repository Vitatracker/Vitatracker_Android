package app.mybad.notifier.ui.screens.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem

@Composable
fun MainAuthorizationScreen(navController: NavHostController) {

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        AuthorizationScreenBackgroundImage()
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            AuthorizationScreenImage()
            AuthorizationScreenButtonEntry(navController = navController)
            SurfaceSignInWith(onClick = { /*TODO*/ })
        }
    }

}

@Composable
fun AuthorizationScreenBackgroundImage() {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_background_authorization_screen),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun AuthorizationScreenImage() {
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
fun AuthorizationScreenButtonEntry(navController: NavHostController) {
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
fun AuthorizationScreenButtonLogin(navController: NavHostController) {
    Button(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        onClick = { navController.navigate(route = AuthorizationNavItem.Login.route) },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_login),
            modifier = Modifier,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AuthorizationScreenButtonRegistration(navController: NavHostController) {
    ElevatedButton(
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
            .fillMaxWidth(),
        onClick = { navController.navigate(route = AuthorizationNavItem.Registration.route) },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_registration),
            modifier = Modifier,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
//    MainAuthorizationScreen()
}