package bob.colbaskin.cookly.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.chat.domain.models.ChatSender
import bob.colbaskin.cookly.chat.presentation.components.ChatInputBar
import bob.colbaskin.cookly.chat.presentation.components.ChatMessageBubble
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.navigation.Screens

@Composable
fun ChatScreenRoot(
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    ChatScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is ChatAction.OpenRecipe -> {
                    navController.navigate(Screens.RecipeDetailed(action.recipeId))
                }
                else -> Unit
            }

            viewModel.onAction(action)
        }
    )
}

@Composable
private fun ChatScreen(
    state: ChatState,
    onAction: (ChatAction) -> Unit
) {
    val colors = CustomTheme.colors
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.background,
        contentColor = colors.text,
        bottomBar = {
            ChatInputBar(
                modifier = Modifier.padding(bottom = 48.dp),
                value = state.inputText,
                enabled = !state.isSending,
                onValueChange = { onAction(ChatAction.ChangeInputText(it)) },
                onSendClick = { onAction(ChatAction.SendMessage) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 48.dp
            )
        ) {
            itemsIndexed(
                items = state.messages,
                key = { _, message -> message.id }
            ) { index, message ->
                val nextMessage = state.messages.getOrNull(index + 1)
                val showChefAvatar =
                    message.sender == ChatSender.Chef &&
                            nextMessage?.sender != ChatSender.Chef

                ChatMessageBubble(
                    message = message,
                    showChefAvatar = showChefAvatar,
                    onRecipeClick = { recipeId ->
                        onAction(ChatAction.OpenRecipe(recipeId))
                    }
                )
            }
        }
    }
}
