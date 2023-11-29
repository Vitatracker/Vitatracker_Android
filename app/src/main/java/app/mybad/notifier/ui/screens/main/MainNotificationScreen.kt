package app.mybad.notifier.ui.screens.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.common.showToast
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.darkTheme
import app.mybad.theme.R
import app.mybad.utils.TIME_IS_UP
import app.mybad.utils.changeDate
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.dayShortDisplay
import app.mybad.utils.displayTime
import app.mybad.utils.getDaysOfMonth
import app.mybad.utils.monthShortDisplay
import app.mybad.utils.plusSeconds
import app.mybad.utils.toText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNotificationScreen(
    state: MainContract.State,
    effectFlow: Flow<ViewSideEffect>? = null,
    sendEvent: (event: MainContract.Event) -> Unit = {},
    navigation: (navigationEffect: MainContract.Effect.Navigation) -> Unit = {},
) {
    val context = LocalContext.current
    val types: Array<String> = stringArrayResource(R.array.types)
    val relations: Array<String> = stringArrayResource(R.array.food_relations)
    val icons: TypedArray = LocalContext.current.resources.obtainTypedArray(R.array.icons)

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is MainContract.Effect.Navigation -> navigation(effect)
                is MainContract.Effect.Toast -> context.showToast(effect.text)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = { TitleText(textStringRes = R.string.main_screen_top_bar_name) })
        }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NotificationMonthPager(
                    date = state.selectedDate,
                    changeData = { sendEvent(MainContract.Event.ChangeDate(it)) },
                )
                if (state.patternsAndUsages.isNotEmpty()) {
                    NotificationLazyMedicines(
                        usagesDisplay = state.patternsAndUsages,
                        types = types,
                        relations = relations,
                        icons = icons,
                        setUsageFactTime = { sendEvent(MainContract.Event.SetUsageFactTime(it)) },
                    )
                } else NotificationRemedyClear()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NotificationMonthPager(
    date: LocalDateTime,
    changeData: (LocalDateTime) -> Unit = {}
) {
    val stateMonth = rememberPagerState(date.month.ordinal) { 12 }
    val scope = rememberCoroutineScope()

    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth(),
        state = stateMonth,
        pageSpacing = 8.dp,
        contentPadding = PaddingValues(
            horizontal = ((LocalConfiguration.current.screenWidthDp - 60) / 2).dp
        ),
        pageSize = PageSize.Fixed(54.dp)
    ) { month ->
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
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
    NotificationWeekPager(
        monthState = stateMonth.currentPage,
        date = date,
        changeData = changeData,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NotificationWeekPager(
    monthState: Int,
    date: LocalDateTime,
    changeData: (LocalDateTime) -> Unit,
) {
    val paddingStart = 10.dp
    val paddingEnd = 10.dp

    var shortNameOfDay by remember { mutableStateOf("") }
    var countDay by remember { mutableIntStateOf(0) }
    val displayedDate by remember { mutableStateOf(date) }
    val stateDay = rememberPagerState(date.dayOfMonth - 1) {
        // количество дней в месяце с учетом высокосного года
        displayedDate.changeDate(month = monthState + 1).getDaysOfMonth()
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(stateDay.currentPage, monthState) {
        delay(50)
        val newDate = date.changeDate(
            month = monthState + 1,
            dayOfMonth = stateDay.currentPage + 1
        )
        changeData(newDate)
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
            color = if (stateDay.currentPage == page) MaterialTheme.colorScheme.primary else Color.Transparent,
            contentColor = if (stateDay.currentPage == page) MaterialTheme.colorScheme.surfaceBright
            else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        ) {
            Column(
                modifier = Modifier
                    .height(height = 50.dp)
                    .width(width = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                countDay = page + 1
                shortNameOfDay = date
                    .changeDate(month = monthState + 1, dayOfMonth = page + 1)
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
private fun NotificationTextCategory() {
    Text(
        text = stringResource(id = R.string.main_screen_text_category),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp),
        style = MaterialTheme.typography.displayMedium,
        textAlign = TextAlign.Start,
    )
}

@SuppressLint("Recycle")
@Composable
private fun NotificationLazyMedicines(
    usagesDisplay: Map<String, UsageDisplayDomainModel>,
    types: Array<String>,
    relations: Array<String>,
    icons: TypedArray,
    setUsageFactTime: (String) -> Unit,
) {
    val state = rememberSaveable(
        usagesDisplay.size,
        saver = LazyListState.Saver,
        key = "usagesDisplay"
    ) {
        LazyListState(
            firstVisibleItemIndex = 0,
            firstVisibleItemScrollOffset = 0,
        )
    }
    val usages = remember(usagesDisplay) {
        usagesDisplay.entries.toList()
    }
    NotificationTextCategory()
    Log.w(
        "VTTAG",
        "MainNotificationScreen::NotificationLazyMedicines: usages=${usagesDisplay.size}"
    )
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentPadding = PaddingValues(bottom = 84.dp),
        state = state,
    ) {
        items(usages, key = { it.key }) { usageEntry ->
            //TODO("переделать так же как и BottomSheetUsages")
            val usage = usageEntry.value
            NotificationCourseItem(
                usage = usage,
                isDone = usage.factUseTime != null,
                relation = relations[usage.beforeFood],
                type = types[usage.type],
                icon = icons.getResourceId(usage.icon, 0),
                onClick = { setUsageFactTime(usageEntry.key) },
            )
        }
    }
}

@SuppressLint("Recycle")
@Composable
private fun NotificationCourseItem(
    usage: UsageDisplayDomainModel,
    isDone: Boolean,
    relation: String,
    type: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        border = setBorderColorCard(
            usageTime = usage.useTime,
            isDone = isDone
        ),
        color = setBackgroundColorCard(
            usageTime = usage.useTime,
            isDone = isDone
        ),
        contentColor = if (isDone) MaterialTheme.colorScheme.onPrimary.copy(0.5f)
        else MaterialTheme.colorScheme.onPrimary,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NotificationTimeCourse(
                usageTime = usage.useTime,
                isDone = isDone
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // иконка препарата
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp),
                        tint = setTextColorTime(
                            usageTime = usage.useTime,
                            isDone = isDone
                        )//PickColor.getColor(usage.color)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    // название препарата
                    Text(
                        text = usage.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // количество и тип
                    Text(
                        text = "${usage.quantity.toText()} $type",
                        style = MaterialTheme.typography.labelMedium
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp)
                            .padding(horizontal = 8.dp),
                        color = Color.Gray
                    )
                    Text(
                        text = relation,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
            NotificationButtonAccept(
                usageTime = usage.useTime,
                isDone = isDone,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun NotificationTimeCourse(
    usageTime: LocalDateTime,
    isDone: Boolean,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = setBorderColorTimeCard(usageTime = usageTime, isDone = isDone),
        color = setBackgroundColorTime(usageTime = usageTime, isDone = isDone),
        contentColor = setTextColorTime(usageTime = usageTime, isDone = isDone),
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = usageTime.displayTime(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Justify,
        )
    }
}

@Composable
private fun NotificationRemedyClear() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        NotificationRemedyClearText()
        Spacer(modifier = Modifier.height(22.dp))
        NotificationRemedyClearImage()
    }
}

@Composable
private fun NotificationRemedyClearText() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.main_screen_meds_clear_start),
            fontSize = 25.sp,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.main_screen_meds_clear_add),
            fontSize = 25.sp,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun NotificationRemedyClearImage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, bottom = 70.dp),
        Alignment.BottomCenter
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.main_screen_clear_med),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
private fun NotificationButtonAccept(
    usageTime: LocalDateTime,
    isDone: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    /*
    val nowDate = currentDateTimeSystem()

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

            nowDate > usageTime.plusSeconds(TIME_IS_UP) -> {
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
    */

    return Icon(
        painter = painterResource(if (isDone) R.drawable.done else R.drawable.undone),
        contentDescription = null,
        tint = setTextColorTime(usageTime = usageTime, isDone = isDone),
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    )
}

private fun getFontWeight(page: Int, month: Int) = when (page) {
    month -> FontWeight.Bold
    (month + 1), (month - 1) -> FontWeight.Normal
    else -> FontWeight.Normal
}

// разобраться с цветами, сделать из темы
@Composable
private fun getMonthColor(page: Int, month: Int) = when (page) {
    month -> MaterialTheme.colorScheme.primary
    (month + 1), (month - 1) -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
    (month + 2), (month - 2) -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
    else -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
}

// цвет рамки вокруг карточки, только в светлой теме и только при ошибке
@Composable
private fun setBorderColorCard(usageTime: LocalDateTime, isDone: Boolean): BorderStroke? {
    return if (darkTheme) null
    else {
        val nowDate = currentDateTimeSystem()
        when {
            isDone -> null
            nowDate < usageTime -> null
            nowDate > usageTime.plusSeconds(TIME_IS_UP) -> {
                BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            }

            nowDate > usageTime -> BorderStroke(1.dp, color = MaterialTheme.colorScheme.error)
            else -> null
        }
    }
}

// цвет фона карточки 5%
@Composable
private fun setBackgroundColorCard(usageTime: LocalDateTime, isDone: Boolean): Color {
    val nowDate = currentDateTimeSystem()

    return when {
        isDone -> MaterialTheme.colorScheme.primary
            .copy(alpha = if (darkTheme) 0.1f else 0.03f)

        nowDate < usageTime -> MaterialTheme.colorScheme.primary
            .copy(alpha = if (darkTheme) 0.3f else 0.05f)

        nowDate > usageTime.plusSeconds(TIME_IS_UP) -> MaterialTheme.colorScheme.error
            .copy(alpha = if (darkTheme) 0.2f else 0.05f)

        nowDate > usageTime -> MaterialTheme.colorScheme.error
            .copy(alpha = if (darkTheme) 0.2f else 0.05f)

        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
    }
}

// цвет рамки вокруг времени
@Composable
private fun setBorderColorTimeCard(usageTime: LocalDateTime, isDone: Boolean): BorderStroke? {
    return if (darkTheme) {
        val nowDate = currentDateTimeSystem()
        return when {
            isDone -> BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(0.3f))
            nowDate < usageTime -> BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(0.5f))
            nowDate > usageTime.plusSeconds(TIME_IS_UP) -> {
                BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(0.5f))
            }

            nowDate > usageTime -> BorderStroke(
                0.dp,
                color = MaterialTheme.colorScheme.error.copy(0.5f)
            )

            else -> BorderStroke(0.dp, Color.Transparent)
        }
    } else null
}

