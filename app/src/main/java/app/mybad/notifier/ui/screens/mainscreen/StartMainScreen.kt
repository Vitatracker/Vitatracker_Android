package app.mybad.notifier.ui.screens.mainscreen

import android.content.res.Resources
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.login.*
import app.mybad.notifier.ui.screens.mainscreen.StartMainScreenViewModel
import app.mybad.notifier.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Time
import java.text.DateFormatSymbols
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainScreen(
    navController: NavHostController,
    vm: StartMainScreenViewModel
) {

    val uiState by vm.uiState.collectAsState()
    val dateNow = remember { mutableStateOf(uiState.date) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.main_screen_top_bar_name)) },
                navigationIcon = {
                    IconButton(onClick = { /*navController.navigate(route = AuthorizationNavItem.Authorization.route)*/ }) {
//                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
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
                usages = uiState.usages,
                meds = uiState.meds
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreen(
    navController: NavHostController,
    uiState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit,
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
            MainScreenLazyMedicines(usages = usages, meds = meds)
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
            horizontal = (Resources.getSystem().configuration.screenWidthDp.dp - paddingStart * 2) / 2
        )
    ) { month ->
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = AnnotatedString(Month.values()[month].toString().substring(0, 3)),
                color = if (stateMonth.currentPage == month) {
                    MaterialTheme.colorScheme.primary
                } else if (stateMonth.currentPage == month + 1) {
                    Color.Black
                } else if (stateMonth.currentPage == month - 1) {
                    Color.Black
                } else if (stateMonth.currentPage == month + 2) {
                    Color.Gray
                } else if (stateMonth.currentPage == month - 2) {
                    Color.Gray
                } else {
                    Color.LightGray
                },
                modifier = Modifier
                    .padding(1.dp)
                    .clickable {
                        scope.launch { stateMonth.animateScrollToPage(month) }
                    },
                fontWeight = if (stateMonth.currentPage == month) {
                    FontWeight.Bold
                } else if (stateMonth.currentPage == month + 1) {
                    FontWeight.Normal
                } else if (stateMonth.currentPage == month - 1) {
                    FontWeight.Normal
                } else {
                    FontWeight.Normal
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
    var dayOfWeek by remember { mutableStateOf(0) }
    var shortNameOfDay by remember { mutableStateOf("") }
    var countDay by remember { mutableStateOf(0) }
    val stateDay = rememberPagerState(uiState.value.dayOfMonth - 1)
    val scope = rememberCoroutineScope()
    val date by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(stateDay.currentPage, monthState) {
        delay(50)
        uiState.value = date.withMonth(monthState + 1).withDayOfMonth(stateDay.currentPage + 1)
        changeData(uiState)
    }

    HorizontalPager(
        pageCount = YearMonth.of(LocalDate.now().year, monthState + 1).lengthOfMonth(),
        state = stateDay,
        pageSpacing = 13.dp,
        pageSize = PageSize.Fixed(40.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = paddingStart, end = paddingEnd),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(
            horizontal = (Resources.getSystem().configuration.screenWidthDp.dp - paddingStart * 2) / 2
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
            color = if (stateDay.currentPage == it) MaterialTheme.colorScheme.primary else Color.White
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                countDay = it + 1
                calendar.time = Date(Year.now().value, monthState, it)
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                shortNameOfDay =
                    DateFormatSymbols.getInstance(Locale.getDefault()).shortWeekdays[dayOfWeek]

                Text(
                    text = AnnotatedString(countDay.toString()),
                    modifier = Modifier
//                        .padding(1.dp)
                        .clickable {
                            scope.launch { stateDay.animateScrollToPage(it) }
                        },
                    fontWeight = if (stateDay.currentPage == it) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = AnnotatedString(shortNameOfDay),
                    modifier = Modifier
//                        .padding(1.dp)
                        .clickable {
                            scope.launch { stateDay.animateScrollToPage(it) }
                        },
                    fontWeight = if (stateDay.currentPage == it) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
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
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Justify, fontSize = 25.sp
    )
}

@Composable
private fun MainScreenLazyMedicines(
    usages: List<UsageCommonDomainModel>,
    meds: List<MedDomainModel>
) {
    if (meds.isNotEmpty() && usages.isNotEmpty()) {
        LazyColumn(modifier = Modifier.padding(top = 10.dp), userScrollEnabled = true) {
            usages.forEach { usage ->
                item {
                    MainScreenCourseItem(
                        usage = usage,
                        med = meds.first()
                    )
                }
            }
        }
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
//        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
        border = SetBorderColor(usageTime = usage.useTime)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainScreenTimeCourse(usageTime = usage.useTime)
            MainScreenFormCourseHeader(med = med, usageTime = usage.useTime)
        }
    }

}

@Composable
private fun MainScreenTimeCourse(usageTime: Long) {
    Surface(
        modifier = Modifier.padding(start = 10.dp),
        shape = RoundedCornerShape(5.dp),
//        border = BorderStroke(0.dp, color = MaterialTheme.colorScheme.primaryContainer)
        border = SetBorderColor(usageTime = usageTime)
    ) {
        Text(
            modifier = Modifier
                .background(SetColor(usageTime = usageTime))//MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            text = getTime(usageTime),
            textAlign = TextAlign.Justify,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun MainScreenFormCourseHeader(
    med: MedDomainModel,
    usageTime: Long
) {
    val units = stringArrayResource(R.array.units)

    Surface(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = "${med.name}",
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                ) {
                    Text(
                        text = "${med.dose} таблетки",
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
                        text = if (med.beforeFood == 0) "после еды" else "до еды",
                        style = Typography.labelMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            MainScreenButtonAccept(usageTime = usageTime)
        }
    }
}

@Composable
private fun MainScreenButtonAccept(usageTime: Long) {
//        var isFalse: Boolean = false
//        RadioButton(
//            onClick = { isFalse = !isFalse },
//            selected = false,
//            modifier = Modifier.size(50.dp)
//        )
    Icon(
        imageVector = Icons.Default.RadioButtonUnchecked,
        contentDescription = null,
        tint = SetColor(usageTime = usageTime),
        modifier = Modifier
            .padding(end = 8.dp)
            .size(35.dp)
            .clip(CircleShape)
            .clickable {

            }
    )
}

@Composable
private fun SetBorderColor(usageTime: Long): BorderStroke {
    val nowTime = convertDateToLong(LocalDateTime.now())
    when {
        getTime(nowTime) < getTime(usageTime) -> return BorderStroke(0.dp, color = Color.Gray)
        getTime(nowTime) > getTime(usageTime) -> return BorderStroke(
            0.dp,
            color = MaterialTheme.colorScheme.error
        )
        getTime(nowTime) == getTime(usageTime) -> return BorderStroke(
            0.dp,
            color = MaterialTheme.colorScheme.primaryContainer
        )
    }
    return BorderStroke(0.dp, color = MaterialTheme.colorScheme.primaryContainer)
}

@Composable
private fun SetColor(usageTime: Long): Color {
    val nowTime = convertDateToLong(LocalDateTime.now())
    when {
        getTime(nowTime) < getTime(usageTime) -> return Color.Gray
        getTime(nowTime) > getTime(usageTime) -> return MaterialTheme.colorScheme.error
        getTime(nowTime) == getTime(usageTime) -> return MaterialTheme.colorScheme.primaryContainer
    }
    return MaterialTheme.colorScheme.primaryContainer
}

private fun getTime(date: Long): String {
    return LocalDateTime
        .ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("HH:mm"))
}

private fun convertDateToLong(date: LocalDateTime): Long {
    val zdt: ZonedDateTime = ZonedDateTime.of(date, ZoneId.systemDefault())
    return zdt.toInstant().toEpochMilli() / 1000
}