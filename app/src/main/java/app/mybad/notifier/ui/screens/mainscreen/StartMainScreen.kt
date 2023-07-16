package app.mybad.notifier.ui.screens.mainscreen

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.PickColor
import app.mybad.notifier.ui.screens.authorization.login.*
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.ui.theme.textColorFirst
import app.mybad.notifier.ui.theme.textColorSecond
import app.mybad.notifier.ui.theme.textColorThird
import app.mybad.theme.R
import app.mybad.theme.utils.TIME_IS_UP
import app.mybad.theme.utils.changeDate
import app.mybad.theme.utils.dayShortDisplay
import app.mybad.theme.utils.getCurrentDateTime
import app.mybad.theme.utils.getDaysOfMonth
import app.mybad.theme.utils.monthShortDisplay
import app.mybad.theme.utils.toEpochSecond
import app.mybad.theme.utils.toTimeDisplay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainScreen(
    navController: NavHostController,
    vm: StartMainScreenViewModel
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val dateNow = remember { mutableStateOf(uiState.date) }
    val usagesSize by remember { mutableIntStateOf(uiState.usagesSize) }
    val usageCommon = remember { mutableStateOf(UsageDomainModel()) }

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
                }
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
                usagesSize = usagesSize,
                usages = uiState.usages,
                remedies = uiState.remedies,
                courses = uiState.courses,
                usageState = usageCommon,
                setUsageFactTime = { vm.setUsagesFactTime(usage = usageCommon.value) }
            )
        }
    }
}

@Composable
private fun MainScreen(
    navController: NavHostController,
    uiState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit,
    remedies: List<RemedyDomainModel>,
    courses: List<CourseDomainModel>,
    usagesSize: Int,
    usages: List<UsageDomainModel>,
    usageState: MutableState<UsageDomainModel>,
    setUsageFactTime: (UsageDomainModel) -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        MainScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            MainScreenMonthPager(
                uiState = uiState,
                changeData = changeData
            )
            MainScreenLazyMedicines(
                uiState = uiState,
                changeData = changeData,
                remedies = remedies,
                courses = courses,
                usages = usages,
                usagesSize = usagesSize,
                usageState = usageState,
                setUsageFactTime = setUsageFactTime,
            )
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
    val stateMonth = rememberPagerState(getCurrentDateTime().month.ordinal) { 12 }
    val scope = rememberCoroutineScope()

    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, start = 10.dp, end = 10.dp),
        state = stateMonth,
        pageSpacing = 13.dp,
        contentPadding = PaddingValues(
            horizontal = ((LocalConfiguration.current.screenWidthDp - 70) / 2).dp
        ),
        pageSize = PageSize.Fixed(50.dp)
    ) { month ->
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = month.monthShortDisplay(),
                color = getMonthColor(stateMonth.currentPage, month),
                modifier = Modifier
                    .padding(1.dp)
                    .clickable {
                        scope.launch { stateMonth.animateScrollToPage(month) }
                    },
                fontWeight = getFontWeight(stateMonth.currentPage, month),
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

    var shortNameOfDay by remember { mutableStateOf("") }
    var countDay by remember { mutableStateOf(0) }
    val currentDate = getCurrentDateTime()
    val date by remember { mutableStateOf(currentDate) }
    val stateDay = rememberPagerState(uiState.value.dayOfMonth - 1) {
        // количество дней в месяце с учетом высокосного года
        date.changeDate(month = monthState + 1).getDaysOfMonth()
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(stateDay.currentPage, monthState) {
        delay(50)
        uiState.value = date.changeDate(
            month = monthState + 1,
            dayOfMonth = stateDay.currentPage + 1
        )
        changeData(uiState)
    }

    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = paddingStart, end = paddingEnd),
        state = stateDay,
        pageSpacing = 13.dp,
        contentPadding = PaddingValues(
            horizontal = ((Resources.getSystem().configuration.screenWidthDp - 60) / 2).dp
        )
    ) { page ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (stateDay.currentPage == page) 1f else 0.5f)
                .clickable {
                    scope.launch { stateDay.animateScrollToPage(page) }
                },
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
            color = if (stateDay.currentPage == page) MaterialTheme.colorScheme.primary else Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .height(height = 50.dp)
                    .width(width = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                countDay = page + 1
                shortNameOfDay =
                    currentDate.changeDate(month = monthState + 1, dayOfMonth = page + 1)
                        .dayShortDisplay()

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
    uiState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit,
    remedies: List<RemedyDomainModel>,
    courses: List<CourseDomainModel>,
    usages: List<UsageDomainModel>,
    usagesSize: Int,
    usageState: MutableState<UsageDomainModel>,
    setUsageFactTime: (UsageDomainModel) -> Unit,
) {
    Log.w(
        "VTTAG",
        "StartMainScreen::MainScreenLazyMedicines: sizeUsages=$usagesSize usages=${usages.size}"
    )
    if (remedies.isNotEmpty() && usages.isNotEmpty()) {
        MainScreenTextCategory()
        Log.w("VTTAG", "StartMainScreen::MainScreenLazyMedicines: usages=${usages.size}")
        LazyColumn(modifier = Modifier.padding(top = 10.dp), userScrollEnabled = true) {
            items(usages.sortedBy { it.useTime }) { usage ->
                val remedy = courses.firstOrNull { it.id == usage.courseId }?.let { course ->
                    remedies.firstOrNull { it.id == course.remedyId }
                } ?: RemedyDomainModel()
                MainScreenCourseItem(
                    usage = usage,
                    remedy = remedy,
                    usageState = usageState,
                    setUsageFactTime = setUsageFactTime,
                    uiState = uiState,
                    changeData = changeData
                )
            }
        }
    } else MainScreenMedsClear()
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
    remedy: RemedyDomainModel,
    usage: UsageDomainModel,
    usageState: MutableState<UsageDomainModel>,
    setUsageFactTime: (UsageDomainModel) -> Unit,
    uiState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = setBorderColorCard(
            usageTime = usage.useTime,
            isDone = usage.factUseTime != -1L
        ),
        color = setBackgroundColorCard(
            usageTime = usage.useTime,
            isDone = usage.factUseTime != -1L
        ),
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainScreenTimeCourse(
                usageTime = usage.useTime,
                isDone = usage.factUseTime != -1L
            )
            MainScreenFormCourseHeader(
                remedy = remedy,
                usage = usage,
                usageState = usageState,
                setUsageFactTime = setUsageFactTime,
                dateState = uiState,
                changeData = changeData
            )
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
            text = usageTime.toTimeDisplay(),
            modifier = Modifier
                .padding(8.dp),
            color = setTextColorTime(usageTime = usageTime, isDone = isDone),
            fontSize = 20.sp,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
        )
    }
}

