package app.mybad.notifier.ui.screens.calender

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.DaySelectorSlider
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
private fun SingleUsageItem(
    modifier: Modifier = Modifier,
    date: Long,
    med: MedDomainModel,
    isTaken: Boolean = false,
    onTake: (Long, Long) -> Unit = { datetime, medId -> }
) {
    val units = stringArrayResource(R.array.units)
    var _isTaken by remember { mutableStateOf(isTaken) }

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth()
    ) {
        val time = LocalDateTime
            .ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))
        Text(
            text = time,
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(10.dp),
            elevation = 3.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val icon = if(med.details.icon == 0) R.drawable.pill else R.drawable.settings
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "${med.name}, ${med.details.dose} ${units[med.details.measureUnit]}",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "2 pcs", style = Typography.labelMedium)
                    }
                }
                Icon(
                    imageVector = if(_isTaken) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable {
                            _isTaken = !_isTaken
                            val now = if(_isTaken) Instant.now().epochSecond else -1L
                            onTake(now, med.id)
                        }
                )
            }
        }
    }
}

@Composable
fun DailyUsages(
    modifier: Modifier = Modifier,
    date: LocalDateTime?,
    meds: List<MedDomainModel>,
    dayData: List<Pair<Long, Long>>,
    onDismiss: () -> Unit = {},
    onNewDate: (LocalDateTime?) -> Unit = {},
    onUsed: (Long, Long, Long) -> Unit = { medId, usageTime, factTime -> }
) {

    Surface(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxWidth().fillMaxHeight(0.8f)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                Text(
                    text = date?.format(DateTimeFormatter.ofPattern("dd MMMM")) ?: "no date",
                    style = Typography.titleLarge,
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource(),
                            onClick = onDismiss::invoke
                        )
                )
            }
            DaySelectorSlider(
                modifier = Modifier.padding(vertical = 8.dp),
                date = date,
                onSelect = onNewDate::invoke
            )
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                dayData.sortedBy { it.first }.forEach { entry ->
                    item { SingleUsageItem(
                        date = entry.first, med = meds.first{ it.id == entry.second },
                        onTake = { datetime, medId ->
                            onUsed(medId, entry.first, datetime)
                        }
                    ) }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}