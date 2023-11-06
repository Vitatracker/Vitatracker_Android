package app.mybad.notifier.ui.common

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import app.mybad.theme.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReUseTopAppBar(titleResId: Int?, onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            if (titleResId != null) {
                TitleText(textStringRes = titleResId)
            }
        },
        navigationIcon = {
            NavigateBackIconButton(onBackPressed)
        }
    )
}

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    @StringRes textStringRes: Int,
    textAlign: TextAlign? = null
) {
    Text(
        modifier = modifier,
        text = stringResource(id = textStringRes),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign
    )
}

@Composable
private fun NavigateBackIconButton(onBackPressed: () -> Unit) {
    var enabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    IconButton(
        enabled = enabled,
        onClick = {
            enabled = false
            onBackPressed()
            coroutineScope.launch {
                delay(200)
                enabled = true
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.navigation_back)
        )
    }
}
