package app.mybad.notifier.ui.screens.addcourse_redesigned.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ModalDateSelector(
    modifier: Modifier = Modifier,
    selected: LocalDateTime,
    label: String,
    style: TextStyle = Typography.bodyMedium,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource(),
                onClick = onClick::invoke
            )
    ) {
        Text(
            text = label,
            style = style.copy(fontWeight = FontWeight.Bold)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected.format(DateTimeFormatter.ISO_LOCAL_DATE),
                style = style
            )
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}