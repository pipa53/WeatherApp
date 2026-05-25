/**
 * @author Бельский Тимофей
 * @version 1.0
 * Точка входа в приложение для Desktop платформ (Linux, Windows, macOS).
 */
package com.example.lab9_project1_belskiy

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    /**
     * Создание окна приложения.
     * Параметр resizable по умолчанию true, что выполняет требование Linux об изменении размера окна.
     */
    Window(
        onCloseRequest = ::exitApplication,
        title = "Weather Lab9 - Desktop",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        resizable = true
    ) {
        // Вызов общего UI-кода с флагом принудительного отображения 3 колонок
        App(isDesktop = true)
    }
}
