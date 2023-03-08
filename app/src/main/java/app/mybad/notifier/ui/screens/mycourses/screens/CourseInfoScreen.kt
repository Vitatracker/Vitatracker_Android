package app.mybad.notifier.ui.screens.mycourses

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.common.StylishTextBox
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val usagesPattern = listOf<Pair<Long, Int>>(
    Pair(1678190400,1),
    Pair(1678197600, 3),
    Pair(1678204800, 2)
)

@Composable
@Preview(showBackground = true)
fun CourseInfoScreen(
    modifier: Modifier = Modifier,
    course: CourseDomainModel = CourseDomainModel(),
    usagePattern: List<Pair<Long, Int>> = usagesPattern,
    med: MedDomainModel = MedDomainModel(),
    onSave: () -> Unit = {},
    onDelete: (Long) -> Unit = {},
) {

    val regime = stringArrayResource(R.array.regime)
    val relations = stringArrayResource(R.array.food_relations)
    val types = stringArrayResource(R.array.types)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.mycourse_duration),
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Column {
                val startDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(course.startDate), ZoneId.systemDefault()).toLocalDate()
                val endDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(course.endDate), ZoneId.systemDefault()).toLocalDate()
                ParameterIndicator(
                    name = stringResource(R.string.add_course_start_time),
                    value = DateTimeFormatter.ISO_LOCAL_DATE.format(startDate),
                    modifier = Modifier.padding(16.dp),
                    onClick = {}
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ParameterIndicator(
                    name = stringResource(R.string.add_course_end_time),
                    value = DateTimeFormatter.ISO_LOCAL_DATE.format(endDate),
                    modifier = Modifier.padding(16.dp),
                    onClick = {}
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ParameterIndicator(
                    name = stringResource(R.string.medication_regime),
                    value = regime[course.regime],
                    modifier = Modifier.padding(16.dp),
                    onClick = {}
                )
            }
        }
        Text(
            text = stringResource(R.string.add_med_settings),
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            ParameterIndicator(
                name = stringResource(R.string.add_course_notifications_time),
                value = course.startDate,
                modifier = Modifier.padding(16.dp),
                onClick = {}
            )

        }
        Text(
            text = stringResource(R.string.mycourse_dosage_and_usage),
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Column {
                StylishTextBox(
                    label = stringResource(R.string.add_med_name),
                    value = med.name,
                    icon = R.drawable.pill,
                    outlined = false,
                    onChange = {},
                    onIconClick = {}
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ParameterIndicator(
                    name = stringResource(R.string.add_med_unit),
                    value = types[med.type],
                    modifier = Modifier.padding(16.dp),
                    onClick = {}
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                var dose = 0
                usagePattern.forEach {
                    dose += it.second
                }
                ParameterIndicator(
                    name = stringResource(R.string.add_med_dose),
                    value = dose,
                    modifier = Modifier.padding(16.dp),
                    onClick = {}
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ParameterIndicator(
                    name = stringResource(R.string.mycourse_per_day),
                    value = usagesPattern.size,
                    modifier = Modifier.padding(16.dp),
                    onClick = {}
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ParameterIndicator(
                    name = stringResource(R.string.add_med_food_relation),
                    value = relations[med.beforeFood],
                    modifier = Modifier.padding(16.dp),
                    onClick = {}
                )
            }
        }
        Text(
            text = stringResource(R.string.add_course_comment),
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        StylishTextBox(
            label = stringResource(R.string.add_course_comment),
            value = course.comment,
            minLines = 5,
            onChange = {},
            modifier = Modifier.padding(bottom = 24.dp)
        )
        SaveDecline(
            onSave = onSave::invoke,
            onDelete = { onDelete(course.id) }
        )
    }
}

@Composable
private fun SaveDecline(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onDelete::invoke,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.error),
            modifier = Modifier
                .height(52.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.mycourse_delete),
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(Modifier.width(16.dp))
        Button(
            onClick = onSave::invoke,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .height(52.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings_save),
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}