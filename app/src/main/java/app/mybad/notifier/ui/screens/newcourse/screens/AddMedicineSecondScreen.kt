package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    var selectedInput by remember { mutableStateOf(-1) }
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
                { ParameterIndicator(name = form, value = types[med.type], onClick = { selectedInput = 1 }) },
                { BasicKeyboardInput(label = dose, init = if(med.dose == 0) "" else med.dose.toString(), hideOnGo = true,
                    keyboardType = KeyboardType.Number, alignRight = true,
                    prefix = { Text(text = dose, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)) },
                    onChange = { reducer(NewCourseIntent.UpdateMed(med.copy(dose = it.toIntOrNull() ?: 0))) }) },
                { ParameterIndicator(name = unit, value = units[med.measureUnit], onClick = { selectedInput = 2 }) },
                { ParameterIndicator(name = rel, value = rels[med.beforeFood], onClick = { selectedInput = 3}) },
                modifier = Modifier,
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = onNext::invoke
        ) {
            Text(
                text = stringResource(R.string.navigation_next),
                style = Typography.bodyLarge
            )
        }
    }
    if(selectedInput != -1) {
        Dialog(onDismissRequest = { selectedInput = -1 } ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                when(selectedInput) {
                    1 -> RollSelector(
                        list = types.toList(),
                        onSelect = {
                            reducer(NewCourseIntent.UpdateMed(med.copy(type = it)))
                            selectedInput = -1
                        }
                    )
                    2 -> RollSelector(
                        list = units.toList(),
                        onSelect = {
                            reducer(NewCourseIntent.UpdateMed(med.copy(measureUnit = it)))
                            selectedInput = -1
                        }
                    )
                    3 -> RollSelector(
                        list = rels.toList(),
                        onSelect = {
                            reducer(NewCourseIntent.UpdateMed(med.copy(beforeFood = it)))
                            selectedInput = -1
                        }
                    )
                    else -> {}
                }
            }
        }
    }
}