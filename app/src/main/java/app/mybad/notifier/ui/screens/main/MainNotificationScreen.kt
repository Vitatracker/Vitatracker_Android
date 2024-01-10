package app.mybad.notifier.ui.screens.main

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.common.DayOfWeekSelectorSlider
import app.mybad.notifier.ui.common.MedicineIcon
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.common.VerticalSpacerSmall
import app.mybad.notifier.ui.common.showToast
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.darkTheme
import app.mybad.theme.R
import app.mybad.utils.TIME_IS_UP
import app.mybad.utils.changeDate
import app.mybad.utils.changeDateCorrectDay
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.displayDate
import app.mybad.utils.displayDateTime
import app.mybad.utils.displayTime
import app.mybad.utils.monthShortDisplay
import app.mybad.utils.plusSeconds
import app.mybad.utils.toText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNotificationScreen(
    state: MainContract.State,
    update: StateFlow<LocalDateTime>,
    effectFlow: Flow<ViewSideEffect>? = null,
    sendEvent: (event: MainContract.Event) -> Unit = {},
    navigation: (navigationEffect: MainContract.Effect.Navigation) -> Unit = {},
) {
    val context = LocalContext.current
    val updateDate by update.collectAsStateWithLifecycle()
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
                    date = state.selectDate,
                    changeData = { sendEvent(MainContract.Event.ChangeDate(it)) },
                )
                if (state.patternsAndUsages.isNotEmpty()) {
                    NotificationLazyMedicines(
                        date = state.selectDate,
                        updateTime = updateDate,
                        usagesDisplay = state.patternsAndUsages,
                        types = types,
                        relations = relations,
                        icons = icons,
                        setUsageFactTime = { sendEvent(MainContract.Event.SetUsageFactTime(it)) },
                    )
                } else {
                    NotificationRemedyClear(
                        if (state.isEmpty) R.string.main_screen_meds_clear_start
                        else R.string.main_screen_meds_dont_have_today,
                        state.selectDate
                    )
                }
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
    Log.w(
        "VTTAG",
        "NotificationMonthPager::init: date=${date.displayDateTime()}"
    )

    var datePage by remember(date.year, date.monthNumber) { mutableStateOf(date) }
    val monthInit by remember(datePage) {
        mutableIntStateOf(months + date.month.ordinal)
    }

    val pagerState = rememberPagerState(monthInit) { monthsSize }
    val scope = rememberCoroutineScope()

    LaunchedEffect(monthInit) {
        scope.launch {
            pagerState.scrollToPage(monthInit)
        }
    }

    LaunchedEffect(pagerState.currentPage, datePage) {
        if (pagerState.currentPage < months || pagerState.currentPage >= monthsPair) {
            scope.launch {
                val newYear = pagerState.currentPage.toYear(datePage.year)
                val nevIndex = pagerState.currentPage +
                    if (newYear > datePage.year) -months else months
                pagerState.scrollToPage(nevIndex)
                datePage = datePage.changeDateCorrectDay(year = newYear)
                Log.w(
                    "VTTAG",
                    "NotificationMonthPager::change: index=${pagerState.currentPage} nevIndex=$nevIndex datePage=${datePage.displayDateTime()}"
                )
            }
        }
    }

    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth(),
        state = pagerState,
        pageSpacing = 8.dp,
        contentPadding = PaddingValues(
            horizontal = ((LocalConfiguration.current.screenWidthDp - 50) / 2).dp
        ),
        pageSize = PageSize.Fixed(50.dp),
    ) { index ->
        val dateView = datePage.changeDate(
            year = index.toYear(datePage.year),
            month = index.toMonth(),
            dayOfMonth = 1,
        )
        Log.d("VTTAG", "NotificationMonthPager::month: dateView=${dateView.displayDateTime()}")

        Text(
            text = dateView.monthShortDisplay(),
            color = getMonthColor(pagerState.currentPage, index),
            fontWeight = getFontWeight(pagerState.currentPage, index),
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)
                .clickable {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
        )
    }
    VerticalSpacerSmall()
    DayOfWeekSelectorSlider(
        date = datePage.changeDateCorrectDay(
            month = pagerState.currentPage.toMonth(),
            year = pagerState.currentPage.toYear(datePage.year),
        ),
        dateReinitialize = true,
        onChangeData = changeData::invoke,
    ) {
        scope.launch {
            pagerState.scrollToPage(pagerState.currentPage + it)
        }
    }
}

private fun Int.toMonth() = this % months + 1

private fun Int.toYear(currentYear: Int) = when {
    this < months -> currentYear - 1
    this >= monthsPair -> currentYear + 1
    else -> currentYear
}

