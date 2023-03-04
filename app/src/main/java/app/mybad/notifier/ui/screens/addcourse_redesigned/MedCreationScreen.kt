package app.mybad.notifier.ui.screens.addcourse_redesigned

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.addcourse_redesigned.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.addcourse_redesigned.common.ModalInteractionSelector
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MedCreationScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel,
    usagesPattern: List<Long>,
    onSelectUnits: () -> Unit,
    onSelectQuantity: () -> Unit,
    onSelectFoodRelation: () -> Unit,
    onSelectNotificationsTime: () -> Unit,
    onSetName: (String) -> Unit,
    onSetDose: (Int) -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.add_med_dosa_and_usage),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                //name
                BasicKeyboardInput(
                    modifier = Modifier.padding(16.dp),
                    label = stringResource(R.string.add_med_name),
                    init = med.name ?: "",
                    onChange = onSetName::invoke
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                //dose
                BasicKeyboardInput(
                    modifier = Modifier.padding(16.dp),
                    label = stringResource(R.string.add_med_dose).lowercase(),
                    init = if(med.details.dose == 0) "" else med.details.dose.toString(),
                    keyboardType = KeyboardType.Number,
                    alignRight = true,
                    hideOnGo = true,
                    onChange = { onSetDose(it.toIntOrNull() ?: 0) },
                    prefix = { Text(
                        text = "${stringResource(R.string.add_med_dose)}: ",
                        style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    ) }
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                //units
                ModalInteractionSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = med.details.measureUnit,
                    label = stringResource(R.string.add_med_unit),
                    items = stringArrayResource(R.array.units).toList(),
                    onClick = onSelectUnits::invoke,   //open modal here
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                //iterations
                val range = (1..10).map { it.toString() }
                ModalInteractionSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = usagesPattern.lastIndex,
                    label = stringResource(R.string.add_course_notifications_quantity),
                    items = range,
                    onClick = onSelectQuantity::invoke,   //open modal here
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ModalInteractionSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = med.details.beforeFood,
                    label = stringResource(R.string.add_med_food_relation),
                    items = stringArrayResource(R.array.food_relations).asList(),
                    onClick = onSelectFoodRelation::invoke,   //open modal here
                )
            }
        }
        Text(
            text = stringResource(R.string.add_med_settings),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.background,
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                    onClick = onSelectNotificationsTime::invoke
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_med_remind_time),
                    style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    val list = mutableListOf<String>().apply {
                        usagesPattern.forEach {
                            val time = LocalDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                            add(time)
                        }
                    }.joinToString(", ")
                    Text(
                        text = list,
                        style = Typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp)
                            .weight(0.1f)
                    )
                }
            }
        }
    }

}