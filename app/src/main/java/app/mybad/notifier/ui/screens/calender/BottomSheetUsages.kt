package app.mybad.notifier.ui.screens.calender

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.common.DaySelectorSlider
import app.mybad.notifier.ui.theme.PickColor
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.toText
import app.mybad.theme.R
import app.mybad.utils.TIME_IS_UP
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.toDayDisplay
import app.mybad.utils.toTimeDisplay
import kotlinx.datetime.LocalDateTime
import kotlin.math.absoluteValue

@SuppressLint("Recycle")
@Composable
private fun SingleUsageItem(
    date: Long,
    remedy: RemedyDomainModel,
    quantity: Float,
    modifier: Modifier = Modifier,
    isTaken: Boolean = false,
    onTake: (Long) -> Unit
) {
    Log.d("VTTAG", "CalendarSelector::BottomSheetUsages:SingleUsageItem: start")
    val types = stringArrayResource(R.array.types)
    val relations = stringArrayResource(R.array.food_relations)
    val now = currentDateTimeInSecond()
    val outlineColor = if (now > date && !isTaken) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.primary
    val alpha = if ((now - date).absoluteValue > TIME_IS_UP) 0.6f else 1f
    val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
    ) {
        Text(
            text = date.toTimeDisplay(),
            modifier = Modifier.padding(end = 8.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Surface(
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, outlineColor),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = PickColor.getColor(remedy.color),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(r.getResourceId(remedy.icon, 0)),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = "${remedy.name}", style = Typography.bodyLarge)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text(text = relations[remedy.beforeFood], style = Typography.labelMedium)
                        VerticalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            modifier = Modifier
                                .height(16.dp)
                                .padding(horizontal = 8.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = "${quantity.toText()} ${types[remedy.type]}",
                            style = Typography.labelMedium
                        )
                    }
                }
                when (now - date) {
                    in Long.MIN_VALUE..-TIME_IS_UP -> {
                        Icon(
                            painter = painterResource(R.drawable.locked),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }

                    in -TIME_IS_UP..TIME_IS_UP -> {
                        Icon(
                            painter = painterResource(if (isTaken) R.drawable.done else R.drawable.undone),
                            contentDescription = null,
                            tint = outlineColor,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    val n = if (!isTaken) currentDateTimeInSecond()
                                    else -1L
                                    onTake(n)
                                }
                        )
                    }

                    in TIME_IS_UP..Long.MAX_VALUE -> {
                        Icon(
                            painter = painterResource(if (isTaken) R.drawable.done else R.drawable.undone),
                            contentDescription = null,
                            tint = outlineColor,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    val n = if (!isTaken) currentDateTimeInSecond()
                                    else -1L
                                    onTake(n)
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyUsages(
    date: LocalDateTime?,
    usages: List<UsageDisplayDomainModel>,
    onDismiss: () -> Unit = {},
    onNewDate: (LocalDateTime?) -> Unit = {},
    onUsed: (UsageDisplayDomainModel) -> Unit
) {
    Log.d("VTTAG", "CalendarSelector::DailyUsages: start")
    Surface(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                Text(
                    text = date?.toDayDisplay() ?: "no date",
                    style = Typography.titleLarge,
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onDismiss::invoke
                        )
                )
            }
            DaySelectorSlider(
                modifier = Modifier.padding(vertical = 16.dp),
                date = date,
                onSelect = onNewDate::invoke
            )
            Log.d("VTTAG", "CalendarSelector::BottomSheetUsages:DailyUsages: LazyColumn")
            LazyColumn {
                usages.sortedBy {
                    it.useTime
                }.forEach { usage ->
                    item {
                        Column {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(vertical = 8.dp)
                            )
                            SingleUsageItem(
                                date = usage.useTime,
                                remedy = RemedyDomainModel(), //TODO("тут нужна реализация")
//                                remedy = remedies.firstOrNull { remedy -> remedy.id == usage.remedyId }
//                                    ?: RemedyDomainModel(),
                                quantity = usage.quantity,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                isTaken = usage.factUseTime > 10L,
                                onTake = { datetime ->
                                    onUsed(usage.copy(factUseTime = datetime))
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
