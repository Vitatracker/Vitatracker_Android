package app.mybad.notifier.ui.screens.addcourse_redesigned

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.addcourse_redesigned.IntentTypes.*
import app.mybad.notifier.ui.screens.addcourse_redesigned.SelectionType.END_DATE
import app.mybad.notifier.ui.screens.addcourse_redesigned.SelectionType.START_DATE
import app.mybad.notifier.ui.screens.addcourse_redesigned.common.*
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.course.CreateCourseIntent
import app.mybad.notifier.ui.screens.course.CreateCourseState
import app.mybad.notifier.ui.theme.Typography
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private enum class IntentTypes {
    MEASURE_UNITS,
    QUANTITY_OF_USAGES,
    RELATION_TO_MEALS,
    NOTIFICATION_TIME,
    SET_REMINDER,
    SET_REGIME,
}
private enum class SelectionType {
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
        id = startTime
    )) }
    var newCourse by remember { mutableStateOf(CourseDomainModel(
        id = startTime,
        startDate = startTime,
        endDate = startLocalTime.plusMonths(1).atZone(ZoneId.systemDefault()).toEpochSecond()
    )) }
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
                   ReminderTimeSelector(
                       startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(newCourse.endDate), ZoneId.systemDefault())
                   ) { }
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
                                    startDate = time.atZone(ZoneId.systemDefault()).toEpochSecond()
                                )
                            },
                            onSetEnd = { time ->
                                newCourse = newCourse.copy(
                                    endDate = time.atZone(ZoneId.systemDefault()).toEpochSecond()
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
                    if(pagerState.currentPage == 1) onFinish()
                    else if(newMed.name.isNullOrBlank() || newMed.details.dose == 0) {
                        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
                    } else scope.launch { pagerState.animateScrollToPage(1) }
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
                    MEASURE_UNITS -> { SelectorBottomSheet(
                        modifier = Modifier,
                        list = stringArrayResource(id = R.array.units).asList(),
                        startOffset = newMed.details.measureUnit,
                        onSelect = {
                            newMed = newMed.copy(details = newMed.details.copy(measureUnit = it))
                            dialogIsShown = false
                        }
                    )}
                    QUANTITY_OF_USAGES -> { SelectorBottomSheet(
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
                    RELATION_TO_MEALS -> { SelectorBottomSheet(
                        modifier = Modifier,
                        startOffset = newMed.details.beforeFood,
                        list = stringArrayResource(id = R.array.food_relations).asList(),
                        onSelect = {
                            newMed = newMed.copy(details = newMed.details.copy(beforeFood = it))
                            dialogIsShown = false
                        }
                    )}
                    SET_REGIME -> { SelectorBottomSheet(
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
private fun MedCreationScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel,
    usagesPattern: List<Long>,
    onSelectUnits: () -> Unit,
    onSelectQuantity: () -> Unit,
    onSelectFoodRelation: () -> Unit,
    onSelectNotificationsTime: () -> Unit,
    onSetName: (String) -> Unit,
    onSetDose: (Int) -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.add_med_dosa_and_usage),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                //name
                BasicKeyboardInput(
                    modifier = Modifier.padding(16.dp),
                    label = stringResource(R.string.add_med_name),
                    init = med.name ?: "",
                    onChange = onSetName::invoke
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                //dose
                BasicKeyboardInput(
                    modifier = Modifier.padding(16.dp),
                    label = stringResource(R.string.add_med_dose).lowercase(),
                    init = if(med.details.dose == 0) "" else med.details.dose.toString(),
                    keyboardType = KeyboardType.Number,
                    alignRight = true,
                    hideOnGo = true,
                    onChange = { onSetDose(it.toIntOrNull() ?: 0) },
                    prefix = { Text(
                        text = "${stringResource(R.string.add_med_dose)}: ",
                        style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    ) }
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                //units
                ModalInteractionSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = med.details.measureUnit,
                    label = stringResource(R.string.add_med_unit),
                    items = stringArrayResource(R.array.units).toList(),
                    onClick = onSelectUnits::invoke,   //open modal here
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                //iterations
                val range = (1..10).map { it.toString() }
                ModalInteractionSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = usagesPattern.lastIndex,
                    label = stringResource(R.string.add_course_notifications_quantity),
                    items = range,
                    onClick = onSelectQuantity::invoke,   //open modal here
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ModalInteractionSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = med.details.beforeFood,
                    label = stringResource(R.string.add_med_food_relation),
                    items = stringArrayResource(R.array.food_relations).asList(),
                    onClick = onSelectFoodRelation::invoke,   //open modal here
                )
            }
        }
        Text(
            text = stringResource(R.string.add_med_settings),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
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
                    onClick = onSelectNotificationsTime::invoke
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_med_remind_time),
                    style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    val list = mutableListOf<String>().apply {
                        usagesPattern.forEach {
                            val time = LocalDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                            add(time)
                        }
                    }.joinToString(", ")
                    Text(
                        text = list,
                        style = Typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp)
                            .weight(0.1f)
                    )
                }
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseCreationScreen(
    modifier: Modifier = Modifier,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    regime: Int,
    onSetStart: (LocalDateTime) -> Unit,
    onSetEnd: (LocalDateTime) -> Unit,
    onSetRegime: () -> Unit,
    onSetComment: (String) -> Unit,
    onSetReminder: () -> Unit
) {
    val pickerState = rememberDatePickerState()
    var datePickerShown by remember { mutableStateOf(false) }
    var datePickerType by remember { mutableStateOf(START_DATE) }

    Column {
        Text(
            text = stringResource(R.string.mycourse_duration),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                ModalDateSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = startTime,
                    label = stringResource(R.string.add_course_start_time),
                    onClick = {
                        datePickerShown = true
                        datePickerType = START_DATE
                    }
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ModalDateSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = endTime,
                    label = stringResource(R.string.add_course_start_time),
                    onClick = {
                        datePickerShown = true
                        datePickerType = END_DATE
                    }
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ModalInteractionSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = regime,
                    label = stringResource(R.string.medication_regime),
                    items = stringArrayResource(R.array.regime).toList(),
                    onClick = onSetRegime::invoke
                )
            }
        }
        Text(
            text = stringResource(R.string.add_course_comment),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
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
            onClick = onSetReminder::invoke,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.clock),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(20.dp)
            )
            Text(
                text = stringResource(R.string.add_course_reminder),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
    if(datePickerShown) {
        DatePickerDialog(
            onDismissRequest = { datePickerShown = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerShown = false
                        when(datePickerType) {
                            START_DATE -> {
                                onSetStart(
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(pickerState.selectedDateMillis ?: 0L
                                        ), ZoneId.systemDefault())
                                )
                            }
                            END_DATE -> {
                                onSetEnd(
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(pickerState.selectedDateMillis ?: 0L
                                        ), ZoneId.systemDefault())
                                )
                            }
                        }
                    },
                    content = { Text(text = stringResource(R.string.settings_save))}
                )
            },
            dismissButton = {
                Button(
                    onClick = { datePickerShown = false },
                    content = { Text(text = stringResource(R.string.settings_cancel))}
                )
            }
        ) {
            DatePicker(
                title = { Text(
                    text = stringResource(R.string.add_course_start_time),
                    style = Typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) },
                state = pickerState,
                showModeToggle = true,
                dateValidator = { when(datePickerType) {
                    START_DATE -> it/1000 > LocalDateTime.now().withHour(0).atZone(ZoneId.systemDefault()).toEpochSecond()
                    END_DATE -> it/1000 > startTime.atZone(ZoneId.systemDefault()).toEpochSecond()
                } }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderTimeSelector(
    modifier: Modifier = Modifier,
    startTime: LocalDateTime,
    onSetReminder: (LocalDateTime) -> Unit
) {
    var dialogIsShown by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = startTime.atZone(ZoneId.systemDefault()).toEpochSecond()*1000
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_med_remind_time),
                    style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    val date = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    Text(
                        text = date,
                        style = Typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp)
                            .weight(0.1f)
                    )
                }
            }
        }
    }

    if(dialogIsShown) {
        DatePickerDialog(
            onDismissRequest = { dialogIsShown = false },
            confirmButton = {
                Button(
                    onClick = {
                        dialogIsShown = false
                        onSetReminder(
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(pickerState.selectedDateMillis ?: 0L),
                                ZoneId.systemDefault()
                            )
                        )
                    },
                    content = { Text(text = stringResource(R.string.settings_save))}
                )
            },
            dismissButton = {
                Button(
                    onClick = { dialogIsShown = false },
                    content = { Text(text = stringResource(R.string.settings_cancel))}
                )
            }
        ) {
            DatePicker(
                state = pickerState,
                showModeToggle = true,
                dateValidator = {
                    it/1000 > LocalDateTime.now().withHour(0).atZone(ZoneId.systemDefault()).toEpochSecond()
                }
            )
        }
    }
}
