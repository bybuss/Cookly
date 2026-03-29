package bob.colbaskin.cookly.home.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.Filter

@Composable
fun TopBarWithSearch(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(43.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val interactionSource = remember { MutableInteractionSource() }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = CustomTheme.typography.inter.titleMedium.copy(
                    color = CustomTheme.colors.text,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        placeholder = {
                            Text(
                                text = "Поиск...",
                                color = CustomTheme.colors.mealCardBorder,
                                style = CustomTheme.typography.inter.titleMedium,
                                fontWeight = FontWeight.Normal
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.search_ic),
                                contentDescription = null,
                                tint = CustomTheme.colors.text,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CustomTheme.colors.background,
                            unfocusedContainerColor = CustomTheme.colors.background,
                            focusedBorderColor = CustomTheme.colors.mealCardBorder,
                            unfocusedBorderColor = CustomTheme.colors.mealCardBorder,
                            focusedTextColor = CustomTheme.colors.text,
                            unfocusedTextColor = CustomTheme.colors.text,
                            cursorColor = CustomTheme.colors.text
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 0.dp
                        ),
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CustomTheme.colors.background,
                                    unfocusedContainerColor = CustomTheme.colors.background,
                                    focusedBorderColor = CustomTheme.colors.mealCardBorder,
                                    unfocusedBorderColor = CustomTheme.colors.mealCardBorder
                                ),
                                shape = RoundedCornerShape(42.dp)
                            )
                        }
                    )
                }
            )

            Icon(
                modifier = Modifier
                    .size(26.dp),
                imageVector = TablerIcons.Filter,
                contentDescription = null,
                tint = CustomTheme.colors.text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarWithSearchPreview() {
    UfoodTheme {
        TopBarWithSearch(
            value = "",
            onValueChange = {}
        )
    }
}
