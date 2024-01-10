package app.mybad.notifier.ui.common

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.minusMonths
import app.mybad.utils.monthFullDisplay
import app.mybad.utils.plusMonths
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MonthSelector(
    modifier: Modifier = Modifier,
    date: LocalDateTime = currentDateTimeSystem(),
    onSwitch: (LocalDateTime) -> Unit = {},
) {
    var newDate by remember(date.year, date.monthNumber) { mutableStateOf(date) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        MonthSelectorIcon(R.drawable.prev) {
            newDate = newDate.minusMonths(1)
            onSwitch(newDate)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            AnimatedContent(
                targetState = newDate.month.value,
                transitionSpec = {
                    EnterTransition.None togetherWith ExitTransition.None
                },
                label = "",
            ) { targetCount ->
                Text(
                    modifier = Modifier.animateEnterExit(
                        enter = scaleIn(),
                        exit = scaleOut()
                    ),
                    text = (targetCount - 1).monthFullDisplay(),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = newDate.year.toString(),
                style = MaterialTheme.typography.labelMedium,
                modifier = modifier.alpha(0.5f)
            )
        }
        MonthSelectorIcon(R.drawable.next) {
            newDate = newDate.plusMonths(1)
            onSwitch(newDate)
        }
    }
}

@Composable
private fun MonthSelectorIcon(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit
) {
    ReUseIcon(
        modifier = Modifier
            .size(36.dp),
        painterId = iconResId,
        tint = MaterialTheme.colorScheme.onPrimary,
        iconSize = 20.dp,
        shape = MaterialTheme.shapes.small,
        border = true,
        onClick = onClick,
    )
}

@Preview(
    showBackground = true,
    widthDp = 1080, heightDp = 1920,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreview"
)
@Preview(
    showBackground = true,
    widthDp = 320, heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
private fun MonthSelectorPreview() {
    MyBADTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MonthSelector()
        }
    }
}
