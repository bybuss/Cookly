package bob.colbaskin.cookly.shopping_cart.presentation

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.home.data.models.recipe_detailed.formatQuantity
import bob.colbaskin.cookly.shopping_cart.domain.models.CartIngredient
import compose.icons.TablerIcons
import compose.icons.tablericons.Edit
import compose.icons.tablericons.Trash

@Composable
fun ShoppingCartScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ShoppingCartViewModel = hiltViewModel()
) {
    val state = viewModel.state

    ShoppingCartScreen(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShoppingCartScreen(
    modifier: Modifier = Modifier,
    state: ShoppingCartState,
    onAction: (ShoppingCartAction) -> Unit
) {
    val colors = CustomTheme.colors

    Scaffold(
        containerColor = colors.background,
        contentColor = colors.text
    ) { innerPadding ->
        when (val cartState = state.cartState) {
            is UiState.Error -> {
                CartErrorContent(
                    modifier = modifier.padding(innerPadding),
                    title = cartState.title,
                    text = cartState.text
                )
            }
            is UiState.Success -> {
                CartContent(
                    modifier = modifier.padding(innerPadding),
                    ingredients = cartState.data,
                    isActionLoading = state.actionState is UiState.Loading,
                    onAction = onAction
                )
            }
            else -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.accentColor)
                }
            }
        }
    }
    state.editIngredient?.let { ingredient ->
        EditCartIngredientBottomSheet(
            ingredient = ingredient,
            quantity = state.editQuantity,
            isLoading = state.actionState is UiState.Loading,
            onDismiss = { onAction(ShoppingCartAction.CloseEditSheet) },
            onQuantityChange = {
                onAction(ShoppingCartAction.UpdateEditingQuantity(it))
            },
            onSave = { onAction(ShoppingCartAction.SaveEditingIngredient) }
        )
    }
}

@Composable
private fun CartContent(
    modifier: Modifier = Modifier,
    ingredients: List<CartIngredient>,
    isActionLoading: Boolean,
    onAction: (ShoppingCartAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (ingredients.isEmpty()) {
            EmptyCartContent(
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = ingredients,
                    key = { it.cartKey }
                ) { ingredient ->
                    CartIngredientCard(
                        ingredient = ingredient,
                        onEdit = {
                            onAction(ShoppingCartAction.OpenEditSheet(ingredient))
                        },
                        onDelete = {
                            onAction(ShoppingCartAction.DeleteIngredient(ingredient.cartKey))
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { onAction(ShoppingCartAction.ClearCart) },
                enabled = !isActionLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.likeColor,
                    contentColor = Color.White
                )
            ) {
                if (isActionLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }

                Text("Очистить корзину")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CartIngredientCard(
    ingredient: CartIngredient,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = CustomTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colors.secondaryCardBackground,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onEdit)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = ingredient.title,
                style = CustomTheme.typography.inter.titleMedium,
                color = colors.text
            )
            Text(
                text = "${ingredient.quantity.formatQuantity()} ${ingredient.unitMeasurement}",
                style = CustomTheme.typography.inter.bodyMedium,
                color = colors.tertiaryText
            )
        }
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = TablerIcons.Edit,
                contentDescription = null,
                tint = colors.text
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = TablerIcons.Trash,
                contentDescription = null,
                tint = colors.likeColor
            )
        }
    }
}

@Composable
private fun EmptyCartContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Корзина пуста",
            style = CustomTheme.typography.inter.headlineSmall,
            color = CustomTheme.colors.text,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Добавьте ингредиенты из рецепта, и они появятся здесь.",
            style = CustomTheme.typography.inter.bodyMedium,
            color = CustomTheme.colors.tertiaryText,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CartErrorContent(
    modifier: Modifier = Modifier,
    title: String,
    text: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.inter.headlineSmall,
            color = CustomTheme.colors.text,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            style = CustomTheme.typography.inter.bodyMedium,
            color = CustomTheme.colors.tertiaryText,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditCartIngredientBottomSheet(
    ingredient: CartIngredient,
    quantity: String,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onQuantityChange: (String) -> Unit,
    onSave: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Редактировать ингредиент",
                style = typography.inter.titleLarge,
                color = colors.text
            )
            Text(
                text = ingredient.title,
                style = typography.inter.titleLarge,
                color = colors.text
            )
            OutlinedTextField(
                value = quantity,
                onValueChange = onQuantityChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Количество") },
                suffix = { Text(ingredient.unitMeasurement) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.text,
                    unfocusedTextColor = colors.tertiaryText,
                    focusedContainerColor = colors.background,
                    unfocusedContainerColor = colors.background,
                    focusedBorderColor = colors.mealCardBorder,
                    unfocusedBorderColor = colors.strokeColor,
                    focusedLabelColor = colors.text,
                    unfocusedLabelColor = colors.tertiaryText,
                    cursorColor = colors.accentColor,
                    focusedSuffixColor = colors.tertiaryText,
                    unfocusedSuffixColor = colors.tertiaryText
                )
            )
            Button(
                onClick = onSave,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accentColor,
                    contentColor = colors.invertedText
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = colors.invertedText,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Text("Сохранить")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShoppingCartScreenPreview() {
    UfoodTheme {
        CartContent(
            ingredients = listOf(
                CartIngredient(
                    cartKey = "asfds",
                    ingredientId = 1,
                    title = "as",
                    quantity = 123.0,
                    unitMeasurement = "г",
                    sourceRecipeId = 1
                ),
                CartIngredient(
                    cartKey = "asfdsf",
                    ingredientId = 2,
                    title = "aqwds",
                    quantity = 13.0,
                    unitMeasurement = "г",
                    sourceRecipeId = 1
                ),
                CartIngredient(
                    cartKey = "asfdsqaaaad",
                    ingredientId = 3,
                    title = "a2ds",
                    quantity = 23.0,
                    unitMeasurement = "г",
                    sourceRecipeId = 1
                ),
            ),
            isActionLoading = false,
            onAction = {}
        )
    }
}
