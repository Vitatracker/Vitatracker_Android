package app.mybad.notifier.ui.screens.mycourses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val coursesList = listOf(
    CourseDomainModel(id=1L, medId = 1L, startDate = 0L, endDate = 11000000L),
    CourseDomainModel(id=2L, medId = 2L, startDate = 0L, endDate = 12000000L),
    CourseDomainModel(id=3L, medId = 3L, startDate = 0L, endDate = 13000000L),
)

val medsList = listOf(
    MedDomainModel(id=1L, name = "Doliprane",   details = MedDetailsDomainModel(type = 1, dose = 500, measureUnit = 1, icon = R.drawable.pill)),
    MedDomainModel(id=2L, name = "Dexedrine",   details = MedDetailsDomainModel(type = 1, dose = 30,  measureUnit = 1, icon = R.drawable.pill)),
    MedDomainModel(id=3L, name = "Prozac",      details = MedDetailsDomainModel(type = 1, dose = 120, measureUnit = 1, icon = R.drawable.pill)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun MyCourses(
    modifier: Modifier = Modifier,
    courses: List<CourseDomainModel> = coursesList,
    meds: List<MedDomainModel> = medsList,
    onDismiss: () -> Unit = {  }
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.my_course_h),
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
        if(courses.isNotEmpty() && meds.isNotEmpty() && validate(meds, courses)) {
            LazyColumn() {
                courses.forEach {course ->
                    item {
                        CourseItem(course = course, med = meds.filter { it.id == course.medId }[0])
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

private fun validate(
    meds: List<MedDomainModel>,
    courses: List<CourseDomainModel>
) : Boolean {
    var isValid = true
    val mm = meds.mapIndexed { index, medDomainModel -> index to medDomainModel.id }.toMap()
    courses.forEach {
        if (!mm.containsValue(it.medId)) isValid = false
    }
    return isValid
}

@Composable
private fun CourseItem(
    modifier: Modifier = Modifier,
    course: CourseDomainModel,
    med: MedDomainModel,
    onSelect: () -> Unit = {}
) {
    val units = stringArrayResource(R.array.units)
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 3.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(med.details.icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${med.name}, ${med.details.dose} ${units[med.details.measureUnit]}",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "2 pcs", style = Typography.labelMedium)
                        Text(text = "2 per day", style = Typography.labelMedium)        //пофиксить эту хуйню!
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.weight(0.7f)) {
                    Text(
                        text = stringResource(R.string.mycourse_duration),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val start = DateTimeFormatter
                            .ofPattern("yyyy.MM.dd")
                            .withZone(ZoneOffset.UTC)
                            .format(Instant.ofEpochSecond(course.startDate))
                        val end = DateTimeFormatter
                            .ofPattern("yyyy.MM.dd")
                            .withZone(ZoneOffset.UTC)
                            .format(Instant.ofEpochSecond(course.endDate))
                        Text(text = start, style = Typography.labelMedium)
                        Icon(
                            painter = painterResource(R.drawable.arrow_right),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 4.dp, end = 4.dp, top = 2.dp)
                                .height(10.dp)
                        )
                        Text(text = end, style = Typography.labelMedium)
                    }
                }
                Column(Modifier.weight(0.3f)) {
                    Text(
                        text = stringResource(R.string.mycourse_interval),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "2 months", style = Typography.labelMedium)
                }
            }
        }
    }
}