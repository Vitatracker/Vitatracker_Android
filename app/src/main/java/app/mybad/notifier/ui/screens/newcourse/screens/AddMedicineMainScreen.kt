package app.mybad.notifier.ui.screens.newcourse.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.*
import app.mybad.notifier.ui.theme.Typography

@Composable
fun AddMedicineMainScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel,
    reducer: (NewCourseIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {

    val types = stringArrayResource(R.array.types)
    val units = stringArrayResource(R.array.units)
    val rels = stringArrayResource(R.array.food_relations)
    var selectedInput by remember { mutableStateOf(-1) }
    val name = stringResource(R.string.add_med_name)
    val form = stringResource(R.string.add_med_form)
    val dose = stringResource(R.string.add_med_dose)
    val unit = stringResource(R.string.add_med_unit)
    val rel = stringResource(R.string.add_med_food_relation)
    val context = LocalContext.current
    val fieldsError = stringResource(R.string.add_med_error_unfilled_fields)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            MultiBox(
                { BasicKeyboardInput(label = name, init = med.name,
                    onChange = { reducer(NewCourseIntent.UpdateMed(med.copy(name  = it))) }) },
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.add_med_icon_color),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            MultiBox(
                { IconSelector(selected = med.icon, color = med.color,
                    onSelect = { reducer(NewCourseIntent.UpdateMed(med.copy(icon = it))) }) },
                { ColorSelector(selected = med.color,
                    onSelect = { reducer(NewCourseIntent.UpdateMed(med.copy(color = it))) }) },
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.add_med_details),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            MultiBox(
                { ParameterIndicator(name = form, value = types[med.type], onClick = { selectedInput = 1 }) },
                { BasicKeyboardInput(label = dose, init = med.dose.toString(), hideOnGo = true,
                    keyboardType = KeyboardType.Number, alignRight = true,
                    prefix = { Text(text = dose, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)) },
                    onChange = { reducer(NewCourseIntent.UpdateMed(med.copy(dose = it.toInt()))) }) },
                { ParameterIndicator(name = unit, value = units[med.measureUnit], onClick = { selectedInput = 2 }) },
                { ParameterIndicator(name = rel, value = rels[med.beforeFood], onClick = { selectedInput = 3})},
                modifier = Modifier,
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
        }

        NavigationRow(
            onBack = onBack::invoke,
            onNext = {
                if(med.name.isNullOrBlank()) Toast.makeText(context, fieldsError, Toast.LENGTH_SHORT).show()
                else onNext()
            },
        )
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