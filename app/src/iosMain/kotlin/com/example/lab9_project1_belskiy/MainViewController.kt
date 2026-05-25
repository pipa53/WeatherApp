/**
 * @author Бельский Тимофей
 * @version 1.0
 * Точка соприкосновения Kotlin и iOS (Swift).
 * Создает контроллер экрана для отображения Compose-интерфейса.
 */
package com.example.lab9_project1_belskiy

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/**
 * Функция, которую будет вызывать Swift-код в Xcode.
 * Имя функции оставлено с заглавной буквы для соответствия стилистике именования UIViewController в iOS.
 */
@Suppress("FunctionName")
fun MainViewController(): UIViewController = ComposeUIViewController { 
    App() 
}
