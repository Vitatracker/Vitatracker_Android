package app.mybad.notifier.ui.screens.mainscreen

import android.content.res.Resources
import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.text.DateFormatSymbols
import java.time.*
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
    val usages = remember { mutableStateOf(uiState.usages) }

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
                usages = usages.value
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    uiState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit,
    usages: List<UsageCommonDomainModel>,
) {

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        MainScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            MainScreenMonthPager(uiState = uiState, changeData = changeData)
            MainScreenTextCategory()
            MainScreenLazyMedicines(usages = usages)
        }
    }

}

@Composable
fun MainScreenBackgroundImage() {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenMonthPager(
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
fun MainScreenWeekPager(
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

    LaunchedEffect(stateDay.currentPage) {
        delay(50)
        uiState.value = date.withDayOfMonth(stateDay.currentPage + 1)
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
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
                        .padding(1.dp)
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
                        .padding(1.dp)
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
fun MainScreenTextCategory() {
    Text(
        text = stringResource(id = R.string.main_screen_text_category),
        modifier = Modifier.padding(top = 25.dp, start = 20.dp),
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Justify, fontSize = 20.sp
    )
}

@Composable
fun MainScreenLazyMedicines(
    usages: List<UsageCommonDomainModel>
) {
//    if (courses && meds.isNotEmpty()) {
//        LazyColumn(modifier = Modifier, userScrollEnabled = true) {
//            courses.forEach { course ->
//                item {
//                    MainScreenCourseItem(
//                        course = course,
//                        med = meds.filter { it.id == course.medId }[0]
//                    )
//                }
//            }
//        }
//    }
}

@Composable
fun MainScreenCourseItem(
    course: CourseDomainModel,
    med: MedDomainModel
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        MainScreenTimeCourse()
        MainScreenFormCourse(course = course, med = med)
    }
}

@Composable
fun MainScreenTimeCourse() {
    Text(
        text = "10:00",
        modifier = Modifier.padding(20.dp),
        textAlign = TextAlign.Justify,
        fontSize = 20.sp
    )
}

@Composable
fun MainScreenFormCourse(
    course: CourseDomainModel,
    med: MedDomainModel
) {
    Column(modifier = Modifier.padding(10.dp)) {
        MainScreenFormCourseHeader(course = course, med = med)
    }
}

@Composable
fun MainScreenFormCourseHeader(
    course: CourseDomainModel,
    med: MedDomainModel
) {
    val units = stringArrayResource(R.array.units)

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp)
    ) {
        Row(modifier = Modifier) {
            Text(
                text = "10:00",
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Justify,
                fontSize = 20.sp
            )
            Column(modifier = Modifier) {
                Row(modifier = Modifier) {
                    Icon(
                        painter = painterResource(med.details.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(13.dp)
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                    Column(modifier = Modifier) {
                        Text(
                            text = "${med.name}, ${med.details.dose} ${units[med.details.measureUnit]}",
                            style = Typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "2 pcs", style = Typography.labelMedium)
                            Text(
                                text = "2 per day",
                                style = Typography.labelMedium,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                }

                Text(text = "${med.comment}", modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MainScreenButtonDetails()
                    MainScreenButtonAccept()
                }
            }
        }
    }
}

@Composable
fun MainScreenButtonDetails() {
    OutlinedButton(modifier = Modifier, onClick = { /*TODO*/ }) {
        Text(text = stringResource(id = R.string.main_screen_button_detail))
        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = null)
    }
}

@Composable
fun MainScreenButtonAccept() {
    Button(modifier = Modifier, onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.RadioButtonUnchecked, contentDescription = null)
        Text(
            text = stringResource(id = R.string.main_screen_button_accept),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

private fun collectUsages(
    date: LocalDateTime?,
    usages: List<UsageCommonDomainModel>,
): List<UsageCommonDomainModel> {
    val fromTime =
        date?.withHour(0)?.withMinute(0)?.withSecond(0)?.toEpochSecond(ZoneOffset.UTC) ?: 0L
    val toTime =
        date?.withHour(23)?.withMinute(59)?.withSecond(59)?.toEpochSecond(ZoneOffset.UTC) ?: 0L
    return usages.filter { it.useTime in fromTime..toTime }
}