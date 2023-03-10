package app.mybad.notifier.ui.screens.newcourse.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.notifier.ui.screens.course.CreateCourseIntent

@Composable
fun AddCourseMainScreen(
    modifier: Modifier = Modifier,
    course: CourseDomainModel,
    reducer: (CreateCourseIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {



}