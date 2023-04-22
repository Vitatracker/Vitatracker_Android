package app.mybad.notifier.ui.screens.mainscreen

import android.content.res.Resources
import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.login.*
import app.mybad.notifier.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainScreen(
    navController: NavHostController,
    vm: StartMainScreenViewModel
) {
//
    val uiState by vm.uiState.collectAsState()
    val dateNow = remember { mutableStateOf(uiState.date) }
    val sizeUsages = remember { mutableStateOf(uiState.allUsages) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(id = R.string.main_screen_top_bar_name),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Justify
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            MainScreen(
                navController = navController,
                uiState = dateNow,
                changeData = { vm.changeData(dateNow.value) },
                sizeUsages = sizeUsages.value,
                usages = uiState.usages,
                meds = uiState.meds
            )
        }
    }

}

@Composable
private fun MainScreen(
    navController: NavHostController,
    uiState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit,
    sizeUsages: Int,
    usages: List<UsageCommonDomainModel>,
    meds: List<MedDomainModel>
) {

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        MainScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            MainScreenMonthPager(
                uiState = uiState,
                changeData = changeData
            )
            MainScreenTextCategory()
            MainScreenLazyMedicines(usages = usages, meds = meds, sizeUsages = sizeUsages)
        }
    }

}

@Composable
private fun MainScreenBackgroundImage() {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreenMonthPager(
    uiState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit = {}
) {

    val paddingStart = 10.dp
    val paddingEnd = 10.dp
    val stateMonth = rememberPagerState(LocalDate.now().month.ordinal)
    val scope = rememberCoroutineScope()

    HorizontalPager(
        pageCount = Month.values().size,
        state = stateMonth,
        pageSpacing = 13.dp,
        pageSize = PageSize.Fixed(40.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, start = paddingStart, end = paddingEnd),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(
            horizontal = ((Resources.getSystem().configuration.screenWidthDp - 60) / 2).dp
        )
    ) { month ->
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = AnnotatedString(Month.values()[month].toString().substring(0, 3)),
                color = when (stateMonth.currentPage) {
                    month -> {
                        MaterialTheme.colorScheme.primary
                    }

                    month + 1 -> {
                        Color.Black
                    }

                    month - 1 -> {
                        Color.Black
                    }

                    month + 2 -> {
                        Color.Gray
                    }

                    month - 2 -> {
                        Color.Gray
                    }

                    else -> {
                        Color.LightGray
                    }
                },
                modifier = Modifier
                    .padding(1.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        scope.launch { stateMonth.animateScrollToPage(month) }
                    },
                fontWeight = when (stateMonth.currentPage) {
                    month -> {
                        FontWeight.Bold
                    }

                    month + 1 -> {
                        FontWeight.Normal
                    }

                    month - 1 -> {
                        FontWeight.Normal
                    }

                    else -> {
                        FontWeight.Normal
                    }
                },
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
    MainScreenWeekPager(
        monthState = stateMonth.currentPage,
        uiState = uiState,
        changeData = { changeData(uiState) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreenWeekPager(
    monthState: Int,
    uiState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit = {}
) {

    val paddingStart = 10.dp
    val paddingEnd = 10.dp

    val calendar: Calendar = Calendar.getInstance()
    var shortNameOfDay by remember { mutableStateOf("") }
    var countDay by remember { mutableStateOf(0) }
    val stateDay = rememberPagerState(uiState.value.dayOfMonth - 1)
    val scope = rememberCoroutineScope()
    val date by remember { mutableStateOf(LocalDateTime.now()) }
    val daysShortsArray = stringArrayResource(R.array.days_short)

    LaunchedEffect(stateDay.currentPage, monthState) {
        delay(50)
        uiState.value = date.withMonth(monthState + 1).withDayOfMonth(stateDay.currentPage + 1)
        changeData(uiState)
    }

    HorizontalPager(
        pageCount = YearMonth.of(LocalDate.now().year, monthState + 1).lengthOfMonth(),
        state = stateDay,
        pageSpacing = 10.dp,
        pageSize = PageSize.Fixed(40.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = paddingStart, end = paddingEnd),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(
            horizontal = ((Resources.getSystem().configuration.screenWidthDp - 60) / 2).dp
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (stateDay.currentPage == it) 1f else 0.5f)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    scope.launch { stateDay.animateScrollToPage(it) }
                },
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
            color = if (stateDay.currentPage == it) MaterialTheme.colorScheme.primary else Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .height(height = 50.dp)
                    .width(width = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                countDay = it + 1
                calendar.time = Date(Year.now().value, monthState, it - 1)
                shortNameOfDay = daysShortsArray[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                //DateFormatSymbols.getInstance(Locale.getDefault()).shortWeekdays[dayOfWeek]

                Text(
                    text = AnnotatedString(countDay.toString()),
                    modifier = Modifier,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = AnnotatedString(shortNameOfDay),
                    modifier = Modifier,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

@Composable
private fun MainScreenTextCategory() {
    Text(
        text = stringResource(id = R.string.main_screen_text_category),
        modifier = Modifier.padding(top = 25.dp, start = 20.dp),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Justify,
        fontSize = 25.sp
    )
}

@Composable
private fun MainScreenLazyMedicines(
    usages: List<UsageCommonDomainModel>,
    meds: List<MedDomainModel>,
    sizeUsages: Int
) {
    if (sizeUsages == 0) {
        MainScreenMedsClear()
    } else {
        if (meds.isNotEmpty() && usages.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(top = 10.dp), userScrollEnabled = true) {
                usages.sortedBy { it.useTime }.forEach { usage ->
                    item {
                        MainScreenCourseItem(
                            usage = usage,
                            med = meds.filter { it.id == usage.medId }[0]
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainScreenMedsClear() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MainScreenMedsClearText()
        Spacer(modifier = Modifier.height(10.dp))
        MainScreenMedsClearImage()
    }
}

@Composable
private fun MainScreenMedsClearText() {
    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.main_screen_meds_clear_start),
            modifier = Modifier,
            fontSize = 25.sp,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.main_screen_meds_clear_add),
            modifier = Modifier,
            fontSize = 25.sp,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MainScreenMedsClearImage() {
    Box(modifier = Modifier.fillMaxSize(), Alignment.BottomCenter) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.main_screen_clear_med),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
        )
    }
}

@Composable
private fun MainScreenCourseItem(
    usage: UsageCommonDomainModel,
    med: MedDomainModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = setBorderColorCard(
            usageTime = usage.useTime,
            usage.factUseTime.toInt() != -1
        ),
        color = setBackgroundColorCard(
            usageTime = usage.useTime,
            usage.factUseTime.toInt() != -1
        ),
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainScreenTimeCourse(usageTime = usage.useTime, usage.factUseTime.toInt() != -1)
            MainScreenFormCourseHeader(med = med, usages = usage)
        }
    }

}

@Composable
private fun MainScreenTimeCourse(usageTime: Long, isDone: Boolean) {
    Surface(
        modifier = Modifier.padding(start = 10.dp),
        shape = RoundedCornerShape(10.dp),
        border = setBorderColorTimeCard(usageTime = usageTime, isDone = isDone),
        color = setBackgroundColorTime(usageTime = usageTime, isDone = isDone)
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = getTimeFromLong(usageTime),
            textAlign = TextAlign.Justify,
            fontSize = 20.sp,
            color = setTextColorTime(usageTime = usageTime, isDone = isDone)
        )
    }
}

@Composable
private fun MainScreenFormCourseHeader(
    med: MedDomainModel,
    usages: UsageCommonDomainModel
) {
    val usageTime = usages.useTime
    val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    val types = stringArrayResource(R.array.types)
    val relations = stringArrayResource(R.array.food_relations)
    val colors = integerArrayResource(R.array.colors)

    Surface(
        modifier = Modifier
            .padding(10.dp),
        color = setBackgroundColorCard(
            usageTime = usageTime,
            isDone = usages.factUseTime.toInt() != -1
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Row(modifier = Modifier) {
                    Text(
                        text = "${med.name}",
                        style = Typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(r.getResourceId(med.icon, 0)),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp),
                        tint = Color(colors[med.color])//MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                ) {
                    Text(
                        text = "${med.dose} ${types[med.type]}",
                        style = Typography.labelMedium
                    )
                    Text(
                        text = "|",
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp),
                        style = Typography.labelMedium,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = relations[med.beforeFood],
                        style = Typography.labelMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            MainScreenButtonAccept(
                usageTime = usageTime,
                isDone = usages.factUseTime.toInt() != -1
            )
        }
    }
}

@Composable
private fun MainScreenButtonAccept(usageTime: Long, isDone: Boolean) {
    val nowTime = convertDateToLong(LocalDateTime.now())
    val nowDate = getDateFromLong(date = nowTime)
    val usageDate = getDateFromLong(date = usageTime)

    return if (isDone) {
        Icon(
            painter = painterResource(R.drawable.done),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .clickable {
//                    val n = Instant.now().epochSecond
                }
        )
    } else if (nowDate > usageDate) {
        Icon(
            painter = painterResource(R.drawable.undone),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .clickable {
//                    val n = Instant.now().epochSecond
                }
        )
    } else if (nowDate < usageDate) {
        Icon(
            painter = painterResource(R.drawable.undone),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .clickable {
//                    val n = Instant.now().epochSecond
                }
        )
    } else if (getTimeFromLong(time = nowTime) > getTimeFromLong(time = usageTime.plus(3600))) {
        Icon(
            painter = painterResource(R.drawable.undone),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .clickable {
//                    val n = Instant.now().epochSecond
                }
        )
    } else {
        Icon(
            painter = painterResource(R.drawable.undone),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .clickable {
//                    val n = Instant.now().epochSecond
                }
        )
    }
}

@Composable
private fun setBorderColorCard(usageTime: Long, isDone: Boolean): BorderStroke {
    val nowTime = convertDateToLong(LocalDateTime.now())
    val nowDate = getDateFromLong(date = nowTime)
    val usageDate = getDateFromLong(date = usageTime)

    return if (isDone) {
        BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
    } else if (nowDate > usageDate) {
        BorderStroke(
            1.dp,
            color = MaterialTheme.colorScheme.error
        )
    } else if (nowDate < usageDate) {
        BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
    } else if (getTimeFromLong(time = nowTime) > getTimeFromLong(time = usageTime.plus(3600))) {
        BorderStroke(0.dp, MaterialTheme.colorScheme.error)
    } else {
        BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
    }

}

@Composable
private fun setBorderColorTimeCard(usageTime: Long, isDone: Boolean): BorderStroke {
    val nowTime = convertDateToLong(LocalDateTime.now())
    val nowDate = getDateFromLong(date = nowTime)
    val usageDate = getDateFromLong(date = usageTime)

    return if (isDone) {
        BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
    } else if (nowDate > usageDate) {
        BorderStroke(
            1.dp,
            color = MaterialTheme.colorScheme.error
        )
    } else if (nowDate < usageDate) {
        BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
    } else if (getTimeFromLong(time = nowTime) > getTimeFromLong(time = usageTime.plus(3600))) {
        BorderStroke(0.dp, MaterialTheme.colorScheme.error)
    } else {
        BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
    }
}

@Composable
private fun setBackgroundColorTime(usageTime: Long, isDone: Boolean): Color {
    val nowTime = convertDateToLong(LocalDateTime.now())
    val nowDate = getDateFromLong(date = nowTime)
    val usageDate = getDateFromLong(date = usageTime)

    return if (isDone) {
        MaterialTheme.colorScheme.primaryContainer
    } else if (nowDate > usageDate) {
        MaterialTheme.colorScheme.errorContainer
    } else if (nowDate < usageDate) {
        MaterialTheme.colorScheme.primaryContainer
    } else if (getTimeFromLong(time = nowTime) > getTimeFromLong(time = usageTime.plus(3600))) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
}

@Composable
private fun setBackgroundColorCard(usageTime: Long, isDone: Boolean): Color {

    val nowTime = convertDateToLong(LocalDateTime.now())
    val nowDate = getDateFromLong(date = nowTime)
    val usageDate = getDateFromLong(date = usageTime)

    return if (isDone) {
        Color(0xFFF9FAFE)//MaterialTheme.colorScheme.primaryContainer
    } else if (nowDate > usageDate) {
        Color(0xFFF2B2B2)//MaterialTheme.colorScheme.errorContainer
    } else if (nowDate < usageDate) {
        Color(0xFFF9FAFE)//MaterialTheme.colorScheme.primaryContainer
    } else if (getTimeFromLong(time = nowTime) > getTimeFromLong(time = usageTime.plus(3600))) {
        Color(0xFFF2B2B2)//MaterialTheme.colorScheme.errorContainer
    } else {
        Color(0xFFF9FAFE)//MaterialTheme.colorScheme.primaryContainer
    }
}

@Composable
private fun setTextColorTime(usageTime: Long, isDone: Boolean): Color {
    val nowTime = convertDateToLong(LocalDateTime.now())
    val nowDate = getDateFromLong(date = nowTime)
    val usageDate = getDateFromLong(date = usageTime)

    return if (isDone) {
        MaterialTheme.colorScheme.primary
    } else if (nowDate > usageDate) {
        MaterialTheme.colorScheme.error
    } else if (nowDate < usageDate) {
        MaterialTheme.colorScheme.primary
    } else if (getTimeFromLong(time = nowTime) > getTimeFromLong(time = usageTime.plus(3600))) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }
}

private fun getTimeFromLong(time: Long): String {
    return LocalDateTime
        .ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("HH:mm"))
}

private fun getDateFromLong(date: Long): String {
    return LocalDateTime
        .ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
}

private fun convertDateToLong(date: LocalDateTime): Long {
    val zdt: ZonedDateTime = ZonedDateTime.of(date, ZoneId.systemDefault())
    return zdt.toInstant().toEpochMilli() / 1000
}