/**
 * @author Бельский Тимофей
 * @version 1.0
 * Ожидаемые (expect) компоненты для различных платформ
 */
package com.example.lab9_project1_belskiy.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Ожидаемая реализация строки поиска.
 * - Android: TextField с иконкой.
 * - iOS: SearchBar + SegmentedControl.
 * - Desktop/Linux: OutlinedTextField с четкими рамками.
 * @version 1.0
 */
@Composable
expect fun PlatformSearchBar(
    query: String,                 // Текущий текст запроса
    onQueryChange: (String) -> Unit, // Обработчик изменения текста
    onSearch: () -> Unit           // Действие при нажатии на кнопку поиска
)

/**
 * Ожидаемая реализация карточки для отображения данных.
 * - Android: ElevatedCard (тени + скругления).
 * - iOS: Плоская панель (без теней).
 * - Linux: Прямоугольник с границей и акцентной полосой.
 * - Web: Карточка с фоном для контраста иконок.
 * @version 1.0
 */
@Composable
expect fun WeatherCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit // Содержимое карточки
)
