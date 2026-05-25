/**
 * @author Бельский Тимофей
 * @version 1.0
 * Сервис для получения данных о погоде через Ktor
 */
package com.example.lab9_project1_belskiy.network

import com.example.lab9_project1_belskiy.model.WeatherResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class WeatherApi(private val client: HttpClient) {
    // API ключ для OpenWeatherMap (для учебных целей оставлен здесь)
    private val apiKey = "c1175509d3e979fd3cfc6bd953ec5bdb"

    /**
     * Выполняет сетевой запрос для получения текущей погоды в указанном городе.
     * @param city Название города на русском или английском языке.
     * @return Объект WeatherResponse с данными о температуре, ветре и т.д.
     */
    suspend fun fetchWeather(city: String): WeatherResponse {
        return client.get("https://api.openweathermap.org/data/2.5/weather") {
            parameter("q", city)
            parameter("appid", apiKey)
            parameter("units", "metric") // Использование метрической системы (Цельсий)
            parameter("lang", "ru")      // Запрос описания погоды на русском языке
        }.body()
    }
}

/**
 * Фабричная функция для создания и настройки HTTP-клиента Ktor.
 * Настраивает JSON-сериализацию для обработки ответов от сервера.
 */
fun createHttpClient(): HttpClient {
    return HttpClient {
        // Установка плагина для автоматической конвертации JSON в объекты Kotlin
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Игнорировать поля в JSON, которых нет в нашей модели
                prettyPrint = true       // Форматированный вывод (для отладки)
                isLenient = true         // Гибкий парсинг строк
            })
        }
    }
}
