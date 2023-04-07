package app.mybad.notifier.ui.screens.mycourses.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import java.time.*
import java.time.format.DateTimeFormatter

@Composable
fun MyCourses(
    modifier: Modifier = Modifier,
    courses: List<CourseDomainModel>,
    usages: List<UsageCommonDomainModel>,
    meds: List<MedDomainModel>,
    onSelect: (Long) -> Unit
) {

    val height = LocalConfiguration.current.screenHeightDp
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        if(courses.isNotEmpty() && meds.isNotEmpty() && validate(meds, courses)) {
            LazyColumn(Modifier.heightIn(max = height.dp)) {
                courses.forEach { course ->
                    item {
                        CourseItem(
                            course = course,
                            med = meds.first { it.id == course.medId },
                            onSelect = onSelect::invoke,
                            usages = emptyList()
                        )
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
    Log.w("MC_meds", "$meds")
    Log.w("MC_courses", "$courses")
    var isValid = true
    val mm = meds.mapIndexed { index, medDomainModel -> index to medDomainModel.id }.toMap()
    courses.forEach {
        if (!mm.containsValue(it.medId)) isValid = false
    }
    return isValid
}

@SuppressLint("Recycle")
@Composable
private fun CourseItem(
    modifier: Modifier = Modifier,
    course: CourseDomainModel,
    usages: List<UsageCommonDomainModel>,
    med: MedDomainModel,
    onSelect: (Long) -> Unit = {}
) {
    val units = stringArrayResource(R.array.units)
    val relations = stringArrayResource(R.array.food_relations)
    val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    val colors = integerArrayResource(R.array.colors)
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
            .fillMaxWidth()
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
                    color = Color(colors[med.color]),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(r.getResourceId(med.icon, 0)),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "${med.name}".replaceFirstChar { it.uppercase() },
                            style = Typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row() {
                            Text(text = "${med.dose} ${units[med.measureUnit]}", style = Typography.labelMedium)
                            Divider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .height(16.dp)
                                    .padding(horizontal = 8.dp)
                                    .width(1.dp)
                            )
                            Text(text = relations[med.beforeFood], style = Typography.labelMedium)
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                onSelect(course.id)
                            }
                    )
                }

            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
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
                    Text(text = start, style = Typography.bodyLarge)
                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 2.dp)
                            .height(10.dp)
                    )
                    Text(text = end, style = Typography.bodyLarge)
                }
            }
        }
    }
}