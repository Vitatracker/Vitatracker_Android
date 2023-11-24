package app.mybad.notifier.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun ReUseImage(
    @DrawableRes imageVector: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    shape: Shape = CircleShape,
    border: Boolean = true,
    onClick: () -> Unit = {},
) {
    Image(
        modifier = modifier,
        imageVector = ImageVector.vectorResource(imageVector),
        contentDescription = null,
    )
}
