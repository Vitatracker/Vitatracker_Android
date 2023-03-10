package app.mybad.notifier.ui.screens.newcourse.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.ui.screens.course.CreateCourseIntent

@Composable
fun AddNotificationsMainScreen(
    modifier: Modifier = Modifier,
    usages: List<UsageCommonDomainModel>,
    reducer: (CreateCourseIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {



}