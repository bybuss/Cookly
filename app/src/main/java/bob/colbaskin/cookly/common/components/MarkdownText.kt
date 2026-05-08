package bob.colbaskin.cookly.common.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = CustomTheme.colors.text,
    style: TextStyle = CustomTheme.typography.inter.bodyMedium,
    codeBackgroundColor: Color = CustomTheme.colors.background,
) {
    Markdown(
        modifier = modifier,
        content = text,
        colors = markdownColor(
            text = color,
            codeBackground = codeBackgroundColor,
            inlineCodeBackground = codeBackgroundColor,
            dividerColor = CustomTheme.colors.mealCardBorder,
            tableBackground = CustomTheme.colors.secondaryCardBackground
        ),
        typography = markdownTypography(
            h1 = CustomTheme.typography.inter.titleLarge,
            h2 = CustomTheme.typography.inter.titleMedium,
            h3 = CustomTheme.typography.inter.titleSmall,
            h4 = CustomTheme.typography.inter.bodyLarge,
            h5 = CustomTheme.typography.inter.bodyMedium,
            h6 = CustomTheme.typography.inter.bodySmall,

            text = style,
            paragraph = style,
            ordered = style,
            bullet = style,
            list = style,
            quote = style,

            code = CustomTheme.typography.inter.bodySmall,
            inlineCode = CustomTheme.typography.inter.bodySmall
        )
    )
}
