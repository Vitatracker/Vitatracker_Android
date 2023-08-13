package app.mybad.notifier.ui.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.minusMonths
import app.mybad.utils.monthFullDisplay
import app.mybad.utils.plusMonths
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MonthSelector(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    onSwitch: (LocalDateTime) -> Unit
) {
    var newDate by remember { mutableStateOf(date) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        MonthSelectorIcon(R.drawable.prev_month) {
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
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = newDate.year.toString(),
                style = Typography.labelSmall,
                modifier = modifier.alpha(0.5f)
            )
        }
        MonthSelectorIcon(R.drawable.next_month) {
            newDate = newDate.plusMonths(1)
            onSwitch(newDate)
        }
    }
}

@Composable
private fun MonthSelectorIcon(iconResId: Int, onClick: () -> Unit) {
//    TODO("проверить тут нужно ли красить в тему и нужна ли тут Icon")
    Image(
        painter = painterResource(iconResId),
        contentDescription = null,
        modifier = Modifier
            .size(36.dp)
            .clickable {
                onClick()
            }
    )
}
