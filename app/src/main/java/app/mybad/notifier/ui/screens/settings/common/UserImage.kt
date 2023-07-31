package app.mybad.notifier.ui.screens.settings.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import app.mybad.theme.R
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

@Preview
@Composable
fun UserImage(
    modifier: Modifier = Modifier,
    url: String? = null,
    showEdit: Boolean = true,
    imageSize: Dp = 130.dp,
    onEditClicked: () -> Unit = {}
) {
    val editAvatar = remember { mutableStateOf(url) }
//    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = {
//            editAvatar.value = it.toString()
//            onEdit(it.toString())
//        }
//    )

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
                    .size(imageSize)
                    .border(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = CircleShape
                    )
            )
            if (showEdit) {
                Image(
                    painter = painterResource(R.drawable.edit),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            onEditClicked()
//                            singlePhotoPickerLauncher.launch(
//                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                            )
                        }
                )
            }
        }
    }
}
