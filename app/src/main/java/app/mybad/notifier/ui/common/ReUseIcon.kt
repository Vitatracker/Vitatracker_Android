package app.mybad.notifier.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.PickColor
import app.mybad.theme.R

@Composable
fun ReUseIcon(
    @DrawableRes painterId: Int,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    iconSize: Dp = 24.dp,
    color: Color = Color.Transparent,
    shape: Shape = CircleShape,
    border: Boolean = true,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape)
            .background(color)
            .border(
                1.dp,
                if (border) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape
            )
            .clickable {
                onClick()
            },
    ) {
        Icon(
            painter = painterResource(painterId),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(iconSize)
        )
    }
}

@Composable
fun MedicineIcon(
    @DrawableRes icon: Int,
    color: Int,
    border: Boolean = false,
    onClick: () -> Unit = {},
) {
    ReUseIcon(
        modifier = Modifier.size(40.dp),
        painterId = icon,
        tint = MaterialTheme.colorScheme.outline,
        iconSize = 24.dp,
        color = PickColor.getColor(color),
        shape = CircleShape,
        border = border,
        onClick = onClick,
    )
}

@Preview
@Composable
private fun ReUseIconPreview() {
    ReUseIcon(
        painterId = R.drawable.icon_pencil,
        tint = MaterialTheme.colorScheme.surfaceBright,
        iconSize = 16.dp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(24.dp),
    )
}
