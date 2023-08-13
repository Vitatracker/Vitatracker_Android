package app.mybad.notifier.ui.screens.settings.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R

@Preview
@Composable
fun SettingsItem(
    label: String = "setting",
    icon: Int = R.drawable.icon_settings_user,
    onSelect: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                //TODO("задать цвет")
                tint = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            )
            Text(text = label, style = Typography.bodyLarge)
        }
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(24.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.icon_settings_arrow_right),
                contentDescription = null,
                //TODO("проверить цвет")
                tint = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemPreview() {
    MyBADTheme {
        SettingsItem()
    }
}
