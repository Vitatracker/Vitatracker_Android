package app.mybad.notifier.ui.screens.mycourses

import android.content.res.TypedArray
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.ui.PickColor
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.cardBackground
import app.mybad.notifier.utils.getFormsPluralsArray
import app.mybad.notifier.utils.toDateDisplay
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesScreen(
    state: MyCoursesScreenContract.State,
    events: Flow<MyCoursesScreenContract.Effect>? = null,
    onEventSent: (event: MyCoursesScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: MyCoursesScreenContract.Effect.Navigation) -> Unit
) {
    val icons = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    val pluralsArray = getFormsPluralsArray()
    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                is MyCoursesScreenContract.Effect.Navigation.ToEditCourse -> {
                    onNavigationRequested(it)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TitleText(textStringRes = R.string.my_course_h)
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(state.courseItems) { courseItem ->
                Log.d("MyCoursesScreen", "courseItem: $courseItem")
                CourseItem(
                    courseItem = courseItem,
                    icons = icons,
                    typePlurals = pluralsArray,
                    onEventSent = onEventSent
                )
            }
        }
    }
}

@Composable
private fun CourseItem(
    courseItem: CoursePresenterItem,
    icons: TypedArray,
    typePlurals: Array<Int>,
    onEventSent: (event: MyCoursesScreenContract.Event) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = PickColor.getColor(courseItem.med.color),
                    modifier = Modifier
                        .size(36.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(icons.getResourceId(courseItem.med.icon, 0)),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = courseItem.med.name?.replaceFirstChar { it.uppercase() } ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (courseItem.startInDays > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(Modifier.size(16.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                                ) {
                                    val startsIn = String.format(
                                        stringResource(R.string.mycourse_remaining),
                                        courseItem.startInDays.toString()
                                    )
                                    Text(
                                        text = startsIn,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight(500),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                        Image(
                            painter = painterResource(R.drawable.edit),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable {
                                    onEventSent(MyCoursesScreenContract.Event.EditCourseClicked(courseItem.med.id))
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    if (courseItem.itemsCount != 0 || courseItem.usagesCount > 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (courseItem.itemsCount != 0) {
                                val medPlurals = LocalContext.current.resources.getQuantityString(
                                    typePlurals[courseItem.med.type],
                                    courseItem.itemsCount,
                                    courseItem.itemsCount
                                )
                                Text(
                                    text = medPlurals,
                                    fontWeight = FontWeight(500),
                                    fontSize = 12.sp
                                )
                                Divider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .height(16.dp)
                                        .width(1.dp)
                                )
                            }
                            if (courseItem.usagesCount > 0) {
                                Text(
                                    text = stringResource(
                                        R.string.mycourse_per_day_listitem,
                                        courseItem.usagesCount
                                    ),
                                    fontWeight = FontWeight(500), fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val start = courseItem.course.startDate.toDateDisplay()
                    val end = courseItem.course.endDate.toDateDisplay()
                    Text(
                        text = start,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(700),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .height(10.dp)
                    )
                    Text(
                        text = end,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(700),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CourseItemPreview() {
    MyBADTheme(darkTheme = false) {
        val pluralsArray = getFormsPluralsArray()
        val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)
        val course = CoursePresenterItem(MedDomainModel(), CourseDomainModel(), listOf(UsageCommonDomainModel()))
        CourseItem(courseItem = course, r, pluralsArray)
    }
}

@Preview(showBackground = true)
@Composable
private fun CourseItemDarkPreview() {
    MyBADTheme(darkTheme = true) {
        val pluralsArray = getFormsPluralsArray()
        val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)
        val course = CoursePresenterItem(MedDomainModel(), CourseDomainModel(), listOf(UsageCommonDomainModel()))
        CourseItem(courseItem = course, r, pluralsArray)
    }
}