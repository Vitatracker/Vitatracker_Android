package app.mybad.notifier.ui.screens.mycourses

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.common.getFormsPluralsArray
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMyCoursesScreen(
    state: MyCoursesContract.State,
    effectFlow: Flow<MyCoursesContract.Effect>? = null,
    sendEvent: (event: MyCoursesContract.Event) -> Unit = {},
    navigation: (navigationEffect: MyCoursesContract.Effect.Navigation) -> Unit,
) {

    val icons = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    val pluralsArray = getFormsPluralsArray()

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
        }) { paddingValues ->
        MyCoursesScreen(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            state = state,
            onSelect = {
                sendEvent(MyCoursesContract.Event.CourseEditing(it))
            },
        )
    }
}