@SuppressLint("Recycle")
@Composable
private fun MainScreenFormCourseHeader(
    remedy: RemedyDomainModel,
    usage: UsageDomainModel,
    usageState: MutableState<UsageDomainModel>,
    setUsageFactTime: (UsageDomainModel) -> Unit,
    dateState: MutableState<LocalDateTime>,
    changeData: (MutableState<LocalDateTime>) -> Unit
) {
    val usageTime = usage.useTime
    val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    val types = stringArrayResource(R.array.types)
    val relations = stringArrayResource(R.array.food_relations)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Row(modifier = Modifier) {
                // название препарата
                Text(
                    text = "${remedy.name}",
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))
                // иконка препарата
                Icon(
                    painter = painterResource(r.getResourceId(remedy.icon, 0)),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = PickColor.getColor(remedy.color)
                )
            }
            Row(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = "${remedy.dose} ${types[remedy.type]}",
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
                    text = relations[remedy.beforeFood],
                    style = Typography.labelMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
        MainScreenButtonAccept(
            usageTime = usageTime,
            isDone = usage.factUseTime.toInt() != -1,
            setUsageFactTime = {
                usageState.value = usage.copy(
                    factUseTime = if (usage.factUseTime == -1L) {
                        getCurrentDateTime().toEpochSecond()
                    } else {
                        -1
                    }
                )
                setUsageFactTime(
                    usageState.value
                )
                changeData(dateState)
            }
        )
    }
}

