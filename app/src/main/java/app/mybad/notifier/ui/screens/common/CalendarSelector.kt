package app.mybad.notifier.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

@Composable
fun CalendarSelectorScreen(
    modifier: Modifier = Modifier,
    date: LocalDate = LocalDate.now(),
    startDay: LocalDate,
    endDay: LocalDate,
    onSelect: (startDate: LocalDate?, endDate: LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {

    var sDate by remember { mutableStateOf(date) }
    var selectedDiapason by remember { mutableStateOf(Pair(startDay, endDay)) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            MonthSelector(
                date = sDate.atStartOfDay(),
                onSwitch = { sDate = it.toLocalDate() }
            )
            Spacer(Modifier.height(16.dp))
            CalendarSelector(
                date = sDate,
                startDay = selectedDiapason.first,
                endDay = selectedDiapason.second,
                onSelect = { sd, ed ->
                    selectedDiapason = sd to ed
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        NavigationRow(
            backLabel = stringResource(R.string.settings_cancel),
            nextLabel = stringResource(R.string.settings_save),
            onBack = onDismiss::invoke,
            onNext = { onSelect(selectedDiapason.first, selectedDiapason.second) }
        )
    }

}

@Composable
fun CalendarSelector(
    modifier: Modifier = Modifier,
    date: LocalDate,
    startDay: LocalDate,
    endDay: LocalDate,
    onSelect: (startDate: LocalDate, endDate: LocalDate) -> Unit = { _, _ -> }
) {

    var startDate by remember { mutableStateOf(startDay) }
    var endDate by remember { mutableStateOf(endDay) }
    val days = stringArrayResource(R.array.days_short)
    val currentDate = LocalDate.now()
    val cdr : Array<Array<LocalDate?>> = Array(6) {
        Array(7) { null }
    }
    val fwd = date.minusDays(date.dayOfMonth.toLong())
    for(w in 0..5) {
        for(d in 0..6) {
            if(w == 0 && d < fwd.dayOfWeek.value) {
                cdr[w][d] = fwd.minusDays(fwd.dayOfWeek.value - d.toLong() - 1)
            } else {
                cdr[w][d] = fwd.plusDays(w * 7L + d - fwd.dayOfWeek.value + 1)
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
            ) {
                repeat(7) {
                    Text(
                        text = days[it],
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, false)
                    )
                }
            }
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
            repeat(6) { w ->
                if (cdr[w].any { it?.month == date.month }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        repeat(7) { d ->
                            CalendarDayItem(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .fillMaxWidth()
                                    .weight(1f, false),
                                date = cdr[w][d],
                                position = d,
                                isFirst = cdr[w][d] == startDate,
                                isLast = cdr[w][d] == endDate,
                                isInRange = (cdr[w][d] ?: currentDate) in startDate..endDate,
                                isSelected = cdr[w][d] == currentDate,
                                isOtherMonth = cdr[w][d]?.month != date.month
                            ) {
                                val selectedDate = it ?: LocalDate.now()
                                val diffStart = ChronoUnit.DAYS.between(
                                    selectedDate,
                                    startDate
                                ).absoluteValue
                                val diffEnd =
                                    ChronoUnit.DAYS.between(selectedDate, endDate).absoluteValue
                                if (selectedDate.isBefore(startDate)) startDate = selectedDate
                                else if (selectedDate.isAfter(startDate) && selectedDate.isBefore(
                                        endDate
                                    )
                                ) {
                                    if (diffStart > diffEnd) endDate = selectedDate
                                    else startDate = selectedDate
                                } else if (selectedDate.isAfter(endDate)) endDate = selectedDate
                                else if (selectedDate == startDate && selectedDate != endDate)
                                    startDate = startDate.plusDays(1L)
                                else if (selectedDate == endDate && selectedDate != startDate)
                                    endDate = endDate.minusDays(1L)
                                onSelect(startDate, endDate)
                            }
                        }
                    }
                } else if(w>4) Spacer(Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    modifier: Modifier = Modifier,
    date: LocalDate?,
    isSelected: Boolean = false,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    isInRange: Boolean = false,
    position: Int,
    isOtherMonth: Boolean = false,
    onSelect: (LocalDate?) -> Unit = {}
) {
    val outlineColor = MaterialTheme.colorScheme.primary
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(40.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else if (isInRange) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                else Color.Transparent,
                selectShape(isFirst, isLast, isInRange, position)
            )
            .drawBehind {
                drawCalendarSelection(isFirst, isLast, isInRange, position, outlineColor)
            }
            .alpha(if (isOtherMonth) 0.5f else 1f)
            .clickable(
                indication = null, interactionSource = MutableInteractionSource()
            ) {
                onSelect(date)
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date?.dayOfMonth.toString(),
                style = Typography.bodyLarge,
                color = if(isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
            )
        }
    }
}

private fun DrawScope.drawCalendarSelection(
    isFirst: Boolean = false,
    isLast: Boolean = false,
    isInRange: Boolean = false,
    position: Int,
    color: Color
) {
    if(isInRange) {
        if((position == 0 && isLast) || (position == 6 && isFirst) || (isFirst && isLast)) {
            drawArc(
                brush = SolidColor(color),
                startAngle = 90f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset.Zero,
                size = this.size.copy(size.height, size.height),
                style = Stroke(width = 1f)
            )
            drawArc(
                brush = SolidColor(color),
                startAngle = 270f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset.Zero.copy(x = size.width-size.height),
                size = this.size.copy(size.height, size.height),
                style = Stroke(width = 1f)
            )
            drawLine(
                brush = SolidColor(color),
                start = Offset.Zero.copy(x = size.height/2,y = size.height),
                end = Offset(x = size.width-size.height/2, y = size.height)
            )
            drawLine(
                brush = SolidColor(color),
                start = Offset.Zero.copy(x = size.height/2),
                end = Offset(x = size.width - size.height/2, y = 0f)
            )
        } else if(isLast || position == 6) {
            drawArc(
                brush = SolidColor(color),
                startAngle = 270f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset.Zero.copy(x = size.width-size.height),
                size = this.size.copy(size.height, size.height),
                style = Stroke(width = 1f)
            )
            drawLine(
                brush = SolidColor(color),
                start = Offset.Zero.copy(x = 0f,y = size.height),
                end = Offset(x = size.width-size.height/2, y = size.height)
            )
            drawLine(
                brush = SolidColor(color),
                start = Offset.Zero,
                end = Offset(x = size.width - size.height/2, y = 0f)
            )
        } else if(isFirst || position == 0) {
            drawArc(
                brush = SolidColor(color),
                startAngle = 90f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset.Zero,
                size = this.size.copy(size.height, size.height),
                style = Stroke(width = 1f)
            )
            drawLine(
                brush = SolidColor(color),
                start = Offset.Zero.copy(x = size.height/2,y=size.height),
                end = Offset(x = size.width, y = size.height)
            )
            drawLine(
                brush = SolidColor(color),
                start = Offset.Zero.copy(x = size.height/2),
                end = Offset(x = size.width, y = 0f)
            )
        } else if(position in 1..5) {
            val y = size.height
            drawLine(
                brush = SolidColor(color),
                start = Offset.Zero.copy(y=y),
                end = Offset(x = size.width, y =y)
            )
            drawLine(
                brush = SolidColor(color),
                start = Offset.Zero,
                end = Offset(x = size.width, y = 0f)
            )
        }
    }
}

private fun selectShape(
    isFirst: Boolean = false,
    isLast: Boolean = false,
    isInRange: Boolean = false,
    position: Int,
) : Shape {
    return if(isInRange) {
        if((position == 0 && isLast) || (position == 6 && isFirst) || (isFirst && isLast)) {
            RoundedCornerShape(500f)
        } else if(isLast || position == 6) {
            RoundedCornerShape(topEnd = 500f, bottomEnd = 500f)
        } else if(isFirst || position == 0) {
            RoundedCornerShape(topStart = 500f, bottomStart = 500f)
        } else RectangleShape
    } else CircleShape
}