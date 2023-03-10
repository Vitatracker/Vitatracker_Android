package app.mybad.notifier.ui.screens.newcourse.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.course.CreateCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.*
import app.mybad.notifier.ui.theme.Typography

@Composable
@Preview
fun AddMedicineMainScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel = MedDomainModel(name = "Xanax"),
    reducer: (CreateCourseIntent) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {

    val types = stringArrayResource(R.array.types)
    val units = stringArrayResource(R.array.units)
    val rels = stringArrayResource(R.array.food_relations)
    var selectedInput by remember { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(16.dp).fillMaxSize()
    ) {
        val form = stringResource(R.string.add_med_form)
        val name = stringResource(R.string.add_med_name)
        val dose = stringResource(R.string.add_med_dose)
        val unit = stringResource(R.string.add_med_unit)
        val rel = stringResource(R.string.add_med_food_relation)
        MultiBox(
            { m, _ -> BasicKeyboardInput(modifier = m, label = name, init = med.name,
                onChange = { reducer(CreateCourseIntent.NewMed(med.copy(name  = it))) }) },
            { m, _ -> IconSelector(modifier = m, selected = med.icon,
                onSelect = { reducer(CreateCourseIntent.NewMed(med.copy(icon = it))) }) },
            { m, _ -> ColorSelector(modifier = m, selected = med.color,
                onSelect = { reducer(CreateCourseIntent.NewMed(med.copy(color = it))) }) },
            { m, c -> ParameterIndicator(modifier = m, name = form, value = types[med.type], onClick = c::invoke) },
            { m, c -> BasicKeyboardInput(modifier = m, label = dose, init = med.dose.toString(), hideOnGo = true,
                keyboardType = KeyboardType.Number, alignRight = true,
                prefix = { Text(text = dose, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)) },
                onChange = { reducer(CreateCourseIntent.NewMed(med.copy(dose = it.toInt()))) }) },
            { m, c -> ParameterIndicator(modifier = m, name = unit, value = units[med.measureUnit], onClick = c::invoke) },
            { m, c -> ParameterIndicator(modifier = m, name = rel, value = rels[med.beforeFood], onClick = c::invoke) },
            modifier = Modifier,
            itemsPadding = PaddingValues(16.dp),
            onSelect = { selectedInput = it },
            outlineColor = MaterialTheme.colorScheme.primary,
        )
        NavigationRow(
            onBack = onBack::invoke,
            onNext = onNext::invoke,
        )
    }
    if(selectedInput == 3 || selectedInput == 5 || selectedInput == 6) {
        Dialog(onDismissRequest = { selectedInput = -1 } ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                when(selectedInput) {
                    3 -> RollSelector(
                            list = stringArrayResource(R.array.types).toList(),
                            onSelect = {
                                reducer(CreateCourseIntent.NewMed(med.copy(type = it)))
                                selectedInput = -1
                            }
                        )

                    5 -> RollSelector(
                            list = stringArrayResource(R.array.units).toList(),
                            onSelect = {
                                reducer(CreateCourseIntent.NewMed(med.copy(measureUnit = it)))
                                selectedInput = -1
                            }
                        )

                    6 -> RollSelector(
                            list = stringArrayResource(R.array.food_relations).toList(),
                            onSelect = {
                                reducer(CreateCourseIntent.NewMed(med.copy(beforeFood = it)))
                                selectedInput = -1
                            }
                        )
                    else -> {}
                }
            }
        }
    }
}