@Composable
private fun MainScreenButtonAccept(
    usageTime: Long,
    isDone: Boolean,
    setUsageFactTime: () -> Unit
) {
    val nowDate = getCurrentDateTime().toEpochSecond()

    val tint: Color
    val painter: Painter

    when {
        isDone -> {
            painter = painterResource(R.drawable.done)
            tint = MaterialTheme.colorScheme.primary
        }

        nowDate < usageTime -> {
            painter = painterResource(R.drawable.undone)
            tint = MaterialTheme.colorScheme.primary
        }

        nowDate > usageTime + TIME_IS_UP -> {
            painter = painterResource(R.drawable.undone)
            tint = MaterialTheme.colorScheme.error
        }

        nowDate > usageTime -> {
            painter = painterResource(R.drawable.undone)
            tint = MaterialTheme.colorScheme.error
        }

        else -> {
            painter = painterResource(R.drawable.undone)
            tint = MaterialTheme.colorScheme.primary
        }
    }

    return Icon(
        painter = painter,
        contentDescription = null,
        tint = tint,
        modifier = Modifier
            .padding(start = 8.dp)
            .size(40.dp)
            .clip(CircleShape)
            .clickable {
                setUsageFactTime()
            }
    )
}

private fun getFontWeight(page: Int, month: Int) = when (page) {
    month -> FontWeight.Bold
    (month + 1), (month - 1) -> FontWeight.Normal
    else -> FontWeight.Normal
}

@Composable
private fun getMonthColor(page: Int, month: Int) = when (page) {
    month -> MaterialTheme.colorScheme.primary
    (month + 1), (month - 1) -> textColorFirst
    (month + 2), (month - 2) -> textColorSecond
    else -> textColorThird
}

// цвет рамки вокруг карточки
@Composable
private fun setBorderColorCard(usageTime: Long, isDone: Boolean): BorderStroke {
    val nowDate = getCurrentDateTime().toEpochSecond()
//TODO("проверить тут время, возможно в usageTime подменять дату или нет")
    return when {
        isDone -> BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
        nowDate < usageTime -> BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
        nowDate > usageTime + TIME_IS_UP -> BorderStroke(0.dp, MaterialTheme.colorScheme.error)
        nowDate > usageTime -> BorderStroke(1.dp, color = MaterialTheme.colorScheme.error)
        else -> BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
    }
}

// цвет фона карточки 5%
@Composable
private fun setBackgroundColorCard(usageTime: Long, isDone: Boolean): Color {
    val nowDate = getCurrentDateTime().toEpochSecond()

    return when {
        isDone -> MaterialTheme.colorScheme.primaryContainer
        nowDate < usageTime -> MaterialTheme.colorScheme.primaryContainer
        nowDate > usageTime + TIME_IS_UP -> MaterialTheme.colorScheme.errorContainer
        nowDate > usageTime -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }.copy(alpha = 0.05f)
}

// цвет рамки вокруг времени
@Composable
private fun setBorderColorTimeCard(usageTime: Long, isDone: Boolean): BorderStroke {
    val nowDate = getCurrentDateTime().toEpochSecond()

    return when {
        isDone -> BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
        nowDate < usageTime -> BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
        nowDate > usageTime + TIME_IS_UP -> BorderStroke(0.dp, MaterialTheme.colorScheme.error)
        nowDate > usageTime -> BorderStroke(1.dp, color = MaterialTheme.colorScheme.error)
        else -> BorderStroke(0.dp, MaterialTheme.colorScheme.primaryContainer)
    }
}

// цвет фона времени 10% в карточке
@Composable
private fun setBackgroundColorTime(usageTime: Long, isDone: Boolean): Color {
    val nowDate = getCurrentDateTime().toEpochSecond()

    return when {
        isDone -> MaterialTheme.colorScheme.primaryContainer
        nowDate < usageTime -> MaterialTheme.colorScheme.primaryContainer
        nowDate > usageTime + TIME_IS_UP -> MaterialTheme.colorScheme.errorContainer
        nowDate > usageTime -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }.copy(alpha = 0.1f)
}

// цвет времени в карточке
@Composable
private fun setTextColorTime(usageTime: Long, isDone: Boolean): Color {
    val nowDate = getCurrentDateTime().toEpochSecond()

    return when {
        isDone -> MaterialTheme.colorScheme.onPrimary
        nowDate < usageTime -> MaterialTheme.colorScheme.onPrimary
        nowDate > usageTime + TIME_IS_UP -> MaterialTheme.colorScheme.onError
        nowDate > usageTime -> MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.onPrimary
    }
}
