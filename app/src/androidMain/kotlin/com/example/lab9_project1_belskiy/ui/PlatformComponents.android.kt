/**
 * @author Бельский Тимофей
 * @version 1.0
 * Реализация платформенно-зависимых компонентов для Android.
 * Использует Material Design 3 для обеспечения нативного внешнего вида.
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
 * Реализация строки поиска для Android.
 * Использует TextField с иконкой поиска, как принято в дизайне Android.
 */
@Composable
actual fun PlatformSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Введите город…") },
        leadingIcon = {
            // Кнопка поиска внутри текстового поля
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = "Поиск")
            }
        },
        singleLine = true
    )
}

/**
 * Реализация карточки погоды для Android.
 * Соответствует требованиям Material 3: карточка с тенями (ElevatedCard) и закруглениями.
 * @version 1.0
 */
@Composable
actual fun WeatherCard(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        // Цвета настроены для обеспечения контраста с иконками погоды
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFF526D82), // Темный серо-голубой фон
            contentColor = Color.White // Белый текст
        ),
        // Тень (Elevation), как требуется в задании для Android
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium // Закругленные углы
    ) {
        content()
    }
}
