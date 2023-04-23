package app.mybad.notifier.ui.screens.newcourse.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MultiBox(
    vararg items: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    outlineColor: Color = MaterialTheme.colorScheme.primary,
    itemsPadding: PaddingValues,
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, outlineColor),
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            items.forEachIndexed { index, item ->
                Box(Modifier.padding(itemsPadding)) { item() }
                if (index < items.lastIndex) Divider(thickness = 1.dp, color = outlineColor)
            }
        }
    }
}
