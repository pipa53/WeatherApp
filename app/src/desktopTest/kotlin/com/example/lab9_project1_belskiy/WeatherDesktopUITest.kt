/**
 * @author Бельский Тимофей
 * @version 1.0
 * UI Тесты для Desktop платформы.
 * Проверяют специфичные для десктопа элементы интерфейса.
 */
package com.example.lab9_project1_belskiy

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class WeatherDesktopUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Тест наличия десктопного поля поиска.
     */
    @Test
    fun testDesktopSearchBarPresence() {
        composeTestRule.setContent {
            App(isDesktop = true)
        }

        // На десктопе мы используем OutlinedTextField с меткой "Город"
        composeTestRule.onNodeWithText("Город").assertExists()
    }

    /**
     * Тест наличия кнопки поиска.
     */
    @Test
    fun testSearchButton() {
        composeTestRule.setContent {
            App(isDesktop = true)
        }

        // Кнопка "НАЙТИ" специфична для десктопной реализации
        composeTestRule.onNodeWithText("НАЙТИ").assertExists()
    }

    /**
     * Тест начального отсутствия карточки погоды.
     */
    @Test
    fun testWeatherCardAbsentInitially() {
        composeTestRule.setContent {
            App(isDesktop = true)
        }

        // Проверяем, что карточка не отображается до поиска
        composeTestRule.onNodeWithText("Влажность:").assertDoesNotExist()
    }
}
