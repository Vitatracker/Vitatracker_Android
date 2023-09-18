package app.mybad.notifier.ui.screens.calender

import android.util.Log
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.lazy.items
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

@Composable
fun DailyUsages(
    date: LocalDateTime,
    usagesDisplay: List<UsageDisplayDomainModel>,
    onDismiss: () -> Unit = {},
    onNewDate: (LocalDateTime?) -> Unit = {},
    onUsed: (UsageDisplayDomainModel) -> Unit = {},
) {
    Log.d("VTTAG", "CalendarSelector::DailyUsages: start")
    val types = stringArrayResource(R.array.types)
    val relations = stringArrayResource(R.array.food_relations)
    val icons = LocalContext.current.resources.obtainTypedArray(R.array.icons)

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
                    text = date.toDayDisplay(),
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
                items(usagesDisplay) { usage ->
                    Column {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            modifier = Modifier
                                .width(40.dp)
                                .padding(vertical = 8.dp)
                        )
                        SingleUsageItem(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            usage = usage,
                            relation = relations[usage.beforeFood],
                            type = types[usage.type],
                            icon = icons.getResourceId(usage.icon, 0),
                            onClick = { onUsed(usage) }
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SingleUsageItem(
    modifier: Modifier = Modifier,
    usage: UsageDisplayDomainModel,
    relation: String,
    type: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Log.d("VTTAG", "CalendarSelector::BottomSheetUsages:SingleUsageItem: start")
    val isTaken = usage.factUseTime > 0L
    val now = currentDateTimeInSecond()
    val outlineColor = if (isTaken || now <= usage.useTime) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.error
    val alpha = if ((now - usage.useTime).absoluteValue > TIME_IS_UP) 0.6f else 1f

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
    ) {
        Text(
            text = usage.useTime.toTimeDisplay(),
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
                    color = PickColor.getColor(usage.color),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(icon),
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
                    Text(text = usage.name, style = Typography.bodyLarge)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text(text = relation, style = Typography.labelMedium)
                        VerticalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            modifier = Modifier
                                .height(16.dp)
                                .padding(horizontal = 8.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = "${usage.quantity.toText()} $type",
                            style = Typography.labelMedium
                        )
                    }
                }
                when (now - usage.useTime) {
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
                                    onClick()
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
                                    onClick()
                                }
                        )
                    }
                }
            }
        }
    }
}
