package app.mybad.notifier.ui.screens.newcourse.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.*
import app.mybad.notifier.ui.theme.Typography

@Composable
fun AddMedicineFirstScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel,
    reducer: (NewCourseIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val name = stringResource(R.string.add_med_name)
    val context = LocalContext.current

    val fieldsError = stringResource(R.string.add_med_error_unfilled_fields)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            MultiBox(
                {
                    BasicKeyboardInput(label = name, init = med.name, hideOnGo = true,
                        onChange = { reducer(NewCourseIntent.UpdateMed(med.copy(name = it))) })
                },
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.add_med_icon),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            MultiBox(
                {
                    IconSelector(selected = med.icon, color = med.color,
                        onSelect = { reducer(NewCourseIntent.UpdateMed(med.copy(icon = it))) })
                },
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.add_med_icon_color),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            MultiBox(
                {
                    ColorSelector(selected = med.color,
                        onSelect = { reducer(NewCourseIntent.UpdateMed(med.copy(color = it))) })
                },
                itemsPadding = PaddingValues(16.dp),
                outlineColor = MaterialTheme.colorScheme.primary,
            )
        }
        NavigationRow(
            onBack = onBack::invoke,
            onNext = {
                if (med.name.isNullOrBlank()) Toast.makeText(
                    context,
                    fieldsError,
                    Toast.LENGTH_SHORT
                ).show()
                else onNext()
            },
        )
    }
}