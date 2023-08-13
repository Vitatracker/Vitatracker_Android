package app.mybad.notifier.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography

@Composable
fun ParameterIndicator(
    modifier: Modifier = Modifier,
    name: String?,
    value: Any?,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick::invoke
            )
    ) {
        if (!name.isNullOrBlank()) {
            Text(
                text = name,
                style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value.toString(),
                style = Typography.bodyMedium,
                color = Color.Unspecified.copy(alpha = 0.6f)
            )
            if (!name.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ParameterIndicatorPreview() {
    MyBADTheme {
        ParameterIndicator(name = "Name", value = "Value")
    }
}
