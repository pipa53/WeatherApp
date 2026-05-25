/**
 * @author Бельский Тимофей
 * @version 1.0
 * Реализация компонентов для Desktop (Linux/Windows/macOS).
 * В соответствии с заданием для Linux: минималистичный дизайн и чёткие границы.
 */
package com.example.lab9_project1_belskiy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

/**
 * Реализация строки поиска для Desktop.
 * Использует OutlinedTextField для создания чётких рамок, согласно требованиям Linux.
 */
@Composable
actual fun PlatformSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            label = { Text("Город") },
            placeholder = { Text("Например: Москва") },
            singleLine = true,
            // Чёткая прямоугольная форма
            shape = RectangleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF333333),
                unfocusedBorderColor = Color(0xFFCCCCCC)
            )
        )
        
        Button(
            onClick = onSearch,
            modifier = Modifier.height(56.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
        ) {
            Text("НАЙТИ")
        }
    }
}

/**
 * Реализация карточки погоды для Desktop.
 * Для Linux: минималистичный дизайн, четкая граница (border), отсутствие теней 
 * и декоративная акцентная полоса слева.
 * @version 1.0
 */
@Composable
actual fun WeatherCard(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(vertical = 12.dp)
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(2.dp)),
        color = Color(0xFFF9F9F9)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Тёмная вертикальная полоса слева (характерно для виджетов Linux)
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF333333))
            )
            Box(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}
