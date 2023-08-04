package app.mybad.notifier.ui.screens.mycourses.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.PickColor
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.theme.utils.currentDateTime
import app.mybad.theme.utils.plusDay
import app.mybad.theme.utils.plusThreeDay
import app.mybad.theme.utils.secondsToDay
import app.mybad.theme.utils.toDateDisplay
import app.mybad.theme.utils.toEpochSecond

@Composable
fun MyCourses(
    modifier: Modifier = Modifier,
    remedies: List<RemedyDomainModel>,
    courses: List<CourseDomainModel>,
    usages: List<UsageDomainModel>,
    onSelect: (Long) -> Unit
) {
    val height = LocalConfiguration.current.screenHeightDp
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        val now = currentDateTime().toEpochSecond()
        courses.forEach {
            Log.w("MC_courses", "$it")
        }
        if (courses.isNotEmpty() && remedies.isNotEmpty() && validate(remedies, courses)) {
            LazyColumn(Modifier.heightIn(max = height.dp)) {
                // Отображение текущих курсов таблеток
                items(courses) { course ->
                    CourseItem(
                        course = course,
                        remedy = remedies.first { it.id == course.remedyId },
                        usages = usages.filter { usage ->
                            usage.courseId == course.id &&
                                usage.useTime >= course.startDate &&
                                usage.useTime < course.endDate.plusDay()
                        },
                        onSelect = onSelect::invoke,
                    )
                    Spacer(Modifier.height(16.dp))
                }
                // Отображение нового курса
                courses.forEach { newCourse ->
                    if (
                        newCourse.interval > 0 &&
                        newCourse.startDate + newCourse.interval > now &&
                        newCourse.startDate + newCourse.interval < now.plusThreeDay()
                    ) {
                        item {
                            CourseItem(
                                course = newCourse.copy(
                                    startDate = newCourse.startDate + newCourse.interval,
                                    endDate = newCourse.endDate + newCourse.interval,
                                ),
                                remedy = remedies.first { it.id == newCourse.remedyId },
                                usages = usages.filter { usage ->
                                    usage.courseId == newCourse.id &&
                                        usage.useTime >= newCourse.startDate &&
                                        usage.useTime < newCourse.startDate.plusDay()
                                }.take(10),
                                startInDays = (newCourse.startDate + newCourse.interval - now).secondsToDay()
                                    .toInt(),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun validate(
    remedies: List<RemedyDomainModel>,
    courses: List<CourseDomainModel>,
) = courses.all { course -> remedies.any { it.id == course.remedyId } }

@SuppressLint("Recycle")
@Composable
private fun CourseItem(
    course: CourseDomainModel,
    usages: List<UsageDomainModel>,
    remedy: RemedyDomainModel,
    modifier: Modifier = Modifier,
    startInDays: Int = -1,
    onSelect: (Long) -> Unit = {},
) {
    Log.w("MC_usages_in_item", "$usages")
    val types = stringArrayResource(R.array.types)
    val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    val itemsCount = if (usages.isNotEmpty()) {
        val firstCount = usages.first().quantity
        val firstTime = usages.first().useTime
        if (usages.filter { it.useTime <= (firstTime.plusDay()) }
                .all { it.quantity == firstCount }) firstCount else 0
    } else {
        0
    }
    val usagesCount = usages.count { it.useTime < (usages.first().useTime.plusDay()) }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
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
                    color = PickColor.getColor(remedy.color),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(r.getResourceId(remedy.icon, 0)),
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
                    Column(Modifier.fillMaxWidth(0.85f)) {
                        Text(
                            text = "${remedy.name}".replaceFirstChar { it.uppercase() },
                            style = Typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Log.w(
                            "VTTAG",
                            "CourseItem: itemsCount=$itemsCount usagesCount=$usagesCount"
                        )
                        if (itemsCount != 0 || usagesCount > 0) {
                            Row {
                                if (itemsCount != 0) {
                                    Text(
                                        text = "$itemsCount, ${types[remedy.type]}",
                                        style = Typography.labelMedium
                                    )
                                    VerticalDivider(
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .height(16.dp)
                                            .padding(horizontal = 8.dp)
                                            .width(1.dp)
                                    )
                                }
                                if (usagesCount > 0) {
//                                Text(text = relations[med.beforeFood], style = Typography.labelMedium)
                                    Text(
                                        text = stringResource(
                                            R.string.mycourse_per_day_listitem,
                                            usagesCount,
                                        ),
                                        style = Typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_pencil),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        onSelect(course.id)
                                    }
                            )
                        }
                    }
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val start = course.startDate.toDateDisplay()
                        val end = course.endDate.toDateDisplay()
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
                    if (startInDays > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.size(16.dp))
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                val startsIn = String.format(
                                    stringResource(R.string.mycourse_remaining),
                                    startInDays.toString()
                                )
                                Text(
                                    text = startsIn,
                                    style = Typography.bodySmall,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
