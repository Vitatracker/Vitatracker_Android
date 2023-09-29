package app.mybad.notifier.ui.screens.mycourses

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.models.patternToCount
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseIcon
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.common.getFormsPluralsArray
import app.mybad.notifier.ui.theme.PickColor
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.ui.theme.iconEditing
import app.mybad.notifier.utils.toText
import app.mybad.theme.R
import app.mybad.utils.displayDate
import kotlinx.coroutines.flow.Flow

@SuppressLint("Recycle")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesScreen(
    state: MyCoursesContract.State,
    effectFlow: Flow<MyCoursesContract.Effect>? = null,
    sendEvent: (event: MyCoursesContract.Event) -> Unit = {},
    navigation: (navigationEffect: MyCoursesContract.Effect.Navigation) -> Unit,
) {

    val icons = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    val typePlurals = getFormsPluralsArray()

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is MyCoursesContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TitleText(textStringRes = R.string.my_course_title)
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .padding(paddingValues),
            contentPadding = PaddingValues(
                top = 8.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 72.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Отображение текущих курсов таблеток
            items(state.courses, key = { course -> course.id }) { course ->
                CourseItem(
                    courseDisplay = course,
                    icon = icons.getResourceId(course.icon, 0),
                    type = typePlurals[course.type],
                    onClick = {
                        sendEvent(
                            MyCoursesContract.Event.CourseEditing(
                                // тут если есть id старого курса, то для редактирования курса берется именно он
                                if (course.idOld > 0) course.idOld else course.id
                            )
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun CourseItem(
    courseDisplay: CourseDisplayDomainModel,
    @DrawableRes icon: Int,
    @PluralsRes type: Int,
    onClick: () -> Unit = {},
) {

    // кол-во приемов, минимальное и максимальное кол-во за 1 прием
    val (countPerDay, countPerDoseMin, countPerDoseMax) = courseDisplay.patternUsages.patternToCount()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ReUseIcon(
                    painterId = icon,
                    color = PickColor.getColor(courseDisplay.color),
                    tint = MaterialTheme.colorScheme.outline,
                    iconSize = 24.dp,
                    modifier = Modifier
                        .size(36.dp),
                )
                Spacer(Modifier.width(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.fillMaxWidth(0.85f)) {
                        Text(
                            text = courseDisplay.name.replaceFirstChar { it.uppercase() },
                            style = Typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        if (countPerDoseMax > 0 || countPerDay > 0) {
                            Row {
                                if (countPerDoseMax > 0) {
                                    Text(
                                        // тут ерунда получается, у нас доза "0.5 таблетки 2 раза, утро и вечер", "по 1-й таблетки 3 раза в день"
                                        text = pluralStringResource(
                                            id = type,
                                            count = countPerDoseMax.toInt(),
                                            Pair(countPerDoseMin, countPerDoseMax).toText()
                                        ),
                                        style = Typography.labelMedium
                                    )
                                    VerticalDivider(
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .height(16.dp)
                                            .padding(horizontal = 8.dp)
                                            .width(1.dp)
                                    )
                                }
                                if (countPerDay > 0) {
                                    Text(
                                        text = stringResource(
                                            R.string.mycourse_per_day_listitem,
                                            countPerDay,
                                        ),
                                        style = Typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                    // иконка редактирования курса
                    if (courseDisplay.remindDate != null || courseDisplay.interval == 0L || courseDisplay.idOld > 0) {
                        ReUseIcon(
                            painterId = R.drawable.icon_pencil,
                            color = MaterialTheme.colorScheme.primary,
                            tint = iconEditing,
                            iconSize = 16.dp,
                            modifier = Modifier
                                .size(24.dp),
                            onClick = onClick,
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // дата старта и окончание курса
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val start = courseDisplay.startDate.displayDate()
                        val end = courseDisplay.endDate.displayDate()
                        Text(
                            text = start, fontSize = 12.sp, fontWeight = FontWeight(500),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Icon(
                            painter = painterResource(R.drawable.arrow_right),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp)
                                .height(10.dp)
                        )
                        Text(
                            text = end, fontSize = 12.sp, fontWeight = FontWeight(500),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    // отобразить старт нового курса через ...
                    if (courseDisplay.remindDate == null && courseDisplay.interval > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.size(16.dp))
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.mycourse_remaining,
                                        courseDisplay.interval
                                    ),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = Typography.bodySmall,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CourseItemPreview() {
    CourseItem(
        courseDisplay = CourseDisplayDomainModel(),
        icon = 0,
        type = 0,
    )
}
