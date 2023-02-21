package app.mybad.notifier.ui.screens.course

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.theme.Typography

@Composable
@Preview
fun AddMedScreen(
    modifier: Modifier = Modifier,
    userId: String = "",
    onNext: (MedDomainModel) -> Unit = {},
    onBack: () -> Unit = {},
) {

    var newMed by remember { mutableStateOf(MedDomainModel(userId = userId)) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(id = R.string.add_med_h),
                style = Typography.headlineLarge
            )
            Spacer(Modifier.height(16.dp))
            NameInput { newMed = newMed.copy(name = it)}
            Spacer(Modifier.height(16.dp))
            DoseInput { newMed = newMed.copy(details = newMed.details.copy(dose = it.toIntOrNull() ?: 0)) }
            Spacer(Modifier.height(16.dp))
            UnitSelector { newMed = newMed.copy(details = newMed.details.copy(measureUnit = it)) }
        }
        NavigationRow(
            onBack = onBack::invoke,
            onNext = { onNext(newMed) }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameInput(
    onChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.add_med_name),
            style = Typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.add_med_name)) },
            value = value,
            onValueChange = {
                onChange(it)
                value = it

            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(
                onGo = { focusManager.clearFocus() }
            ),
            shape = RoundedCornerShape(10.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoseInput(
    onChange: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.add_med_dose),
            style = Typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.add_med_dose)) },
            value = value,
            onValueChange = {
                onChange(it)
                value = it
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(
                onGo = { focusManager.clearFocus() }
            ),
            shape = RoundedCornerShape(10.dp),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun UnitSelector(
    onSelect: (Int) -> Unit
) {

    val units = stringArrayResource(R.array.units).asList()
    val pagerState = rememberPagerState()
    var dropdownExpanded by remember { mutableStateOf(false) }
    var fieldValue by remember { mutableStateOf(units[0]) }
    val fieldInteractionSource = remember { MutableInteractionSource() }
    if(fieldInteractionSource.collectIsPressedAsState().value) {
        dropdownExpanded = !dropdownExpanded
    }

    LaunchedEffect(pagerState.currentPage) { onSelect(pagerState.currentPage) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.add_med_unit),
            style = Typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            value = fieldValue,
            onValueChange = {},
            interactionSource = fieldInteractionSource,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        )
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            offset = DpOffset(16.dp, (-16).dp)
        ) {
            units.forEachIndexed { index, unit ->
                Text(
                    text = unit,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable {
                            dropdownExpanded = false
                            fieldValue = unit
                            onSelect(index)
                        }
                )
            }
        }
    }
}
