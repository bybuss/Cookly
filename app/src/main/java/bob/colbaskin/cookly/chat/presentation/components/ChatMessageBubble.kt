package bob.colbaskin.cookly.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.chat.domain.models.ChatMessage
import bob.colbaskin.cookly.chat.domain.models.ChatSender
import bob.colbaskin.cookly.common.components.MarkdownText
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme

@Composable
fun ChatMessageBubble(
    modifier: Modifier = Modifier,
    message: ChatMessage,
    showChefAvatar: Boolean,
    onRecipeClick: (Int) -> Unit
) {
    val isUser = message.sender == ChatSender.User

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .padding(
                start = if (isUser) 24.dp else 0.dp,
                end = if (!isUser) 24.dp else 0.dp
            ),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            if (showChefAvatar) {
                ChefAvatar()
            } else {
                Spacer(modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isUser) 18.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 18.dp
                        )
                    )
                    .background(
                        if (isUser) {
                            CustomTheme.colors.accentColor
                        } else {
                            CustomTheme.colors.secondaryCardBackground
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                if (isUser || message.isTyping) {
                    Text(
                        text = message.text,
                        color = if (isUser) CustomTheme.colors.background else CustomTheme.colors.text,
                        style = CustomTheme.typography.inter.bodyMedium,
                        fontStyle = if (message.isTyping) FontStyle.Italic else FontStyle.Normal
                    )
                } else {
                    MarkdownText(
                        text = message.text,
                        color = CustomTheme.colors.text,
                        style = CustomTheme.typography.inter.bodyMedium
                    )
                }
            }

            if (!isUser && message.recipes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                ChefRecipeRowMessage(
                    recipes = message.recipes,
                    onRecipeClick = onRecipeClick
                )
            }
        }
    }
}

@Composable
private fun ChefAvatar() {
    Image(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(50)),
        painter = painterResource(R.drawable.cheif_ai_avatar),
        contentDescription = null
    )
}
