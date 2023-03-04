package app.mybad.notifier.ui.screens.addcourse_redesigned

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.addcourse_redesigned.IntentTypes.*
import app.mybad.notifier.ui.screens.addcourse_redesigned.common.*
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.course.CreateCourseIntent
import app.mybad.notifier.ui.screens.course.CreateCourseState
import app.mybad.notifier.ui.theme.Typography
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private enum class IntentTypes {
    MEASURE_UNITS,
    QUANTITY_OF_USAGES,
    RELATION_TO_MEALS,
    NOTIFICATION_TIME,
    SET_REMINDER,
    SET_REGIME,
    SET_INTERVAL,
    SET_REMIND_TIME
}
internal enum class SelectionType {
    START_DATE,
    END_DATE,
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun AddCourseScreen(
    modifier: Modifier = Modifier,
    state: CreateCourseState = CreateCourseState(),
    reducer: (CreateCourseIntent) -> Unit = {},
    onDismiss: () -> Unit = {},
    onFinish: () -> Unit = {}
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val title = stringResource(R.string.add_med_h)
    val topBarLabel by remember { mutableStateOf(title) }
    var sheetIntent by remember { mutableStateOf(MEASURE_UNITS) }
    var dialogIsShown by remember { mutableStateOf(false) }
    val startLocalTime = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0)
    val startTime = startLocalTime.atZone(ZoneId.systemDefault()).toEpochSecond()

    val scaffoldState = rememberBottomSheetScaffoldState()
    val pagerState = rememberPagerState(0)

