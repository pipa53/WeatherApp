/**
 * @author Бельский Тимофей
 * @version 1.0
 * Тесты для репозитория погоды (WeatherRepository).
 * Проверяют корректность взаимодействия репозитория с API и механизмом кеширования.
 */
package com.example.lab9_project1_belskiy

import com.example.lab9_project1_belskiy.network.WeatherApi
import com.example.lab9_project1_belskiy.repository.WeatherRepository
import com.russhwolf.settings.MapSettings
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WeatherRepositoryTest {

    /**
     * Тест успешного получения погоды.
     * Проверяет, что при корректном ответе от сервера:
     * 1. Репозиторий возвращает правильный объект погоды.
     * 2. Полученные данные сохраняются в кеш (StateFlow).
     */
    @Test
    fun testGetWeatherSuccess() = runTest {
        // Настройка имитации ответа сервера (MockEngine)
        val mockEngine = MockEngine { _ ->
            respond(
                content = """{"name":"Minsk","main":{"temp":20.5,"humidity":50},"weather":[{"main":"Clear","description":"ясно","icon":"01d"}],"wind":{"speed":3.0}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        // Создание клиента с поддержкой JSON-сериализации
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val api = WeatherApi(client)
        // Используем MapSettings для тестов (хранение в оперативной памяти)
        val repository = WeatherRepository(api, MapSettings())

        // Выполнение запроса через репозиторий
        val result = repository.getWeather("Minsk")

        // Проверка полученных данных
        assertEquals("Minsk", result.name, "Имя города должно совпадать")
        assertEquals(20.5, result.main.temp, "Температура должна быть 20.5")

        // Проверка, что данные попали в StateFlow кеша
        assertNotNull(repository.cachedWeather.value, "Данные должны быть сохранены в кеш")
    }

    /**
     * Тест механизма кеширования.
     * Проверяет, что после успешного сетевого вызова StateFlow кеша обновляется.
     */
    @Test
    fun testCaching() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = """{"name":"Minsk","main":{"temp":20.5,"humidity":50},"weather":[],"wind":{"speed":3.0}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
        }
        val repository = WeatherRepository(WeatherApi(client), MapSettings())

        repository.getWeather("Minsk")

        // Проверяем наличие данных в кеше
        assertNotNull(repository.cachedWeather.value, "Кеш не должен быть пустым после успешного запроса")
    }
}