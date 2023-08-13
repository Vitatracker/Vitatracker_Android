package app.mybad.notifier.ui.common

import android.view.Gravity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kotlinx.coroutines.delay

@Composable
fun BottomSlideInDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    contentAlignment: Alignment = Alignment.BottomCenter,
    content: @Composable () -> Unit
) {
    val animateTrigger = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        delay(100)
        animateTrigger.value = true
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)
        Box(
            contentAlignment = contentAlignment,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(0.dp, 1000.dp)
        ) {
            val density = LocalDensity.current
            AnimatedVisibility(
                visible = animateTrigger.value,
                enter = slideInVertically {
                    with(density) { 500.dp.roundToPx() }
                },
                exit = slideOutVertically(),
                content = { content() }
            )
        }
    }
}
