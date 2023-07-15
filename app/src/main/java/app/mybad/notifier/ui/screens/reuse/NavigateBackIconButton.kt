package app.mybad.notifier.ui.screens.reuse

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import app.mybad.theme.R

@Composable
fun NavigateBackIconButton(onBackPressed: () -> Unit) {
    IconButton(
        onClick = {
            onBackPressed()
        }
    ) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.navigation_back))
    }
}