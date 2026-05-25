/**
 * @author Бельский Тимофей
 * @version 1.0
 * Репозиторий для управления данными о погоде, кешированием и историей поиска
 */
package com.example.lab9_project1_belskiy.repository

import com.example.lab9_project1_belskiy.model.WeatherResponse
import com.example.lab9_project1_belskiy.network.WeatherApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WeatherRepository(private val api: WeatherApi, private val settings: Settings = Settings()) {
    // Поток состояния для кешированной погоды (загружается из настроек при старте)
    private val _cachedWeather = MutableStateFlow(loadCachedWeather())
    val cachedWeather: StateFlow<WeatherResponse?> = _cachedWeather

    // Поток состояния для списка последних городов поиска
    private val _searchHistory = MutableStateFlow(loadSearchHistory())
    val searchHistory: StateFlow<List<String>> = _searchHistory

    /**
     * Основной метод получения погоды.
     * Сначала пытается загрузить данные из сети. В случае успеха сохраняет их в кеш и историю.
     * В случае ошибки сети (офлайн режим) возвращает ранее закэшированные данные.
     */
    suspend fun getWeather(city: String): WeatherResponse {
        return try {
            val response = api.fetchWeather(city)
            saveWeatherToCache(response)
            addToHistory(response.name)
            response
        } catch (e: Exception) {
            // Если интернета нет, возвращаем последнее успешное значение из памяти или кидаем ошибку
            _cachedWeather.value ?: throw e
        }
    }

    /**
     * Сохранение объекта погоды в постоянное хранилище (Settings) в формате JSON.
     */
    private fun saveWeatherToCache(weather: WeatherResponse) {
        _cachedWeather.value = weather
        val json = Json.encodeToString(weather)
        settings["cached_weather"] = json
    }

    /**
     * Загрузка данных из кеша при запуске приложения.
     */
    private fun loadCachedWeather(): WeatherResponse? {
        val json = settings.getString("cached_weather", "")
        if (json.isEmpty()) return null
        return try {
            Json.decodeFromString<WeatherResponse>(json)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Добавление города в историю поиска.
     * Новые города добавляются в начало списка. Дубликаты перемещаются в начало.
     * Хранится не более 5 элементов.
     */
    private fun addToHistory(city: String) {
        val current = _searchHistory.value.toMutableList()
        current.remove(city) // Удаляем старое вхождение, если оно было
        current.add(0, city) // Добавляем в начало
        val limited = current.take(5)
        _searchHistory.value = limited
        settings["search_history"] = limited.joinToString(",")
    }

    /**
     * Загрузка списка городов из истории из настроек.
     */
    private fun loadSearchHistory(): List<String> {
        val saved = settings.getString("search_history", "")
        if (saved.isEmpty()) return emptyList()
        return saved.split(",").filter { it.isNotBlank() }
    }
}
