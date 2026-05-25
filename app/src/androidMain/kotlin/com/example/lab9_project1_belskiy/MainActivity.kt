/**
 * @author Бельский Тимофей
 * @version 1.0
 * Главная Activity для Android-приложения.
 * Является точкой входа и устанавливает Jetpack Compose контент.
 */
package com.example.lab9_project1_belskiy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    /**
     * Вызывается при создании Activity.
     * Здесь инициализируется пользовательский интерфейс с помощью функции App().
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Вызов общей функции App из commonMain, которая содержит логику всего приложения
            App()
        }
    }
}
