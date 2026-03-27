package bob.colbaskin.cookly.agreement.presentation.terms_of_use


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfUseScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Правила размещения рецептов",
                        style = CustomTheme.typography.inter.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.carret_left),
                            modifier = Modifier.scale(2f),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.background,
                    navigationIconContentColor = CustomTheme.colors.text,
                    titleContentColor = CustomTheme.colors.text
                )
            )
        },
        containerColor = CustomTheme.colors.background,
        contentColor = CustomTheme.colors.text
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(1) {
                Text(
                    text = """
                        1. Я подтверждаю, что все права на материалы размещаемого мной рецепта принадлежат мне.
                        2. В случае нарушения я готов(а) понести наказание со стороны администрации.
                        3. Я несу ответственность за соблюдение авторского права.
                        4. Передаю исключительные права на использование рецепта приложению Coockly.
                        5. После публикации рецепт может быть модифицирован или удален администрацией.
                        6. Приложение обеспечивает защиту моих авторских прав.
                        7. Ознакомлен(а) с правилами удаления постов рецептов.

                        ПРАВИЛА УДАЛЕНИЯ РЕЦЕПТОВ

                        1. Рецепт не понятен для читателя, содержит сложный текст или не подлежит модерации.
                        2. Рецепт неполный или не пригоден для приготовления.
                        3. Плохое качество фотографий: перевернуто, размыто, не фокус.
                        4. Использование нецензурных слов или реклама.
                        5. Рецепт с отрицательным рейтингом может быть удален.
                        6. Несоблюдение правил размещения рецептов на проекте Coockly.
                        7. Фото сгенерированы ИИ нейросетью.
                    """.trimIndent(),
                    style = CustomTheme.typography.inter.bodyMedium
                )
            }
        }
    }
}
