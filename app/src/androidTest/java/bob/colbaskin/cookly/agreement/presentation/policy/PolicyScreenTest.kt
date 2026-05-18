package bob.colbaskin.cookly.agreement.presentation.policy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import bob.colbaskin.cookly.agreement.presentation.terms_of_use.TermsOfUseScreen
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TermsOfUseScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun title_isDisplayed() {
        composeRule.setContent {
            UfoodTheme {
                TermsOfUseScreen(
                    onBackClick = {}
                )
            }
        }

        composeRule
            .onNodeWithText("Правила размещения рецептов")
            .assertIsDisplayed()
    }

    @Test
    fun termsText_isDisplayed() {
        composeRule.setContent {
            UfoodTheme {
                TermsOfUseScreen(
                    onBackClick = {}
                )
            }
        }

        composeRule
            .onNodeWithText(
                text = "Я подтверждаю, что все права на материалы размещаемого мной рецепта принадлежат мне.",
                substring = true
            )
            .assertIsDisplayed()
    }

    @Test
    fun clickBackButton_callsOnBackClick() {
        var isBackClicked = false

        composeRule.setContent {
            UfoodTheme {
                TermsOfUseScreen(
                    onBackClick = {
                        isBackClicked = true
                    }
                )
            }
        }

        composeRule
            .onNodeWithContentDescription("Назад")
            .performClick()

        assertTrue(isBackClicked)
    }
}
