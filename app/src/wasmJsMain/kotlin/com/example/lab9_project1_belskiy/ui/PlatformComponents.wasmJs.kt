/**
 * @author Бельский Тимофей
 * @version 1.0
 * Реализация компонентов для Web (WasmJs).
 * Использует адаптивный дизайн для поддержки различных разрешений экрана.
 */
package com.example.lab9_project1_belskiy.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Реализация строки поиска для Web.
 */
@Composable
actual fun PlatformSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        placeholder = { Text("Введите город для поиска...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Поиск")
        },
        trailingIcon = {
            TextButton(onClick = onSearch) {
                Text("Найти")
            }
        },
        singleLine = true
    )
}

/**
 * Реализация карточки погоды для Web.
 * Использует синий фон (как на Android) для лучшей видимости иконок OpenWeather.
 * @version 1.0
 */
@Composable
actual fun WeatherCard(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        // Синий фон для соответствия стилю Android и контраста с иконками
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF526D82),
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        content()
    }
}
