package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.theme.R
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.newcourse.common.ColorSelector
import app.mybad.notifier.ui.screens.newcourse.common.IconSelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.reuse.NavigateBackIconButton
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineFirstScreen(
    med: MedDomainModel,
    onNext: (MedDomainModel) -> Unit,
    onBackPressed: () -> Unit
) {
    val name = stringResource(R.string.enter_med_name)
    var isError by rememberSaveable { mutableStateOf(false) }
    var currentMed by remember { mutableStateOf(med.copy()) }
    var currentName by rememberSaveable { mutableStateOf(currentMed.name) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                TitleText(textStringRes = R.string.add_med_h)
            },
            navigationIcon = { NavigateBackIconButton(onBackPressed) }
        )
    }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.add_med_name),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                MultiBox(
                    {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BasicKeyboardInput(
                                modifier = Modifier.width(300.dp),
                                label = name,
                                init = currentName,
                                hideOnGo = true,
                                onChange = {
                                    currentName = it
                                    isError = it.isBlank()
                                }
                            )
                            if (isError) {
                                Icon(
                                    imageVector = Icons.Rounded.Error,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                    },
                    itemsPadding = PaddingValues(16.dp),
                    outlineColor = if (isError) MaterialTheme.colorScheme.errorContainer
                    else MaterialTheme.colorScheme.primary,
                )
                if (isError) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        text = stringResource(id = R.string.add_med_error_empty_name)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.add_med_icon),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                MultiBox(
                    {
                        IconSelector(
                            selected = currentMed.icon,
                            color = currentMed.color,
                            onSelect = { currentMed = currentMed.copy(icon = it) }
                        )
                    },
                    itemsPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.add_med_icon_color),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                MultiBox(
                    {
                        ColorSelector(
                            selected = currentMed.color,
                            onSelect = { currentMed = currentMed.copy(color = it) }
                        )
                    },
                    itemsPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
            }
            ReUseFilledButton(textId = R.string.navigation_next) {
                if (!currentName.isNullOrBlank()) {
                    currentMed = currentMed.copy(name = currentName)
                    onNext(currentMed)
                } else {
                    isError = true
                }
            }
        }
    }

}
