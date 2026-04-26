package bob.colbaskin.cookly.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.InfoCircle

@Composable
fun CustomSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = {data ->
            CustomSnackbarContent(snackbarData = data)
        }
    )
}

@Composable
private fun CustomSnackbarContent(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    val colors = CustomTheme.colors

    Surface(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(10.dp),
        color = colors.statsCardBackground
    ) {
        Row(
            modifier = modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = TablerIcons.InfoCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = colors.cardBackground
            )
            Text(
                text = snackbarData.visuals.message,
                modifier = Modifier.weight(1f),
                color = colors.text,
                style = CustomTheme.typography.inter.bodyMedium,
                textAlign = TextAlign.Start
            )
        }
    }
}
