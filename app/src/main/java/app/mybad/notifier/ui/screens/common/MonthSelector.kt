package app.mybad.notifier.ui.screens.common

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import java.time.LocalDateTime

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MonthSelector(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    onSwitch: (LocalDateTime) -> Unit
) {
    val months = stringArrayResource(R.array.months_full)
    var newDate by remember { mutableStateOf(date) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.prev_month),
            tint = MaterialTheme.colorScheme.outlineVariant,
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) {
                    newDate = newDate.minusMonths(1)
                    onSwitch(newDate)
                }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            AnimatedContent(
                targetState = newDate.monthValue,
                transitionSpec = {
                    EnterTransition.None with ExitTransition.None
                }
            ) { targetCount ->
                Text(
                    modifier = Modifier.animateEnterExit(
                        enter = scaleIn(),
                        exit = scaleOut()
                    ),
                    text = months[targetCount-1],
                    style = Typography.bodyLarge
                )
            }
            Text(
                text = newDate.year.toString(),
                style = Typography.labelSmall,
                modifier = modifier.alpha(0.5f)
            )
        }
        Icon(
            painter = painterResource(R.drawable.next_month),
            tint = MaterialTheme.colorScheme.outlineVariant,
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) {
                    newDate = newDate.plusMonths(1)
                    onSwitch(newDate)
                }
        )
    }

}