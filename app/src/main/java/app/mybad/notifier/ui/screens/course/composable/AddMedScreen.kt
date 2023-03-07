package app.mybad.notifier.ui.screens.course.composable

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant

@Composable
fun AddMedScreen(
    modifier: Modifier = Modifier,
    userId: String = "",
    init: MedDomainModel,
    onNext: () -> Unit = {},
    onChange: (MedDomainModel) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val context = LocalContext.current
    var newMed by remember { mutableStateOf(init.copy(userId = userId, id = Instant.now().epochSecond)) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            NameInput(init = init.name ?: "") {
                newMed = newMed.copy(name = it)
                onChange(newMed)
            }
            Spacer(Modifier.height(16.dp))
            DoseInput(init = if(init.dose == 0) "" else init.dose.toString()) {
                newMed = newMed.copy(dose = it.toIntOrNull() ?: 0)
                onChange(newMed)
            }
            Spacer(Modifier.height(16.dp))
            UnitSelector(init = init.measureUnit) {
                newMed = newMed.copy(measureUnit = it)
                onChange(newMed)
            }
        }
        val unfilledError = stringResource(R.string.add_med_error_unfilled_fields)
        NavigationRow(
            onBack = onBack::invoke,
            onNext = {
                if(newMed.dose == 0 || newMed.name.isNullOrBlank()) {
                    Toast.makeText(context, unfilledError, Toast.LENGTH_SHORT).show()
                } else onNext()
            }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameInput(
    init: String = "",
    onChange: (String) -> Unit
) {
    var value by remember { mutableStateOf(init) }
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
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(10.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoseInput(
    init: String = "",
    onChange: (String) -> Unit
) {

    var value by remember { mutableStateOf(init) }

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
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(10.dp),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun UnitSelector(
    init: Int,
    onSelect: (Int) -> Unit
) {

    val units = stringArrayResource(R.array.units).asList()
    var dropdownExpanded by remember { mutableStateOf(false) }
    var fieldValue by remember { mutableStateOf(units[init]) }
    val fieldInteractionSource = remember { MutableInteractionSource() }
    if(fieldInteractionSource.collectIsPressedAsState().value) {
        dropdownExpanded = !dropdownExpanded
    }

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

