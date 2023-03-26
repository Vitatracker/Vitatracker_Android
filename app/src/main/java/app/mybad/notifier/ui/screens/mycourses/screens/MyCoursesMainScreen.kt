package app.mybad.notifier.ui.screens.mycourses.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.mycourses.*

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
                        text = if(ncState.value?.destination?.route == MyCoursesNavItem.Main.route) stringResource(R.string.my_course_h)
                        else state.value.meds.firstOrNull { it.id == selectedCourse?.medId }?.name ?: "no data",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp)
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
                        meds = state.value.meds,
                        onSelect = {
                            selectedCourse = state.value.courses.first { c -> c.id == it }
                            navHostController.navigate(MyCoursesNavItem.Course.route)
                        }
                    )
                }
                composable(MyCoursesNavItem.Course.route) {
                    if(selectedCourse != null) {
                        CourseInfoScreen(
                            course = selectedCourse!!,
                            med = state.value.meds.first { it.id == selectedCourse!!.medId },
                            usagePattern = generatePattern(selectedCourse!!.medId, state.value.usages),
                            onSave = {
                                navHostController.popBackStack()
                            },
                            onDelete = {
                                navHostController.popBackStack()
                                vm.reduce(MyCoursesIntent.Delete(it))
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun generatePattern(
    medId : Long,
    usages: List<UsageCommonDomainModel>
) : List<Pair<Long, Int>> {
    if (usages.isNotEmpty()) {
        val list = usages.filter { it.medId == medId }
        if(list.isNotEmpty()) {
            val firstTime = list.minByOrNull { it.useTime }!!.useTime
            val prePattern = list.filter { it.useTime < (firstTime + 86400) }
            if(prePattern.isNotEmpty()) return prePattern.map { it.useTime to it.quantity }
        }
    }
    return emptyList()
}