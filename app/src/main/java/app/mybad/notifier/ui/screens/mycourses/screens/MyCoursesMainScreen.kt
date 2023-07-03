package app.mybad.notifier.ui.screens.mycourses.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.ui.screens.mycourses.CourseInfoScreen
import app.mybad.notifier.ui.screens.mycourses.MyCoursesIntent
import app.mybad.notifier.ui.screens.mycourses.MyCoursesNavItem
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.plusDay
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesMainScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    vm: MyCoursesViewModel,
) {
    var selectedCourse by remember { mutableStateOf<CourseDomainModel?>(null) }
    val state = vm.state.collectAsState()
    val ncState = navHostController.currentBackStackEntryAsState()

    LazyColumn {
        item {
            TopAppBar(
                title = {
                    Text(
                        text = if (ncState.value?.destination?.route == MyCoursesNavItem.Main.route) {
                            stringResource(
                                R.string.my_course_h
                            )
                        } else {
                            state.value.meds.firstOrNull {
                                it.id == selectedCourse?.medId
                            }?.name ?: "no data"
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            )
        }
        item {
            NavHost(
                modifier = modifier,
                navController = navHostController,
                startDestination = MyCoursesNavItem.Main.route
            ) {
                composable(MyCoursesNavItem.Main.route) {
                    MyCourses(
                        courses = state.value.courses,
                        usages = state.value.usages,
                        meds = state.value.meds,
                        onSelect = {
                            selectedCourse = state.value.courses.first { course -> course.id == it }
                            navHostController.navigate(MyCoursesNavItem.Course.route)
                        }
                    )
                }
                composable(MyCoursesNavItem.Course.route) {
                    if (state.value.meds.isEmpty()) selectedCourse = null
                    selectedCourse?.let { selected ->
                        CourseInfoScreen(
                            course = selected,
                            med = state.value.meds.firstOrNull { it.id == selected.medId }
                                ?: MedDomainModel(),
                            usagePattern = generatePattern(selected.medId, state.value.usages),
                            reducer = {
                                when (it) {
                                    is MyCoursesIntent.Update -> {
                                        vm.reduce(
                                            MyCoursesIntent.Update(
                                                it.course,
                                                it.med,
                                                it.usagesPattern
                                            )
                                        )
                                        navHostController.popBackStack()
                                    }

                                    is MyCoursesIntent.Delete -> {
                                        vm.reduce(MyCoursesIntent.Delete(selected.id))
                                        navHostController.popBackStack()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun generatePattern(
    medId: Long,
    usages: List<UsageCommonDomainModel>
): List<Pair<Long, Int>> {
    if (usages.isNotEmpty()) {
        val list = usages.filter { it.medId == medId }
        if (list.isNotEmpty()) {
            val firstTimeTomorrow = list.minBy { it.useTime }.useTime.plusDay()
            return list.mapNotNull { usage ->
                if (usage.useTime < firstTimeTomorrow) usage.useTime to usage.quantity
                else null
            }
        }
    }
    return emptyList()
}
