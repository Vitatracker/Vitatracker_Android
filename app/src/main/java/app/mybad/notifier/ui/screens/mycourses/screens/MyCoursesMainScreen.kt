package app.mybad.notifier.ui.screens.mycourses.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.utils.plusDay
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesMainScreen() {
    val viewModel: MyCoursesViewModel = hiltViewModel()
//    var selectedCourse by remember { mutableStateOf<CourseDomainModel?>(null) }
    val state = viewModel.state.collectAsState()
//    val ncState = navHostController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
        CenterAlignedTopAppBar(title = {
            TitleText(textStringRes = R.string.my_course_h)
        })
    }) { paddingValues ->
        MyCourses(
            modifier = Modifier.padding(paddingValues),
            courses = state.value.courses,
            usages = state.value.usages,
            meds = state.value.meds,
            onSelect = {
//                        selectedCourse = state.value.courses.first { course -> course.id == it }
//                        navHostController.navigate(MyCoursesNavItem.Course.route)
            }
        )
    }
//    LazyColumn {
//        item {
//            TopAppBar(
//                title = {
//                    CenterAlignedTopAppBar(title = {
//                        TitleText(textStringRes = R.string.my_course_h)
//                    })
//                }
//            )
//        }
//        item {
//
//            NavHost(
//                navController = navHostController,
//                startDestination = MyCoursesNavItem.Main.route
//            ) {
//                composable(MyCoursesNavItem.Main.route) {
//
//                }
//                composable(MyCoursesNavItem.Course.route) {
//                    if (state.value.meds.isEmpty()) selectedCourse = null
//                    selectedCourse?.let { selected ->
//                        CourseInfoScreen(
//                            course = selected,
//                            med = state.value.meds.firstOrNull { it.id == selected.medId }
//                                ?: MedDomainModel(),
//                            usagePattern = generatePattern(selected.medId, state.value.usages),
//                            reducer = {
//                                when (it) {
//                                    is MyCoursesIntent.Update -> {
//                                        vm.reduce(
//                                            MyCoursesIntent.Update(
//                                                it.course,
//                                                it.med,
//                                                it.usagesPattern
//                                            )
//                                        )
//                                        navHostController.popBackStack()
//                                    }
//
//                                    is MyCoursesIntent.Delete -> {
//                                        vm.reduce(MyCoursesIntent.Delete(selected.id))
//                                        navHostController.popBackStack()
//                                    }
//                                }
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
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
