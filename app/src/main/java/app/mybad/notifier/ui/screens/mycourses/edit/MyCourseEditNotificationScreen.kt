package app.mybad.notifier.ui.screens.mycourses.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.mycourses.MyCoursesContract
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun MyCourseEditNotificationScreen(
    state: MyCoursesContract.State,
    effectFlow: Flow<MyCoursesContract.Effect>? = null,
    sendEvent: (event: MyCoursesContract.Event) -> Unit = {},
    navigation: (navigationEffect: MyCoursesContract.Effect.Navigation) -> Unit = {},
) {

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is MyCoursesContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.edit_med_title,
                onBackPressed = { sendEvent(MyCoursesContract.Event.ActionBack) }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

        }
    }
}
