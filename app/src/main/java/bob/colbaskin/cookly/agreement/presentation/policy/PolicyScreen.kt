package bob.colbaskin.cookly.agreement.presentation.policy

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
fun PolicyScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Политика конфиденциальности",
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
                        1. Приложение собирает персональные данные только для работы функционала.
                        2. Все данные хранятся на серверах ФНС России и защищены согласно закону №152-ФЗ.
                        3. Передача данных третьим лицам осуществляется только в случаях, предусмотренных договором или законом.
                        4. Пользователь имеет право запросить удаление своих данных в любое время.
                        5. Приложение использует автоматизированную обработку данных для улучшения сервиса.
                        6. При использовании сервиса данные могут анализироваться для статистики, без раскрытия персональных данных.
                        7. Любые изменения политики конфиденциальности будут опубликованы в приложении.
                    """.trimIndent(),
                    style = CustomTheme.typography.inter.bodyMedium
                )
            }
        }
    }
}
