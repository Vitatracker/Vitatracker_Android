package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.*
import app.mybad.notifier.ui.theme.Typography

@Composable
fun AddMedicineSecondScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel,
    reducer: (NewCourseIntent) -> Unit,
    onNext: () -> Unit,
) {

    val types = stringArrayResource(R.array.types)
    val units = stringArrayResource(R.array.units)
    val rels = stringArrayResource(R.array.food_relations)
    val form = stringResource(R.string.add_med_form)
    val dose = stringResource(R.string.add_med_dose)
    val unit = stringResource(R.string.add_med_unit)
    val rel = stringResource(R.string.add_med_food_relation)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            Text(
                text = stringResource(R.string.add_med_details),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            MultiBox(
                {
                    var exp by remember { mutableStateOf(false) }
                    ParameterIndicator(name = form, value = types[med.type], onClick = { exp = true })
                    DropdownMenu(
                        expanded = exp,
                        onDismissRequest = { exp = false },
                        offset = DpOffset(x = 300.dp, y = 0.dp)
                    ) {
                        types.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) }, onClick = {
                                    reducer(NewCourseIntent.UpdateMed(med.copy(type = index)))
                                    exp = false
                                })
                        }
                    }
                },
                { BasicKeyboardInput(label = dose, init = if(med.dose == 0) "" else med.dose.toString(), hideOnGo = true,
                    keyboardType = KeyboardType.Number, alignRight = true,
                    prefix = { Text(text = dose, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)) },
                    onChange = { reducer(NewCourseIntent.UpdateMed(med.copy(dose = it.toIntOrNull() ?: 0))) }) },
                {
                    var exp by remember { mutableStateOf(false) }
                    ParameterIndicator(name = unit, value = units[med.measureUnit], onClick = { exp = true })
                    DropdownMenu(
                        expanded = exp,
                        onDismissRequest = { exp = false },
                        offset = DpOffset(x = 300.dp, y = 0.dp)
                    ) {
                        units.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) }, onClick = {
                                    reducer(NewCourseIntent.UpdateMed(med.copy(measureUnit = index)))
                                    exp = false
                                })
                        }
                    }
                },
                {
                    var exp by remember { mutableStateOf(false) }
                    ParameterIndicator(name = rel, value = rels[med.beforeFood], onClick = { exp = true })
                    DropdownMenu(
                        expanded = exp,
                        onDismissRequest = { exp = false },
                        offset = DpOffset(x = 300.dp, y = 0.dp)
                    ) {
                        rels.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(item) }, onClick = {
                                    reducer(NewCourseIntent.UpdateMed(med.copy(beforeFood = index)))
                                    exp = false
                                })
                        }
                    }
                },
                modifier = Modifier,
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = onNext::invoke
        ) {
            Text(
                text = stringResource(R.string.navigation_next),
                style = Typography.bodyLarge
            )
        }
    }
}