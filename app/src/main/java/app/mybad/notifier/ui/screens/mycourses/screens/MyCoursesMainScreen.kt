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
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.screens.mycourses.CourseInfoScreen
import app.mybad.notifier.ui.screens.mycourses.MyCoursesIntent
import app.mybad.notifier.ui.screens.mycourses.MyCoursesNavItem
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.timeInMinutes
import app.mybad.utils.dateTimeTomorrow

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
                            state.value.remedies.firstOrNull { remedy ->
                                remedy.id == selectedCourse?.remedyId
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
                        remedies = state.value.remedies,
                        courses = state.value.courses,
                        usages = state.value.usages,
                        onSelect = { courseId ->
                            selectedCourse = state.value.courses.first { course ->
                                course.id == courseId
                            }
                            navHostController.navigate(MyCoursesNavItem.Course.route)
                        }
                    )
                }
                composable(MyCoursesNavItem.Course.route) {
                    if (state.value.remedies.isEmpty()) selectedCourse = null
                    selectedCourse?.let { selectedCourse ->
                        CourseInfoScreen(
                            course = selectedCourse,
                            remedy = state.value.remedies.firstOrNull { remedy ->
                                remedy.id == selectedCourse.remedyId
                            } ?: RemedyDomainModel(),
                            usagePattern = generatePattern(
                                selectedCourse.id,
                                state.value.usages
                            ),
                            reducer = {intent->
                                when (intent) {
                                    is MyCoursesIntent.Update -> {
                                        vm.reduce(
                                            MyCoursesIntent.Update(
                                                intent.course,
                                                intent.remedy,
                                                intent.usagesPattern
                                            )
                                        )
                                        navHostController.popBackStack()
                                    }

                                    is MyCoursesIntent.Delete -> {
                                        vm.reduce(MyCoursesIntent.Delete(selectedCourse.id))
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
    courseId: Long,
    usages: List<UsageDomainModel>
): List<UsageFormat> {
    if (usages.isNotEmpty()) {
        val list = usages.filter { it.courseId == courseId }
        if (list.isNotEmpty()) {
            //TODO("тут нужно понять что нужно, время или дата и время")
            // тут берется дата и время
            val firstTimeTomorrow = list.minBy { it.useTime }.useTime.dateTimeTomorrow()
            return list.mapNotNull { usage ->
                if (usage.useTime < firstTimeTomorrow) {
                    UsageFormat(
                        timeInMinutes = usage.useTime.timeInMinutes(),
                        quantity = usage.quantity
                    )
                } else null
            }
        }
    }
    return emptyList()
}