// цвет фона времени только в светлой теме - 10% в карточке
@Composable
private fun setBackgroundColorTime(usageTime: LocalDateTime, isDone: Boolean): Color {
    return if (darkTheme) Color.Transparent
    else {
        val nowDate = currentDateTimeSystem()

        when {
            isDone -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
            nowDate < usageTime -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            nowDate > usageTime.plusSeconds(TIME_IS_UP) -> {
                MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
            }

            nowDate > usageTime -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        }
    }
}

// цвет времени в карточке
@Composable
private fun setTextColorTime(usageTime: LocalDateTime, isDone: Boolean): Color {
    val nowDate = currentDateTimeSystem()

    return when {
        isDone -> MaterialTheme.colorScheme.primary.copy(0.4f)
        nowDate < usageTime -> MaterialTheme.colorScheme.primary
        nowDate > usageTime.plusSeconds(TIME_IS_UP) -> MaterialTheme.colorScheme.onError
        nowDate > usageTime -> MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.primary
    }
}

@Preview
@Composable
fun MainNotificationScreenPreview() {
    MyBADTheme {
        MainNotificationScreen(
            state = MainContract.State(
                patternsAndUsages = sortedMapOf<String, UsageDisplayDomainModel>().apply {
                    patternsAndUsages.map { it.toUsageKey() to it }
                },
            )
        )
    }
}

private val patternsAndUsages = listOf(
    UsageDisplayDomainModel(
        id = 1,
        courseId = 1,
        remedyId = 1,
        timeInMinutes = 200,
        name = "Очень длинное лекарство для проверки макета и верстки",
        dose = 2,
        icon = 1,
        type = 1,
    ),
    UsageDisplayDomainModel(
        id = 2,
        courseId = 2,
        remedyId = 2,
        timeInMinutes = 300,
        name = "Лекарство 2",
        dose = 2,
        icon = 1,
        type = 1,
    ),
)
