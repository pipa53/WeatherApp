/**
 * @author Бельский Тимофей
 * @version 1.0
 * UI Тесты для Android платформы.
 * Эти тесты запускаются на эмуляторе или реальном устройстве.
 * Проверяют корректность отображения элементов интерфейса и базовое взаимодействие.
 */
package com.example.lab9_project1_belskiy

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class WeatherUITest {

    /**
     * Правило для тестирования Compose. Позволяет управлять контентом экрана и искать элементы.
     */
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Тест начального состояния.
     * Проверяет, что при старте отображается поле ввода с нужной подсказкой.
     */
    @Test
    fun testAppLoadingState() {
        composeTestRule.setContent {
            App()
        }

        // Проверяем, что поле поиска с текстом "Введите город…" существует на экране
        composeTestRule.onNodeWithText("Введите город…").assertExists()
    }

    /**
     * Тест ввода текста в поле поиска.
     * Проверяет, что пользователь может ввести название города.
     */
    @Test
    fun testSearchFieldInput() {
        composeTestRule.setContent {
            App()
        }

        val inputField = composeTestRule.onNodeWithText("Введите город…")
        // Вводим название города
        inputField.performTextInput("Minsk")
        // Проверяем, что текст в поле изменился (теперь ищем по тексту "Minsk")
        composeTestRule.onNodeWithText("Minsk").assertExists()
    }

    /**
     * Тест наличия истории поиска.
     * Если история существует (сохранена с прошлых запусков), проверяем, что чипы отображаются.
     * Если истории нет, проверяем, что чипов нет.
     */
    @Test
    fun testSearchHistoryDisplay() {
        composeTestRule.setContent {
            App()
        }

        // Получаем все ноды с тегом истории
        val historyChips = composeTestRule.onAllNodesWithTag("HistoryChip")
        
        // Проверяем корректность отображения истории, если она не пуста
        val count = historyChips.fetchSemanticsNodes().size
        if (count > 0) {
            historyChips.onFirst().assertHasClickAction()
        }
    }

    /**
     * Тест отсутствия карточки погоды до поиска.
     * Проверяет, что данные о влажности не отображаются, пока город не найден.
     */
    @Test
    fun testWeatherCardPresenceInitially() {
        composeTestRule.setContent {
            App()
        }

        // Текст "Влажность:" появляется только в карточке погоды.
        // Проверяем, что его нет при первом запуске.
        composeTestRule.onNodeWithText("Влажность:").assertDoesNotExist()
    }

    /**
     * Тест клика по кнопке поиска.
     * Проверяет наличие кнопки поиска и возможность нажатия.
     */
    @Test
    fun testSearchButtonClickable() {
        composeTestRule.setContent {
            App()
        }

        // Проверяем кликабельность поля/кнопки поиска
        composeTestRule.onNodeWithText("Введите город…").assertHasClickAction()
    }
}
