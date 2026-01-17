package bob.colbaskin.ufood.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bob.colbaskin.ufood.R
import bob.colbaskin.ufood.common.design_system.theme.CustomTheme
import bob.colbaskin.ufood.common.design_system.theme.UfoodTheme

@Composable
fun TopBarWithSearch(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(49.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_main_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(37.dp)
            )
            Text(
                text = "UFOOD",
                style = CustomTheme.typography.madeInfinity.headlineSmall,
                color = CustomTheme.colors.text
            )
        }
        Image(
            painter = painterResource(R.drawable.search_ic),
            contentDescription = null,
            colorFilter = ColorFilter.tint(CustomTheme.colors.text)
        )
    }
}

@Preview
@Composable
fun TopBarWithSearchPreview(modifier: Modifier = Modifier) {
    UfoodTheme {
        TopBarWithSearch()
    }
}
