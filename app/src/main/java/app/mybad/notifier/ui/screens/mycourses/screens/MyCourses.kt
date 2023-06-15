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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.plusDay
import app.mybad.notifier.utils.plusThreeDay
import app.mybad.notifier.utils.secondsToDay
import app.mybad.notifier.utils.toDateDisplay
import java.time.Instant

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
        val now = Instant.now().epochSecond
        courses.forEach {
            Log.w("MC_courses", "$it")
        }
        if (courses.isNotEmpty() && meds.isNotEmpty() && validate(meds, courses)) {
            LazyColumn(Modifier.heightIn(max = height.dp)) {
                courses.forEach { course ->
                    item {
                        CourseItem(
                            course = course,
                            med = meds.first { it.id == course.medId },
                            usages = usages.filter {
                                it.medId == course.medId && it.useTime >= course.startDate
                                        && it.useTime < course.endDate.plusDay()
                            },
                            onSelect = onSelect::invoke,
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
                courses.forEach { nCourse ->
                    if (
                        nCourse.interval > 0 &&
                        nCourse.startDate + nCourse.interval > now &&
                        nCourse.startDate + nCourse.interval < now.plusThreeDay()
                    ) {
                        item {
                            CourseItem(
                                course = nCourse.copy(
                                    startDate = nCourse.startDate + nCourse.interval,
                                    endDate = nCourse.endDate + nCourse.interval,
                                ),
                                med = meds.first { it.id == nCourse.medId },
                                usages = usages.filter {
                                    it.medId == nCourse.medId && it.useTime >= nCourse.startDate
                                            && it.useTime < nCourse.startDate.plusDay()
                                }.take(10),
                                startInDays = (nCourse.startDate + nCourse.interval - now).secondsToDay(),
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun validate(
    meds: List<MedDomainModel>,
    courses: List<CourseDomainModel>
): Boolean {
    var isValid = true
    courses.forEach { course ->
        if (meds.find { it.id == course.medId } == null) isValid = false
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
    startInDays: Int = -1,
    onSelect: (Long) -> Unit = {},
) {
    Log.w("MC_usages_in_item", "$usages")
    val types = stringArrayResource(R.array.types)
    val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    val colors = integerArrayResource(R.array.colors)
    val itemsCount = if (usages.isNotEmpty()) {
        val firstCount = usages.first().quantity
        val firstTime = usages.first().useTime
        var correct = true
        usages.filter { it.useTime <= (firstTime.plusDay()) }.forEach {
            if (it.quantity != firstCount) correct = false
        }
        if (correct) firstCount else 0
    } else {
        0
    }
    val usagesCount = usages.count { it.useTime <= (usages.first().useTime.plusDay()) }

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
                    Column(Modifier.fillMaxWidth(0.85f)) {
                        Text(
                            text = "${med.name}".replaceFirstChar { it.uppercase() },
                            style = Typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Log.w("VTTAG","CourseItem: itemsCount=$itemsCount usagesCount=$usagesCount")
                        if (itemsCount != 0 || usagesCount > 0) {
                            Row {
                                if (itemsCount != 0) {
                                    Text(
                                        text = "$itemsCount, ${types[med.type]}",
                                        style = Typography.labelMedium
                                    )
                                    Divider(
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
                                        text = "$usagesCount ${stringResource(R.string.mycourse_per_day_listitem)}",
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
