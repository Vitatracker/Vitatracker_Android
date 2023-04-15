package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.*
import app.mybad.notifier.ui.theme.Typography

@Composable
fun AddMedicineFirstScreen(
    modifier: Modifier = Modifier,
    med: MedDomainModel,
    reducer: (NewCourseIntent) -> Unit,
    onNext: () -> Unit,
) {
    val name = stringResource(R.string.add_med_name)

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
}