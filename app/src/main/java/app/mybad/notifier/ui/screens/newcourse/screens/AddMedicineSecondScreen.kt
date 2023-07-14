package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.theme.R
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.*
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineSecondScreen(
    med: MedDomainModel,
    onNext: (MedDomainModel) -> Unit,
    onBackPressed: () -> Unit,
) {

    Scaffold(topBar = {
        TopAppBar(title = {
            TitleText(textStringRes = R.string.add_med_h)
        },
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.navigation_back))
                }
            })
    }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SecondScreenContent(
                med = med,
                onNext = onNext
            )
        }
    }
}

@Composable
private fun SecondScreenContent(med: MedDomainModel, onNext: (MedDomainModel) -> Unit) {
    val types = stringArrayResource(R.array.types)
    val units = stringArrayResource(R.array.units)
    val relations = stringArrayResource(R.array.food_relations)
    val dose = stringResource(R.string.add_med_dose)
    val unit = stringResource(R.string.add_med_unit)
    var currentMed by rememberSaveable { mutableStateOf(med) }

    Column {
        Text(
            text = stringResource(R.string.add_med_reception),
            style = Typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        MultiBox(
            {
                var exp by remember { mutableStateOf(false) }
                ParameterIndicator(
                    name = stringResource(R.string.add_med_form),
                    value = types[currentMed.type],
                    onClick = {
                        exp = true
                    }
                )
                DropdownMenu(
                    expanded = exp,
                    onDismissRequest = { exp = false },
                    offset = DpOffset(x = 300.dp, y = 0.dp)
                ) {
                    types.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                currentMed = currentMed.copy(type = index)
                                exp = false
                            }
                        )
                    }
                }
            },
            {
                BasicKeyboardInput(
                    label = dose,
                    init = if (currentMed.dose == 0) "" else currentMed.dose.toString(),
                    hideOnGo = true,
                    keyboardType = KeyboardType.Number,
                    alignRight = true,
                    prefix = {
                        Text(text = dose, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    },
                    onChange = {
                        currentMed = currentMed.copy(dose = it.toIntOrNull() ?: 0)
                    }
                )
            },
            {
                var exp by remember { mutableStateOf(false) }
                ParameterIndicator(name = unit, value = units[currentMed.measureUnit], onClick = {
                    exp = true
                })
                DropdownMenu(
                    expanded = exp,
                    onDismissRequest = { exp = false },
                    offset = DpOffset(x = 300.dp, y = 0.dp)
                ) {
                    units.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                currentMed = currentMed.copy(measureUnit = index)
                                exp = false
                            }
                        )
                    }
                }
            },
            {
                var exp by remember { mutableStateOf(false) }
                ParameterIndicator(
                    name = stringResource(R.string.add_med_food_relation),
                    value = relations[currentMed.beforeFood],
                    onClick = {
                        exp = true
                    }
                )
                DropdownMenu(
                    expanded = exp,
                    onDismissRequest = { exp = false },
                    offset = DpOffset(x = 300.dp, y = 0.dp)
                ) {
                    relations.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                currentMed = currentMed.copy(beforeFood = index)
                                exp = false
                            }
                        )
                    }
                }
            },
            modifier = Modifier,
            itemsPadding = PaddingValues(16.dp),
            outlineColor = MaterialTheme.colorScheme.primary,
        )
    }
    ReUseFilledButton(textId = R.string.navigation_next) {
        onNext(currentMed)
    }
}
