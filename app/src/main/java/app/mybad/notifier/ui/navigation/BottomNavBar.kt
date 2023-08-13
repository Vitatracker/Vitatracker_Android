package app.mybad.notifier.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.mybad.theme.R

@Composable
@Preview(showBackground = true)
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    onItemSelected: (MainScreens) -> Unit = {},
    onAddItemClicked: () -> Unit = {}
) {
    val backStackEntry = navController?.currentBackStackEntryAsState()
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
                    icon = MainScreens.Notifications.icon,
                    isSelected = backStackEntry?.value?.destination?.route == MainScreens.Notifications.route
                ) {
                    onItemSelected(MainScreens.Notifications)
                }
                MainNavigationItem(
                    icon = MainScreens.Courses.icon,
                    isSelected = backStackEntry?.value?.destination?.hierarchy?.any {
                        it.route == MainScreens.Courses.route
                    } ?: false
                ) {
                    onItemSelected(MainScreens.Courses)
                }
                Spacer(Modifier.width(64.dp))
                MainNavigationItem(
                    icon = MainScreens.Calendar.icon,
                    isSelected = backStackEntry?.value?.destination?.route == MainScreens.Calendar.route
                ) {
                    onItemSelected(MainScreens.Calendar)
                }
                MainNavigationItem(
                    icon = MainScreens.Settings.icon,
                    isSelected = backStackEntry?.value?.destination?.hierarchy?.any {
                        it.route == MainScreens.Settings.route
                    } ?: false
                ) {
                    onItemSelected(MainScreens.Settings)
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
                            onAddItemClicked()
                        }
                ) {
                    Icon(painterResource(R.drawable.plus_small), null, tint = Color.White)
                }
            }
        }
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
            interactionSource = remember { MutableInteractionSource() },
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
