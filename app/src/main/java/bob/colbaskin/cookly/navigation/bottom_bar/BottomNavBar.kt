package bob.colbaskin.cookly.navigation.bottom_bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.navigation.Destinations

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar(
        modifier = modifier
            .fillMaxWidth(1F)
            .height(118.dp)
            .clip(BottomNavCurve()),
        contentColor = CustomTheme.colors.text,
        containerColor = CustomTheme.colors.background,
    ) {
        Destinations.entries.forEach { destination ->
            val selected: Boolean = currentDestination?.hierarchy?.any {
                it.hasRoute(destination.screen::class)
            } == true

            NavigationBarItem(
                modifier = Modifier
                    .padding(top = 16.dp),
                selected = selected,
                onClick = {
                    navController.navigate(destination.screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if (destination.icon == R.drawable.chat_ic) {
                        Image(
                            painter = painterResource(destination.icon),
                            contentDescription = stringResource(destination.label),
                            modifier = Modifier
                                .padding(bottom = 32.dp)
                                .size(72.dp)
                                .dropShadow(
                                    shape = CircleShape,
                                    shadow = Shadow(
                                        radius = 12.dp,
                                        color = CustomTheme.colors.accentColor.copy(alpha = 0.5F),
                                        offset = DpOffset(x = 0.dp, y = 8.dp)
                                    )
                                )
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = stringResource(destination.label),
                            modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(destination.label),
                        style = CustomTheme.typography.inter.bodySmall
                    )
                },
                alwaysShowLabel = selected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CustomTheme.colors.accentColor,
                    selectedTextColor = CustomTheme.colors.accentColor,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = CustomTheme.colors.bottomBarIcon,
                    unselectedTextColor = CustomTheme.colors.bottomBarIcon,
                    disabledIconColor = CustomTheme.colors.bottomBarIcon,
                    disabledTextColor = CustomTheme.colors.bottomBarIcon,
                )
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    UfoodTheme {
        val navController: NavHostController = rememberNavController()
        BottomNavBar(navController = navController)
    }
}
