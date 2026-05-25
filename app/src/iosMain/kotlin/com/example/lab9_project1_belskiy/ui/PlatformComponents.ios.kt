/**
 * @author Бельский Тимофей
 * @version 1.0
 * Реализация компонентов для iOS.
 * В соответствии с заданием: плоские карточки без теней и SegmentedControl.
 */
package com.example.lab9_project1_belskiy.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

/**
 * Реализация строки поиска (SearchBar) для iOS.
 * Использует SegmentedButton для быстрого переключения городов (требование задания)
 * и стилизованный TextField, имитирующий нативный SearchBar iOS.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun PlatformSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val cities = listOf("Минск", "Москва", "Лондон")
    var selectedIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // Использование SegmentedControl для переключения между городами
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            cities.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = cities.size),
                    onClick = { 
                        selectedIndex = index
                        onQueryChange(label)
                        onSearch() // Сразу запускаем поиск при выборе города
                    },
                    selected = index == selectedIndex
                ) {
                    Text(label, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // SearchBar вместо обычного TextField (закругленные углы, иконка, без подчеркивания)
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search city...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            // Характерное для iOS сильное закругление углов
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            singleLine = true
        )
    }
}

/**
 * Реализация карточки погоды для iOS.
 * В соответствии с заданием: плоская карточка без теней (elevation = 0).
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
        // Плоский дизайн: убираем тень (elevation = 0)
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        // Прямоугольная форма или легкое скругление (традиционно для iOS панелей)
        shape = RectangleShape, 
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        content()
    }
}
