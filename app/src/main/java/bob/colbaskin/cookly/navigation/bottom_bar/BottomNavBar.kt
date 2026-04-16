package bob.colbaskin.cookly.navigation.bottom_bar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
            .fillMaxWidth()
            .height(118.dp)
            .clip(BottomNavCurve()),
        windowInsets = NavigationBarDefaults.windowInsets,
        contentColor = CustomTheme.colors.text,
        containerColor = CustomTheme.colors.background,
    ) {
        Destinations.entries.forEach { destination ->
            val selected: Boolean = currentDestination?.hierarchy?.any {
                it.hasRoute(destination.screen::class)
            } == true

            val labelAlpha: Float = animateFloatAsState(
                targetValue = if (selected) 1f else 0f,
                label = "BottomBarLabelAlpha"
            ).value

            NavigationBarItem(
                modifier = Modifier.offset(y = 25.dp),
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
                    if (destination == Destinations.CHAT) {
                        Image(
                            painter = painterResource(
                                if (selected) destination.filledIcon else destination.outlinedIcon
                            ),
                            contentDescription = stringResource(destination.label),
                            modifier = Modifier
                                .size(72.dp)
                                .padding(bottom = 8.dp)
                                .offset(y = (-15).dp)
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
                            painter = painterResource(
                                if (selected) destination.filledIcon else destination.outlinedIcon
                            ),
                            contentDescription = stringResource(destination.label),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(destination.label),
                        modifier = Modifier
                            .alpha(labelAlpha)
                            .then(
                                if (destination == Destinations.CHAT) {
                                    Modifier.offset(y = (-25).dp)
                                } else Modifier
                            ),
                        style = CustomTheme.typography.inter.bodySmall,
                        maxLines = 1
                    )
                },
                alwaysShowLabel = true,
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
