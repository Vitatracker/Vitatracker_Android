package app.mybad.notifier.ui.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource

@Composable
fun ScreenBackgroundImage(idImage: Int) {
    Image(
        imageVector = ImageVector.vectorResource(id = idImage),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.FillWidth
    )
}