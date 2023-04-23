package app.mybad.notifier.ui.screens.settings.about

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.screens.settings.common.SettingsAboutItem

@Composable
@Preview(showBackground = true)
fun SettingsAbout(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        SettingsAboutItem(
            label = "point 1",
            summary = "short description",
            text = "Lorem Ipsum - это текст-\"рыба\", часто используемый в печати и вэб-дизайне. Lorem Ipsum является стандартной \"рыбой\" um для"
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
        SettingsAboutItem(
            label = "point 2",
            summary = "short description",
            text = "Lorem Ipsum - это текст-\"рыба\", часто используемый в печати и вэб-дизайне. Lorem Ipsum является стандартной \"рыбой\" um для"
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
        SettingsAboutItem(
            label = "point 3",
            summary = "short description",
            text = "Lorem Ipsum - это текст-\"рыба\", часто используемый в печати и вэб-дизайне. Lorem Ipsum является стандартной \"рыбой\" um для"
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
        SettingsAboutItem(
            label = "point 4",
            summary = "short description",
            text = "Lorem Ipsum - это текст-\"рыба\", часто используемый в печати и вэб-дизайне. Lorem Ipsum является стандартной \"рыбой\" um для"
        )
    }
}
