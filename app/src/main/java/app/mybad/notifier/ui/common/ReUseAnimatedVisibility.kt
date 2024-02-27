package app.mybad.notifier.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

@Composable
fun ReUseAnimatedVisibility(
    visible: Boolean,
    enter: EnterTransition = fadeIn(animationSpec = tween(300)),
    exit: ExitTransition = fadeOut(animationSpec = tween(100)),
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(visible = visible, enter = enter, exit = exit, content = content)
}