    var newMed by remember { mutableStateOf(MedDomainModel(
        id = startTime,
        creationDate = startTime
    )) }
    var newCourse by remember { mutableStateOf(CourseDomainModel(
        id = startTime,
        creationDate = startTime,
        startDate = startTime,
        endDate = startLocalTime.plusMonths(1).atZone(ZoneId.systemDefault()).toEpochSecond()
    )) }
    var newUsages by remember { mutableStateOf(emptyList<UsageCommonDomainModel>()) }
    var newUsagesPattern by remember { mutableStateOf(listOf(startTime)) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetGesturesEnabled = false,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetElevation = 5.dp,
        sheetContent = {
           when(sheetIntent) {
               NOTIFICATION_TIME -> {
                   NotificationsTimeSelector(
                       pattern = newUsagesPattern,
                       onSelect = { newUsagesPattern = it },
                       onFinish = { scope.launch { scaffoldState.bottomSheetState.collapse() } }
                   )
               }
               SET_REMINDER -> {
                   var remindTime = startLocalTime
                   var remindOffset = 0
                   ReminderTimeSelector(
                       oldCourseEndTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(newCourse.endDate), ZoneId.systemDefault()),
                       newCourseStartTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(newCourse.endDate + newCourse.interval), ZoneId.systemDefault()),
                       remindTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(newCourse.endDate + newCourse.interval), ZoneId.systemDefault()),
                       interval = newCourse.interval,
                       onSetInterval = { newCourse = newCourse.copy(interval = it) },
                       onSetRemindTime = { remindTime = it },
                       onSetRemindOffset = { remindOffset = it },
                       onSetComment = { newCourse = newCourse.copy(comment = it)},
                       onSave = {
                           scope.launch { scaffoldState.bottomSheetState.collapse() }
                           newCourse = newCourse.copy(remindDate = remindTime.minusDays(remindOffset.toLong()).atZone(ZoneId.systemDefault()).toEpochSecond())
                       }
                   )
               }
               else -> {}
           }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopNavigation(
                label = topBarLabel,
                position = pagerState.currentPage,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HorizontalPager(
                pageCount = 2,
                userScrollEnabled = false,
                state = pagerState,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                when(it) {
                    0 -> {
                        MedCreationScreen(
                            modifier = Modifier.padding(16.dp),
                            med = newMed,
                            usagesPattern = newUsagesPattern,
                            onSelectUnits = {
                                sheetIntent = MEASURE_UNITS
                                dialogIsShown = true
                            },
                            onSelectFoodRelation = {
                                sheetIntent = RELATION_TO_MEALS
                                dialogIsShown = true
                            },
                            onSelectQuantity = {
                                sheetIntent = QUANTITY_OF_USAGES
                                dialogIsShown = true
                            },
                            onSelectNotificationsTime = {
                                sheetIntent = NOTIFICATION_TIME
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            },
                            onSetDose = { dose -> newMed = newMed.copy(details = newMed.details.copy(dose = dose)) },
                            onSetName = { name -> newMed = newMed.copy(name = name) }
                        )
                    }
                    1 -> {
                        CourseCreationScreen(
                            modifier = Modifier.padding(16.dp),
                            startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(newCourse.startDate), ZoneId.systemDefault()),
                            endTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(newCourse.endDate), ZoneId.systemDefault()),
                            regime = newCourse.regime,
                            onSetStart = { time ->
                                newCourse = newCourse.copy(
                                    startDate = time.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0).toEpochSecond()
                                )
                            },
                            onSetEnd = { time ->
                                newCourse = newCourse.copy(
                                    endDate = time.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59).toEpochSecond()
                                )
                            },
                            onSetRegime = {
                                sheetIntent = SET_REGIME
                                dialogIsShown = true
                            },
                            onSetComment = { comment -> newCourse = newCourse.copy(comment = comment) },
                            onSetReminder = {
                                sheetIntent = SET_REMINDER
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            },
                        )
                    }
                }
            }
            val toast = stringResource(R.string.add_med_error_unfilled_fields)
            NavigationRow(
                modifier = Modifier.padding(16.dp),
                nextLabel = if(pagerState.currentPage == 0) stringResource(R.string.navigation_next)
                            else stringResource(id = R.string.navigation_finish),
                onBack = {
                    if(pagerState.currentPage == 0) onDismiss()
                    else scope.launch { pagerState.animateScrollToPage(pagerState.initialPage) }
                },
                onNext = {
                    if(pagerState.currentPage == 1) {
                        newUsages = mutableListOf<UsageCommonDomainModel>().apply {
                            val s = LocalDateTime.ofInstant(Instant.ofEpochSecond(newCourse.startDate), ZoneId.systemDefault())
                            val e = LocalDateTime.ofInstant(Instant.ofEpochSecond(newCourse.endDate), ZoneId.systemDefault())
                            repeat(ChronoUnit.DAYS.between(s,e).toInt()) { iteration ->
                                newUsagesPattern.forEach {
                                    add(UsageCommonDomainModel(
                                        medId = newMed.id,
                                        userId = state.userId,
                                        creationTime = startTime,
                                        useTime = it + 86400*iteration*(1+newCourse.regime)
                                    ))
                                }
                            }
                        }.toList()
                        newCourse = newCourse.copy(
                            startDate = newUsages.first().useTime,
                            endDate = newUsages.last().useTime,
                        )
                        Log.w("ACS_", "$newMed")
                        Log.w("ACS_", "$newCourse")
                        Log.w("ACS_", "$newUsages")
                        onFinish()
                        reducer(CreateCourseIntent.NewMed(newMed))
                        reducer(CreateCourseIntent.NewCourse(newCourse))
                        reducer(CreateCourseIntent.NewUsages(newUsages))
                        reducer(CreateCourseIntent.Finish)
                    }
                    else if(newMed.name.isNullOrBlank() || newMed.details.dose == 0) {
                        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    }
                },
            )
        }
    }

    if(dialogIsShown) {
        Dialog(
            onDismissRequest = { dialogIsShown = false },
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background,
                elevation = 5.dp,
            ) {
                when(sheetIntent) {
                    MEASURE_UNITS -> { RollSelector(
                        modifier = Modifier,
                        list = stringArrayResource(id = R.array.units).asList(),
                        startOffset = newMed.details.measureUnit,
                        onSelect = {
                            newMed = newMed.copy(details = newMed.details.copy(measureUnit = it))
                            dialogIsShown = false
                        }
                    )}
                    QUANTITY_OF_USAGES -> { RollSelector(
                        modifier = Modifier,
                        list = (1..10).map { it.toString() }.toList(),
                        startOffset = newUsagesPattern.lastIndex,
                        onSelect = {
                            val newList = mutableListOf<Long>()
                            if(it>newUsagesPattern.lastIndex) {
                                newList.addAll(newUsagesPattern)
                                repeat(it-newUsagesPattern.lastIndex) {
                                    newList.add(newList.last() + 3600)
                                }
                            } else {
                                newList.addAll(newUsagesPattern)
                                repeat(newUsagesPattern.lastIndex - it) {
                                    newList.removeLast()
                                }
                            }
                            newUsagesPattern = newList
                            dialogIsShown = false
                        }
                    )}
                    RELATION_TO_MEALS -> { RollSelector(
                        modifier = Modifier,
                        startOffset = newMed.details.beforeFood,
                        list = stringArrayResource(id = R.array.food_relations).asList(),
                        onSelect = {
                            newMed = newMed.copy(details = newMed.details.copy(beforeFood = it))
                            dialogIsShown = false
                        }
                    )}
                    SET_REGIME -> { RollSelector(
                        modifier = Modifier,
                        startOffset = newCourse.regime,
                        list = stringArrayResource(id = R.array.regime).asList(),
                        onSelect = {
                            newCourse = newCourse.copy(regime = it)
                            dialogIsShown = false
                        }
                    )}
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun TabsIndicator(
    modifier: Modifier = Modifier,
    position: Int,
    quantity: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        repeat(quantity) {
            Indicator(isSelected = position == it, modifier = Modifier.weight(1f))
            if(it < quantity-1) Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
private fun Indicator(
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    val indicatorColor by animateColorAsState(
        targetValue = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 40,
            easing = LinearOutSlowInEasing
        )
    )
    Surface(
        shape = RoundedCornerShape(5.dp),
        color = indicatorColor,
        modifier = modifier
            .height(5.dp)
            .fillMaxWidth(),
        content = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopNavigation(
    modifier: Modifier = Modifier,
    label: String,
    position: Int
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TopAppBar(
            title = {
                Text(
                    text = label,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
        TabsIndicator(position = position, quantity = 2)
    }
}

@Composable
private fun NotificationsTimeSelector(
    modifier: Modifier = Modifier,
    onSelect: (List<Long>) -> Unit = {},
    onFinish: () -> Unit,
    pattern: List<Long>,
) {

    var dialogIsShown by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        LazyColumn {
            pattern.forEachIndexed { index, item ->
                item {
                    val itsDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(item), ZoneId.systemDefault())
                    NotificationItem(date = itsDate) {
                        dialogIsShown = true
                        selectedItem = index
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onFinish::invoke,
        content = { Text(text = stringResource(R.string.settings_save)) }
    )

    if(dialogIsShown) {
        Dialog(
            onDismissRequest = { dialogIsShown = false }
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp)
            ) {
                TimeSelector(
                    initTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(pattern[selectedItem]), ZoneId.systemDefault()),
                    onSelect = {
                        dialogIsShown = false
                        val newList = mutableListOf<Long>().apply {
                            addAll(pattern)
                            removeAt(selectedItem)
                            add(selectedItem, it.atZone(ZoneId.systemDefault()).toEpochSecond())
                            sort()
                        }
                        onSelect(newList)
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    onSelect: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = onSelect::invoke
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(32.dp)
        ) {
            Text(text = date.format(DateTimeFormatter.ofPattern("HH")), style = Typography.bodyLarge)
            Divider(modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
                .width(1.dp))
            Text(text = date.format(DateTimeFormatter.ofPattern("mm")), style = Typography.bodyLarge)
        }
    }
}

@Composable
private fun ReminderTimeSelector(
    modifier: Modifier = Modifier,
    oldCourseEndTime: LocalDateTime,
    newCourseStartTime: LocalDateTime,
    remindTime: LocalDateTime,
    interval: Long,
    onSetInterval: (Long) -> Unit,
    onSetRemindTime: (LocalDateTime) -> Unit,
    onSetRemindOffset: (Int) -> Unit,
    onSetComment: (String) -> Unit,
    onSave: () -> Unit
) {
    var dialogIsShown by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(SET_REMINDER) }
    var remindOffset by remember { mutableStateOf(0) }
    var updateNewCourseStartTime by remember { mutableStateOf(newCourseStartTime) }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_med_settings),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.background,
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                ) { dialogIsShown = true }
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxWidth()
            ) {
                val diff = Period.between(oldCourseEndTime.toLocalDate(), updateNewCourseStartTime.toLocalDate())
                ModalStringIndicator(
                    modifier = Modifier.padding(16.dp),
                    selected = "${diff.months} months ${diff.days} days",
                    label = stringResource(R.string.add_next_course_interval),
                    onClick = {
                        dialogType = SET_INTERVAL
                        dialogIsShown = true
                    }
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ModalStringIndicator(
                    modifier = Modifier.padding(16.dp),
                    selected = "$remindOffset days",
                    label = stringResource(R.string.add_next_course_remind_before),
                    onClick = {
                        dialogType = SET_REMINDER
                        dialogIsShown = true
                    }
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ModalStringIndicator(
                    modifier = Modifier.padding(16.dp),
                    selected = remindTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    label = stringResource(R.string.add_next_course_remind_time),
                    onClick = {
                        dialogType = SET_REMIND_TIME
                        dialogIsShown = true
                    }
                )
            }

        }
        Text(
            text = stringResource(R.string.add_course_comment),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.background,
            modifier = modifier
                .fillMaxWidth()
        ) {
            BasicKeyboardInput(
                modifier = Modifier.padding(16.dp),
                label = stringResource(R.string.add_course_comment),
                hideOnGo = true,
                capitalization = KeyboardCapitalization.Sentences,
                onChange = { onSetComment(it) }
            )
        }
        Button(
            onClick = onSave::invoke,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            content = { Text(text = stringResource(R.string.settings_save)) }
        )
    }
    if(dialogIsShown) {
        Dialog(onDismissRequest = { dialogIsShown = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background,
                elevation = 5.dp
            ) {
                when(dialogType) {
                    SET_INTERVAL -> {
                        DateDelaySelector(
                            initValue = Period.ofDays((interval/86400).toInt()),
                            onSelect = {
                                val i1 = (oldCourseEndTime).atZone(ZoneId.systemDefault()).toEpochSecond()
                                val i2 = (oldCourseEndTime + it).atZone(ZoneId.systemDefault()).toEpochSecond()
                                onSetInterval(i2-i1)
                                dialogIsShown = false
                                updateNewCourseStartTime = newCourseStartTime + it
                            }
                        )
                    }
                    SET_REMINDER -> {
                        val range = (0..10).toList().map { it.toString() }
                        RollSelector(
                            list = range,
                            startOffset = remindOffset,
                            onSelect = {
                                remindOffset = it
                                onSetRemindOffset(it)
                                dialogIsShown = false
                            }
                        )
                    }
                    SET_REMIND_TIME -> {
                        TimeSelector(
                            initTime = remindTime,
                            onSelect = {
                                val rt = oldCourseEndTime.plusSeconds(interval).withHour(it.hour).withMinute(it.minute)
                                onSetRemindTime(rt)
                                dialogIsShown = false
                            }
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun ModalStringIndicator(
    modifier: Modifier = Modifier,
    selected: String,
    label: String,
    style: TextStyle = Typography.bodyMedium,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = onClick::invoke
            )
    ) {
        Text(
            text = label,
            style = style.copy(fontWeight = FontWeight.Bold)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected,
                style = style
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}