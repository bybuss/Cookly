package bob.colbaskin.cookly.profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.components.ProfileAvatar
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.common.utils.getFirstLetter
import bob.colbaskin.cookly.navigation.Screens
import bob.colbaskin.cookly.navigation.graphs.Graphs
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.ClipboardList
import compose.icons.tablericons.Clock
import compose.icons.tablericons.FileText
import compose.icons.tablericons.Logout
import compose.icons.tablericons.Settings
import compose.icons.tablericons.ToolsKitchen
import kotlinx.coroutines.launch

@Composable
fun ProfileScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.logoutState) {
        when (val logoutState = state.logoutState) {
            is UiState.Success -> {
                navController.navigate(Graphs.Auth) {
                    popUpTo (Screens.Profile) { inclusive = true }
                }
            }
            is UiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = logoutState.title,
                        duration = SnackbarDuration.Short
                    )
                }
                viewModel.onAction(ProfileAction.DismissError)
            }
            else -> Unit
        }
    }

    ProfileScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                ProfileAction.OpenApplicationsReview -> {
                    navController.navigate(Screens.ApplicationsReview)
                }
                ProfileAction.OpenCookingHistory -> {
                    //navController.navigate(Screens.CookingHistory)
                    navController.navigate(Screens.RecipeDetailed(recipeId = 67))
                }
                ProfileAction.OpenPreferencesAndAllergies -> {
                    navController.navigate(Graphs.Onboarding)
                }
                ProfileAction.OpenRecipeStatuses -> {
                    navController.navigate(Screens.RecipeStatuses)
                }
                ProfileAction.OpenCreateRecipe -> {
                    navController.navigate(Screens.CreateRecipe)
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CustomTheme.colors.background)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ProfileHeader(
                state = state,
                onLogout = { onAction(ProfileAction.Logout) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (state.isModeratorOrAdmin) {
                    ProfileMenuItem(
                        title = "Разбор заявок",
                        icon = {
                            Icon(
                                imageVector = TablerIcons.ClipboardList,
                                contentDescription = null,
                                tint = CustomTheme.colors.accentColor
                            )
                        },
                        onClick = { onAction(ProfileAction.OpenApplicationsReview) }
                    )
                }

                ProfileMenuItem(
                    title = "История приготовленных блюд",
                    icon = {
                        Icon(
                            imageVector = TablerIcons.Clock,
                            contentDescription = null,
                            tint = CustomTheme.colors.accentColor
                        )
                    },
                    onClick = { onAction(ProfileAction.OpenCookingHistory) }
                )

                ProfileMenuItem(
                    title = "Предпочтения и аллергены",
                    icon = {
                        Icon(
                            imageVector = TablerIcons.Settings,
                            contentDescription = null,
                            tint = CustomTheme.colors.accentColor
                        )
                    },
                    onClick = { onAction(ProfileAction.OpenPreferencesAndAllergies) }
                )

                ProfileMenuItem(
                    title = "Статусы моих рецептов",
                    icon = {
                        Icon(
                            imageVector = TablerIcons.FileText,
                            contentDescription = null,
                            tint = CustomTheme.colors.accentColor
                        )
                    },
                    onClick = { onAction(ProfileAction.OpenRecipeStatuses) }
                )

                ProfileMenuItem(
                    title = "Создание своего рецепта",
                    icon = {
                        Icon(
                            imageVector = TablerIcons.ToolsKitchen,
                            contentDescription = null,
                            tint = CustomTheme.colors.accentColor
                        )
                    },
                    onClick = { onAction(ProfileAction.OpenCreateRecipe) }
                )
            }
        }

        if (state.isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 168.dp)
                    .size(22.dp),
                color = CustomTheme.colors.accentColor,
                strokeWidth = 2.dp
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    state: ProfileState,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                color = Color(0xFF1F1F1F),
                shape = RoundedCornerShape(
                    bottomStart = 28.dp,
                    bottomEnd = 28.dp
                )
            )
            .padding(horizontal = 24.dp)
    ) {
        TextButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onLogout
        ) {
            Icon(
                imageVector = TablerIcons.Logout,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = "Выйти",
                style = CustomTheme.typography.inter.bodyMedium,
                color = Color.White
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .padding(top = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(
                avatarUrl = state.avatarUrl,
                fallbackLetter = state.email.getFirstLetter(),
                avatarSize = 80.dp
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = state.displayName,
                    style = CustomTheme.typography.inter.titleLarge,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = state.email.ifBlank { "user@example.com" },
                    style = CustomTheme.typography.inter.bodyMedium,
                    color = Color(0xFFBEBEBE),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    title: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = CustomTheme.colors.secondaryCardBackground,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = CustomTheme.typography.inter.bodyMedium,
            color = CustomTheme.colors.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            imageVector = TablerIcons.ChevronRight,
            contentDescription = null,
            tint = CustomTheme.colors.tertiaryText,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    UfoodTheme {
        ProfileScreen(
            state = ProfileState(),
            onAction = {}
        )
    }
}
