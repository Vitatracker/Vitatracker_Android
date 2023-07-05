package app.mybad.notifier.ui.screens.newcourse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import app.mybad.notifier.ui.screens.newcourse.screens.AddCourseMainScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedicineFirstScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedicineSecondScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddNotificationsMainScreen
import app.mybad.notifier.ui.screens.newcourse.screens.SuccessMainScreen
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCourseNavScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    vm: CreateCourseViewModel,
    onCancel: () -> Unit,
    onFinish: () -> Unit
) {
    val state by vm.state.collectAsState()
    val dest = navHostController.currentBackStackEntryAsState()
    val h = stringResource(R.string.add_med_h)
    var title by remember { mutableStateOf(h) }

    Column {
        if (dest.value?.destination?.route != NewCourseNavItem.Success.route) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (dest.value?.destination?.route == NewCourseNavItem.AddMedicineFirst.route) {
                                    onCancel()
                                } else {
                                    navHostController.popBackStack()
                                }
                            }
                    )
                }
            )
        }
        NavHost(
            modifier = modifier,
            navController = navHostController,
            startDestination = NewCourseNavItem.AddMedicineFirst.route
        ) {
            composable(NewCourseNavItem.AddMedicineFirst.route) {
                title = stringResource(R.string.add_med_h)
                AddMedicineFirstScreen(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    med = state.med,
                    reducer = vm::reduce,
                    onNext = {
                        navHostController.navigate(NewCourseNavItem.AddMedicineSecond.route)
                    },
                )
            }
            composable(NewCourseNavItem.AddMedicineSecond.route) {
                title = state.med.name ?: error("Med has empty name")
                AddMedicineSecondScreen(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    med = state.med,
                    reducer = vm::reduce,
                    onNext = {
                        navHostController.navigate(NewCourseNavItem.AddCourse.route)
                    },
                )
            }
            composable(NewCourseNavItem.AddCourse.route) {
                title = stringResource(R.string.add_course_h)
                AddCourseMainScreen(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    course = state.course,
                    reducer = vm::reduce,
                    onNext = {
                        navHostController.navigate(NewCourseNavItem.AddNotifications.route)
                    },
                )
            }
            composable(NewCourseNavItem.AddNotifications.route) {
                title = stringResource(R.string.add_course_notifications_time)
                AddNotificationsMainScreen(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    med = state.med,
                    reducer = vm::reduce,
                    onNext = { navHostController.navigate(NewCourseNavItem.Success.route) }
                )
            }
            composable(NewCourseNavItem.Success.route) {
                SuccessMainScreen(
                    onGo = onFinish::invoke
                )
            }
        }
    }
}
