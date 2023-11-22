package app.mybad.notifier.ui.screens.settings.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R

@Composable
fun SettingsItem(
    @StringRes label: Int,
    @DrawableRes icon: Int = R.drawable.icon_settings_user,
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
                tint = MaterialTheme.colorScheme.surfaceContainerHighest,
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(label),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_settings_arrow_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemPreview() {
    MyBADTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                SettingsItem(R.string.settings_profile)
                Spacer(modifier = Modifier.width(12.dp))
                SettingsItem(
                    label = R.string.settings_notifications,
                    icon = R.drawable.icon_settings_notifications,
                )
            }
        }
    }
}
