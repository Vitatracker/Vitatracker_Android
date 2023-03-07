package app.mybad.notifier.ui.screens.mycourses

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
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesMainScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    courses: List<CourseDomainModel>,
    meds: List<MedDomainModel>,
    usages: List<UsageCommonDomainModel>
) {

    var selectedCourse by remember { mutableStateOf<CourseDomainModel?>(null) }

    LazyColumn {
        item {
            TopAppBar(
                title = {
                    Text(
                        text = if(selectedCourse == null) stringResource(R.string.my_course_h)
                        else meds.firstOrNull { it.id == selectedCourse?.medId }?.name ?: "no data",
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
                        courses = courses,
                        meds = meds,
                        onSelect = {
                            selectedCourse = courses.first { c -> c.id == it }
                            navHostController.navigate(MyCoursesNavItem.Course.route)
                        }
                    )
                }
                composable(MyCoursesNavItem.Course.route) {
                    if(selectedCourse != null) {
                        CourseEditScreen(
                            course = selectedCourse!!,
                            med = meds.first { it.id == selectedCourse!!.medId },
                            usagePattern = generatePattern(selectedCourse!!.medId, usages),
                            onSave = {
                                navHostController.popBackStack()
                                selectedCourse = null
                            },
                            onDecline = {
                                navHostController.popBackStack()
                                selectedCourse = null
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