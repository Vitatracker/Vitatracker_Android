package app.mybad.notifier.ui.screens.calender

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
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
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

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
    UsageDomainModel(1677355481L),
)

val usagesList = listOf(
    UsagesDomainModel(medId = 1L, usages = usages),
    UsagesDomainModel(medId = 2L, usages = usages),
    UsagesDomainModel(medId = 3L, usages = usages2),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
@Preview(showBackground = true)
fun CalendarScreen(
    modifier: Modifier = Modifier,
    courses: List<CourseDomainModel> = coursesList,
    usages: List<UsagesDomainModel> = usagesList,
    meds: List<MedDomainModel> = medsList,
) {

    val now = Instant.now().epochSecond
    var date by remember { mutableStateOf(LocalDateTime.ofInstant(Instant.ofEpochSecond(now), ZoneId.systemDefault())) }
    var selectedDate : LocalDateTime? by remember { mutableStateOf(date) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
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
                }
            )
            MonthSelector(now = now) { date = it }
            Spacer(Modifier.height(16.dp))
            CalendarScreenItem(
                now = date.toEpochSecond(ZoneOffset.UTC),
                usages = usages,
                onSelect = {
                    selectedDate = it
                }
            )
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MonthSelector(
    modifier: Modifier = Modifier,
    now: Long,
    onSwitch: (LocalDateTime) -> Unit
) {
    val months = stringArrayResource(R.array.months_full)
    val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(now), ZoneId.systemDefault())
    var newDate by remember { mutableStateOf(date) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.prev_month),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .clickable {
                    newDate = newDate.minusMonths(1)
                    onSwitch(newDate)
                }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            AnimatedContent(
                targetState = newDate.monthValue,
                transitionSpec = {
                    EnterTransition.None with ExitTransition.None
                }
            ) { targetCount ->
                Text(
                    modifier = Modifier.animateEnterExit(
                        enter = scaleIn(),
                        exit = scaleOut()
                    ),
                    text = months[targetCount-1],
                    style = Typography.bodyLarge
                )
            }
            Text(
                text = newDate.year.toString(),
                style = Typography.labelSmall
            )
        }
        Icon(
            painter = painterResource(R.drawable.next_month),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .clickable {
                    newDate = newDate.plusMonths(1)
                    onSwitch(newDate)
                }
        )
    }

}

@Composable
private fun CalendarScreenItem(
    modifier: Modifier = Modifier,
    now: Long,
    usages: List<UsagesDomainModel>,
    onSelect: (LocalDateTime?) -> Unit
) {

    val days = stringArrayResource(R.array.days_short)
    val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(now), ZoneId.systemDefault())
    val cdr : Array<Array<LocalDateTime?>> = Array(6) {
        Array(7) { null }
    }
    val usgs = Array(6) {
        Array(7) { mutableListOf<UsageDomainModel>() }
    }
    val fwd = date.minusDays(date.dayOfMonth.toLong())
    for(w in 0..5) {
        for(d in 0..6) {
            if(w == 0 && d < fwd.dayOfWeek.value) {
                val time = fwd.minusDays(fwd.dayOfWeek.value - d.toLong() - 1)
                usages.forEach { it.usages.forEach { usage ->
                    val day = usage.timeToUse - (usage.timeToUse % 86400)
                    if(day == time.toEpochSecond(ZoneOffset.UTC)- time.toEpochSecond(ZoneOffset.UTC)%86400)
                        usgs[w][d].add(usage)
                } }
                cdr[w][d] = time
            }
            else {
                val time = fwd.plusDays(w * 7L + d - fwd.dayOfWeek.value + 1)
                usages.forEach { it.usages.forEach { usage ->
                    val day = usage.timeToUse - (usage.timeToUse % 86400)
                    if(day == time.toEpochSecond(ZoneOffset.UTC)- time.toEpochSecond(ZoneOffset.UTC)%86400)
                        usgs[w][d].add(usage)
                } }
                cdr[w][d] = fwd.plusDays(w * 7L + d - fwd.dayOfWeek.value + 1)
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                repeat(7) {
                    Text(
                        text = days[it],
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(40.dp)
                    )
                }
            }
            repeat(6) { w ->
                if(cdr[w].any { it?.month == date.month }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        repeat(7) { d ->
                            CalendarDayItem(
                                modifier = Modifier.padding(bottom = 8.dp),
                                date = cdr[w][d],
                                usages = usgs[w][d].size,
                                isSelected = date == cdr[w][d],
                                isOtherMonth = date.month.value != cdr[w][d]?.month?.value
                            ) { onSelect(it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    modifier: Modifier = Modifier,
    usages: Int = 0,
    date: LocalDateTime?,
    isSelected: Boolean = false,
    isOtherMonth: Boolean = false,
    onSelect: (LocalDateTime?) -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                CircleShape
            )
            .alpha(if (isOtherMonth) 0.5f else 1f)
            .clickable { onSelect(date) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val indicatorColor = if(usages > 0 && isSelected) MaterialTheme.colorScheme.primary
                else if(usages > 0) MaterialTheme.colorScheme.primaryContainer
                else Color.Transparent
            Box(
                Modifier
                    .size(5.dp)
                    .background(
                        color = indicatorColor,
                        shape = CircleShape
                    ))
            Text(
                text = date?.dayOfMonth.toString(),
                style = Typography.bodyLarge,
                color = if(isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
            )
        }
    }
}