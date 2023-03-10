package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.ui.screens.course.NewCourseIntent

@Composable
fun AddMedicineDetailsScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel,
    reducer: (NewCourseIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {



}