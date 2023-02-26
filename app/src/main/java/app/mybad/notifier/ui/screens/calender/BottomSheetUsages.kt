package app.mybad.notifier.ui.screens.calender

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
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
    isTaken: Boolean = false
) {
    val units = stringArrayResource(R.array.units)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        val time = LocalDateTime
            .ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))
        Text(text = time)
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(10.dp),
            elevation = 1.dp,
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
                Icon(
                    painter = painterResource(med.details.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                )
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
                    imageVector = if(isTaken) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(40.dp)
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
    onNextDay: () -> Unit = {},
    onPrevDay: () -> Unit = {},
) {
    Surface(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
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
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp)
            )
            LazyColumn {
                dayData.sortedBy { it.first }.forEach { entry ->
                    item { SingleUsageItem(date = entry.first, med = meds.first{ it.id == entry.second } ) }
                }
            }
        }
    }
}