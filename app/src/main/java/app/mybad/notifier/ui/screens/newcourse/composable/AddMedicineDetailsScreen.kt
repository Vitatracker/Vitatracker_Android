package app.mybad.notifier.ui.screens.newcourse.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.ui.screens.course.CreateCourseIntent

@Composable
fun AddMedicineDetailsScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel,
    reducer: (CreateCourseIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {



}