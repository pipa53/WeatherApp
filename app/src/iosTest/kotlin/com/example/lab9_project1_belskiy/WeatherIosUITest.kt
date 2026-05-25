/**
 * @author Бельский Тимофей
 * @version 1.0
 * UI Тесты для iOS платформы.
 * Проверяют наличие специфичных для iOS компонентов (SegmentedControl).
 * ВНИМАНИЕ: Запуск возможен только на macOS.
 */
package com.example.lab9_project1_belskiy

import androidx.compose.ui.test.*
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class WeatherIosUITest {

    @Test
    fun testIosSpecificComponents() = runComposeUiTest {
        setContent {
            App()
        }

        // Проверяем наличие городов в SegmentedButton (требование iOS)
        onNodeWithText("Минск").assertExists()
        onNodeWithText("Москва").assertExists()
        onNodeWithText("Лондон").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testIosSearchBarPlaceholder() = runComposeUiTest {
        setContent {
            App()
        }

        // Проверяем наличие плейсхолдера в стилизованном SearchBar
        onNodeWithText("Search city...").assertExists()
    }
}
