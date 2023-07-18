package app.mybad.notifier.ui.screens.reuse

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBackAction(titleResId: Int, onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            TitleText(textStringRes = titleResId)
        },
        navigationIcon = {
            NavigateBackIconButton(onBackPressed)
        }
    )
}

@Composable
fun TitleText(@StringRes textStringRes: Int) {
    Text(
        text = stringResource(id = textStringRes),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun NavigateBackIconButton(onBackPressed: () -> Unit) {
    var enabled by remember {
        mutableStateOf(true)
    }
    IconButton(
        enabled = enabled,
        onClick = {
            enabled = false
            onBackPressed()
        }
    ) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.navigation_back))
    }
}