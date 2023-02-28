package app.mybad.notifier.ui.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IterationsSelector(
    label: String,
    limit: Int = 10,
    readOnly: Boolean = true,
    onSelect: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var iterations by remember { mutableStateOf(if(readOnly) 2 else 0) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = Typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(R.drawable.minus),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (iterations > 1) {
                            iterations--
                            onSelect(iterations)
                        }
                    }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                readOnly = readOnly,
                value = iterations.toString(),
                textStyle = Typography.bodyLarge.copy(textAlign = TextAlign.Center),
                onValueChange = {
                    iterations = it.toInt()
                    onSelect(it.toInt())
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = { focusManager.clearFocus() }
                ),
                shape = RoundedCornerShape(10.dp),
                placeholder = { if(!readOnly) Text(stringResource(R.string.add_course_date_from)) },
            )
            Icon(
                painter = painterResource(R.drawable.plus),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (iterations < limit) {
                            iterations++
                            onSelect(iterations)
                        }
                    }
            )
        }

    }
}
