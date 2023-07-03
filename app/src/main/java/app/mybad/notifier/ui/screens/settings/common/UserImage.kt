package app.mybad.notifier.ui.screens.settings.common

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import app.mybad.theme.R
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun UserImage(
    modifier: Modifier = Modifier,
    url: String? = null,
    showEdit: Boolean = true,
    onEdit: (String) -> Unit
) {
    val editAvatar = remember { mutableStateOf(url) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            editAvatar.value = it.toString()
            onEdit(it.toString())
        }
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            val req = ImageRequest.Builder(LocalContext.current)
                .data(editAvatar.value?.toUri())
                .placeholder(R.drawable.icon_profile_mobile_app)
                .error(R.drawable.icon_profile_mobile_app)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
            AsyncImage(
                model = req,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(130.dp)
                    .border(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = CircleShape
                    )
            )
            if (showEdit) {
                Icon(
                    painter = painterResource(R.drawable.edit),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                )
            }
        }
    }
}
