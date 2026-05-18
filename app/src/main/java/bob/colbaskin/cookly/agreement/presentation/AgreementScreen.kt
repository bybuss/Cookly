package bob.colbaskin.cookly.agreement.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import bob.colbaskin.cookly.R
import bob.colbaskin.cookly.agreement.domain.models.AgreementTestTags
import bob.colbaskin.cookly.common.design_system.theme.CustomTheme
import bob.colbaskin.cookly.navigation.Screens

@Composable
fun AgreementScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AgreementViewModel = hiltViewModel()
) {
    val state = viewModel.state

    AgreementScreen(
        modifier = modifier,
        state = state,
        onAction = { action ->
            when (action) {
                AgreementAction.Back -> navController.popBackStack()
                AgreementAction.NavigateToPolicy -> {
                    navController.navigate(Screens.Policy)
                }
                AgreementAction.NavigateToTermsOfUse -> {
                    navController.navigate(Screens.TermsOfUse)
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementScreen(
    modifier: Modifier = Modifier,
    state: AgreementState,
    onAction: (AgreementAction) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Соглашение",
                        style = CustomTheme.typography.inter.headlineSmall
                    )
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
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
                .background(CustomTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Для обеспечения процессов удаленного взаимодействия с ФНС России, в соответствии с Федеральным законом от 27.07.2006 №152-ФЗ «О персональных данных», требуется получение Вашего согласия на обработку персональных данных и согласие с правилами пользования приложением",
                modifier = Modifier.fillMaxWidth(),
                style = CustomTheme.typography.inter.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column {
                AgreementItem(
                    modifier = Modifier.testTag(AgreementTestTags.PolicyItem),
                    title = "СОГЛАШЕНИЕ НА ОБРАБОТКУ",
                    text = "персональных данных",
                    onClick = { onAction(AgreementAction.NavigateToPolicy) }
                )
                AgreementItem(
                    modifier = Modifier.testTag(AgreementTestTags.TermsItem),
                    title = "ПРАВИЛА",
                    text = "пользования приложением",
                    onClick = { onAction(AgreementAction.NavigateToTermsOfUse) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            CheckboxRow(
                modifier = Modifier.testTag(AgreementTestTags.AcceptCheckbox),
                checked = state.isRulesAccepted,
                onCheckedChange = { isChecked ->
                    onAction(AgreementAction.UpdateAccept(isChecked))
                },
                text = "Я прочитал(а) всю информацию и согласен(на) с условиями"
            )
            Button(
                onClick = { onAction(AgreementAction.IAgree) },
                enabled = state.isRulesAccepted,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(AgreementTestTags.AgreeButton),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.accentColor,
                    contentColor = Color.White,
                    disabledContainerColor = CustomTheme.colors.accentColor.copy(alpha = 0.5f),
                    disabledContentColor = CustomTheme.colors.strokeColor
                )
            ) {
                Text(text = "СОГЛАСЕН")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AgreementItem(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            color = CustomTheme.colors.mealCardBorder,
            thickness = 1.dp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = CustomTheme.typography.inter.titleSmall
                )
                Text(
                    text = text,
                    style = CustomTheme.typography.inter.bodyMedium
                )
            }
            Icon(
                painter = painterResource(R.drawable.carret_right),
                modifier = Modifier.scale(2f),
                contentDescription = null
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = CustomTheme.colors.mealCardBorder,
            thickness = 1.dp
        )
    }
}

@Composable
private fun CheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = CustomTheme.colors.accentColor,
                uncheckedColor = CustomTheme.colors.accentColor.copy(alpha = 0.5f),
                checkmarkColor = Color.White
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = CustomTheme.typography.inter.bodyMedium
        )
    }
}
