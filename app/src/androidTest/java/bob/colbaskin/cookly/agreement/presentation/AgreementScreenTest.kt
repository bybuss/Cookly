package bob.colbaskin.cookly.agreement.presentation


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import bob.colbaskin.cookly.agreement.domain.models.AgreementTestTags
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AgreementScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun title_isDisplayed() {
        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(),
                    onAction = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Соглашение")
            .assertIsDisplayed()
    }

    @Test
    fun description_isDisplayed() {
        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(),
                    onAction = {}
                )
            }
        }

        composeRule
            .onNodeWithText(
                text = "Для обеспечения процессов удаленного взаимодействия с ФНС России",
                substring = true
            )
            .assertIsDisplayed()
    }

    @Test
    fun policyItem_isDisplayed() {
        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(),
                    onAction = {}
                )
            }
        }

        composeRule
            .onNodeWithText("СОГЛАШЕНИЕ НА ОБРАБОТКУ")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("персональных данных")
            .assertIsDisplayed()
    }

    @Test
    fun termsItem_isDisplayed() {
        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(),
                    onAction = {}
                )
            }
        }

        composeRule
            .onNodeWithText("ПРАВИЛА")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("пользования приложением")
            .assertIsDisplayed()
    }

    @Test
    fun agreeButton_isDisabled_whenRulesAreNotAccepted() {
        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(isRulesAccepted = false),
                    onAction = {}
                )
            }
        }

        composeRule
            .onNodeWithTag(AgreementTestTags.AgreeButton)
            .assertIsNotEnabled()
    }

    @Test
    fun agreeButton_isEnabled_whenRulesAreAccepted() {
        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(isRulesAccepted = true),
                    onAction = {}
                )
            }
        }

        composeRule
            .onNodeWithTag(AgreementTestTags.AgreeButton)
            .assertIsEnabled()
    }

    @Test
    fun clickPolicyItem_sendsNavigateToPolicyAction() {
        var lastAction: AgreementAction? = null

        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(),
                    onAction = { action ->
                        lastAction = action
                    }
                )
            }
        }

        composeRule
            .onNodeWithTag(AgreementTestTags.PolicyItem)
            .performClick()

        assertEquals(
            AgreementAction.NavigateToPolicy,
            lastAction
        )
    }

    @Test
    fun clickTermsItem_sendsNavigateToTermsOfUseAction() {
        var lastAction: AgreementAction? = null

        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(),
                    onAction = { action ->
                        lastAction = action
                    }
                )
            }
        }

        composeRule
            .onNodeWithTag(AgreementTestTags.TermsItem)
            .performClick()

        assertEquals(
            AgreementAction.NavigateToTermsOfUse,
            lastAction
        )
    }

    @Test
    fun clickCheckbox_sendsUpdateAcceptActionWithTrue_whenUnchecked() {
        var lastAction: AgreementAction? = null

        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(isRulesAccepted = false),
                    onAction = { action ->
                        lastAction = action
                    }
                )
            }
        }

        composeRule
            .onNodeWithTag(AgreementTestTags.AcceptCheckbox)
            .performClick()

        assertEquals(
            AgreementAction.UpdateAccept(isRulesAccepted = true),
            lastAction
        )
    }

    @Test
    fun clickCheckbox_sendsUpdateAcceptActionWithFalse_whenChecked() {
        var lastAction: AgreementAction? = null

        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(isRulesAccepted = true),
                    onAction = { action ->
                        lastAction = action
                    }
                )
            }
        }

        composeRule
            .onNodeWithTag(AgreementTestTags.AcceptCheckbox)
            .performClick()

        assertEquals(
            AgreementAction.UpdateAccept(isRulesAccepted = false),
            lastAction
        )
    }

    @Test
    fun clickAgreeButton_sendsIAgreeAction_whenButtonEnabled() {
        var lastAction: AgreementAction? = null

        composeRule.setContent {
            UfoodTheme {
                AgreementScreen(
                    state = AgreementState(isRulesAccepted = true),
                    onAction = { action ->
                        lastAction = action
                    }
                )
            }
        }

        composeRule
            .onNodeWithTag(AgreementTestTags.AgreeButton)
            .performClick()

        assertEquals(
            AgreementAction.IAgree,
            lastAction
        )
    }
}