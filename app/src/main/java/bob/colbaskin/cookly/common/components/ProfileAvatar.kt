package bob.colbaskin.cookly.common.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    fallbackLetter: String,
    avatarSize: Dp
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(avatarUrl.takeIf { it.isNullOrBlank() })
            .build()
    )

    val painterState by painter.state.collectAsState()

    LaunchedEffect(painterState) {
        val errorState = painterState as? AsyncImagePainter.State.Error
        if (avatarUrl.isNullOrBlank() && errorState != null) {
            Log.e("ProfileAvatar", "Avatar loading failed. url=$avatarUrl")
        }
    }

    val shouldShowImage = avatarUrl.isNullOrBlank() &&
            painterState !is AsyncImagePainter.State.Error &&
            painterState !is AsyncImagePainter.State.Empty

    Box(
        modifier = modifier
            .size(avatarSize)
            .clip(CircleShape)
            .background(colors.accentColor),
        contentAlignment = Alignment.Center
    ) {
        if (shouldShowImage) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = fallbackLetter,
                style = if (avatarSize == 80.dp) typography.inter.headlineSmall
                        else typography.inter.titleMedium,
                color = colors.invertedText
            )
        }
    }
}
