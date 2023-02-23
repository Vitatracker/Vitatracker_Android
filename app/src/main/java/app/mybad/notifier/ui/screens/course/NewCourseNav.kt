package app.mybad.notifier.ui.screens.course

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel
import app.mybad.notifier.ui.screens.settings.NavItemSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCourseNav(
    modifier: Modifier = Modifier,
    userId: String = "userid",
    navController: NavHostController,
    onDismiss: () -> Unit = {},
    onFinish: (Triple<MedDomainModel, CourseDomainModel, UsagesDomainModel>) -> Unit = {},
) {

    var newMed by remember { mutableStateOf(MedDomainModel()) }
    var newCourse by remember { mutableStateOf(CourseDomainModel()) }
    var newUsages by remember { mutableStateOf(UsagesDomainModel()) }
    var title by remember { mutableStateOf("") }
    val currentDest = navController.currentBackStackEntryAsState()
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if(currentDest.value?.destination?.route != NavItemCourse.CourseCreated.route) {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = { Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp)
                ) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                if(navController.currentDestination?.route == NavItemCourse.AddMed.route) onDismiss()
                                navController.popBackStack()
                            }
                            .clip(CircleShape)
                    )
                },
            )
        }
        NavHost(
            navController = navController,
            startDestination = NavItemCourse.AddMed.route
        ) {
            composable(NavItemCourse.AddMed.route) {
                title = stringResource(NavItemCourse.AddMed.stringId)
                AddMedScreen(
                    modifier = modifier.fillMaxSize(),
                    userId = userId,
                    onNext = {
                        navController.navigate(NavItemCourse.AddCourse.route)
                        newMed = it
                        Log.w("NCN_", "$newMed")
                    },
                    onBack = {
                        onDismiss()
                        navController.popBackStack()
                    },
                )
            }
            composable(NavItemCourse.AddCourse.route) {
                title = stringResource(NavItemCourse.AddCourse.stringId)
                AddCourse(
                    modifier = modifier.fillMaxSize(),
                    userId = userId,
                    medId = newMed.id,
                    onNext = {
                        navController.navigate(NavItemCourse.NextCourse.route)
                        newCourse = it.first
                        newUsages = it.second
                        Log.w("NCN_", "$newCourse")
                        Log.w("NCN_", "$newUsages")
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(NavItemCourse.NextCourse.route) {
                title = stringResource(NavItemCourse.NextCourse.stringId)
                NextCourse(
                    modifier = modifier.fillMaxSize(),
                    previousEndDate = newCourse.endTime,
                    onNext = {
                        navController.navigate(NavItemCourse.CourseCreated.route)
                        onFinish(Triple(newMed, newCourse, newUsages))
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(NavItemCourse.CourseCreated.route) {
                title = stringResource(NavItemCourse.CourseCreated.stringId)
                CourseCreated { }
            }
        }
    }
}