@Composable
private fun NotificationTextCategory(date: LocalDateTime) {
    Text(
        text = stringResource(id = R.string.main_screen_text_category, date.displayDate()),
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
    date: LocalDateTime,
    updateTime: LocalDateTime,
    usagesDisplay: Map<String, UsageDisplayDomainModel>,
    types: Array<String>,
    relations: Array<String>,
    icons: TypedArray,
    setUsageFactTime: (String) -> Unit,
) {
    val usages = remember(usagesDisplay) {
        usagesDisplay.entries.toList()
    }
    NotificationTextCategory(date)
    Log.w(
        "VTTAG",
        "MainNotificationScreen::NotificationLazyMedicines: usages=${usagesDisplay.size}"
    )
    LazyColumn(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 0.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        items(usages, key = { it.key }) { usageEntry ->
            //TODO("переделать так же как и BottomSheetUsages")
            val usage = usageEntry.value
            NotificationCourseItem(
                updateTime = updateTime,
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
    updateTime: LocalDateTime,
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
            nowDate = updateTime,
            usageTime = usage.useTime,
            isDone = isDone
        ),
        color = setBackgroundColorCard(
            nowDate = updateTime,
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
                updateTime = updateTime,
                usageTime = usage.useTime,
                isDone = isDone
            )
            Spacer(modifier = Modifier.width(16.dp))
            // иконка препарата
            MedicineIcon(
                icon = icon,
                color = usage.color,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                // название препарата
                Text(
                    text = usage.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
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
                updateTime = updateTime,
                usageTime = usage.useTime,
                isDone = isDone,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun NotificationTimeCourse(
    updateTime: LocalDateTime,
    usageTime: LocalDateTime,
    isDone: Boolean,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = setBorderColorTimeCard(
            nowDate = updateTime,
            usageTime = usageTime,
            isDone = isDone
        ),
        color = setBackgroundColorTime(
            nowDate = updateTime,
            usageTime = usageTime,
            isDone = isDone
        ),
        contentColor = setTextColorTime(
            nowDate = updateTime,
            usageTime = usageTime,
            isDone = isDone
        ),
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
private fun NotificationRemedyClear(
    @StringRes text: Int,
    date: LocalDateTime,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(text, date.displayDate()),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.main_screen_clear_med),
            contentDescription = null,
            alignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
private fun NotificationButtonAccept(
    updateTime: LocalDateTime,
    usageTime: LocalDateTime,
    isDone: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Icon(
        painter = painterResource(if (isDone) R.drawable.done else R.drawable.undone),
        contentDescription = null,
        tint = setTextColorTime(nowDate = updateTime, usageTime = usageTime, isDone = isDone),
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
private fun setBorderColorCard(
    nowDate: LocalDateTime,
    usageTime: LocalDateTime,
    isDone: Boolean
): BorderStroke? = if (darkTheme) null else when {
    isDone -> null
    nowDate < usageTime -> null
    nowDate > usageTime.plusSeconds(TIME_IS_UP) -> {
        BorderStroke(1.dp, MaterialTheme.colorScheme.error)
    }

    nowDate > usageTime -> BorderStroke(1.dp, color = MaterialTheme.colorScheme.error)
    else -> null
}

// цвет фона карточки 5%
@Composable
private fun setBackgroundColorCard(
    nowDate: LocalDateTime,
    usageTime: LocalDateTime,
    isDone: Boolean
): Color = when {
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

// цвет рамки вокруг времени
@Composable
private fun setBorderColorTimeCard(
    nowDate: LocalDateTime,
    usageTime: LocalDateTime,
    isDone: Boolean
): BorderStroke? = if (!darkTheme) null else when {
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

// цвет фона времени только в светлой теме - 10% в карточке
@Composable
private fun setBackgroundColorTime(
    nowDate: LocalDateTime,
    usageTime: LocalDateTime,
    isDone: Boolean
): Color = if (darkTheme) Color.Transparent else when {
    isDone -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
    nowDate < usageTime -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    nowDate > usageTime.plusSeconds(TIME_IS_UP) -> {
        MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
    }

    nowDate > usageTime -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
    else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
}

// цвет времени в карточке
@Composable
private fun setTextColorTime(
    nowDate: LocalDateTime,
    usageTime: LocalDateTime,
    isDone: Boolean
): Color = when {
    isDone -> MaterialTheme.colorScheme.primary.copy(0.4f)
    nowDate < usageTime -> MaterialTheme.colorScheme.primary
    nowDate > usageTime.plusSeconds(TIME_IS_UP) -> MaterialTheme.colorScheme.onError
    nowDate > usageTime -> MaterialTheme.colorScheme.onError
    else -> MaterialTheme.colorScheme.primary
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
            ),
            update = MutableStateFlow(currentDateTimeSystem()),
//            update = MutableStateFlow(emptyMap<String, UsageDisplayDomainModel>() to currentDateTimeSystem()),
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

const val months = 12
const val monthsPair = 24
const val monthsSize = 36 // 12 * 3
