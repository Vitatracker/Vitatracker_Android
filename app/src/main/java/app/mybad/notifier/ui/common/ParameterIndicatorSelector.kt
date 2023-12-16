package app.mybad.notifier.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.mybad.theme.R

@Composable
fun ParameterIndicatorSelector(
    @StringRes parameter: Int,
    value: Int,
    items: Array<String>,
    onSelect: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ParameterIndicator(
        name = parameter,
        value = items[value],
        onClick = { expanded = true }
    )
    ReUseAnimatedVisibility(visible = expanded) {
        ParameterSelectorDialog(
            title = parameter,
            items = items,
            value = value,
            onDismiss = { expanded = false },
            onSelect = onSelect::invoke
        )
    }
}

@Composable
private fun ParameterSelectorDialog(
    @StringRes title: Int,
    value: Int,
    items: Array<String>,
    onDismiss: () -> Unit,
    onSelect: (Int) -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(value) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TitleText(
                    textStringRes = title,
                    textAlign = TextAlign.Center,
                )
                VerticalSpacerMedium()
                items.forEachIndexed { index, item ->
                    ParameterSelectorItem(
                        text = item,
                        selected = index == selectedIndex,
                        onClick = { selectedIndex = index },
                    )
                }
                VerticalSpacerLarge()
                ReUseFilledButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textId = R.string.settings_save,
                    onClick = {
                        onDismiss()
                        onSelect(selectedIndex)
                    },
                )
            }
        }
    }
}

@Composable
private fun ParameterSelectorItem(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent
            )
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall,
            fontSize = if (selected) 18.sp else 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary.copy(if (selected) 0.8f else 0.5f),
            maxLines = 1,
        )
    }
    ReUseHorizontalDivider()
}

@Composable
private fun ReUseHorizontalDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 1.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    )
}
