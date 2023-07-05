package app.mybad.notifier.ui.screens.calender

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.ui.PickColor
import app.mybad.notifier.ui.screens.common.DaySelectorSlider
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.toDayDisplay
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toTimeDisplay
import app.mybad.theme.R
import kotlinx.datetime.LocalDateTime
import kotlin.math.absoluteValue

@SuppressLint("Recycle")
@Composable
private fun SingleUsageItem(
    modifier: Modifier = Modifier,
    date: Long,
    med: MedDomainModel,
    quantity: Int,
    isTaken: Boolean = false,
    onTake: (Long) -> Unit
) {
    val types = stringArrayResource(R.array.types)
    val relations = stringArrayResource(R.array.food_relations)
    val now = getCurrentDateTime().toEpochSecond()
    val outlineColor =
        if (now > date && !isTaken) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val alpha = if ((now - date).absoluteValue > 3600) 0.6f else 1f
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
                    color = PickColor.getColor(med.color),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(r.getResourceId(med.icon, 0)),
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
                    Text(text = "${med.name}", style = Typography.bodyLarge)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text(text = relations[med.beforeFood], style = Typography.labelMedium)
                        Divider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            modifier = Modifier
                                .height(16.dp)
                                .padding(horizontal = 8.dp)
                                .width(1.dp)
                        )
                        Text(text = "$quantity ${types[med.type]}", style = Typography.labelMedium)
                    }
                }
                when (now - date) {
                    in Long.MIN_VALUE..-3600 -> {
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

                    in -3600..3600 -> {
                        Icon(
                            painter = painterResource(if (isTaken) R.drawable.done else R.drawable.undone),
                            contentDescription = null,
                            tint = outlineColor,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    val n = if (!isTaken) getCurrentDateTime().toEpochSecond()
                                    else -1L
                                    onTake(n)
                                }
                        )
                    }

                    in 3600..Long.MAX_VALUE -> {
                        Icon(
                            painter = painterResource(if (isTaken) R.drawable.done else R.drawable.undone),
                            contentDescription = null,
                            tint = outlineColor,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    val n = if (!isTaken) getCurrentDateTime().toEpochSecond()
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
    modifier: Modifier = Modifier,
    date: LocalDateTime?,
    meds: List<MedDomainModel>,
    dayData: List<UsageCommonDomainModel>,
    onDismiss: () -> Unit = {},
    onNewDate: (LocalDateTime?) -> Unit = {},
    onUsed: (UsageCommonDomainModel) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
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
            LazyColumn {
                dayData.sortedBy {
                    it.useTime
                }.forEach { entry ->
                    item {
                        Column {
                            Divider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(vertical = 8.dp)
                            )
                            SingleUsageItem(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                date = entry.useTime,
                                quantity = entry.quantity,
                                med = meds.first { it.id == entry.medId },
                                isTaken = entry.factUseTime > 10L,
                                onTake = { datetime ->
                                    onUsed(entry.copy(factUseTime = datetime))
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
