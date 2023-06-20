package app.mybad.notifier.ui.screens.mycourses

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.CalendarSelectorScreen
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.newcourse.common.*
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

private val usagesPattern = listOf<Pair<Long, Int>>(
    Pair(1678190400, 1),
    Pair(1678197600, 3),
    Pair(1678204800, 2)
)

@Composable
fun CourseInfoScreen(
    modifier: Modifier = Modifier,
    course: CourseDomainModel = CourseDomainModel(),
    usagePattern: List<Pair<Long, Int>> = usagesPattern,
    med: MedDomainModel = MedDomainModel(),
    reducer: (MyCoursesIntent) -> Unit = {},
) {
    val types = stringArrayResource(R.array.types)
    val units = stringArrayResource(R.array.units)
    val rels = stringArrayResource(R.array.food_relations)
    val name = stringResource(R.string.add_med_name)
    val form = stringResource(R.string.add_med_form)
    val dose = stringResource(R.string.mycourse_dosage_and_usage)
    val unit = stringResource(R.string.add_med_unit)
    val rel = stringResource(R.string.add_med_food_relation)
    var medInternal by remember { mutableStateOf(med) }
    var courseInternal by remember { mutableStateOf(course) }
    var patternInternal by remember { mutableStateOf(usagePattern) }

    val startLabel = stringResource(R.string.add_course_start_time)
    val endLabel = stringResource(R.string.add_course_end_time)
    val regimeLabel = stringResource(R.string.medication_regime)
    val regimeList = stringArrayResource(R.array.regime)
    var startDate = LocalDateTime.ofEpochSecond(
        courseInternal.startDate,
        0,
        ZoneId.systemDefault().rules.getOffset(
            Instant.now()
        )
    )
        .withHour(0).withMinute(0)
    var endDate = LocalDateTime.ofEpochSecond(
        courseInternal.endDate,
        0,
        ZoneId.systemDefault().rules.getOffset(
            Instant.now()
        )
    )
        .withHour(23).withMinute(59)
    var selectedInput by remember { mutableStateOf(-1) }

    LaunchedEffect(courseInternal) {
        val currentDate = ZoneId.systemDefault().rules.getOffset(
            Instant.now()
        )
        startDate = LocalDateTime.ofEpochSecond(
            courseInternal.startDate,
            0,
            currentDate
        )
            .withHour(0).withMinute(0)
        endDate = LocalDateTime.ofEpochSecond(
            courseInternal.endDate,
            0,
            currentDate
        )
            .withHour(23).withMinute(59)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = stringResource(R.string.mycourse_edit_icon),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            MultiBox(
                {
                    IconSelector(
                        selected = medInternal.icon,
                        color = medInternal.color,
                        onSelect = { medInternal = medInternal.copy(icon = it) }
                    )
                },
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.mycourse_edit_icon_color),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            MultiBox(
                {
                    ColorSelector(
                        selected = medInternal.color,
                        onSelect = { medInternal = medInternal.copy(color = it) }
                    )
                },
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.mycourse_dosage_and_usage),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            MultiBox(
                {
                    BasicKeyboardInput(
                        label = name,
                        init = medInternal.name,
                        hideOnGo = true,
                        onChange = { medInternal = medInternal.copy(name = it) }
                    )
                },
                {
                    var exp by remember { mutableStateOf(false) }
                    ParameterIndicator(
                        name = form,
                        value = types[med.type],
                        onClick = { exp = true }
                    )
                    DropdownMenu(
                        expanded = exp,
                        onDismissRequest = { exp = false },
                        offset = DpOffset(x = 300.dp, y = 0.dp)
                    ) {
                        types.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    medInternal = medInternal.copy(type = index)
                                    exp = false
                                }
                            )
                        }
                    }
                },
                {
                    BasicKeyboardInput(
                        label = dose,
                        init = if (medInternal.dose == 0) "" else medInternal.dose.toString(),
                        hideOnGo = true,
                        keyboardType = KeyboardType.Number,
                        alignRight = true,
                        prefix = {
                            Text(
                                text = dose,
                                style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        },
                        onChange = { medInternal = medInternal.copy(dose = it.toIntOrNull() ?: 0) }
                    )
                },
                {
                    var exp by remember { mutableStateOf(false) }
                    ParameterIndicator(
                        name = unit,
                        value = units[medInternal.measureUnit],
                        onClick = { exp = true }
                    )
                    DropdownMenu(
                        expanded = exp,
                        onDismissRequest = { exp = false },
                        offset = DpOffset(x = 300.dp, y = 0.dp)
                    ) {
                        units.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    medInternal = medInternal.copy(measureUnit = index)
                                    exp = false
                                }
                            )
                        }
                    }
                },
                {
                    var exp by remember { mutableStateOf(false) }
                    ParameterIndicator(
                        name = rel,
                        value = rels[medInternal.beforeFood],
                        onClick = { exp = true }
                    )
                    DropdownMenu(
                        expanded = exp,
                        onDismissRequest = { exp = false },
                        offset = DpOffset(x = 300.dp, y = 0.dp)
                    ) {
                        rels.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    medInternal = medInternal.copy(beforeFood = index)
                                    exp = false
                                }
                            )
                        }
                    }
                },
                modifier = Modifier,
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.mycourse_duration),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            val monthStart = if (Locale.getDefault().language == "ru") {
                stringArrayResource(R.array.months_full_more)[startDate.monthValue - 1]
            } else {
                stringArrayResource(R.array.months_full)[startDate.monthValue - 1]
            }
            val monthEnd = if (Locale.getDefault().language == "ru") {
                stringArrayResource(R.array.months_full_more)[endDate.monthValue - 1]
            } else {
                stringArrayResource(R.array.months_full)[endDate.monthValue - 1]
            }
            val sd = "${startDate.dayOfMonth} $monthStart ${startDate.year}"
            val ed = "${endDate.dayOfMonth} $monthEnd ${endDate.year}"
            MultiBox(
                {
                    ParameterIndicator(
                        name = startLabel,
                        value = sd,
                        onClick = { selectedInput = 1 }
                    )
                },
                {
                    ParameterIndicator(
                        name = endLabel,
                        value = ed,
                        onClick = { selectedInput = 2 }
                    )
                },
                {
                    ParameterIndicator(
                        name = regimeLabel,
                        value = regimeList[courseInternal.regime],
                        onClick = { selectedInput = 3 }
                    )
                },
                itemsPadding = PaddingValues(16.dp)
            )

            Text(
                text = stringResource(R.string.mycourse_reminders),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            MultiBox(
                {
                    Text(
                        text = stringResource(R.string.mycourse_reminders),
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource(),
                            onClick = { }
                        )
                    )
                },
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(Modifier.height(16.dp))
        SaveDecline(
            onSave = {
                reducer(MyCoursesIntent.Update(courseInternal, medInternal, patternInternal))
            },
            onDelete = { reducer(MyCoursesIntent.Delete(courseInternal.id)) },
        )
    }

    if (selectedInput != -1) {
        Dialog(
            onDismissRequest = { selectedInput = -1 },
            properties = DialogProperties()
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                when (selectedInput) {
                    1 -> CalendarSelectorScreen(
                        startDay = startDate.toLocalDate(),
                        endDay = endDate.toLocalDate(),
                        onSelect = { sd ->
                            courseInternal = courseInternal.copy(
                                startDate = sd?.atStartOfDay()?.toEpochSecond(ZoneOffset.UTC) ?: 0L,
                            )
                            selectedInput = -1
                        },
                        onDismiss = { selectedInput = -1 },
                        editStart = true
                    )
                    2 -> CalendarSelectorScreen(
                        startDay = startDate.toLocalDate(),
                        endDay = endDate.toLocalDate(),
                        onSelect = { ed ->
                            courseInternal = courseInternal.copy(
                                endDate = ed?.atStartOfDay()?.withHour(23)?.withMinute(59)?.toEpochSecond(
                                    ZoneOffset.UTC
                                ) ?: 0L,
                            )
                            selectedInput = -1
                        },
                        onDismiss = { selectedInput = -1 },
                        editStart = false
                    )
                    3 -> RollSelector(
                        list = regimeList.toList(),
                        startOffset = course.regime,
                        onSelect = {
                            courseInternal = courseInternal.copy(regime = it)
                            selectedInput = -1
                        }
                    )
                }
            }
        }
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
