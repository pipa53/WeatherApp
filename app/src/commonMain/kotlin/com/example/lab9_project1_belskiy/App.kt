/**
 * @author Бельский Тимофей
 * @version 1.0
 * Главный экран приложения погоды.
 * Содержит общую логику интерфейса и адаптивную верстку для всех платформ.
 */
package com.example.lab9_project1_belskiy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.lab9_project1_belskiy.model.WeatherResponse
import com.example.lab9_project1_belskiy.network.WeatherApi
import com.example.lab9_project1_belskiy.network.createHttpClient
import com.example.lab9_project1_belskiy.repository.WeatherRepository
import com.example.lab9_project1_belskiy.ui.PlatformSearchBar
import com.example.lab9_project1_belskiy.ui.WeatherCard
import kotlinx.coroutines.launch

/**
 * Основная Composable-функция приложения.
 */
@Composable
fun App(isDesktop: Boolean = false) {
    val scope = rememberCoroutineScope()
    val api = remember { WeatherApi(createHttpClient()) }
    val repository = remember { WeatherRepository(api) }
    
    var city by remember { mutableStateOf("") }
    val weatherData by repository.cachedWeather.collectAsState()
    val searchHistory by repository.searchHistory.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                /**
                 * [РЕАЛИЗАЦИЯ ТРЕБОВАНИЙ ANDROID / IOS / LINUX]
                 * Функция PlatformSearchBar является 'expect'. 
                 * - На Android: TextField с иконкой поиска.
                 * - На iOS: SearchBar стиль + SegmentedControl (выбор из 3 городов).
                 * - На Linux: OutlinedTextField с четкими рамками.
                 */
                PlatformSearchBar(
                    query = city,
                    onQueryChange = { city = it },
                    onSearch = {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            try {
                                repository.getWeather(city)
                            } catch (e: Exception) {
                                errorMessage = "Ошибка: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                )

                // История поиска (общая для всех платформ)
                if (searchHistory.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                        Text(
                            text = "Последние запросы:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(searchHistory) { historyCity ->
                                SuggestionChip(
                                    onClick = { 
                                        city = historyCity
                                        scope.launch {
                                            isLoading = true
                                            errorMessage = null
                                            try { repository.getWeather(historyCity) } 
                                            catch (e: Exception) { errorMessage = e.message }
                                            finally { isLoading = false }
                                        }
                                    },
                                    label = { Text(historyCity) },
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.testTag("HistoryChip")
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                }

                errorMessage?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                /**
                 * [РЕАЛИЗАЦИЯ ТРЕБОВАНИЙ WEB / DESKTOP]
                 * Отзывчивая верстка на основе сетки (пункт 3 задания):
                 * - Мобильные: 1 колонка.
                 * - Планшеты: 2 колонки.
                 * - Десктопы/Web: 3 колонки.
                 */
                weatherData?.let { data ->
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val width = maxWidth
                        
                        if (width < 600.dp && !isDesktop) {
                            // МОБИЛЬНЫЕ (< 600dp) или не Desktop: Одноколоночная верстка
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                WeatherCard { WeatherContentMobile(data) }
                            }
                        } else if (width < 900.dp && !isDesktop) {
                            // ПЛАНШЕТЫ (600-900dp) или не Desktop: Двухколоночная верстка
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    WeatherCard {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            WeatherContentMain(data)
                                            // Добавляем описание и температуру под иконку
                                            WeatherContentDescription(data)
                                        }
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    WeatherCard { WeatherContentDetails(data) }
                                }
                            }
                        } else {
                            // ДЕСКТОП (всегда 3 колонки) или WEB (> 900dp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Колонка 1: Город и иконка
                                WeatherCard(modifier = Modifier.weight(1f)) { 
                                    WeatherContentMain(data) 
                                }
                                // Колонка 2: Температура и описание
                                WeatherCard(modifier = Modifier.weight(1f)) { 
                                    WeatherContentDescription(data) 
                                }
                                // Колонка 3: Дополнительные детали
                                WeatherCard(modifier = Modifier.weight(1f)) { 
                                    WeatherContentDetails(data) 
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * [ПЛАТФОРМЕННЫЕ КАРТОЧКИ]
 * WeatherCard является 'expect'.
 * - Android: Карточка с тенями (ElevatedCard) и закруглениями.
 * - iOS: Плоская карточка без теней.
 * - Linux: Карточка с четкими границами (border) и акцентной полосой.
 * - Web: OutlinedCard (синий фон для контраста с иконками).
 */

@Composable
fun WeatherContentMain(data: WeatherResponse) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = data.name, style = MaterialTheme.typography.headlineMedium)
        val iconCode = data.weather.firstOrNull()?.icon
        if (iconCode != null) {
            AsyncImage(
                model = "https://openweathermap.org/img/wn/$iconCode@4x.png",
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun WeatherContentDescription(data: WeatherResponse) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${data.main.temp}°C", style = MaterialTheme.typography.displayMedium)
        Text(
            text = data.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun WeatherContentDetails(data: WeatherResponse) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Подробности:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Влажность: ${data.main.humidity}%", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Ветер: ${data.wind.speed} м/с", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun WeatherContentMobile(data: WeatherResponse) {
    Column(modifier = Modifier.padding(16.dp)) {
        WeatherContentMain(data)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        WeatherContentDescription(data)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        WeatherContentDetails(data)
    }
}
