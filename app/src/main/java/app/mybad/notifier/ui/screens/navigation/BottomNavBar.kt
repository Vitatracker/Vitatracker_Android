package app.mybad.notifier.ui.screens.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.mybad.notifier.R

@Composable
@Preview(showBackground = true)
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    isVisible: Boolean = true
) {
    val backStackEntry = navController?.currentBackStackEntryAsState()
    var selected by remember { mutableStateOf(NavItemMain.Notifications.route) }

    if (isVisible) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp)
                    .height(80.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(Modifier.width(0.dp))
                    MainNavigationItem(
                        icon = NavItemMain.Notifications.icon,
                        isSelected = backStackEntry?.value?.destination?.route == NavItemMain.Notifications.route
                    ) {
                        selected = NavItemMain.Notifications.route
                        navController?.popBackStack(NavItemMain.Notifications.route, false)
                        navController?.navigate(selected)
                    }
                    MainNavigationItem(
                        icon = NavItemMain.Courses.icon,
                        isSelected = backStackEntry?.value?.destination?.route == NavItemMain.Courses.route
                    ) {
                        selected = NavItemMain.Courses.route
                        navController?.popBackStack(NavItemMain.Notifications.route, false)
                        navController?.navigate(selected)
                    }
                    Spacer(Modifier.width(64.dp))
                    MainNavigationItem(
                        icon = NavItemMain.Calendar.icon,
                        isSelected = backStackEntry?.value?.destination?.route == NavItemMain.Calendar.route
                    ) {
                        selected = NavItemMain.Calendar.route
                        navController?.popBackStack(NavItemMain.Notifications.route, false)
                        navController?.navigate(selected)
                    }
                    MainNavigationItem(
                        icon = NavItemMain.Settings.icon,
                        isSelected = backStackEntry?.value?.destination?.route == NavItemMain.Settings.route
                    ) {
                        selected = NavItemMain.Settings.route
                        navController?.popBackStack(NavItemMain.Notifications.route, false)
                        navController?.navigate(selected)
                    }
                    Spacer(Modifier.width(0.dp))
                }
            }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .size(84.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .size(64.dp)
                            .clickable {
                                selected = NavItemMain.Add.route
                                navController?.popBackStack(NavItemMain.Notifications.route, false)
                                navController?.navigate(selected)
                            }
                    ) {
                        Icon(painterResource(R.drawable.plus_small), null, tint = Color.White)
                    }
                }
            }
        }
    } else {
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun MainNavigationItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    isSelected: Boolean = false,
    onSelect: () -> Unit,
) {
    val indicatorColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.outlineVariant else Color.Transparent,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 40,
            easing = LinearOutSlowInEasing
        )
    )
    val iconAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 40,
            easing = LinearOutSlowInEasing
        )
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            indication = null,
            interactionSource = MutableInteractionSource(),
            onClick = onSelect::invoke
        )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
                .size(32.dp)
                .alpha(iconAlpha)
                .padding(bottom = 4.dp)
        )
        Surface(
            shape = CircleShape,
            modifier = Modifier.size(5.dp),
            color = indicatorColor
        ) { }
    }
}
