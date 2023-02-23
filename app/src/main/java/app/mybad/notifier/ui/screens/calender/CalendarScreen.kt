package app.mybad.notifier.ui.screens.calender

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.mycourses.coursesList

val coursesList = listOf(
    CourseDomainModel(id="1", medId = 1L, startDate = 0L, endTime = 11000000L),
    CourseDomainModel(id="2", medId = 2L, startDate = 0L, endTime = 12000000L),
    CourseDomainModel(id="3", medId = 3L, startDate = 0L, endTime = 13000000L),
)

val medsList = listOf(
    MedDomainModel(id=1L, name = "Doliprane",   details = MedDetailsDomainModel(type = 1, dose = 500, measureUnit = 1, icon = R.drawable.pill)),
    MedDomainModel(id=2L, name = "Dexedrine",   details = MedDetailsDomainModel(type = 1, dose = 30,  measureUnit = 1, icon = R.drawable.pill)),
    MedDomainModel(id=3L, name = "Prozac",      details = MedDetailsDomainModel(type = 1, dose = 120, measureUnit = 1, icon = R.drawable.pill)),
)

val usages = listOf(
    UsageDomainModel(1677182681L),
    UsageDomainModel(1677182681L),
    UsageDomainModel(1677254681L),
    UsageDomainModel(1677269081L),
    UsageDomainModel(1677355481L),
    UsageDomainModel(1677341081L),
    UsageDomainModel(1677427481L),
)
val usages2 = listOf(
    UsageDomainModel(1677182681L),
    UsageDomainModel(1677182681L),
    UsageDomainModel(1677254681L),
    UsageDomainModel(1677269081L),
)

val usagesList = listOf(
    UsagesDomainModel(medId = 1L, usages = usages),
    UsagesDomainModel(medId = 2L, usages = usages),
    UsagesDomainModel(medId = 3L, usages = usages2),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun CalendarScreen(
    modifier: Modifier = Modifier,
    courses: List<CourseDomainModel> = coursesList,
    usages: List<UsagesDomainModel> = usagesList,
    meds: List<MedDomainModel> = medsList,
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.calendar_h),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp)
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) { }
                        .clip(CircleShape)
                )
            },
        )
    }

}