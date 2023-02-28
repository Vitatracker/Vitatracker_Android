package app.mybad.notifier.ui.screens.course.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.ui.screens.course.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCourseNav(
    modifier: Modifier = Modifier,
    vm: CreateCourseViewModel,
    userId: String = "userid",
    navController: NavHostController,
    onDismiss: () -> Unit = {},
) {

    var newMed by remember { mutableStateOf(MedDomainModel()) }
    var newCourse by remember { mutableStateOf(CourseDomainModel()) }
    var newCommonUsages by remember { mutableStateOf(listOf<UsageCommonDomainModel>()) }
    var title by remember { mutableStateOf("") }
    val currentDest = navController.currentBackStackEntryAsState()
    val state = vm.state.collectAsState()

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
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {
                                if (navController.currentDestination?.route == NavItemCourse.AddMed.route) onDismiss()
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
                    init = state.value.med,
                    userId = userId,
                    onChange = {
                        newMed = it
                        vm.reduce(CreateCourseIntent.NewMed(newMed))
                    },
                    onNext = { navController.navigate(NavItemCourse.AddCourse.route) },
                    onBack = {
                        onDismiss()
                        vm.reduce(CreateCourseIntent.Drop)
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
                        newCourse = it.first
                        newCommonUsages = it.second
                        navController.navigate(NavItemCourse.NextCourse.route)
                        vm.reduce(CreateCourseIntent.NewCourse(it.first))
                        vm.reduce(CreateCourseIntent.NewUsages(it.second))
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(NavItemCourse.NextCourse.route) {
                title = stringResource(NavItemCourse.NextCourse.stringId)
                NextCourse(
                    modifier = modifier.fillMaxSize(),
                    previousEndDate = newCourse.endDate,
                    onNext = {
                        vm.reduce(CreateCourseIntent.NewCourse(newCourse))
                        vm.reduce(CreateCourseIntent.Finish)
                        navController.navigate(NavItemCourse.CourseCreated.route)
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(NavItemCourse.CourseCreated.route) {
                title = stringResource(NavItemCourse.CourseCreated.stringId)
                CourseCreated {
                    onDismiss()
                    vm.reduce(CreateCourseIntent.Drop)
                }
            }
        }
    }
}