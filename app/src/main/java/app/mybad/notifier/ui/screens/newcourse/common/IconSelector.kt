package app.mybad.notifier.ui.screens.newcourse.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.PickColor
import app.mybad.theme.R

@SuppressLint("Recycle")
@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    selected: Int,
    color: Int = 0,
    onSelect: (Int) -> Unit
) {
    val icons = integerArrayResource(R.array.icons)
    val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        icons.forEachIndexed { index, _ ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == selected) PickColor.getColor(color) else MaterialTheme.colorScheme.background
                    )
                    .border(
                        1.dp,
                        if (index == selected) PickColor.getColor(color) else MaterialTheme.colorScheme.outline,
                        CircleShape
                    )
                    .clickable { onSelect(index) }
            ) {
                Icon(
                    painter = painterResource(r.getResourceId(index, 0)),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
