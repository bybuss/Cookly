package bob.colbaskin.cookly.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.Send

@Composable
fun ChatInputBar(
    modifier: Modifier = Modifier,
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val colors = CustomTheme.colors
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    val canSend = value.isNotBlank() && enabled

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            textStyle = CustomTheme.typography.inter.bodyMedium.copy(
                color = colors.text,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .weight(1f),
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (canSend) {
                        focusManager.clearFocus()
                        onSendClick()
                    }
                }
            ),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = false,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = {
                        Text(
                            text = "Напишите сообщение...",
                            color = colors.secondaryText,
                            style = CustomTheme.typography.inter.bodyMedium
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.background,
                        unfocusedContainerColor = colors.background,
                        disabledContainerColor = colors.background,
                        focusedBorderColor = colors.mealCardBorder,
                        unfocusedBorderColor = colors.mealCardBorder,
                        disabledBorderColor = colors.mealCardBorder,
                        cursorColor = colors.text
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = enabled,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = colors.background,
                                unfocusedContainerColor = colors.background,
                                disabledContainerColor = colors.background,
                                focusedBorderColor = colors.mealCardBorder,
                                unfocusedBorderColor = colors.mealCardBorder,
                                disabledBorderColor = colors.mealCardBorder
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                    }
                )
            }
        )

        IconButton(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(44.dp),
            enabled = canSend,
            onClick = {
                focusManager.clearFocus()
                onSendClick()
            }
        ) {
            Icon(
                imageVector = TablerIcons.Send,
                contentDescription = "Отправить",
                tint = if (canSend) colors.accentColor else colors.secondaryText
            )
        }
    }
}
