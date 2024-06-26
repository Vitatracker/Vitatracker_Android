package app.mybad.notifier.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.theme.R

@Composable
fun NavigationRow(
    modifier: Modifier = Modifier,
    backLabel: String = stringResource(R.string.navigation_back),
    nextLabel: String = stringResource(R.string.navigation_next),
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 8.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = onBack::invoke,
        ) {
            Text(
                text = backLabel,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 8.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = onNext::invoke
        ) {
            Text(
                text = nextLabel,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
