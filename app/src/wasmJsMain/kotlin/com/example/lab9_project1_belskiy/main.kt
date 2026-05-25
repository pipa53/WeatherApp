/**
 * @author Бельский Тимофей
 * @version 1.0
 * Точка входа для Web-приложения (WasmJs)
 */
package com.example.lab9_project1_belskiy

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Привязка Compose к элементу body в index.html
    ComposeViewport(document.body!!) {
        App()
    }
}
