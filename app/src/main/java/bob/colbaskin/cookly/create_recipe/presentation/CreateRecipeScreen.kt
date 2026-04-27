package bob.colbaskin.cookly.create_recipe.presentation

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.UiState
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeCategory
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeIngredient
import bob.colbaskin.cookly.create_recipe.domain.models.CreateRecipeStep
import bob.colbaskin.cookly.create_recipe.domain.models.LocalImage
import bob.colbaskin.cookly.create_recipe.domain.models.PhotoTarget
import bob.colbaskin.cookly.home.domain.models.MealTimeType
import coil3.compose.rememberAsyncImagePainter
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronDown
import compose.icons.tablericons.ChevronUp
import compose.icons.tablericons.Photo
import compose.icons.tablericons.Plus
import compose.icons.tablericons.Trash
import kotlinx.coroutines.launch

@Composable
fun CreateRecipeScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: CreateRecipeViewModel = hiltViewModel(),
    onNavigateToSuccess: (Int) -> Unit = {}
) {
    val state = viewModel.state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var photoTarget by remember { mutableStateOf<PhotoTarget?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val image = uri?.let { context.toLocalImageUi(it) }

        when (val target = photoTarget) {
            PhotoTarget.Main -> {
                viewModel.onAction(CreateRecipeAction.SetMainPhoto(image))
            }

            is PhotoTarget.Step -> {
                viewModel.onAction(
                    CreateRecipeAction.SetStepPhoto(
                        stepLocalId = target.stepLocalId,
                        image = image
                    )
                )
            }

            null -> Unit
        }

        photoTarget = null
    }

    LaunchedEffect(state.submitState) {
        when (val submitState = state.submitState) {
            is UiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = submitState.title,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is UiState.Success -> {
                onNavigateToSuccess(submitState.data)
                viewModel.onAction(CreateRecipeAction.ConsumeSuccess)
            }

            else -> Unit
        }
    }

    CreateRecipeScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                CreateRecipeAction.Back -> navController.popBackStack()
                else -> viewModel.onAction(action)
            }
        },
        onPickMainPhoto = {
            photoTarget = PhotoTarget.Main
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onPickStepPhoto = { stepLocalId ->
            photoTarget = PhotoTarget.Step(stepLocalId)
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateRecipeScreen(
    modifier: Modifier = Modifier,
    state: CreateRecipeState,
    onAction: (CreateRecipeAction) -> Unit,
    onPickMainPhoto: () -> Unit,
    onPickStepPhoto: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Оформление рецепта",
                        style = CustomTheme.typography.inter.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(CreateRecipeAction.Back) }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_left),
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.background,
                    titleContentColor = CustomTheme.colors.text,
                    navigationIconContentColor = CustomTheme.colors.text
                )
            )
        },
        containerColor = CustomTheme.colors.background
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(CustomTheme.colors.background)
                .padding(innerPadding)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RequiredFieldLabel(text = "Фотография готового блюда")
                    PhotoBlock(
                        image = state.mainPhoto,
                        onUpload = onPickMainPhoto,
                        onRemove = { onAction(CreateRecipeAction.RemoveMainPhoto) }
                    )
                }
            }

            item {
                RequiredFieldLabel(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Основная информация"
                )
            }

            item {
                FormTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Название рецепта",
                    value = state.title,
                    placeholder = "Введите название блюда",
                    isRequired = true,
                    onValueChange = {
                        onAction(CreateRecipeAction.UpdateTitle(it))
                    }
                )
            }

            item {
                FormTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Описание рецепта",
                    value = state.description,
                    placeholder = "Краткое описание блюда",
                    singleLine = false,
                    isRequired = true,
                    minLines = 4,
                    onValueChange = {
                        onAction(CreateRecipeAction.UpdateDescription(it))
                    }
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RequiredFieldLabel(text = "Время приготовления")
                    OutlinedButton(
                        onClick = { onAction(CreateRecipeAction.OpenTimePicker) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = state.estimatedTimeLabel(),
                            color = CustomTheme.colors.text
                        )
                    }
                }
            }

            item {
                FormTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Калории на 100 грамм",
                    value = state.caloriesBy100Grams,
                    placeholder = "Например, 250",
                    keyboardType = KeyboardType.Number,
                    isRequired = true,
                    onValueChange = {
                        onAction(CreateRecipeAction.UpdateCaloriesBy100Grams(it))
                    }
                )
            }

            item {
                MealTimeSelector(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    selectedMealTimeType = state.mealTimeType,
                    onSelect = {
                        onAction(CreateRecipeAction.SelectMealTime(it))
                    }
                )
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RequiredFieldLabel(text = "Категории кухни")
                    AddItemTextButton(
                        text = "Добавить категории",
                        onClick = { onAction(CreateRecipeAction.ShowCategorySheet) }
                    )
                    if (state.categories.isEmpty()) {
                        HelperText("Категории пока не добавлены.")
                    } else {
                        state.categories.forEach { category ->
                            CategoryRow(
                                category = category,
                                onDelete = {
                                    onAction(
                                        CreateRecipeAction.RemoveCategory(category.categoryId)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RequiredFieldLabel(text = "Ингредиенты")
                    AddItemTextButton(
                        text = "Добавить ингредиент",
                        onClick = { onAction(CreateRecipeAction.ShowIngredientSheet) }
                    )
                    if (state.ingredients.isEmpty()) {
                        HelperText("Добавьте хотя бы один ингредиент.")
                    } else {
                        state.ingredients.forEachIndexed { index, ingredient ->
                            IngredientRow(
                                ingredient = ingredient,
                                canMoveUp = index > 0,
                                canMoveDown = index < state.ingredients.lastIndex,
                                onClick = {
                                    onAction(CreateRecipeAction.ShowEditIngredientSheet(ingredient))
                                },
                                onMoveUp = {
                                    onAction(
                                        CreateRecipeAction.MoveIngredient(
                                            fromIndex = index,
                                            toIndex = index - 1
                                        )
                                    )
                                },
                                onMoveDown = {
                                    onAction(
                                        CreateRecipeAction.MoveIngredient(
                                            fromIndex = index,
                                            toIndex = index + 1
                                        )
                                    )
                                },
                                onDelete = {
                                    onAction(
                                        CreateRecipeAction.RemoveIngredient(
                                            ingredientId = ingredient.ingredientId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Пошаговая инструкция",
                        style = CustomTheme.typography.inter.titleLarge,
                        color = CustomTheme.colors.text
                    )
                }
            }

            itemsIndexed(
                items = state.steps,
                key = { _, item -> item.localId }
            ) { index, step ->
                StepCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    step = step,
                    canMoveUp = index > 0,
                    canMoveDown = index < state.steps.lastIndex,
                    onStepTitleChange = {
                        onAction(
                            CreateRecipeAction.UpdateStepTitle(
                                stepLocalId = step.localId,
                                value = it
                            )
                        )
                    },
                    onDescriptionChange = {
                        onAction(
                            CreateRecipeAction.UpdateStepDescription(
                                stepLocalId = step.localId,
                                value = it
                            )
                        )
                    },
                    onPickImage = { onPickStepPhoto(step.localId) },
                    onRemoveImage = {
                        onAction(
                            CreateRecipeAction.SetStepPhoto(
                                stepLocalId = step.localId,
                                image = null
                            )
                        )
                    },
                    onDeleteStep = {
                        onAction(CreateRecipeAction.RemoveStep(step.localId))
                    },
                    onMoveUp = {
                        onAction(
                            CreateRecipeAction.MoveStep(
                                fromIndex = index,
                                toIndex = index - 1
                            )
                        )
                    },
                    onMoveDown = {
                        onAction(
                            CreateRecipeAction.MoveStep(
                                fromIndex = index,
                                toIndex = index + 1
                            )
                        )
                    }
                )
            }

            item {
                AddItemTextButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Добавить шаг",
                    onClick = { onAction(CreateRecipeAction.AddStep) }
                )
            }

            item {
                Button(
                    onClick = { onAction(CreateRecipeAction.Submit) },
                    enabled = !state.isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomTheme.colors.accentColor,
                        contentColor = Color.White,
                        disabledContainerColor = CustomTheme.colors.accentColor.copy(alpha = 0.5f),
                        disabledContentColor = CustomTheme.colors.strokeColor
                    )
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }

                    Text("Отправить рецепт")
                }
            }
        }
    }

    if (state.isTimePickerVisible) {
        CreateRecipeTimePickerDialog(
            initialHour = state.estimatedHour,
            initialMinute = state.estimatedMinute,
            onDismiss = { onAction(CreateRecipeAction.DismissTimePicker) },
            onConfirm = { hour, minute ->
                onAction(CreateRecipeAction.ConfirmTime(hour, minute))
            }
        )
    }

    if (state.isCategorySheetVisible) {
        CategoryPickerBottomSheet(
            state = state,
            onDismiss = { onAction(CreateRecipeAction.HideCategorySheet) },
            onSave = { categories ->
                onAction(CreateRecipeAction.SetCategories(categories))
            }
        )
    }

    if (state.isIngredientSheetVisible) {
        IngredientPickerBottomSheet(
            state = state,
            onDismiss = { onAction(CreateRecipeAction.HideIngredientSheet) },
            onSearch = { query ->
                onAction(CreateRecipeAction.SearchIngredients(query))
            },
            onSave = { ingredient ->
                onAction(CreateRecipeAction.SaveIngredient(ingredient))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryPickerBottomSheet(
    state: CreateRecipeState,
    onDismiss: () -> Unit,
    onSave: (List<CreateRecipeCategory>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedIds = remember(state.categories) {
        mutableStateListOf<Int>().apply {
            addAll(state.categories.map { it.categoryId })
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CustomTheme.colors.background
    ) {
        FullHeightSheetContainer {
            Text(
                text = "Выберите категории",
                style = CustomTheme.typography.inter.titleLarge,
                color = CustomTheme.colors.text
            )

            when {
                state.isCategoriesLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CustomTheme.colors.accentColor)
                    }
                }

                state.categoriesError != null -> {
                    ErrorText(text = state.categoriesError)
                }

                state.availableCategories.isEmpty() -> {
                    HelperText("Категории не найдены.")
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.availableCategories,
                            key = { it.categoryId }
                        ) { category ->
                            val checked = category.categoryId in selectedIds

                            SelectableRow(
                                title = category.title,
                                checked = checked,
                                onClick = {
                                    if (checked) {
                                        selectedIds.remove(category.categoryId)
                                    } else {
                                        selectedIds.add(category.categoryId)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val selected = state.availableCategories.filter {
                        it.categoryId in selectedIds
                    }
                    onSave(selected)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.accentColor,
                    contentColor = CustomTheme.colors.invertedText
                )
            ) {
                Text("Сохранить")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IngredientPickerBottomSheet(
    state: CreateRecipeState,
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit,
    onSave: (CreateRecipeIngredient) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val editingIngredient = state.editingIngredient
    val isEditMode = editingIngredient != null

    var selectedIngredient by remember(editingIngredient?.ingredientId) {
        mutableStateOf(editingIngredient)
    }

    var quantity by rememberSaveable(editingIngredient?.ingredientId) {
        mutableStateOf(editingIngredient?.quantity.orEmpty())
    }

    var localError by rememberSaveable(editingIngredient?.ingredientId) {
        mutableStateOf<String?>(null)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CustomTheme.colors.background
    ) {
        FullHeightSheetContainer {
            Text(
                text = if (isEditMode) {
                    "Редактировать ингредиент"
                } else {
                    "Добавить ингредиент"
                },
                style = CustomTheme.typography.inter.titleLarge,
                color = CustomTheme.colors.text
            )
            FormTextField(
                title = "Поиск",
                value = state.ingredientSearchQuery,
                placeholder = "Например, картофель",
                onValueChange = { query ->
                    selectedIngredient = null
                    localError = null
                    onSearch(query)
                }
            )

            selectedIngredient?.let { ingredient ->
                Text(
                    text = "Выбран ингредиент: ${ingredient.title}",
                    style = CustomTheme.typography.inter.bodyMedium,
                    color = CustomTheme.colors.text
                )
                Text(
                    text = "Единица измерения: ${ingredient.unitMeasurement}",
                    style = CustomTheme.typography.inter.bodyMedium,
                    color = CustomTheme.colors.tertiaryText
                )
                FormTextField(
                    title = "Количество",
                    value = quantity,
                    placeholder = "Например, 250",
                    keyboardType = KeyboardType.Decimal,
                    isRequired = true,
                    onValueChange = { value ->
                        quantity = value
                            .replace(",", ".")
                            .filter { char -> char.isDigit() || char == '.' }
                            .take(10)
                    }
                )
            }
            localError?.let { ErrorText(text = it) }
            when {
                state.isIngredientSearchLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = CustomTheme.colors.accentColor
                        )
                    }
                }
                state.ingredientSearchError != null -> {
                    ErrorText(text = state.ingredientSearchError)
                    Spacer(modifier = Modifier.weight(1f))
                }
                state.ingredientSearchQuery.isBlank() -> {
                    HelperText("Введите название ингредиента для поиска.")
                    Spacer(modifier = Modifier.weight(1f))
                }
                state.ingredientSearchResults.isEmpty() -> {
                    if (selectedIngredient == null) {
                        HelperText("Ничего не найдено.")
                    } else {
                        HelperText("Можно изменить количество или найти другой ингредиент.")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.ingredientSearchResults,
                            key = { it.ingredientId }
                        ) { ingredient ->
                            val checked = selectedIngredient?.ingredientId == ingredient.ingredientId
                            SelectableRow(
                                title = ingredient.title,
                                subtitle = "Единица: ${ingredient.unitMeasurement}",
                                checked = checked,
                                onClick = {
                                    selectedIngredient = ingredient.copy(
                                        quantity = quantity
                                    )
                                    localError = null
                                },
                                isIngredientSelect = true
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val ingredient = selectedIngredient
                    val parsedQuantity
                        = quantity.replace(",", ".").toDoubleOrNull()
                    when {
                        ingredient == null -> {
                            localError = "Выберите ингредиент из списка."
                        }
                        parsedQuantity == null || parsedQuantity <= 0.0 -> {
                            localError = "Укажите корректное количество."
                        }
                        parsedQuantity > 100_000.0 -> {
                            localError = "Количество не должно быть больше 100000."
                        }
                        else -> {
                            onSave(
                                ingredient.copy(
                                    quantity = quantity.trim()
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.accentColor,
                    contentColor = CustomTheme.colors.invertedText
                )
            ) {
                Text(text = if (isEditMode) "Сохранить изменения" else "Добавить")
            }
        }
    }
}

@Composable
private fun FullHeightSheetContainer(
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val sheetHeight = configuration.screenHeightDp.dp * 0.88f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(sheetHeight)
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
private fun SelectableRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onClick: () -> Unit,
    isIngredientSelect: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CustomTheme.colors.secondaryCardBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isIngredientSelect) {
            RadioButton(
                selected = checked,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = CustomTheme.colors.accentColor,
                    unselectedColor = CustomTheme.colors.secondaryText
                )
            )
        } else {
            Checkbox(
                checked = checked,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = CustomTheme.colors.accentColor,
                    uncheckedColor = CustomTheme.colors.secondaryText,
                    checkmarkColor = CustomTheme.colors.invertedText
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = CustomTheme.typography.inter.titleSmall,
                color = CustomTheme.colors.text
            )
            subtitle?.let { text ->
                Text(
                    text = text,
                    style = CustomTheme.typography.inter.bodyMedium,
                    color = CustomTheme.colors.tertiaryText
                )
            }
        }
    }
}

@Composable
private fun RequiredFieldLabel(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = CustomTheme.typography.inter.titleLarge
) {
    val colors = CustomTheme.colors

    Text(
        text = buildAnnotatedString {
            append(text)
            withStyle(
                style = SpanStyle(color = colors.flameColor)
            ) {
                append(" *")
            }
        },
        modifier = modifier,
        style = style,
        color = colors.text
    )
}

@Composable
private fun FormTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    isRequired: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (isRequired) {
            RequiredFieldLabel(
                text = title,
                style = typography.inter.bodyMedium
            )
        } else {
            Text(
                text = title,
                style = typography.inter.bodyMedium,
                color = colors.text
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            singleLine = singleLine,
            minLines = minLines,
            placeholder = {
                Text(
                    text = placeholder,
                    color = colors.tertiaryText
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun PhotoBlock(
    image: LocalImage?,
    onUpload: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CustomTheme.colors.secondaryCardBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (image == null) {
            Icon(
                imageVector = TablerIcons.Photo,
                contentDescription = null,
                tint = CustomTheme.colors.tertiaryText,
                modifier = Modifier.size(48.dp)
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(model = image.uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Button(
            onClick = onUpload,
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.accentColor,
                contentColor = CustomTheme.colors.invertedText
            )
        ) {
            Text(if (image == null) "Загрузить фото" else "Заменить фото")
        }

        if (image != null) {
            TextButton(
                onClick = onRemove,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = CustomTheme.colors.text
                )
            ) {
                Text("Удалить")
            }
        }
    }
}

@Composable
private fun MealTimeSelector(
    selectedMealTimeType: MealTimeType?,
    onSelect: (MealTimeType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RequiredFieldLabel(
            text = "Тип блюда",
            style = CustomTheme.typography.inter.bodyMedium
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = MealTimeType.entries,
                key = { it.name }
            ) { mealTimeType ->
                MealTimeCard(
                    title = mealTimeType.title,
                    isSelected = selectedMealTimeType == mealTimeType,
                    onClick = { onSelect(mealTimeType) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MealTimeCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    val backgroundColor =
        if (isSelected) colors.secondAccentColor else colors.secondaryCardBackground

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            text = title,
            style = typography.inter.bodyMedium,
            color = colors.text
        )
    }
}

@Composable
private fun CategoryRow(
    category: CreateRecipeCategory,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CustomTheme.colors.ingredientSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.title,
                style = CustomTheme.typography.inter.titleMedium,
                color = CustomTheme.colors.text
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = TablerIcons.Trash,
                contentDescription = null,
                tint = CustomTheme.colors.text
            )
        }
    }
}

@Composable
private fun IngredientRow(
    ingredient: CreateRecipeIngredient,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onClick: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colors.ingredientSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = ingredient.title,
                style = typography.inter.titleMedium,
                color = colors.text
            )
            Text(
                text = "${ingredient.quantity} ${ingredient.unitMeasurement}",
                style = typography.inter.titleMedium,
                color = colors.tertiaryText
            )
        }
        IconButton(
            onClick = onMoveUp,
            enabled = canMoveUp
        ) {
            Icon(
                imageVector = TablerIcons.ChevronUp,
                contentDescription = null,
                tint = colors.text,
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(
            onClick = onMoveDown,
            enabled = canMoveDown
        ) {
            Icon(
                imageVector = TablerIcons.ChevronDown,
                contentDescription = null,
                tint = colors.text,
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = TablerIcons.Trash,
                contentDescription = null,
                tint = colors.text
            )
        }
    }
}

@Composable
private fun StepCard(
    modifier: Modifier = Modifier,
    step: CreateRecipeStep,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onStepTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPickImage: () -> Unit,
    onRemoveImage: () -> Unit,
    onDeleteStep: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    val colors = CustomTheme.colors
    val typography = CustomTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colors.secondaryCardBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Шаг ${step.number}",
                style = typography.inter.headlineSmall,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onMoveUp,
                enabled = canMoveUp
            ) {
                Icon(
                    imageVector = TablerIcons.ChevronUp,
                    contentDescription = null,
                    tint = colors.text,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(
                onClick = onMoveDown,
                enabled = canMoveDown
            ) {
                Icon(
                    imageVector = TablerIcons.ChevronDown,
                    contentDescription = null,
                    tint = colors.text,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (step.number > 1) {
                IconButton(onClick = onDeleteStep) {
                    Icon(
                        imageVector = TablerIcons.Trash,
                        contentDescription = null,
                        tint = colors.text,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        FormTextField(
            title = "Заголовок шага",
            value = step.title,
            placeholder = "Например, Подготовка ингредиентов",
            isRequired = true,
            onValueChange = onStepTitleChange
        )
        FormTextField(
            title = "Описание шага",
            value = step.description,
            placeholder = "Поддерживает markdown",
            singleLine = false,
            minLines = 5,
            isRequired = true,
            onValueChange = onDescriptionChange
        )
        HelperText("Поле поддерживает markdown.")
        Text(
            text = "Фото шага",
            style = typography.inter.bodyMedium,
            color = colors.text
        )
        step.image?.let {
            Image(
                painter = rememberAsyncImagePainter(model = it.uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onPickImage) {
                Text(if (step.image == null) "Загрузить фото" else "Заменить фото")
            }
            if (step.image != null) {
                TextButton(
                    onClick = onRemoveImage,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = CustomTheme.colors.text
                    )
                ) {
                    Text("Удалить")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateRecipeTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val pickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CustomTheme.colors.background,
        title = {
            Text(
                text = "Время приготовления",
                style = CustomTheme.typography.inter.titleLarge,
                color = CustomTheme.colors.text
            )
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TimePicker(
                    state = pickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = CustomTheme.colors.background,
                        selectorColor = CustomTheme.colors.accentColor,
                        containerColor = CustomTheme.colors.background,
                        clockDialSelectedContentColor = CustomTheme.colors.background,
                        clockDialUnselectedContentColor = CustomTheme.colors.text,
                        timeSelectorSelectedContainerColor = CustomTheme.colors.accentColor,
                        timeSelectorSelectedContentColor = CustomTheme.colors.background,
                        timeSelectorUnselectedContainerColor = CustomTheme.colors.background,
                        timeSelectorUnselectedContentColor = CustomTheme.colors.text
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(pickerState.hour, pickerState.minute)
                }
            ) {
                Text(
                    text = "Готово",
                    color = CustomTheme.colors.accentColor
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Отмена",
                    color = CustomTheme.colors.text
                )
            }
        }
    )
}

@Composable
private fun AddItemTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = CustomTheme.colors.accentColor
        )
    ) {
        Icon(
            imageVector = TablerIcons.Plus,
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))
        Text(text)
    }
}

@Composable
private fun HelperText(text: String) {
    Text(
        text = text,
        style = CustomTheme.typography.inter.bodyMedium,
        color = CustomTheme.colors.tertiaryText
    )
}

@Composable
private fun ErrorText(text: String) {
    Text(
        text = text,
        style = CustomTheme.typography.inter.bodyMedium,
        color = CustomTheme.colors.errorContainer
    )
}

private fun Context.toLocalImageUi(uri: Uri): LocalImage {
    val mimeType = contentResolver.getType(uri)
    var displayName = uri.lastPathSegment ?: "image"
    var sizeBytes: Long? = null

    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

        if (cursor.moveToFirst()) {
            if (nameIndex != -1) {
                displayName = cursor.getString(nameIndex) ?: displayName
            }

            if (sizeIndex != -1 && !cursor.isNull(sizeIndex)) {
                sizeBytes = cursor.getLong(sizeIndex)
            }
        }
    }

    return LocalImage(
        uri = uri,
        displayName = displayName,
        mimeType = mimeType,
        sizeBytes = sizeBytes
    )
}

@Preview
@Composable
private fun CreateRecipeScreenPreview() {
    UfoodTheme {
        CreateRecipeScreen(
            state = CreateRecipeState(),
            onAction = {},
            onPickMainPhoto = {},
            onPickStepPhoto = {}
        )
    }
}
